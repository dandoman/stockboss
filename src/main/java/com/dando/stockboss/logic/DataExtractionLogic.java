package com.dando.stockboss.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.dando.stockboss.BalanceSheetEntry;
import com.dando.stockboss.CashFlowEntry;
import com.dando.stockboss.IncomeEntry;

import lombok.Cleanup;

public class DataExtractionLogic {
	
	public List<CashFlowEntry> extractCashFlows(String raw) {
		CashFlowEntry [] cashFlows = new CashFlowEntry [5];
		for (int i = 0; i < 5; i++) {
			cashFlows[i] = new CashFlowEntry();
		}
		@Cleanup Scanner textScanner = new Scanner(raw);
		while(textScanner.hasNextLine()) {
			String line = textScanner.nextLine();
			String [] rawTokenizedLines = line.split(",");
			if(line.contains("Fiscal year ends")) {
				//Sample: 'Fiscal year ends in December. USD in thousands except per share data.,2011-12,2012-12,2013-12,2014-12,2015-12,TTM'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					String [] dateParts = tokenizedLines[i].split("\\-");
					DateTime endPeriod = new DateTime(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), 1, 0, 0);
					cashFlows[i].setPeriodEnd(endPeriod.toDate());
				}
				continue;
			}
			
			if(line.contains("Dividend paid")) {
				//Sample: 'Dividend paid,,-627000,-614000,-587000,-626000,-2454000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					cashFlows[i].setDividendPaid(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Net cash used for investing activities")) {
				//Sample: 'Net cash used for investing activities,-3552000,-4186000,-2438000,-2109000,-3284000,-12017000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					cashFlows[i].setNetCashInvested(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Net cash provided by (used for) financing activities")) {
				//Sample: 'Net cash provided by (used for) financing activities,-5341000,6644000,-3544000,-8396000,-1516000,-6812000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					cashFlows[i].setTotalFinancing(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Net change in cash")) {
				//Sample: 'Net change in cash,-3218000,6548000,-1114000,-6536000,170000,-932000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					cashFlows[i].setNetCashChange(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Common stock repurchased")) {
				//Sample: 'Common stock repurchased,-900000,-3050000,-3051000,-8000000,-1001000,-15102000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					cashFlows[i].setStockRepurchased(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			
		}
		
		return Arrays.asList(cashFlows).stream().sorted((c1,c2) -> -1 * c1.getPeriodEnd().compareTo(c2.getPeriodEnd())).collect(Collectors.toList());
	}
	
	public List<BalanceSheetEntry> extractBalanceSheets(String raw) {
		BalanceSheetEntry [] balanceSheetss = new BalanceSheetEntry [5];
		for (int i = 0; i < 5; i++) {
			balanceSheetss[i] = new BalanceSheetEntry();
		}
		@Cleanup Scanner textScanner = new Scanner(raw);
		while(textScanner.hasNextLine()) {
			String line = textScanner.nextLine();
			String [] rawTokenizedLines = line.split(",");
			if(line.contains("Fiscal year ends")) {
				//Sample: 'Fiscal year ends in December. USD in thousands except per share data.,2011-12,2012-12,2013-12,2014-12,2015-12,TTM'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					String [] dateParts = tokenizedLines[i].split("\\-");
					DateTime endPeriod = new DateTime(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), 1, 0, 0);
					balanceSheetss[i].setPeriodEnd(endPeriod.toDate());
				}
				continue;
			}
			
			if(line.contains("Total assets")) {
				//Sample: 'Total assets,39167000,50637000,51839000,47765000,49980000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					balanceSheetss[i].setTotalAssets(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Cash and cash equivalents")) {
				//Sample: 'Cash and cash equivalents,7417000,13965000,12851000,6315000,6485000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					balanceSheetss[i].setCashAndEquivalents(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Total liabilities,")) {
				//Sample: 'Total liabilities,22880000,33223000,33305000,34367000,34444000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					balanceSheetss[i].setTotalLiabilities(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Total stockholders' equity")) {
				//Sample: 'Total stockholders' equity,16287000,17414000,18534000,13398000,15536000'
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					balanceSheetss[i].setTotalEquity(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
		}
		
		return Arrays.asList(balanceSheetss).stream().sorted((c1,c2) -> -1 * c1.getPeriodEnd().compareTo(c2.getPeriodEnd())).collect(Collectors.toList());
	}
	
	public List<IncomeEntry> extractIncomeStatements(String raw) {
		IncomeEntry [] incomes = new IncomeEntry [5];
		for (int i = 0; i < 5; i++) {
			incomes[i] = new IncomeEntry();
		}
		@Cleanup Scanner textScanner = new Scanner(raw);
		while(textScanner.hasNextLine()) {
			String line = textScanner.nextLine();
			String [] rawTokenizedLines = line.split(",");
			if(line.contains("Fiscal year ends")) {
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					String [] dateParts = tokenizedLines[i].split("\\-");
					DateTime endPeriod = new DateTime(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), 1, 0, 0);
					incomes[i].setPeriodEnd(endPeriod.toDate());
				}
				continue;
			}
			
			if(line.contains("Net income,")) {
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					incomes[i].setNetIncome(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("Operating income,")) {
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					incomes[i].setOperatingIncome(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			if(line.contains("EBITDA")) {
				String [] tokenizedLines = Arrays.copyOfRange(rawTokenizedLines, 1, 6);
				for(int i = 0; i < tokenizedLines.length; i++) {
					long amount = tokenizedLines[i] == null || tokenizedLines[i].isEmpty() ? 0 : Long.parseLong(tokenizedLines[i]) * 1000;
					incomes[i].setEbitda(amount);  //Numbers are in thousands as per format
				}
				continue; 
			}
			
			
		}
		
		return Arrays.asList(incomes).stream().sorted((c1,c2) -> -1 * c1.getPeriodEnd().compareTo(c2.getPeriodEnd())).collect(Collectors.toList());
	}
	
}
