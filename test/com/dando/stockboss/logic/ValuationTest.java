package com.dando.stockboss.logic;

import org.junit.Test;

import com.dando.stockboss.Exchange;

public class ValuationTest {
	private ValuationLogic valuationLogic = new ValuationLogic();
	
	@Test
	public void testGhettoValuation() {
		String ticker = "BMO";
		Exchange exchange = Exchange.TSE;
		Long davesGhettoValuation = valuationLogic.davesGhettoValuation(exchange, ticker);
		System.out.println("Value of " + ticker + " is: " + davesGhettoValuation);
	}
}
