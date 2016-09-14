package com.dando.stockboss;


import java.util.Date;

import lombok.Data;

@Data
public class CashFlowEntry {
	private Date periodEnd;
	private long dividendPaid;
	private long netCashInvested;
	private long totalFinancing;
	private long stockRepurchased;
	private long netCashChange;
}
