package ru.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.client.OrderClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderSteps {
    private final OrderClient orderClient = new OrderClient();

    @Step("Создать заказ с авторизацией")
    public Response createWithAuth(List<String> ingredients, String accessToken) {
        return orderClient.create(ingredients, accessToken);
    }

    @Step("Создать заказ без авторизации")
    public Response createWithoutAuth(List<String> ingredients) {
        return orderClient.createWithoutAuth(ingredients);
    }

    @Step("Получить заказы пользователя с авторизацией")
    public Response getUserOrders(String accessToken) {
        return orderClient.getUserOrders(accessToken);
    }

    @Step("Получить заказы пользователя без авторизации")
    public Response getUserOrdersWithoutAuth() {
        return orderClient.getUserOrdersWithoutAuth();
    }

    @Step("Проверить успешное создание заказа")
    public void assertOrderCreated(Response response) {
        assertEquals(200, response.getStatusCode());
        assertEquals("true", response.jsonPath().getString("success"));
        assertNotNull(response.jsonPath().getString("order.number"));
        assertTrue(response.jsonPath().getList("order.ingredients").size() > 0);
    }

    @Step("Проверить успешное получение заказов")
    public void assertOrdersReceived(Response response) {
        assertEquals(200, response.getStatusCode());
        assertEquals("true", response.jsonPath().getString("success"));
        assertNotNull(response.jsonPath().getList("orders"));
    }

    @Step("Проверить ошибку: {message}")
    public void assertError(Response response, int expectedStatus, String message) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals("false", response.jsonPath().getString("success"));
        assertEquals(message, response.jsonPath().getString("message"));
    }
}
