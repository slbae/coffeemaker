package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 * @author Caleb Twigg
 * @author Tyler Bradshaw
 * @author Helen O'Connell
 * @author Sylbi Bae
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    // @GetMapping ( { "/index", "/" } )
    // public String index ( final Model model ) {
    // return "index";
    // }
    @GetMapping ( { "/mainLogin", "/mainLogin.html" } )
    public String index ( final Model model ) {
        return "mainLogin";
    }

    /**
     * On a GET request to /staff, the RecipeController will return
     * /src/main/resources/templates/staffMainPage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/staffMainPage", "/staffMainPage.html" } )
    public String staffMainPage ( final Model model ) {
        return "staffMainPage";
    }

    /**
     * On a GET request to /customer, the RecipeController will return
     * /src/main/resources/templates/customerMainPage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/customerMainPage", "/customerMainPage.html" } )
    public String customerMainPage ( final Model model ) {
        return "customerMainPage";
    }

    /**
     * On a GET request to /login, the RecipeController will return
     * /src/main/resources/templates/customerMainPage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/login", "/secondaryLogin.html" } )
    public String secondaryLogin ( final Model model ) {
        return "secondaryLogin";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/AddRecipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "AddRecipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editRecipe", "/editRecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editRecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addInventory2", "/addInventory2.html" } )
    public String inventoryForm ( final Model model ) {
        return "addInventory2";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    /**
     * On a GET request to /useCaseSeven, the MappingController will return
     * /src/main/resources/templates/AddIngredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/AddIngredient", "/AddIngredient.html" } )
    public String useCaseSeven ( final Model model ) {
        return "AddIngredient";
    }

    /**
     * On a GET request to /orderBeverage, the MappingController will return
     * /src/main/resources/templates/orderBeverage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/orderBeverage", "/orderBeverage.html" } )
    public String orderBeverageForm ( final Model model ) {
        return "orderBeverage";
    }

    /**
     * On a GET request to /pickupOrder, the MappingController will return
     * /src/main/resources/templates/pickupOrder.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/pickupOrder", "/pickupOrder.html" } )
    public String pickupOrderForm ( final Model model ) {
        return "pickupOrder";
    }

    /**
     * On a GET request to /fulfillOrder, the MappingController will return
     * /src/main/resources/templates/fulfillOrder.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/fulfillOrder", "/fulfillOrder.html" } )
    public String fulfillOrder ( final Model model ) {
        return "fulfillOrder";
    }

    /**
     * On a GET request to /orderHistory, the MappingController will return
     * /src/main/resources/templates/orderHistory.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/orderHistory", "/orderHistory.html" } )
    public String orderHistory ( final Model model ) {
        return "orderHistory";
    }

}
