package site.stellaburgers.userTest;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellaburgers.user.UserCreate;
import site.stellaburgers.user.UserLogin;
import site.stellaburgers.user.UserStep;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class UserCreateTest {
    private UserStep userStep;
    private UserCreate userCreate;
    private String accessTokenUser;

    @Before
    public void setUp() {
        userStep = new UserStep();
        userCreate = UserCreate.getDataGeneratorUser();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Уникальный пользователь создается при передаче валидных значений в параметрах email, password, name")
    public void createUserWithAllParameters() {
        //Создаем пользователя с заданными параметрами
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "user.email", notNullValue(), "user.name", notNullValue(), "accessToken", notNullValue(), "refreshToken", notNullValue());
        //Получаем accessToken пользователя  из ответа сервера
        ValidatableResponse setloginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        accessTokenUser = setloginResponse.extract().path("accessToken").toString().substring(7);
    }

    @Test
    @DisplayName("Создание 2-х одинаковых пользователей")
    @Description("Проверка запрета создания двух пользователей с одинаковыми данными (email, password, name)")
    public void createDuplicateUser() {
        //Создаем пользователя с заданными параметрами
        userStep.createNewUser(userCreate);
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("User already exists"));
        //Получаем accessToken пользователя  из ответа сервера
        ValidatableResponse setloginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        accessTokenUser = setloginResponse.extract().path("accessToken").toString().substring(7);//
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Новый пользователь не создается при передаче пустого значения в параметре email")
    public void createUserWithOutEmail() {
        //Устанавливаем пустой email для пользователя
        userCreate.setEmail("");
        //Создаем пользователя с заданными параметрами
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Новый пользователь не создается при передаче пустого значения в параметре password")
    public void createUserWithOutPassword() {
        //Устанавливаем пустой password для пользователя
        userCreate.setPassword("");
        //Создаем пользователя с заданными параметрами
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }


    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Новый пользователь не создается при передаче пустого значения в параметре name")
    public void createUserWithOutName() {
        //Устанавливаем пустой name для пользователя
        userCreate.setName("");
        //Создаем пользователя с заданными параметрами
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без email, password, name")
    @Description("Новый пользователь не создается при передаче пустых значений в параметрах email, password, name")
    public void createUserWithOutEmailPasswordName() {
        //Устанавливаем пустой email для пользователя
        userCreate.setEmail("");
        //Устанавливаем пустой password для пользователя
        userCreate.setPassword("");
        //Устанавливаем пустой name для пользователя
        userCreate.setName("");
        //Создаем пользователя с заданными параметрами
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        //Проверяем, что статус код 403 и возвращается ожидаемое тело ответа
        userResponse.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false), "message", equalTo("Email, password and name are required fields"));
    }

    @After//очистка данных после теста
    public void deleteUserAfterTest() {
        if (accessTokenUser != null) {
            userStep.deleteUser(accessTokenUser);
        }

    }
}
