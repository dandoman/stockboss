package com.dando.stockboss.client;


import java.util.ArrayList;
import java.util.List;

import com.dando.stockboss.CashFlowEntry;
import com.dando.stockboss.Exchange;
import com.dando.stockboss.logic.DataExtractionLogic;

import lombok.Setter;

public class MorningstarClient {

	private HttpTextClient textClient = new HttpTextClient();
	private DataExtractionLogic extractionLogic = new DataExtractionLogic();
	private static final String parametrizedUrl = "http://financials.morningstar.com/ajax/ReportProcess4CSV.html?&t=%s:%s&culture=en-US&cur=&reportType=%s&period=%s&dataType=A&order=asc&columnYear=5&curYearPart=1st5year&rounding=3&view=raw&denominatorView=raw&number=2";

	
	private String getStockData(String exchange, String ticker, String reportType, String period) {
		return textClient.getBlogText(String.format(parametrizedUrl, exchange, ticker, reportType, period));
	}
	
	public Object getIncomeStatements(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "is", annual ? "3" : "12");
		return null;
	}
	
	public List<CashFlowEntry> getCashFlowStatements(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "cf", annual ? "12" : "3");
		if(raw == null) {
			return new ArrayList<>();
		}
		return extractionLogic.extractCashFlows(raw);
	}
	
	public Object getBalanceSheets(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "bs", annual ? "3" : "12");
		return null;
	}
}
