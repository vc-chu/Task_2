package ru.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.stellarburgers.model.User;
import ru.stellarburgers.steps.IngredientSteps;
import ru.stellarburgers.steps.OrderSteps;
import ru.stellarburgers.steps.UserSteps;

import java.util.List;

@Epic("Stellar Burgers API")
@Feature("Получение заказов пользователя")
class GetUserOrdersTest {

    private final UserSteps userSteps = new UserSteps();
    private final IngredientSteps ingredientSteps = new IngredientSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        user = userSteps.createUniqueUser();
        accessToken = userSteps.extractAccessToken(userSteps.register(user));
    }

    @AfterEach
    void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Авторизованный пользователь получает свои заказы")
    @Description("GET /api/orders с accessToken возвращает 200 и список заказов")
    void getUserOrdersWithAuthTest() {
        List<String> ingredients = ingredientSteps.getTwoIngredientIds();
        orderSteps.createWithAuth(ingredients, accessToken);

        Response response = orderSteps.getUserOrders(accessToken);

        orderSteps.assertOrdersReceived(response);
    }

    @Test
    @DisplayName("Неавторизованный пользователь не получает заказы")
    @Description("GET /api/orders без accessToken возвращает 401")
    void getUserOrdersWithoutAuthTest() {
        Response response = orderSteps.getUserOrdersWithoutAuth();

        orderSteps.assertError(
                response,
                401,
                "You should be authorised"
        );
    }
}
