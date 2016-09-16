package com.dando.stockboss.logic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dando.stockboss.Exchange;
import com.dando.stockboss.StockData;
import com.dando.stockboss.client.NasdaqFTPClient;
import com.dando.stockboss.client.YahooFinanceClient;

public class StockFindingLogic {
	
	private ValuationLogic valuationLogic = new ValuationLogic();
	private NasdaqFTPClient nasdaq = new NasdaqFTPClient();
	private YahooFinanceClient financeClient = new YahooFinanceClient();
	
	public Map<String, StockData> getStockCandidates() {
		ExecutorService executor = Executors.newCachedThreadPool();
		Map<String, StockData> results = new ConcurrentHashMap<>();
		List<String> allCompanyTickers = nasdaq.getAllCompanyTickers();
		allCompanyTickers.parallelStream().forEach(ticker -> {
			Future<StockData> futureStockData = executor.submit(() -> financeClient.getStockData(ticker));
			Future<Long> futureDavesGhettoEvaluation = executor.submit(() -> valuationLogic.davesGhettoValuation(Exchange.NASDAQ, ticker));
			
			StockData stockData= null;
			Long davesGhettoValuation = null;
			
			try{
				stockData = futureStockData.get();
				davesGhettoValuation = futureDavesGhettoEvaluation.get();
			} catch (Exception e) {
				//Wtv..
			}
			
			if(davesGhettoValuation != null && stockData != null) {
				if(davesGhettoValuation > stockData.getMarketCapInDollars() && stockData.getMarketCapInDollars() > 1000000000l) {
					results.put(ticker, stockData);
				}
			}
		});
		
		return results;
	}
	
}
