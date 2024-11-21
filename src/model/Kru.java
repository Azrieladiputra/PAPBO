package model;

public class Kru extends User {
    public Kru(int id, String username, String fullName, String password) {
        super(id, username, fullName, password);
    }

    @Override
    public void navigateToMenu() {
        System.out.println("Navigasi ke menu admin: " + getFullName());
    }
}
