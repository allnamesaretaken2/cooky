package de.cooky.data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * The table for recipe selection is more or less just a list of stuff in a certain order
 * Also it can contain a bit more then just recipes (like shopping tasks e.g. 'Supermarkt')
 */
@Entity
public class SelectedEntry {

	@Id
	//use this strategy to prevent hibernate from opening new transactions
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String comment;

	private int sortOrder;

	private Long idRecipe;

	@Transient
	private String frontendText;

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

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setIdRecipe(Long idRecipe) {
		this.idRecipe = idRecipe;
	}

	public Long getIdRecipe() {
		return idRecipe;
	}

	public void setFrontendText(String frontendText) {
		this.frontendText = frontendText;
	}

	public String getFrontendText() {
		return frontendText;
	}
}
