package site.stellaburgers.User;

public class UserLogin {


    private String email;
    private String password;

    // конструктор со всеми параметрами
    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // конструктор без параметров
    public UserLogin() {
    }

    //Метод возвращает объект пользователя со случайно сгенерированным email и password
    public static UserLogin getUserLogin(UserCreate userCreate) {
        return new UserLogin(userCreate.getEmail(), userCreate.getPassword());
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

}
