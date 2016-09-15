package com.dando.stockboss;

import java.util.Date;

import lombok.Data;

@Data
public class BalanceSheetEntry {
	private Date periodEnd;
	private long cashAndEquivalents;
	private long totalAssets;
	private long totalLiabilities;
	private long totalEquity;
}
