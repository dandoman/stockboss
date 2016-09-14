package com.dando.stockboss.logic;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.dando.stockboss.CashFlowEntry;

public class DataExtractionLogicTest {

	@Test
	public void testCashFlowExtraction() {
		String cashFlowRaw = "GILEAD SCIENCES INC  (GILD) Statement of  CASH FLOW\n"
				+ "Fiscal year ends in December. USD in thousands except per share data.,2015-06,2015-09,2015-12,2016-03,2016-06,TTM\n"
				+ "Cash Flows From Operating Activities\n"
				+ "Net income,4497000,4592000,4685000,3567000,3497000,16341000\n"
				+ "Depreciation & amortization,273000,278000,278000,283000,284000,1123000\n"
				+ "Investment/asset impairment charges,,,,114000,,\n"
				+ "Deferred income taxes,-139000,-182000,49000,15000,-131000,-249000\n"
				+ "Stock based compensation,96000,97000,97000,88000,95000,377000\n"
				+ "Accounts receivable,-462000,-800000,213000,-191000,381000,-397000\n"
				+ "Inventory,-264000,-25000,-196000,-14000,-83000,-318000\n"
				+ "Prepaid expenses,-179000,-40000,77000,-126000,-209000,-298000\n"
				+ "Accounts payable,562000,-332000,-62000,-239000,172000,-461000\n"
				+ "Accrued liabilities,515000,572000,15000,195000,681000,1463000\n"
				+ "Income taxes payable,425000,-51000,-254000,205000,440000,340000\n"
				+ "Other working capital,304000,-21000,30000,37000,-199000,-153000\n"
				+ "Other non-cash items,30000,8000,-58000,-21000,12000,55000\n"
				+ "Net cash provided by operating activities,5658000,4096000,4874000,3913000,4940000,17823000\n"
				+ "Cash Flows From Investing Activities\n"
				+ "\"Investments in property, plant, and equipment\",-171000,-286000,-166000,-177000,-204000,-833000\n"
				+ "Purchases of investments,-4385000,-5444000,-4948000,-5334000,-7045000,-22771000\n"
				+ "Sales/Maturities of investments,1004000,1544000,2676000,3402000,3965000,11587000\n"
				+ "Net cash used for investing activities,-3552000,-4186000,-2438000,-2109000,-3284000,-12017000\n"
				+ "Cash Flows From Financing Activities\n"
				+ "Debt issued,354000,9994000,-9718000,95000,1210000,1581000\n"
				+ "Debt repayment,-451000,-113000,-234000,-126000,-1120000,-1593000\n"
				+ "Common stock issued,84000,79000,38000,92000,28000,237000\n"
				+ "Common stock repurchased,-900000,-3050000,-3051000,-8000000,-1001000,-15102000\n"
				+ "Excess tax benefit from stock based compensation,,-1000,,2000,,1000\n"
				+ "Dividend paid,,-627000,-614000,-587000,-626000,-2454000\n"
				+ "Other financing activities,-4428000,362000,10035000,128000,-7000,10518000\n"
				+ "Net cash provided by (used for) financing activities,-5341000,6644000,-3544000,-8396000,-1516000,-6812000\n"
				+ "Effect of exchange rate changes,17000,-6000,-6000,56000,30000,74000\n"
				+ "Net change in cash,-3218000,6548000,-1114000,-6536000,170000,-932000\n"
				+ "Cash at beginning of period,10635000,7417000,13965000,12851000,6315000,7417000\n"
				+ "Cash at end of period,7417000,13965000,12851000,6315000,6485000,6485000\n" + "Free Cash Flow\n"
				+ "Operating cash flow,5658000,4096000,4874000,3913000,4940000,17823000\n"
				+ "Capital expenditure,-171000,-286000,-166000,-177000,-204000,-833000\n"
				+ "Free cash flow,5487000,3810000,4708000,3736000,4736000,16990000";
		
		DataExtractionLogic logic = new DataExtractionLogic();
		List<CashFlowEntry> cashFlows = logic.extractCashFlows(cashFlowRaw);
		Assert.assertTrue(cashFlows.size() == 5);
		Date periodEnd = cashFlows.get(0).getPeriodEnd();
		for(CashFlowEntry cf : cashFlows) {
			Assert.assertTrue(cf.getPeriodEnd().getTime() <= periodEnd.getTime());
			periodEnd = cf.getPeriodEnd();
		}
		
		Assert.assertTrue(cashFlows.get(4).getNetCashInvested() == -3552000000l);
		Assert.assertTrue(cashFlows.get(2).getDividendPaid() == -614000000l);
		Assert.assertTrue(cashFlows.get(3).getNetCashChange() == 6548000000l);
	}

}
