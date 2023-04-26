package com.jit.rec.recipetoria.swagger;

import com.jit.rec.recipetoria.dto.IngredientDTO;
import com.jit.rec.recipetoria.entity.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;

public interface ApplicationUserInterface {

    @Operation(summary = "!!!!!!!!!!!!!!!",
            description = """
                    !!!!!!!!!!!!!!!\n
                    !!!!!!!!\n
                    !!!!!!!!
                    """)
    @Parameter(name = "name", required = true)
    @Parameter(name = "mu", required = false)
    ResponseEntity<ApiResponse> createIngredient(IngredientDTO newIngredientInfo);
}
