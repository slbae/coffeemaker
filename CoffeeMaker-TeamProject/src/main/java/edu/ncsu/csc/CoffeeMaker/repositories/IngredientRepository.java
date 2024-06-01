package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * Repository for Ingredients
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
