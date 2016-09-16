package com.dando.stockboss.logic;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		Future<List<BalanceSheetEntry>> futureBalanceSheets = executor.submit(() -> financialDocsClient.getBalanceSheets(exchange, ticker, false));
		Future<List<IncomeEntry>> futureIncomeStatements = executor.submit(() -> financialDocsClient.getIncomeStatements(exchange, ticker, false));
		
		List<BalanceSheetEntry> balanceSheets;
		List<IncomeEntry> incomeStatements; 
		try {
			balanceSheets = futureBalanceSheets.get();
			incomeStatements = futureIncomeStatements.get();
		} catch (InterruptedException | ExecutionException e) {
			//FUckuppp
			return null;
		}
		
		if(balanceSheets == null || incomeStatements == null) {
			return null;
		}
		
		long totalEquity = balanceSheets.get(0).getTotalEquity();
		long averageQuarterlyNetProfit = Math.round(incomeStatements.stream().map(is -> is.getNetIncome()).mapToLong(val -> val).average().getAsDouble());
		
		
		return totalEquity + ((averageQuarterlyNetProfit * 4)*10);
	}
}
