package com.dando.stockboss.logic;

import java.util.List;

import org.junit.Test;

import com.dando.stockboss.BalanceSheetEntry;
import com.dando.stockboss.CashFlowEntry;
import com.dando.stockboss.Exchange;
import com.dando.stockboss.IncomeEntry;
import com.dando.stockboss.client.MorningstarClient;
import com.dando.stockboss.client.NasdaqFTPClient;
import com.dando.stockboss.client.YahooFinanceClient;

public class ClientTest {
	
	MorningstarClient client = new MorningstarClient();
	YahooFinanceClient financeClient = new YahooFinanceClient();
	NasdaqFTPClient nasdaqClient = new NasdaqFTPClient();
	
	@Test
	public void testCashflow() {
		List<CashFlowEntry> cashFlowStatements = client.getCashFlowStatements(Exchange.NASDAQ, "aapl", true);
		cashFlowStatements.forEach(cf -> System.out.println(cf));
		System.out.println(financeClient.getStockData("aapl"));
	}
	
	@Test
	public void testBalanceSheet() {
		List<BalanceSheetEntry> balanceSheets = client.getBalanceSheets(Exchange.NASDAQ, "aapl", true);
		balanceSheets.forEach(bs -> System.out.println(bs));
	}
	
	@Test
	public void testIncomeStatement() {
		List<IncomeEntry> incomeStatements = client.getIncomeStatements(Exchange.NASDAQ, "aapl", true);
		incomeStatements.forEach(is -> System.out.println(is));
	}
	
	@Test
	public void testNasdaqClient() {
		List<String> allCompanyTickers = nasdaqClient.getAllCompanyTickers();
		for(String s : allCompanyTickers) {
			System.out.println(s);
		}
	}
	
	
}
