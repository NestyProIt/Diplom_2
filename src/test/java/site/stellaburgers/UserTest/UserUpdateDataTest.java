package site.stellaburgers.UserTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellaburgers.User.UserCreate;
import site.stellaburgers.User.UserStep;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class UserUpdateDataTest {
    private final String newUserEmail = "qavasya@yandex.ru";
    private final String newUserName = "Васенька";
    private final String newUserPassword = "tjgjh122";
    private UserStep userStep;
    private UserCreate userCreate;
    private String accessTokenUser;

    @Before
    public void setUp() {
        //Создаем пользователя с заданными параметрами
        userStep = new UserStep();
        userCreate = UserCreate.getDataGeneratorUser();
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Получаем accessToken пользователя из ответа сервера
        accessTokenUser = userResponse.extract().path("accessToken").toString().substring(7);

    }

    @Test
    @DisplayName("Изменение email у авторизированного пользователя")
    @Description("Успешное обновление email авторизованного пользователя на уникальный адрес")
    public void updateAuthorizedUserEmail() {
        //Устанавливаем новый email для пользователя
        userCreate.setEmail(newUserEmail);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithTokenUser(accessTokenUser, userCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", equalTo(newUserEmail), "user.name", notNullValue());
        //Проверяем, что данные пользователя изменились
        ValidatableResponse userResponseGet = userStep.getUserData(accessTokenUser);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponseGet.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", equalTo(newUserEmail), "user.name", notNullValue());
    }

    @Test
    @DisplayName("Изменение name у авторизированного пользователя")
    @Description("Успешное обновление name авторизованного пользователя на уникальное name")
    public void updateAuthorizedUserName() {
        //Устанавливаем новое name для пользователя
        userCreate.setName(newUserName);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithTokenUser(accessTokenUser, userCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", notNullValue(), "user.name", equalTo(newUserName));
        //Проверяем, что данные пользователя изменились
        ValidatableResponse userResponseGet = userStep.getUserData(accessTokenUser);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponseGet.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", notNullValue(), "user.name", equalTo(newUserName));
    }

    @Test
    @DisplayName("Изменение password у авторизированного пользователя")
    @Description("Успешное обновление password авторизованного пользователя на уникальный password")
    public void updateAuthorizedUserPassword() {
        //Устанавливаем новый password для пользователя
        userCreate.setPassword(newUserPassword);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithTokenUser(accessTokenUser, userCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", notNullValue(), "user.name", notNullValue());
        //Проверяем, что данные пользователя изменились
        ValidatableResponse userResponseGet = userStep.getUserData(accessTokenUser);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponseGet.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", notNullValue(), "user.name", notNullValue());
    }

    @Test
    @DisplayName("Изменение email, name, password у авторизированного пользователя")
    @Description("Успешное обновление email, name, password авторизованного пользователя на уникальный email, name, password")
    public void updateAuthorizedUserEmailNamePassword() {
        //Устанавливаем новый email для пользователя
        userCreate.setEmail(newUserEmail);
        //Устанавливаем новое name для пользователя
        userCreate.setName(newUserName);
        //Устанавливаем новый password для пользователя
        userCreate.setPassword(newUserPassword);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithTokenUser(accessTokenUser, userCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", equalTo(newUserEmail), "user.name", equalTo(newUserName));
        //Проверяем, что данные пользователя изменились
        ValidatableResponse userResponseGet = userStep.getUserData(accessTokenUser);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponseGet.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", equalTo(newUserEmail), "user.name", equalTo(newUserName));

    }

    @Test
    @DisplayName("Изменение авторизированному пользователю email на email, который уже используется")
    @Description("Проверка невозможности изменения email авторизованного пользователя на email, который уже используется другим аккаунтом")
    public void updateAuthorizedUserDuplicateEmail() {
        //Получаем данные созданного 1 пользователя
        ValidatableResponse userResponseGet = userStep.getUserData(accessTokenUser);
        //Получаем  email 1 пользователя из ответа сервера
        String userEmail = userResponseGet.extract().path("user.email").toString();
        //Создаем нового 2 пользователя
        userCreate = UserCreate.getDataGeneratorUser();
        ValidatableResponse userResponseTwo = userStep.createNewUser(userCreate);
        //Получаем accessToken 2 пользователя из ответа сервера
        accessTokenUser = userResponseTwo.extract().path("accessToken").toString().substring(7); //
        //Устанавливаем новый email для 2 пользователя равным текущему email 1 пользователя
        userCreate.setEmail(userEmail);
        //Отправляем запрос на обновление данных 2 пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithTokenUser(accessTokenUser, userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("User with such email already exists"));
    }

    @Test
    @DisplayName("Изменение email неавторизированного пользователя")
    @Description("Проверка невозможности изменения email неавторизованного пользователя")
    public void updateNotRegisteredUserEmail() {
        //Устанавливаем новый email для пользователя
        userCreate.setEmail(newUserEmail);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithOutTokenUser(userCreate);
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение name неавторизированного пользователя")
    @Description("Проверка невозможности изменения name неавторизированного пользователя")
    public void updateNotRegisteredUserName() {
        //Устанавливаем новое name для пользователя
        userCreate.setName(newUserName);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithOutTokenUser(userCreate);
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение password неавторизированного пользователя")
    @Description("Проверка невозможности изменения password неавторизированного пользователя")
    public void updateNotRegisteredUserPassword() {
        //Устанавливаем новый password для пользователя
        userCreate.setPassword(newUserPassword);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithOutTokenUser(userCreate);
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение email, name, password  неавторизированного пользователя")
    @Description("Проверка невозможности изменения email, name, password  неавторизированного пользователя")
    public void updateNotRegisteredUserEmailNamePassword() {
        //Устанавливаем новый email для пользователя
        userCreate.setEmail(newUserEmail);
        //Устанавливаем новое name для пользователя
        userCreate.setName(newUserName);
        //Устанавливаем новый password для пользователя
        userCreate.setPassword(newUserPassword);
        //Отправляем запрос на обновление данных пользователя
        ValidatableResponse userResponsePath = userStep.updateUserDataWithOutTokenUser(userCreate);
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        userResponsePath.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }

    @After//очистка данных после теста
    public void deleteUserAfterTest() {
        if (accessTokenUser != null) {
            userStep.deleteUser(accessTokenUser);
        }

    }
}

