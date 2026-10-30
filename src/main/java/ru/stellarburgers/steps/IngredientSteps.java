package ru.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.client.IngredientClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IngredientSteps {
    private final IngredientClient ingredientClient = new IngredientClient();

    @Step("Получить два валидных id ингредиентов")
    public List<String> getTwoIngredientIds() {
        Response response = ingredientClient.getIngredients();

        assertEquals(200, response.getStatusCode());
        List<String> ids = response.jsonPath().getList("data._id");

        assertFalse(ids.isEmpty(), "Список ингредиентов пуст");
        return ids.subList(0, Math.min(2, ids.size()));
    }
}