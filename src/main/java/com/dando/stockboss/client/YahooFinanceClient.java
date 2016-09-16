package com.dando.stockboss.client;

import com.dando.stockboss.StockData;

public class YahooFinanceClient {

	private HttpTextClient httpClient = new HttpTextClient();
	private static final String parametrizedUrl = "http://finance.yahoo.com/d/quotes.csv?s=%s&f=bj1";
	
	public StockData getStockData(String ticker) {
		String res = httpClient.getBlogText(String.format(parametrizedUrl, ticker));
		String [] parts = res.replaceAll("\\s", "").split(",");
		if("N/A".equalsIgnoreCase(parts[0]) || "N/A".equalsIgnoreCase(parts[1])) {
			return null;
		}
		
		StockData data = new StockData();
		data.setPriceInCents(getStringValueInCents(parts[0].replaceAll("\\s", "")));
		
		if(parts[1].contains("B")) {
			String marketCapString = parts[1].replaceAll("B", "").replaceAll("\\s", "");
			long marketCap = getStringValueInCents(marketCapString) * 1000000000 / 100;
			data.setMarketCapInDollars(marketCap);
			
		} else {
			String marketCapString = parts[1].replaceAll("M", "").replaceAll("\\s", "");
			long marketCap = getStringValueInCents(marketCapString) * 1000000 / 100;
			data.setMarketCapInDollars(marketCap);
		}
		
		
		return data;
		
	}
	private long getStringValueInCents(String moneyString) {
		String [] priceParts = moneyString.split("\\.");
		long dollars = Integer.parseInt(priceParts[0]);
		long cents = Integer.parseInt(priceParts[1]);
		long price = (dollars * 100) + cents;
		return price;
	}
	
}
