import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MusicShopApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applyLookAndFeel();
            new MusicShopApplication().start();
        });
    }

    private static void applyLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Swing's default look and feel is acceptable if Nimbus is unavailable.
        }
    }

    private void start() {
        ShopDatabase database = new ShopDatabase();
        try {
            database.initialize();
        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(
                    null,
                    exception.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
            return;
        }

        LoginDialog loginDialog = new LoginDialog(null, new AuthenticationService(database));
        loginDialog.setVisible(true);
        User user = loginDialog.getUser();
        if (user == null) {
            System.exit(0);
            return;
        }

        MusicShopFrame frame = new MusicShopFrame(user, database);
        frame.setVisible(true);
    }
}
