package edu.ncsu.csc.CoffeeMaker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * The IngredientService is used to handle CRUD operations on the Ingredient
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /** IngredientRepository, to be autowired in by Spring and provide CRUD operations on Ingredient model. */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository<Ingredient, Long> getRepository () {
        return ingredientRepository;
    }

}
