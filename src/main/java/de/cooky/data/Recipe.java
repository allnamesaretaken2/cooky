package de.cooky.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Recipe {

	@Id
	//use this strategy to prevent hibernate from opening new transactions
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//does the notblank-annotation even work?
	@NotBlank
	private String name;

	@Column(length = 4000)
	private String description;

	private int persons;

	/** a cookbook-page or a link */
	private String source;

	private boolean selected;

	private Integer durationInMinutes;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("itemOrder ASC")
	@JoinColumn(name = "id_recipe")
	private Set<RecipePart> recipeParts = new LinkedHashSet<>();
	
	public Recipe() {

	}

	@JsonCreator
	public Recipe(@JsonProperty("recipeParts") LinkedHashSet<RecipePart> recipeParts){
		//make sure the json deserialization uses a LinkedHashSet to keep the order in the json array
		this.recipeParts = recipeParts;
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

	public void setRecipeParts(Set<RecipePart> recipeParts) {
		this.recipeParts = recipeParts;
	}

	public Set<RecipePart> getRecipeParts() {
		return recipeParts;
	}
}
