package com.dando.stockboss;

import lombok.Getter;

public enum Exchange {
	NYSE("XNYS"),
	NASDAQ("XNAS"),
	TSE("XTSE");
	
	@Getter
	private String stringName;
	private Exchange(String stringName) {
		this.stringName = stringName;
	}
}
