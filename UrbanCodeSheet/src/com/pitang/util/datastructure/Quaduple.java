package com.pitang.util.datastructure;

public class Quaduple<T1, T2, T3, T4> {
	
	private final T1 value1;
	
	private final T2 value2;
	
	private final T3 value3;
	
	private final T4 value4;

	public Quaduple(final T1 value1, final T2 value2, final T3 value3, final T4 value4) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
	}
	
	public T1 getValue1() {
		return this.value1;
	}
	
	public T2 getValue2() {
		return this.value2;
	}
	
	public T3 getValue3() {
		return this.value3;
	}
	
	public T4 getValue4() {
		return this.value4;
	}
	
}
