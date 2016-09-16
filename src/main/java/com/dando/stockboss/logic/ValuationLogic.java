package com.dando.stockboss.logic;

import java.util.List;
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
		
		List<BalanceSheetEntry> balanceSheets = financialDocsClient.getBalanceSheets(exchange, ticker, false);
		List<IncomeEntry> incomeStatements = financialDocsClient.getIncomeStatements(exchange, ticker, false);
		StockData stockData = stockDataClient.getStockData(ticker);
		if(balanceSheets == null || incomeStatements == null || stockData == null) {
			return null;
		}
		
		long totalEquity = balanceSheets.get(0).getTotalEquity();
		long averageQuarterlyNetProfit = Math.round(incomeStatements.stream().map(is -> is.getNetIncome()).mapToLong(val -> val).average().getAsDouble());
		
		
		return totalEquity + ((averageQuarterlyNetProfit * 4)*10);
	}
}
