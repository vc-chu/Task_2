package ru.stellarburgers.client;

import io.restassured.response.Response;
import ru.stellarburgers.config.Config;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    public Response getIngredients() {
        return given()
                .when()
                .get(Config.BASE_URL + Config.INGREDIENTS);
    }
}
