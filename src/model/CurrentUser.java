package model;

public class CurrentUser {
    private static User instance;

    public static User getInstance() {
        return instance;
    }

    public static void setInstance(User user) {
        instance = user;
    }
}
