package site.stellaburgers.OrderTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.stellaburgers.Order.OrderCreate;
import site.stellaburgers.Order.OrderStep;
import site.stellaburgers.User.UserCreate;
import site.stellaburgers.User.UserStep;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class OrderGetTest {
    private OrderStep orderStep;
    private OrderCreate orderCreate;
    private UserStep userStep;
    private UserCreate userCreate;
    private String accessTokenUser;


    @Before
    public void setUp() {
        //Создаем пользователя с заданными параметрами
        userStep = new UserStep();
        userCreate = UserCreate.getDataGeneratorUser();
        ValidatableResponse userResponse = userStep.createNewUser(userCreate);
        accessTokenUser = userResponse.extract().path("accessToken").toString().substring(7);
        //Получаем список ингредиентов
        orderStep = new OrderStep();
        orderCreate = new OrderCreate();
        ValidatableResponse response = orderStep.getIngredientsOrder();
        //Получаем id ингредиентов из ответа сервера
        List<String> ingredients = response.extract().body().jsonPath().getList("data._id[0,1]");
        orderCreate.setIngredients(ingredients);
        //Создаем заказ с заданными параметрами
        orderStep.orderCreateWithTokenUser(accessTokenUser, orderCreate);
    }

    @Test
    @DisplayName("Получение заказа авторизированным пользователем")
    @Description("Успешное получение заказа конкретного авторизированного пользователя")
    public void getListOrderUserWithTokenUser() {
        //Получаем заказ авторизированным пользователем
        ValidatableResponse response = orderStep.getOrderUserWithTokenUser(accessTokenUser);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true), "orders.number", notNullValue());
    }

    @Test
    @DisplayName("Получение заказа неавторизированным пользователем")
    @Description("Проверка невозможности получения заказа неавторизированным пользователем")
    public void getListOrderUserWithOutTokenUser() {
        //Получаем заказ неавторизированным пользователем
        ValidatableResponse response = orderStep.getOrderUserWithOutTokenUser();
        //Проверяем, что статус код 401 и возвращается ожидаемое тело ответа
        response.assertThat()
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

