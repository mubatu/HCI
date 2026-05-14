import java.util.List;

public class AuthenticationService {
    private final ShopDatabase database;

    public AuthenticationService(ShopDatabase database) {
        this.database = database;
    }

    public User login(String username, String password) {
        String requestedUsername = ValidationUtils.requireText(username, "Username");
        String requestedPassword = ValidationUtils.requireText(password, "Password");
        List<User> users = database.loadUsers();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(requestedUsername)
                    && user.getPassword().equals(requestedPassword)) {
                return user;
            }
        }
        throw new IllegalArgumentException("Invalid username or password.");
    }
}
