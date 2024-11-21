package model;

public class Pengguna extends User {
    public Pengguna(int id, String username, String fullName, String password) {
        super(id, username, fullName, password);
    }

    @Override
    public void navigateToMenu() {
        System.out.println("Navigasi ke menu masyarakat: " + getFullName());
    }
}
