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

public class UserLoginTest {
    private UserStep userStep;
    private UserCreate userCreate;
    private String accessTokenUser;


    @Before
    public void setUp() {
        userStep = new UserStep();
        userCreate = UserCreate.getDataGeneratorUser();
        userStep.createNewUser(userCreate);
    }

    @Test
    @DisplayName("Авторизация пользователя в системе")
    @Description("Уникальный пользователь может авторизоваться при передаче валидных значений в параметрах email, password")
    public void checkUserAuthorizationWithAllParameters() {
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "accessToken", notNullValue(), "refreshToken", notNullValue(), "user.email", notNullValue(), "user.name", notNullValue());
        //Получаем accessToken пользователя  из ответа сервера
        ValidatableResponse setloginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        accessTokenUser = setloginResponse.extract().path("accessToken").toString().substring(7);//
    }

    @Test
    @DisplayName("Авторизация пользователя без email")
    @Description("Пользователь не может авторизоваться при передаче пустого значения в параметре email")
    public void checkUserAuthorizationWithOutEmail() {
        //Устанавливаем пустой email для пользователя
        userCreate.setEmail("");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без password")
    @Description("Пользователь не может авторизоваться при передаче пустого значения в параметре password")
    public void checkUserAuthorizationWithOutPassword() {
        //Устанавливаем пустой password для пользователя
        userCreate.setPassword("");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без email, password")
    @Description("Пользователь не может авторизоваться при передаче пустых значений в параметрах email, password")
    public void checkUserAuthorizationWithOutEmailPassword() {
        //Устанавливаем пустой email для пользователя
        userCreate.setEmail("");
        //Устанавливаем пустой password для пользователя
        userCreate.setPassword("");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с несуществующим email")
    @Description("Пользователь не может авторизоваться с несуществующим email")
    public void checkUserAuthorizationWithEmailNotRegistered() {
        //Устанавливаем несуществующий email для пользователя
        userCreate.setEmail("0");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с несуществующим password")
    @Description("Пользователь не может авторизоваться с несуществующим password")
    public void checkUserAuthorizationWithPasswordNotRegistered() {
        //Устанавливаем несуществующий password для пользователя
        userCreate.setPassword("0");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с несуществующим email, password")
    @Description("Пользователь не может авторизоваться с несуществующими email, password")
    public void checkUserAuthorizationWithEmailPasswordNotRegistered() {
        //Устанавливаем несуществующий email для пользователя
        userCreate.setEmail("0");
        //Устанавливаем несуществующий password для пользователя
        userCreate.setPassword("0");
        //Авторизация пользователя с заданными параметрами
        ValidatableResponse setUserLoginResponse = userStep.loginUser(UserLogin.getUserLogin(userCreate));
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        setUserLoginResponse.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false), "message", equalTo("email or password are incorrect"));
    }

    @After//очистка данных после теста
    public void deleteUserAfterTest() {
        if (accessTokenUser != null) {
            userStep.deleteUser(accessTokenUser);
        }

    }

}


