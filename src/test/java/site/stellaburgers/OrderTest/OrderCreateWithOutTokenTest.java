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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;


public class OrderCreateWithOutTokenTest {

    List<String> invalidIngredients = new ArrayList<>();
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
        //Получаем accessToken пользователя из ответа сервера
        accessTokenUser = userResponse.extract().path("accessToken").toString().substring(7);
        orderCreate = new OrderCreate();
        orderStep = new OrderStep();
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизированным пользователем")
    @Description("Успешное создание заказа при передаче валидного списка ингредиентов")
    public void createOrderWithOutTokenUserWithValidIngredients() {
        //Получаем список ингредиентов
        ValidatableResponse response = orderStep.getIngredientsOrder();
        //Получаем id ингредиентов из ответа сервера
        List<String> ingredients = response.extract().body().jsonPath().getList("data._id[0,1]");
        //Создаем заказ с заданными параметрами
        orderCreate.setIngredients(ingredients);
        ValidatableResponse orderResponse = orderStep.orderCreateWithOutTokenUser(orderCreate);
        //Проверяем, что статус код 200 и возвращается ожидаемое тело ответа
        orderResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов неавторизированным пользователем")
    @Description("Проверка невозможности создания заказа при передаче пустого списка ингредиентов")
    public void createOrderWithOutTokenUserWithOutIngredients() {
        //Устанавливаем пустой список ингредиентов
        orderCreate.setIngredients(Collections.emptyList());
        //Создаем заказ с заданными параметрами
        ValidatableResponse orderResponse = orderStep.orderCreateWithOutTokenUser(orderCreate);
        //Проверяем, что статус код 400 и возвращается ожидаемое тело ответа
        orderResponse.assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false), "message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиента неавторизированным пользователем")
    @Description("Проверка невозможности создания заказа при передаче невалидного хеша ингредиента")
    public void createOrderWithOutTokenUserWithInvalidHashIngredients() {
        //Устанавливаем невалидный хеш ингредиента
        invalidIngredients.add("0");
        orderCreate.setIngredients(invalidIngredients);
        //Создаем заказ с заданными параметрами
        ValidatableResponse orderResponse = orderStep.orderCreateWithOutTokenUser(orderCreate);
        //Проверяем, что статус код 500
        orderResponse.assertThat()
                .statusCode(500);
    }

    @After//очистка данных после теста
    public void deleteUserAfterTest() {
        if (accessTokenUser != null) {
            userStep.deleteUser(accessTokenUser);
        }

    }
}



