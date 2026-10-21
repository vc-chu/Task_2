package ru.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ru.stellarburgers.model.User;
import ru.stellarburgers.steps.IngredientSteps;
import ru.stellarburgers.steps.OrderSteps;
import ru.stellarburgers.steps.UserSteps;

import java.util.List;

@Epic("Stellar Burgers API")
@Feature("Создание заказа")
class CreateOrderTest {

    private final UserSteps userSteps = new UserSteps();
    private final IngredientSteps ingredientSteps = new IngredientSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private User user;
    private String accessToken;
    private List<String> ingredientIds;

    @BeforeEach
    void setUp() {
        user = userSteps.createUniqueUser();
        accessToken = userSteps.extractAccessToken(userSteps.register(user));
        ingredientIds = ingredientSteps.getTwoIngredientIds();
    }

    @AfterEach
    void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Авторизованный пользователь может создать заказ с ингредиентами")
    @Description("POST /api/orders с валидными ингредиентами и токеном возвращает 200")
    void createOrderWithAuthAndIngredientsTest() {
        Response response = orderSteps.createWithAuth(ingredientIds, accessToken);

        orderSteps.assertOrderCreated(response);
    }

    @Test
    @DisplayName("Неавторизованный пользователь может создать заказ с ингредиентами")
    @Description("POST /api/orders без Authorization и с валидными ингредиентами возвращает 200")
    void createOrderWithoutAuthAndWithIngredientsTest() {
        Response response = orderSteps.createWithoutAuth(ingredientIds);

        orderSteps.assertOrderCreated(response);
    }

    @Test
    @DisplayName("Заказ без ингредиентов возвращает ошибку")
    @Description("POST /api/orders с пустым массивом ingredients возвращает 400")
    void createOrderWithoutIngredientsTest() {
        Response response = orderSteps.createWithAuth(List.of(), accessToken);

        orderSteps.assertError(
                response,
                400,
                "Ingredient ids must be provided"
        );
    }

    @Test
    @DisplayName("Заказ с неверным хешем ингредиента возвращает ошибку")
    @Description("POST /api/orders с несуществующим id ингредиента возвращает 500")
    void createOrderWithInvalidIngredientHashTest() {
        Response response = orderSteps.createWithAuth(
                List.of("invalid-ingredient-hash"),
                accessToken
        );

        assertEquals(500, response.getStatusCode());
    }
}