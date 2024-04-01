package de.cooky.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Recipe {

	@Id
	//use this strategy to prevent hibernate from opening new transactions
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@Column(length = 4000)
	private String description;

	private int persons;

	/** a cookbook-page or a link */
	private String source;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order ASC")
	@JoinColumn(name = "id_recipe")
	private Set<IngredientToRecipe> ingredients = new HashSet<>();

	private boolean selected;

	private Integer durationInMinutes;
	
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

	public Set<IngredientToRecipe> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<IngredientToRecipe> ingredients) {
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

	public void setPersons(int persons) {
		this.persons = persons;
	}

	public int getPersons() {
		return persons;
	}
	
	public boolean getSelected() {
		return this.selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setDurationInMinutes(Integer durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public Integer getDurationInMinutes() {
		return durationInMinutes;
	}
}
