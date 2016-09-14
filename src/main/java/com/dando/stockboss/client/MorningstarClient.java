package com.dando.stockboss.client;


import com.dando.stockboss.Exchange;

import lombok.Setter;

public class MorningstarClient {

	@Setter
	private HttpTextClient textClient = new HttpTextClient();
	private static final String parametrizedUrl = "http://financials.morningstar.com/ajax/ReportProcess4CSV.html?&t=%s:%s&culture=en-US&cur=&reportType=%s&period=%s&dataType=A&order=asc&columnYear=5&curYearPart=1st5year&rounding=3&view=raw&denominatorView=raw&number=2";

	
	private String getStockData(String exchange, String ticker, String reportType, String period) {
		return textClient.getBlogText(String.format(parametrizedUrl, exchange, ticker, reportType));
	}
	
	public Object getIncomeStatements(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "is", annual ? "3" : "12");
		return null;
	}
	
	public Object getCashFlowStatements(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "cf", annual ? "3" : "12");
		return null;
	}
	
	public Object getBalanceSheets(Exchange exchange, String ticker, boolean annual) {
		String raw = getStockData(exchange.getStringName(), ticker, "bs", annual ? "3" : "12");
		return null;
	}
}
