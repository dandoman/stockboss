package com.dando.stockboss.logic;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.dando.stockboss.BalanceSheetEntry;
import com.dando.stockboss.CashFlowEntry;
import com.dando.stockboss.IncomeEntry;

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
		for (CashFlowEntry cf : cashFlows) {
			Assert.assertTrue(cf.getPeriodEnd().getTime() <= periodEnd.getTime());
			periodEnd = cf.getPeriodEnd();
		}

		Assert.assertTrue(cashFlows.get(4).getNetCashInvested() == -3552000000l);
		Assert.assertTrue(cashFlows.get(2).getDividendPaid() == -614000000l);
		Assert.assertTrue(cashFlows.get(3).getNetCashChange() == 6548000000l);
	}

	@Test
	public void testBalanceSheetExtraction() {
		String balanceSheetRaw = "GILEAD SCIENCES INC  (GILD) CashFlowFlag BALANCE SHEET\n"
				+ "Fiscal year ends in December. USD in thousands except per share data.,2015-06,2015-09,2015-12,2016-03,2016-06\n"
				+ "Assets\n" + "Current assets\n" + "Cash\n"
				+ "Cash and cash equivalents,7417000,13965000,12851000,6315000,6485000\n"
				+ "Short-term investments,1194000,1749000,1756000,2004000,2267000\n"
				+ "Total cash,8611000,15714000,14607000,8319000,8752000\n"
				+ "Receivables,5331000,6105000,5854000,6163000,5752000\n"
				+ "Inventories,2039000,1988000,1955000,1880000,1862000\n"
				+ "Deferred income taxes,833000,894000,828000,830000,835000\n"
				+ "Prepaid expenses,726000,1209000,1013000,1024000,635000\n"
				+ "Other current assets,553000,,506000,1051000,517000\n"
				+ "Total current assets,18093000,25910000,24763000,19267000,18353000\n" + "Non-current assets\n"
				+ "\"Property, plant and equipment\"\n" + "\"Gross property, plant and equipment\",,,3039000,,\n"
				+ "Accumulated Depreciation,,,-763000,,\n"
				+ "\"Net property, plant and equipment\",1899000,2143000,2276000,2431000,2599000\n"
				+ "Equity and other investments,6056000,9400000,11601000,13003000,15864000\n"
				+ "Goodwill,1172000,1172000,1172000,1172000,1172000\n"
				+ "Intangible assets,10660000,10454000,10247000,9923000,9713000\n"
				+ "Deferred income taxes,177000,291000,324000,292000,433000\n"
				+ "Other long-term assets,1110000,1267000,1456000,1677000,1846000\n"
				+ "Total non-current assets,21074000,24727000,27076000,28498000,31627000\n"
				+ "Total assets,39167000,50637000,51839000,47765000,49980000\n"
				+ "Liabilities and stockholders' equity\n" + "Liabilities\n" + "Current liabilities\n"
				+ "Short-term debt,352000,331000,983000,1745000,700000\n"
				+ "Accounts payable,1571000,1239000,1178000,945000,1122000\n"
				+ "Taxes payable,356000,159000,65000,51000,368000\n"
				+ "Accrued liabilities,6145000,7275000,7225000,7640000,7909000\n"
				+ "Deferred revenues,501000,356000,440000,529000,345000\n"
				+ "Total current liabilities,8925000,9360000,9891000,10910000,10444000\n" + "Non-current liabilities\n"
				+ "Long-term debt,11922000,21894000,21195000,21077000,21427000\n"
				+ "Deferred taxes liabilities,36000,30000,,,\n"
				+ "Minority interest,347000,530000,579000,621000,579000\n"
				+ "Total non-current liabilities,13955000,23863000,23414000,23457000,24000000\n"
				+ "Total liabilities,22880000,33223000,33305000,34367000,34444000\n" + "Stockholders' equity\n"
				+ "Common stock,2000,1000,1000,1000,1000\n"
				+ "Additional paid-in capital,,285000,444000,516000,632000\n"
				+ "Retained earnings,16038000,16961000,18001000,13045000,14949000\n"
				+ "Accumulated other comprehensive income,247000,167000,88000,-164000,-46000\n"
				+ "Total stockholders' equity,16287000,17414000,18534000,13398000,15536000\n"
				+ "Total liabilities and stockholders' equity,39167000,50637000,51839000,47765000,49980000";

		DataExtractionLogic logic = new DataExtractionLogic();
		List<BalanceSheetEntry> balanceSheetsFlows = logic.extractBalanceSheets(balanceSheetRaw);
		Assert.assertTrue(balanceSheetsFlows.size() == 5);
		Date periodEnd = balanceSheetsFlows.get(0).getPeriodEnd();
		for (BalanceSheetEntry bf : balanceSheetsFlows) {
			Assert.assertTrue(bf.getPeriodEnd().getTime() <= periodEnd.getTime());
			periodEnd = bf.getPeriodEnd();
		}

		Assert.assertTrue(balanceSheetsFlows.get(4).getTotalAssets() == 39167000000l);
		Assert.assertTrue(balanceSheetsFlows.get(2).getTotalLiabilities() == 33305000000l);
		Assert.assertTrue(balanceSheetsFlows.get(3).getCashAndEquivalents() == 13965000000l);
	}

	@Test
	public void testIncomeStatementExtraction() {
		String incomeStatementRaw = "APPLE INC  (AAPL) CashFlowFlag INCOME STATEMENT\n"
				+ "Fiscal year ends in September. USD in thousands except per share data.,2015-06,2015-09,2015-12,2016-03,2016-06,TTM\n"
				+ "Revenue,49605000,51501000,75872000,50557000,42358000,220288000\n"
				+ "Cost of revenue,29924000,30953000,45449000,30636000,26252000,133290000\n"
				+ "Gross profit,19681000,20548000,30423000,19921000,16106000,86998000\n" + "Operating expenses\n"
				+ "Research and development,2034000,2220000,2404000,2511000,2560000,9695000\n"
				+ "\"Sales, General and administrative\",3564000,3705000,3848000,3423000,3441000,14417000\n"
				+ "Total operating expenses,5598000,5925000,6252000,5934000,6001000,24112000\n"
				+ "Operating income,14083000,14623000,24171000,13987000,10105000,62886000\n"
				+ "Interest Expense,201000,238000,276000,321000,409000,1244000\n"
				+ "Other income (expense),591000,677000,678000,476000,773000,2604000\n"
				+ "Income before taxes,14473000,15062000,24573000,14142000,10469000,64246000\n"
				+ "Provision for income taxes,3796000,3938000,6212000,3626000,2673000,16449000\n"
				+ "Net income from continuing operations,10677000,11124000,18361000,10516000,7796000,47797000\n"
				+ "Net income,10677000,11124000,18361000,10516000,7796000,47797000\n"
				+ "Net income available to common shareholders,10677000,11124000,18361000,10516000,7796000,47797000\n"
				+ "Earnings per share\n" + "Basic,1.86,1.98,3.30,1.91,1.43,8.64\n"
				+ "Diluted,1.85,1.97,3.28,1.90,1.42,8.59\n" + "Weighted average shares outstanding\n"
				+ "Basic,5729886,5646918,5558930,5514381,5443058,5540822\n"
				+ "Diluted,5773099,5682516,5594127,5540886,5472781,5572577\n"
				+ "EBITDA,17758000,18419000,27803000,16940000,13404000,76566000";

		DataExtractionLogic logic = new DataExtractionLogic();
		List<IncomeEntry> incomeStatements = logic.extractIncomeStatements(incomeStatementRaw);
		Assert.assertTrue(incomeStatements.size() == 5);
		Date periodEnd = incomeStatements.get(0).getPeriodEnd();
		for (IncomeEntry bf : incomeStatements) {
			Assert.assertTrue(bf.getPeriodEnd().getTime() <= periodEnd.getTime());
			periodEnd = bf.getPeriodEnd();
		}

		Assert.assertTrue(incomeStatements.get(4).getEbitda() == 17758000000l);
		Assert.assertTrue(incomeStatements.get(2).getOperatingIncome() == 24171000000l);
		Assert.assertTrue(incomeStatements.get(3).getNetIncome() == 11124000000l);
	}

}
