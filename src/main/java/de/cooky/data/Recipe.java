package de.cooky.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Recipe {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String name;

	private String description;

	/** a cookbook-page or a link */
	private String source;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Ingredient> ingredients = new HashSet<>();

	public Recipe() {

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

	public Set<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public static enum UnitType {

		KILOGRAMM, GRAMM, SPOONS;
	}
}
