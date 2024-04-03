package de.cooky.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class RecipePart {

    @Id
    //use this strategy to prevent hibernate from opening new transactions
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** optional */
    private String name;

    @Column(length = 4000)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    @JoinColumn(name = "id_recipepart")
    private Set<IngredientToRecipePart> ingredients = new LinkedHashSet<>();

    public RecipePart(){}

    @JsonCreator
    public RecipePart(@JsonProperty("ingredients") LinkedHashSet<IngredientToRecipePart> ingredients){
        //make sure the json deserialization uses a LinkedHashSet to keep the order in the json array
        this.ingredients = ingredients;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Set<IngredientToRecipePart> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientToRecipePart> ingredients) {
        this.ingredients = ingredients;
    }
}
