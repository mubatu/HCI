import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

public class MusicShopFrame extends JFrame {
    private final User user;
    private final ShopDatabase database;
    private final ProductTableModel productTableModel;
    private final JLabel totalLabel = new JLabel("$0.00");
    private final JLabel paymentStatusLabel = new JLabel("Payment: none selected");
    private final JComboBox<CreditCardType> cardTypeCombo = new JComboBox<>(CreditCardType.values());
    private PaymentInfo paymentInfo;
    private boolean changingCardSelection;

    public MusicShopFrame(User user, ShopDatabase database) {
        super("Music Shop Online Shopping");
        this.user = user;
        this.database = database;
        this.productTableModel = new ProductTableModel(database.loadProducts());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(createHeader(), BorderLayout.NORTH);
        add(createCatalog(), BorderLayout.CENTER);
        add(createCheckoutPanel(), BorderLayout.SOUTH);

        productTableModel.addTableModelListener(event -> refreshTotal());
        refreshTotal();

        pack();
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(32, 75, 89));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel title = new JLabel("Music Shop");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 28f));

        JLabel subtitle = new JLabel("Choose from 10 music products and checkout securely");
        subtitle.setForeground(new Color(221, 239, 242));
        subtitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(title, BorderLayout.NORTH);
        textPanel.add(subtitle, BorderLayout.SOUTH);

        JLabel userLabel = new JLabel("Signed in as " + user.getFullName());
        userLabel.setForeground(Color.WHITE);
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(textPanel, BorderLayout.WEST);
        panel.add(userLabel, BorderLayout.EAST);
        return panel;
    }

    private JScrollPane createCatalog() {
        JTable table = new JTable(productTableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        TableColumnModel columns = table.getColumnModel();
        columns.getColumn(0).setPreferredWidth(55);
        columns.getColumn(1).setPreferredWidth(160);
        columns.getColumn(2).setPreferredWidth(100);
        columns.getColumn(3).setPreferredWidth(340);
        columns.getColumn(4).setPreferredWidth(75);
        columns.getColumn(5).setPreferredWidth(45);
        columns.getColumn(6).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Music Products"));
        return scrollPane;
    }

    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        panel.add(createPaymentPanel(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.EAST);
        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Checkout"));

        cardTypeCombo.setSelectedItem(CreditCardType.NONE);
        cardTypeCombo.addActionListener(event -> handleCardSelection());

        addLabel(panel, "Order Total:", 0, 0);
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 18f));
        addValue(panel, totalLabel, 1, 0);
        addLabel(panel, "Payment Type:", 0, 1);
        addValue(panel, cardTypeCombo, 1, 1);
        addLabel(panel, "Payment Status:", 0, 2);
        addValue(panel, paymentStatusLabel, 1, 2);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JButton paymentButton = new JButton("Edit Payment");
        JButton checkoutButton = new JButton("Checkout");
        JButton statisticsButton = new JButton("Statistics");
        JButton clearButton = new JButton("Clear Cart");

        paymentButton.addActionListener(event -> editPayment());
        checkoutButton.addActionListener(event -> checkout());
        statisticsButton.addActionListener(event -> showStatistics());
        clearButton.addActionListener(event -> clearCart());

        addButton(panel, paymentButton, 0);
        addButton(panel, checkoutButton, 1);
        addButton(panel, statisticsButton, 2);
        addButton(panel, clearButton, 3);
        return panel;
    }

    private void addLabel(JPanel panel, String text, int gridX, int gridY) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(text), constraints);
    }

    private void addValue(JPanel panel, java.awt.Component component, int gridX, int gridY) {
        GridBagConstraints constraints = baseConstraints(gridX, gridY);
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, constraints);
    }

    private void addButton(JPanel panel, JButton button, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(4, 6, 4, 6);
        panel.add(button, constraints);
    }

    private GridBagConstraints baseConstraints(int gridX, int gridY) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.insets = new Insets(7, 8, 7, 8);
        return constraints;
    }

    private void handleCardSelection() {
        if (changingCardSelection) {
            return;
        }
        CreditCardType selectedType = getSelectedCardType();
        if (selectedType == CreditCardType.NONE) {
            paymentInfo = null;
            paymentStatusLabel.setText("Payment: none selected");
            return;
        }
        openPaymentDialog(selectedType, true);
    }

    private void editPayment() {
        CreditCardType selectedType = getSelectedCardType();
        if (selectedType == CreditCardType.NONE) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select Visa, MasterCard, or Discover first.",
                    "Payment Required",
                    JOptionPane.WARNING_MESSAGE
            );
            cardTypeCombo.requestFocusInWindow();
            return;
        }
        openPaymentDialog(selectedType, false);
    }

    private void openPaymentDialog(CreditCardType selectedType, boolean resetOnCancel) {
        PaymentDialog dialog = new PaymentDialog(this, selectedType);
        dialog.setVisible(true);
        PaymentInfo newPaymentInfo = dialog.getPaymentInfo();
        if (newPaymentInfo == null) {
            if (resetOnCancel) {
                resetPaymentSelection();
            }
            return;
        }
        paymentInfo = newPaymentInfo;
        paymentStatusLabel.setText("Payment ready: "
                + selectedType.getDisplayName()
                + " " + paymentInfo.getMaskedCardNumber());
    }

    private void checkout() {
        List<CartItem> selectedItems = productTableModel.getSelectedItems();
        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one music product before checkout.",
                    "Cart Empty",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (getSelectedCardType() == CreditCardType.NONE || paymentInfo == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a credit card type and complete the payment form.",
                    "Payment Required",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        OrderRecord order = new OrderRecord(
                createOrderId(),
                user.getUsername(),
                LocalDateTime.now(),
                paymentInfo,
                selectedItems
        );

        try {
            database.saveOrder(order);
        } catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new InvoiceDialog(this, order).setVisible(true);
        clearCart();
    }

    private void showStatistics() {
        StatisticsService.StatisticsSummary summary =
                new StatisticsService().summarize(database.loadOrders());
        new StatisticsDialog(this, summary).setVisible(true);
    }

    private void clearCart() {
        productTableModel.clearSelections();
        resetPaymentSelection();
        refreshTotal();
    }

    private void resetPaymentSelection() {
        changingCardSelection = true;
        cardTypeCombo.setSelectedItem(CreditCardType.NONE);
        changingCardSelection = false;
        paymentInfo = null;
        paymentStatusLabel.setText("Payment: none selected");
    }

    private void refreshTotal() {
        BigDecimal total = productTableModel.getSelectedTotal();
        totalLabel.setText("$" + total.toPlainString());
    }

    private CreditCardType getSelectedCardType() {
        Object selected = cardTypeCombo.getSelectedItem();
        return selected instanceof CreditCardType ? (CreditCardType) selected : CreditCardType.NONE;
    }

    private String createOrderId() {
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
