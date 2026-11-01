package ru.stellarburgers.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.stellarburgers.client.UserClient;
import ru.stellarburgers.model.Credentials;
import ru.stellarburgers.model.User;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSteps {
    private final UserClient userClient = new UserClient();

    @Step("Создать уникального пользователя")
    public User createUniqueUser() {
        String unique = randomAlphanumeric(10);
        return new User(
                "test-" + unique + "@example.com",
                "Pass-" + unique,
                "Name-" + unique
        );
    }

    @Step("Зарегистрировать пользователя")
    public Response register(User user) {
        return userClient.register(user);
    }

    @Step("Залогиниться под пользователем")
    public Response login(User user) {
        return userClient.login(new Credentials(user.getEmail(), user.getPassword()));
    }

    @Step("Получить accessToken из ответа")
    public String extractAccessToken(Response response) {
        return response.jsonPath().getString("accessToken");
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        if (accessToken != null && !accessToken.isBlank()) {
            userClient.delete(accessToken);
        }
    }

    @Step("Изменить email пользователя с авторизацией")
    public Response updateEmailWithAuth(User user, String accessToken) {
        String newEmail = "updated-" + randomAlphanumeric(8) + "@example.com";
        user.setEmail(newEmail);
        return userClient.updateUser(user, accessToken);
    }

    @Step("Изменить имя пользователя с авторизацией")
    public Response updateNameWithAuth(User user, String accessToken) {
        String newName = "Updated-" + randomAlphanumeric(8);
        user.setName(newName);
        return userClient.updateUser(user, accessToken);
    }

    @Step("Изменить пароль пользователя с авторизацией")
    public Response updatePasswordWithAuth(User user, String accessToken) {
        String newPassword = "NewPass-" + randomAlphanumeric(8);
        user.setPassword(newPassword);
        return userClient.updateUser(user, accessToken);
    }

    @Step("Изменить email пользователя без авторизации")
    public Response updateEmailWithoutAuth(User user) {
        user.setEmail("updated-" + randomAlphanumeric(8) + "@example.com");
        return userClient.updateUserWithoutAuth(user);
    }

    @Step("Изменить имя пользователя без авторизации")
    public Response updateNameWithoutAuth(User user) {
        user.setName("Updated-" + randomAlphanumeric(8));
        return userClient.updateUserWithoutAuth(user);
    }

    @Step("Изменить пароль пользователя без авторизации")
    public Response updatePasswordWithoutAuth(User user) {
        user.setPassword("NewPass-" + randomAlphanumeric(8));
        return userClient.updateUserWithoutAuth(user);
    }

    @Step("Проверить успешный ответ")
    public void assertSuccess(Response response, int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals("true", response.jsonPath().getString("success"));
    }

    @Step("Проверить, что пользователь создан и данные совпадают")
    public void assertUserCreated(Response response, User user) {
        assertSuccess(response, 200);

        String actualEmail = response.jsonPath().getString("user.email");

        assertTrue(
                user.getEmail().equalsIgnoreCase(actualEmail),
                "Email должен совпадать без учёта регистра. Ожидался: "
                        + user.getEmail() + ", получен: " + actualEmail
        );

        assertEquals(user.getName(), response.jsonPath().getString("user.name"));
        assertNotNull(response.jsonPath().getString("accessToken"));
        assertNotNull(response.jsonPath().getString("refreshToken"));
    }

    @Step("Проверить ошибку: {message}")
    public void assertError(Response response, int expectedStatus, String message) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals("false", response.jsonPath().getString("success"));
        assertEquals(message, response.jsonPath().getString("message"));
    }
}
