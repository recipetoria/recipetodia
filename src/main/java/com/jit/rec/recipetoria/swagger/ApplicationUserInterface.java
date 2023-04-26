package com.jit.rec.recipetoria.swagger;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;

public interface ApplicationUserInterface {

    @Operation(summary = "Create an ingredient in Shopping List",
            description = """
                    send: IngredientDTO\n
                             "id": 0,  --unused\n
                             "name": "string", --required\n
                             "amount": 0, --oprional\n
                             "measurementUnit": "KG", --optional\n
                             "applicationUserId": 0  --unused\n
                           }!\n
                    response: IngredientDTO with all fields filled
                            "id": 0,  \n
                             "name": "string", \n
                             "amount": 0, \n
                             "measurementUnit": "KG", \n
                             "applicationUserId": 0  --ShoppingList owner (current ApplicationUser)\n
                    """)
    @Parameter(name = "name", required = true)
    @Parameter(name = "mu", required = false)
    ResponseEntity<ApiResponse> createIngredient(IngredientDTO newIngredientInfo);
}
