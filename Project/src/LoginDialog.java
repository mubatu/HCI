import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends JDialog {
    private final AuthenticationService authenticationService;
    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private User user;

    public LoginDialog(java.awt.Window owner, AuthenticationService authenticationService) {
        super(owner, "Music Shop Login", ModalityType.APPLICATION_MODAL);
        this.authenticationService = authenticationService;

        setLayout(new BorderLayout());
        add(createHeader(), BorderLayout.NORTH);
        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        getRootPane().setDefaultButton((JButton) ((JPanel) getContentPane().getComponent(2)).getComponent(1));
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        javax.swing.SwingUtilities.invokeLater(usernameField::requestFocusInWindow);
    }

    public User getUser() {
        return user;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(32, 75, 89));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Music Shop");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JLabel subtitle = new JLabel("Sign in to browse the product catalog");
        subtitle.setForeground(new Color(221, 239, 242));
        subtitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JPanel text = new JPanel(new BorderLayout());
        text.setOpaque(false);
        text.add(title, BorderLayout.NORTH);
        text.add(subtitle, BorderLayout.SOUTH);
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(18, 22, 10, 22));

        addLabel(panel, "Username:", 0);
        addField(panel, usernameField, 0);
        addLabel(panel, "Password:", 1);
        addField(panel, passwordField, 1);

        JLabel hint = new JLabel("Demo: student / hci123");
        hint.setForeground(new Color(88, 88, 88));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 8, 8, 8);
        panel.add(hint, constraints);

        return panel;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(4, 22, 18, 22));
        JButton cancelButton = new JButton("Cancel");
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(event -> login());
        cancelButton.addActionListener(event -> dispose());
        panel.add(cancelButton);
        panel.add(loginButton);
        return panel;
    }

    private void addLabel(JPanel panel, String label, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(8, 8, 8, 8);
        panel.add(new JLabel(label), constraints);
    }

    private void addField(JPanel panel, java.awt.Component field, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 8, 8, 8);
        panel.add(field, constraints);
    }

    private void login() {
        try {
            user = authenticationService.login(
                    usernameField.getText(),
                    new String(passwordField.getPassword())
            );
            dispose();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            passwordField.selectAll();
            passwordField.requestFocusInWindow();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(430, super.getPreferredSize().height);
    }
}
