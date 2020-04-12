package de.cooky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cooky.data.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {

	

}
