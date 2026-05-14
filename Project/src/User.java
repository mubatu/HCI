public class User {
    private final String username;
    private final String password;
    private final String fullName;

    public User(String username, String password, String fullName) {
        this.username = ValidationUtils.requireText(username, "Username");
        this.password = ValidationUtils.requireText(password, "Password");
        this.fullName = ValidationUtils.requireText(fullName, "Full name");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}
