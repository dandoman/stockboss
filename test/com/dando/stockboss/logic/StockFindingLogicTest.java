package com.dando.stockboss.logic;

import java.util.Map;

import org.junit.Test;

import com.dando.stockboss.StockData;

public class StockFindingLogicTest {
	private StockFindingLogic logic = new StockFindingLogic();
	
	@Test
	public void testLogic() {
		Map<String, StockData> stockCandidates = logic.getStockCandidates();
		stockCandidates.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		});
	}
}
