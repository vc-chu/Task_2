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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Stellar Burgers API")
@Feature("Логин пользователя")
class LoginUserTest {

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
    @DisplayName("Успешный логин под существующим пользователем")
    @Description("POST /api/auth/login возвращает 200, success=true и токены")
    void loginWithExistingUserTest() {
        Response response = userSteps.login(user);

        assertEquals(200, response.getStatusCode());
        assertEquals("true", response.jsonPath().getString("success"));
        assertEquals(user.getEmail(), response.jsonPath().getString("user.email"));
        assertEquals(user.getName(), response.jsonPath().getString("user.name"));
        assertNotNull(response.jsonPath().getString("accessToken"));
        assertNotNull(response.jsonPath().getString("refreshToken"));
    }

    @Test
    @DisplayName("Логин с неверным email и паролем возвращает ошибку")
    @Description("POST /api/auth/login с несуществующими учётными данными возвращает 401")
    void loginWithInvalidCredentialsTest() {
        User invalidUser = new User(
                "invalid-" + System.nanoTime() + "@example.com",
                "invalid-password",
                "Invalid User"
        );

        Response response = userSteps.login(invalidUser);

        userSteps.assertError(
                response,
                401,
                "email or password are incorrect"
        );
    }
}
