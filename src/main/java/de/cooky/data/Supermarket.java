package de.cooky.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}, name = "uk_name"))
public class Supermarket {

    @Id
    //use this strategy to prevent hibernate from opening new transactions
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("itemOrder ASC")
    @JoinColumn(name = "id_supermarket")
    private Set<SupermarketArea> areas = new LinkedHashSet<>();

    public Supermarket() {

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

    public Set<SupermarketArea> getAreas() {
        return areas;
    }

    public void setAreas(Set<SupermarketArea> areas) {
        this.areas = areas;
    }
}
