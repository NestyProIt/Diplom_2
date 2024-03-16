package site.stellaburgers.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.stellaburgers.config.Config;

import static io.restassured.RestAssured.given;

public class UserStep extends Config {
    public static final String POST_USER_CREATE = "/api/auth/register"; //API Создание пользователя
    public static final String DELETE_USER = "/api/auth/user"; //API Удаление пользователя
    public static final String POST_LOGIN_USER = "/api/auth/login"; //API Авторизация пользователя в системе
    public static final String GET_USER_DATA = "/api/auth/user"; //API Получение данных о пользователе
    public static final String PATH_USER_DATA = "/api/auth/user"; //API Изменение данных пользователя

    @Step("Создать пользователя")
    public ValidatableResponse createNewUser(UserCreate userCreate) {
        return given()
                .spec(getSpec())
                .body(userCreate)
                .when()
                .post(POST_USER_CREATE)
                .then();
    }

    @Step("Удалить пользователя")
    public ValidatableResponse deleteUser(String accessTokenUser) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessTokenUser)
                .when()
                .delete(DELETE_USER)
                .then();
    }

    @Step("Авторизация пользователя")//по логину и паролю
    public ValidatableResponse loginUser(UserLogin userLogin) {
        return given()
                .spec(getSpec())
                .body(userLogin)
                .when()
                .post(POST_LOGIN_USER)
                .then();
    }

    @Step("Получить данные о пользователе") //по токену
    public ValidatableResponse getUserData(String accessTokenUser) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessTokenUser)
                .get(GET_USER_DATA)
                .then();
    }

    @Step("Изменить данные пользователя с авторизацией")
    public ValidatableResponse updateUserDataWithTokenUser(String accessTokenUser, UserCreate userCreate) {
        return given()
                .spec(getSpec())
                .auth().oauth2(accessTokenUser)
                .body(userCreate)
                .when()
                .patch(PATH_USER_DATA)
                .then();


    }

    @Step("Изменить данные пользователя без авторизации")
    public ValidatableResponse updateUserDataWithOutTokenUser(UserCreate userCreate) {
        return given()
                .spec(getSpec())
                .body(userCreate)
                .when()
                .patch(PATH_USER_DATA)
                .then();
    }
}

