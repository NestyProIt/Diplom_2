# Diplom_2


# Тестирование API
Тестирование API для сервиса [Stellar Burgers](https://stellarburgers.nomoreparties.site/).
[Документация API](https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf). 

## **Задание**
## **Создание пользователя:**

* создать уникального пользователя;
* создать пользователя, который уже зарегистрирован;
* создать пользователя и не заполнить одно из обязательных полей.

## **Логин пользователя:**

* логин под существующим пользователем,
* логин с неверным логином и паролем.

## **Изменение данных пользователя:**

* с авторизацией,
* без авторизации,
  Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.

## **Создание заказа:**

* с авторизацией,
* без авторизации,
* с ингредиентами,
* без ингредиентов,
* с неверным хешем ингредиентов.

## **Получение заказов конкретного пользователя:**

* авторизованный пользователь,
* неавторизованный пользователь.


## Технологии
* Java 11
* JUnit 4.13.2
* Maven 11
* Allure (JUnit 4) 2.15.0
* REST Assured 4.4.0
* Maven Surefire Plugin 2.22.2

## Запуск проекта

Ввести в консоли Run Anything (дважды нажав Ctrl): `mvn clean test`

## **Что нужно сделать**

 
 