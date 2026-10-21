package ru.stellarburgers.client;

import io.restassured.response.Response;
import ru.stellarburgers.config.Config;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class OrderClient {

    public Response create(List<String> ingredients, String accessToken) {
        return given()
                .contentType(JSON)
                .header("Authorization", accessToken)
                .body(Map.of("ingredients", ingredients == null ? List.of() : ingredients))
                .when()
                .post(Config.BASE_URL + Config.ORDERS);
    }

    public Response createWithoutAuth(List<String> ingredients) {
        return given()
                .contentType(JSON)
                .body(Map.of("ingredients", ingredients == null ? List.of() : ingredients))
                .when()
                .post(Config.BASE_URL + Config.ORDERS);
    }

    public Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(Config.BASE_URL + Config.ORDERS);
    }

    public Response getUserOrdersWithoutAuth() {
        return given()
                .when()
                .get(Config.BASE_URL + Config.ORDERS);
    }
}
