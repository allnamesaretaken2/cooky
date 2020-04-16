package de.cooky.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {

	List<ShoppingItem> findByNameIn(Set<String> ingredientNames);

}
