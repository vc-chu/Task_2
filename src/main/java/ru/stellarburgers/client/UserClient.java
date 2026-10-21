package ru.stellarburgers.client;

import io.restassured.response.Response;
import ru.stellarburgers.config.Config;
import ru.stellarburgers.model.Credentials;
import ru.stellarburgers.model.User;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class UserClient {

    public Response register(User user) {
        return given()
                .contentType(JSON)
                .body(user)
                .when()
                .post(Config.BASE_URL + Config.REGISTER);
    }

    public Response login(Credentials credentials) {
        return given()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post(Config.BASE_URL + Config.LOGIN);
    }

    public Response updateUser(User user, String accessToken) {
        return given()
                .contentType(JSON)
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(Config.BASE_URL + Config.USER);
    }

    public Response updateUserWithoutAuth(User user) {
        return given()
                .contentType(JSON)
                .body(user)
                .when()
                .patch(Config.BASE_URL + Config.USER);
    }

    public Response delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Config.BASE_URL + Config.USER);
    }
}
