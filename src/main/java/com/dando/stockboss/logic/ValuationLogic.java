package com.dando.stockboss.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.dando.stockboss.BalanceSheetEntry;
import com.dando.stockboss.Exchange;
import com.dando.stockboss.IncomeEntry;
import com.dando.stockboss.StockData;
import com.dando.stockboss.client.MorningstarClient;
import com.dando.stockboss.client.YahooFinanceClient;

public class ValuationLogic {

	private MorningstarClient financialDocsClient = new MorningstarClient();
	private YahooFinanceClient stockDataClient = new YahooFinanceClient();

	public Long davesGhettoValuation(Exchange exchange, String ticker) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<List<BalanceSheetEntry>> futureBalanceSheets = executor
				.submit(() -> financialDocsClient.getBalanceSheets(exchange, ticker, false));
		Future<List<IncomeEntry>> futureIncomeStatements = executor
				.submit(() -> financialDocsClient.getIncomeStatements(exchange, ticker, false));

		List<BalanceSheetEntry> balanceSheets;
		List<IncomeEntry> incomeStatements;
		try {
			balanceSheets = futureBalanceSheets.get();
			incomeStatements = futureIncomeStatements.get();
		} catch (InterruptedException | ExecutionException e) {
			// FUckuppp
			return null;
		}

		if (balanceSheets == null || incomeStatements == null) {
			return null;
		}

		long totalEquity = balanceSheets.get(0).getTotalEquity();
		long averageQuarterlyNetProfit = Math.round(
				incomeStatements.stream().map(is -> is.getNetIncome()).mapToLong(val -> val).average().getAsDouble());

		return totalEquity + ((averageQuarterlyNetProfit * 4) * 10);
	}
	
	
	public Long dcfValuation(Exchange exchange, String ticker, double discountRate) {
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<List<BalanceSheetEntry>> futureBalanceSheets = executor
				.submit(() -> financialDocsClient.getBalanceSheets(exchange, ticker, false));
		Future<List<IncomeEntry>> futureIncomeStatements = executor
				.submit(() -> financialDocsClient.getIncomeStatements(exchange, ticker, true));

		List<BalanceSheetEntry> balanceSheets;
		List<IncomeEntry> incomeStatements;
		try {
			balanceSheets = futureBalanceSheets.get();
			incomeStatements = futureIncomeStatements.get();
		} catch (InterruptedException | ExecutionException e) {
			// FUckuppp
			return null;
		}

		if (balanceSheets == null || incomeStatements == null) {
			return null;
		}
		
		incomeStatements = incomeStatements.stream().sorted((x,y) -> x.getPeriodEnd().compareTo(y.getPeriodEnd())).collect(Collectors.toList());
		
		long totalEquity = balanceSheets.get(0).getTotalEquity();
		List<Double> yoyGrowth = new ArrayList<>();
		for(int i = 1; i < incomeStatements.size(); i ++) {
			double growth = incomeStatements.get(i).getNetIncome() / (1.0*(incomeStatements.get(i - 1).getNetIncome()));
			double growthPercentage = growth - 1;
			yoyGrowth.add(growthPercentage);
		}
		
		double avgYoyGrowth = yoyGrowth.stream().mapToDouble(val -> val).average().getAsDouble();
		double discountedGrowth = avgYoyGrowth * 0.6;
		
		long forwardIncome = incomeStatements.get(incomeStatements.size() - 1).getNetIncome();
		
		List<Long> forwardNetIncomes = new ArrayList<>();
		for(int i = 0; i < 100; i++) {
			double growthToUse = discountedGrowth;
			if(i <= 2) {
				growthToUse = discountedGrowth;
			} else if (i > 2 && i <= 4) {
				growthToUse = discountedGrowth * 0.25;
			} else if (i > 4 && i < 6) {
				growthToUse = discountedGrowth * 0.05;
			} else {
				growthToUse = 0;
			}
			long income = new Double(forwardIncome * (1 + growthToUse)).longValue();
			forwardNetIncomes.add(income);
			forwardIncome = income;
		}
		
		
		return getDiscountedValue(discountRate, forwardNetIncomes) + totalEquity;
	}

	// NPV = {$2,000/(1+.045)^1} + {$7,000/(1+.045)^2} + {$11,000/(1+.045)^3} -
	// $10,000
	private long getDiscountedValue(double discountRate, List<Long> cashFlows) {
		long totalValue = 0;
		for (int i = 0; i < cashFlows.size(); i++) {
			int year = i + 1;
			totalValue += new Double(cashFlows.get(i) / Math.pow((1 + discountRate), year)).longValue();
		}
		return totalValue;
	}
}
