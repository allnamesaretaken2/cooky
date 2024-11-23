package de.cooky.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SupermarketArea {

    @Id
    //use this strategy to prevent hibernate from opening new transactions
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** required */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
