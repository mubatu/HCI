import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PurchaserPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField phoneField;
    private final JTextField postalCodeField;
    private final JTextField provinceField;
    private final JTextField cityField;
    private final JTextField deliveryAddressField;
    private final JTextField dateField;
    private final JTextField creditCardField;
    private final JTextField validationIdField;

    public PurchaserPanel() {
        setBorder(BorderFactory.createTitledBorder("Purchaser"));
        setLayout(new GridBagLayout());

        nameField = new JTextField(16);
        phoneField = new JTextField(14);
        postalCodeField = new JTextField(8);
        provinceField = new JTextField(8);
        cityField = new JTextField(10);
        deliveryAddressField = new JTextField(28);
        dateField = new JTextField(LocalDate.now().toString(), 10);
        creditCardField = new JTextField(16);
        validationIdField = new JTextField(6);

        addLabel("Name:", 0, 0);
        addField(nameField, 1, 0, 2, 1.0);
        addLabel("Phone:", 3, 0);
        addField(phoneField, 4, 0, 2, 1.0);

        addLabel("Postal Code:", 0, 1);
        addField(postalCodeField, 1, 1, 1, 0.5);
        addLabel("Province:", 2, 1);
        addField(provinceField, 3, 1, 1, 0.3);
        addLabel("City:", 4, 1);
        addField(cityField, 5, 1, 1, 0.6);

        addLabel("Delivery Address:", 0, 2);
        addField(deliveryAddressField, 1, 2, 5, 1.0);

        addLabel("Today's Date:", 0, 3);
        addField(dateField, 1, 3, 2, 0.5);

        addLabel("Credit Card No:", 0, 4);
        addField(creditCardField, 1, 4, 2, 0.7);
        addLabel("Validation Id:", 3, 4);
        addField(validationIdField, 4, 4, 2, 0.3);
    }

    public Purchaser getPurchaser() {
        LocalDate orderDate = parseDate(dateField.getText());

        return new Purchaser(
                nameField.getText(),
                phoneField.getText(),
                postalCodeField.getText(),
                provinceField.getText(),
                cityField.getText(),
                deliveryAddressField.getText(),
                orderDate,
                creditCardField.getText(),
                validationIdField.getText()
        );
    }

    void requestInitialFocus() {
        nameField.requestFocusInWindow();
    }

    private LocalDate parseDate(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Today's date is required.");
        }

        try {
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Today's date must use the format YYYY-MM-DD.");
        }
    }

    private void addLabel(String text, int gridX, int gridY) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
        constraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(text), constraints);
    }

    private void addField(java.awt.Component component, int gridX, int gridY, int gridWidth, double weightX) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
        constraints.gridwidth = gridWidth;
        constraints.weightx = weightX;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(component, constraints);
    }

    private GridBagConstraints baseConstraints(int gridX, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.insets = new Insets(6, 6, 6, 6);
        return constraints;
    }
}
