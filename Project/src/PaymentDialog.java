import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaymentDialog extends JDialog {
    private final CreditCardType cardType;
    private final JTextField cardNumberField = new JTextField(18);
    private final JTextField expiryField = new JTextField(6);
    private final JTextField emailField = new JTextField(22);
    private PaymentInfo paymentInfo;

    public PaymentDialog(java.awt.Window owner, CreditCardType cardType) {
        super(owner, "Payment Details", ModalityType.APPLICATION_MODAL);
        this.cardType = cardType;

        setLayout(new BorderLayout(10, 10));
        add(createForm(), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        javax.swing.SwingUtilities.invokeLater(cardNumberField::requestFocusInWindow);
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    private JPanel createForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(16, 18, 8, 18),
                BorderFactory.createTitledBorder(cardType.getDisplayName() + " Payment")
        ));

        addLabel(panel, "Card Type:", 0);
        addValue(panel, cardType.getDisplayName(), 0);
        addLabel(panel, "16-Digit Card No:", 1);
        addField(panel, cardNumberField, 1);
        addLabel(panel, "Expiry (mm/yy):", 2);
        addField(panel, expiryField, 2);
        addLabel(panel, "Email:", 3);
        addField(panel, emailField, 3);
        return panel;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 18, 16, 18));
        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Payment");
        cancelButton.addActionListener(event -> dispose());
        saveButton.addActionListener(event -> save());
        getRootPane().setDefaultButton(saveButton);
        panel.add(cancelButton);
        panel.add(saveButton);
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

    private void addValue(JPanel panel, String value, int row) {
        GridBagConstraints constraints = fieldConstraints(row);
        panel.add(new JLabel(value), constraints);
    }

    private void addField(JPanel panel, java.awt.Component field, int row) {
        panel.add(field, fieldConstraints(row));
    }

    private GridBagConstraints fieldConstraints(int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 8, 8, 8);
        return constraints;
    }

    private void save() {
        try {
            paymentInfo = PaymentInfo.create(
                    cardType,
                    cardNumberField.getText(),
                    expiryField.getText(),
                    emailField.getText()
            );
            dispose();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
