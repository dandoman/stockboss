package com.dando.stockboss.logic;

import java.util.List;

import org.junit.Test;

import com.dando.stockboss.BalanceSheetEntry;
import com.dando.stockboss.CashFlowEntry;
import com.dando.stockboss.Exchange;
import com.dando.stockboss.client.MorningstarClient;

public class ClientTest {
	
	MorningstarClient client = new MorningstarClient();
	
	@Test
	public void testCashflow() {
		List<CashFlowEntry> cashFlowStatements = client.getCashFlowStatements(Exchange.NASDAQ, "aapl", true);
		cashFlowStatements.forEach(cf -> System.out.println(cf));
	}
	
	@Test
	public void testBalanceSheet() {
		List<BalanceSheetEntry> balanceSheets = client.getBalanceSheets(Exchange.NASDAQ, "aapl", true);
		balanceSheets.forEach(bs -> System.out.println(bs));
	}
}
