package de.cooky.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class ShoppingItem {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String name;

	private Float amount;

	private String unit;

	private int itemOrder;

	public ShoppingItem() {

	}

	public void setId(Long id) {
		this.id = id;
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

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getAmount() {
		return amount;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}

	public int getItemOrder() {
		return itemOrder;
	}
}
