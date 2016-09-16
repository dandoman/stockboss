package com.dando.stockboss.logic;

import org.junit.Test;

import com.dando.stockboss.Exchange;

public class ValuationTest {
	private ValuationLogic valuationLogic = new ValuationLogic();
	
	@Test
	public void testGhettoValuation() {
		Long davesGhettoValuation = valuationLogic.davesGhettoValuation(Exchange.NASDAQ, "GILD");
		System.out.println("Value of GILD is: " + davesGhettoValuation);
	}
}
