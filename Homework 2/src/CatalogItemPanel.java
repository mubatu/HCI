import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CatalogItemPanel extends JPanel {
    private final JTextField itemNumberField;
    private final JSpinner quantitySpinner;
    private final JTextField costField;
    private final JFormattedTextField totalField;
    private final List<Runnable> valueChangeListeners = new ArrayList<>();

    public CatalogItemPanel() {
        setBorder(BorderFactory.createTitledBorder("Catalog Item"));
        setLayout(new GridBagLayout());

        itemNumberField = new JTextField(10);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        costField = new JTextField(10);
        totalField = new JFormattedTextField();
        totalField.setColumns(10);
        totalField.setEditable(false);

        addLabel("Number:", 0, 0);
        addField(itemNumberField, 1, 0, 1.0);
        addLabel("Quantity:", 2, 0);
        addField(quantitySpinner, 3, 0, 0.0);
        addLabel("Cost/Item:", 4, 0);
        addField(costField, 5, 0, 0.5);
        addLabel("Total:", 6, 0);
        addField(totalField, 7, 0, 0.5);

        bindLiveUpdates();
        updateTotal();
    }

    public String getItemNumber() {
        return itemNumberField.getText().trim();
    }

    public void setItemNumber(String itemNumber) {
        itemNumberField.setText(itemNumber == null ? "" : itemNumber.trim());
    }

    public int getQuantity() {
        return (Integer) quantitySpinner.getValue();
    }

    public void setQuantity(int quantity) {
        quantitySpinner.setValue(Math.max(quantity, 1));
        updateTotal();
    }

    public BigDecimal getCostPerItem() {
        BigDecimal cost = parsePositiveAmount(costField.getText(), true);
        return cost.setScale(2, RoundingMode.HALF_UP);
    }

    public void setCostPerItem(BigDecimal costPerItem) {
        costField.setText(costPerItem == null ? "" : formatMoney(costPerItem));
        updateTotal();
    }

    public BigDecimal getLineTotal() {
        BigDecimal cost = parsePositiveAmount(costField.getText(), false);
        if (cost == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return cost.multiply(BigDecimal.valueOf(getQuantity())).setScale(2, RoundingMode.HALF_UP);
    }

    public CatalogItem getCatalogItem() {
        return new CatalogItem(getItemNumber(), getQuantity(), getCostPerItem());
    }

    public void setCatalogItem(CatalogItem item) {
        Objects.requireNonNull(item, "Catalog item is required.");
        setItemNumber(item.getItemNumber());
        setQuantity(item.getQuantity());
        setCostPerItem(item.getCostPerItem());
        updateTotal();
    }

    public void clear() {
        itemNumberField.setText("");
        quantitySpinner.setValue(1);
        costField.setText("");
        totalField.setValue(null);
        totalField.setText("");
        notifyValueChangeListeners();
    }

    public void updateTotal() {
        BigDecimal total = getLineTotal();
        if (parsePositiveAmount(costField.getText(), false) == null) {
            totalField.setValue(null);
            totalField.setText("");
            return;
        }
        totalField.setText(formatMoney(total));
    }

    public void addValueChangeListener(Runnable listener) {
        valueChangeListeners.add(Objects.requireNonNull(listener, "Listener is required."));
    }

    void requestItemNumberFocus() {
        itemNumberField.requestFocusInWindow();
    }

    private void bindLiveUpdates() {
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                handleValueChange();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                handleValueChange();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                handleValueChange();
            }
        };

        costField.getDocument().addDocumentListener(documentListener);
        itemNumberField.getDocument().addDocumentListener(documentListener);
        quantitySpinner.addChangeListener(event -> handleValueChange());
    }

    private void handleValueChange() {
        updateTotal();
        notifyValueChangeListeners();
    }

    private void notifyValueChangeListeners() {
        for (Runnable listener : valueChangeListeners) {
            listener.run();
        }
    }

    private void addLabel(String text, int gridX, int gridY) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
        constraints.anchor = GridBagConstraints.WEST;
        add(new JLabel(text), constraints);
    }

    private void addField(java.awt.Component component, int gridX, int gridY, double weightX) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
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

    private BigDecimal parsePositiveAmount(String text, boolean failOnInvalid) {
        if (text == null || text.trim().isEmpty()) {
            if (failOnInvalid) {
                throw new IllegalArgumentException("Cost per item is required.");
            }
            return null;
        }

        try {
            BigDecimal value = new BigDecimal(text.trim().replace(',', '.'));
            if (value.compareTo(BigDecimal.ZERO) <= 0) {
                if (failOnInvalid) {
                    throw new IllegalArgumentException("Cost per item must be greater than zero.");
                }
                return null;
            }
            return value.setScale(2, RoundingMode.HALF_UP);
        } catch (NumberFormatException exception) {
            if (failOnInvalid) {
                throw new IllegalArgumentException("Cost per item must be a valid number.");
            }
            return null;
        }
    }

    private String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
