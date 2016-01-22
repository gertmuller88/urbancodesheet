package com.pitang.util.datastructure;

public class Duple<T1, T2> {
	
	private final T1 value1;
	
	private final T2 value2;

	public Duple(final T1 value1, final T2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public T1 getValue1() {
		return this.value1;
	}
	
	public T2 getValue2() {
		return this.value2;
	}
	
}
