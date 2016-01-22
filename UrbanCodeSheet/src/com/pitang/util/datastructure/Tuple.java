package com.pitang.util.datastructure;

public class Tuple<T1, T2, T3> {

	private final T1 value1;

	private final T2 value2;

	private final T3 value3;

	public Tuple(final T1 value1, final T2 value2, final T3 value3) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
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

}
