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
import ru.stellarburgers.steps.UserSteps;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Stellar Burgers API")
@Feature("Изменение данных пользователя")
class UpdateUserTest {

    private final UserSteps userSteps = new UserSteps();
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        user = userSteps.createUniqueUser();
        Response registerResponse = userSteps.register(user);
        accessToken = userSteps.extractAccessToken(registerResponse);
    }

    @AfterEach
    void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Авторизованный пользователь может изменить email")
    @Description("PATCH /api/auth/user изменяет email и возвращает обновлённые данные")
    void updateEmailWithAuthTest() {
        Response response = userSteps.updateEmailWithAuth(user, accessToken);

        userSteps.assertSuccess(response, 200);
        assertEquals(user.getEmail(), response.jsonPath().getString("user.email"));
    }

    @Test
    @DisplayName("Авторизованный пользователь может изменить имя")
    @Description("PATCH /api/auth/user изменяет name и возвращает обновлённые данные")
    void updateNameWithAuthTest() {
        Response response = userSteps.updateNameWithAuth(user, accessToken);

        userSteps.assertSuccess(response, 200);
        assertEquals(user.getName(), response.jsonPath().getString("user.name"));
    }

    @Test
    @DisplayName("Авторизованный пользователь может изменить пароль")
    @Description("PATCH /api/auth/user изменяет password; успешный ответ подтверждает изменение")
    void updatePasswordWithAuthTest() {
        Response response = userSteps.updatePasswordWithAuth(user, accessToken);

        userSteps.assertSuccess(response, 200);
    }

    @Test
    @DisplayName("Неавторизованный пользователь не может изменить email")
    @Description("PATCH /api/auth/user без Authorization возвращает 401")
    void updateEmailWithoutAuthTest() {
        Response response = userSteps.updateEmailWithoutAuth(user);

        userSteps.assertError(
                response,
                401,
                "You should be authorised"
        );
    }

    @Test
    @DisplayName("Неавторизованный пользователь не может изменить имя")
    @Description("PATCH /api/auth/user без Authorization возвращает 401")
    void updateNameWithoutAuthTest() {
        Response response = userSteps.updateNameWithoutAuth(user);

        userSteps.assertError(
                response,
                401,
                "You should be authorised"
        );
    }

    @Test
    @DisplayName("Неавторизованный пользователь не может изменить пароль")
    @Description("PATCH /api/auth/user без Authorization возвращает 401")
    void updatePasswordWithoutAuthTest() {
        Response response = userSteps.updatePasswordWithoutAuth(user);

        userSteps.assertError(
                response,
                401,
                "You should be authorised"
        );
    }
}
