package com.gcd.input.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="gcdTable")
public class Gcd {
	
	@Id
	private int id;

	@Column(nullable = false)
	private int firstNumber;
	
	@Column(nullable = false)
	private int SecondNumber;
	
	@Column
	private int value;

	public int getFirstNumber() {
		return firstNumber;
	}

	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}

	public int getSecondNumber() {
		return SecondNumber;
	}

	public void setSecondNumber(int secondNumber) {
		SecondNumber = secondNumber;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
