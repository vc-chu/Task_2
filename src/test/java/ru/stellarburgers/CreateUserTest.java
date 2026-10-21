package ru.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.stellarburgers.model.User;
import ru.stellarburgers.steps.UserSteps;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Stellar Burgers API")
@Feature("Создание пользователя")
class CreateUserTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @AfterEach
    void tearDown() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("POST /api/auth/register создаёт пользователя с уникальным email и возвращает токены")
    void createUniqueUserTest() {
        User user = userSteps.createUniqueUser();

        Response response = userSteps.register(user);
        accessToken = userSteps.extractAccessToken(response);

        userSteps.assertUserCreated(response, user);
    }

    @Test
    @DisplayName("Нельзя создать уже зарегистрированного пользователя")
    @Description("Повторная регистрация с тем же email возвращает 403 и ошибку")
    void createExistingUserTest() {
        User user = userSteps.createUniqueUser();

        Response firstResponse = userSteps.register(user);
        accessToken = userSteps.extractAccessToken(firstResponse);

        Response duplicateResponse = userSteps.register(user);

        userSteps.assertError(
                duplicateResponse,
                403,
                "User already exists"
        );
    }

    @Test
    @DisplayName("Нельзя создать пользователя без обязательного поля email")
    @Description("POST /api/auth/register без email возвращает 403 и ошибку")
    void createUserWithoutEmailTest() {
        User user = userSteps.createUniqueUser();
        user.setEmail(null);

        Response response = userSteps.register(user);

        userSteps.assertError(
                response,
                403,
                "Email, password and name are required fields"
        );
    }

    @Test
    @DisplayName("Нельзя создать пользователя без обязательного поля password")
    @Description("POST /api/auth/register без password возвращает 403 и ошибку")
    void createUserWithoutPasswordTest() {
        User user = userSteps.createUniqueUser();
        user.setPassword(null);

        Response response = userSteps.register(user);

        userSteps.assertError(
                response,
                403,
                "Email, password and name are required fields"
        );
    }

    @Test
    @DisplayName("Нельзя создать пользователя без обязательного поля name")
    @Description("POST /api/auth/register без name возвращает 403 и ошибку")
    void createUserWithoutNameTest() {
        User user = userSteps.createUniqueUser();
        user.setName(null);

        Response response = userSteps.register(user);

        userSteps.assertError(
                response,
                403,
                "Email, password and name are required fields"
        );
    }
}
