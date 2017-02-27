package com.dando.stockboss;

import java.util.Date;

import lombok.Data;

@Data
public class IncomeEntry {
	private Date periodEnd;
	private long operatingIncome;
	private long netIncome;
	private long ebitda;
	private long revenue;
	private long costOfRevenue;
	private long pretaxIncome;
}
