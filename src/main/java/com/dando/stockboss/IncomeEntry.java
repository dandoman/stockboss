package com.dando.stockboss;

import java.sql.Date;

import lombok.Data;

@Data
public class IncomeEntry {
	private Date periodStart;
	private Date periodEnd;
	private long operatingIncome;
	private long netIncome;
	private long ebitda;
}
