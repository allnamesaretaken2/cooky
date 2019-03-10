package de.cooky.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Recipe {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToMany(cascade=CascadeType.ALL)
	private Set<Ingredient> ingredients = new HashSet<>();

	@Enumerated(EnumType.STRING)
	private UnitType unit;

	public Recipe() {

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

	public UnitType getUnit() {
		return unit;
	}

	public void setUnit(UnitType unit) {
		this.unit = unit;
	}

	public Set<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public static enum UnitType {

		KILOGRAMM, GRAMM, SPOONS;
	}
}
