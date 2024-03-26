package de.cooky.data;

import javax.persistence.*;

@Entity
public class IngredientToRecipe {

	@Id
	//use this strategy to prevent hibernate from opening new transactions
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_ingredient")
	private Ingredient ingredient;

	private Float amount;

	private String unit;

	@Column(name = "sort_order", columnDefinition = "integer default 0 not null")
	private int order = 0;

	public IngredientToRecipe() {

	}

	public Long getId() {
		return id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}
}
