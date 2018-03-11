package com.gcd.output.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="gcdtable")
public class Gcd {
	
	@Id
	private int id;

	@Column
	private int firstNumber;
	
	@Column
	private int secondNumber;

	@Column
	private int value;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFirstNumber() {
		return firstNumber;
	}

	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}

	public int getSecondNumber() {
		return secondNumber;
	}

	public void setSecondNumber(int secondNumber) {
		this.secondNumber = secondNumber;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int gcdValue) {
		this.value = gcdValue;
	}
}
