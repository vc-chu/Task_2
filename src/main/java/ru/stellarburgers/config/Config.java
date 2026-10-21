package ru.stellarburgers.config;

public final class Config {
    public static final String BASE_URL = "https://qa-stellarburgers.education-services.ru";
    public static final String API_PREFIX = "/api";

    public static final String REGISTER = API_PREFIX + "/auth/register";
    public static final String LOGIN = API_PREFIX + "/auth/login";
    public static final String USER = API_PREFIX + "/auth/user";
    public static final String ORDERS = API_PREFIX + "/orders";
    public static final String INGREDIENTS = API_PREFIX + "/ingredients";

    private Config() {
    }
}
