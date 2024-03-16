package site.stellaburgers.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserCreate {

    private String email;
    private String password;
    private String name;

    // конструктор со всеми параметрами
    public UserCreate(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // конструктор без параметров
    public UserCreate() {
    }

    //Метод создает случайные значения для email, password и name пользователя
    public static UserCreate getDataGeneratorUser() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(7);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new UserCreate(email, password, name);
    }

    //Геттеры и сеттеры
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
