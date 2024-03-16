package site.stellaburgers.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.stellaburgers.config.Config;

import static io.restassured.RestAssured.given;

public class OrderStep extends Config {
    public static final String GET_ORDER_INGREDIENTS = "/api/ingredients"; //API Получение данных об ингредиентах
    public static final String POST_ORDER_CREATE = "/api/orders"; //API Создание заказа
    public static final String GET_ORDER_USER = "/api/orders"; //API Получить заказы конкретного пользователя

    @Step("Получить данных об ингредиентах")
    public ValidatableResponse getIngredientsOrder() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDER_INGREDIENTS)
                .then();
    }

    @Step("Создать заказ авторизированным пользователем")
    public ValidatableResponse orderCreateWithTokenUser(String accessTokenUser, OrderCreate orderCreate) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessTokenUser)
                .body(orderCreate)
                .when()
                .post(POST_ORDER_CREATE)
                .then();
    }

    @Step("Создать заказ неавторизированным пользователем")
    public ValidatableResponse orderCreateWithOutTokenUser(OrderCreate orderCreate) {
        return given()
                .spec(getSpec())
                .body(orderCreate)
                .when()
                .post(POST_ORDER_CREATE)
                .then();
    }

    @Step("Получить заказ авторизированным пользователем")
    public ValidatableResponse getOrderUserWithTokenUser(String accessTokenUser) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessTokenUser)
                .when()
                .get(GET_ORDER_USER)
                .then();
    }

    @Step("Получить заказ неавторизированным пользователем")
    public ValidatableResponse getOrderUserWithOutTokenUser() {
        return given()
                .spec(getSpec())
                .when()
                .get(GET_ORDER_USER)
                .then();
    }
}
