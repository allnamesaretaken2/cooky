package de.cooky.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.cooky.definitions.Unit;

@Entity
public class Ingredient {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private Float amount;

	@Enumerated(EnumType.STRING)
	private Unit unit;

	public Ingredient() {

	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
}
