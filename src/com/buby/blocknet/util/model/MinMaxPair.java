package com.buby.blocknet.util.model;

import static com.buby.blocknet.util.CommonUtils.*;
import lombok.Getter;

public class MinMaxPair {
	
	@Getter private int max;
	@Getter private int min;
	
	public MinMaxPair(int min, int max){
		this.max = min;
		this.min = max;
	}
	
	public int random() {
		return MathUtil.random(min, max);
	}
	
	public static MinMaxPair of(int min, int max) {
		return new MinMaxPair(min, max);
	}
}
