import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class InvoiceDialog extends JDialog {
    public InvoiceDialog(java.awt.Window owner, Order order) {
        super(owner, "Invoice Summary", ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(createHeaderPanel(order), BorderLayout.NORTH);
        add(createItemTable(order), BorderLayout.CENTER);
        add(createFooterPanel(order), BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(720, 420));
        setLocationRelativeTo(owner);
    }

    private JPanel createHeaderPanel(Order order) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 12));

        JTextArea purchaserArea = new JTextArea(buildPurchaserSummary(order.getPurchaser()));
        purchaserArea.setEditable(false);
        purchaserArea.setOpaque(false);
        purchaserArea.setLineWrap(true);
        purchaserArea.setWrapStyleWord(true);
        purchaserArea.setBorder(BorderFactory.createTitledBorder("Purchaser Details"));

        JTextArea orderArea = new JTextArea(buildOrderSummary(order));
        orderArea.setEditable(false);
        orderArea.setOpaque(false);
        orderArea.setLineWrap(true);
        orderArea.setWrapStyleWord(true);
        orderArea.setBorder(BorderFactory.createTitledBorder("Order Details"));

        panel.add(purchaserArea);
        panel.add(orderArea);
        return panel;
    }

    private JScrollPane createItemTable(Order order) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Number", "Quantity", "Cost/Item", "Line Total"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (CatalogItem item : order.getItems()) {
            model.addRow(new Object[]{
                    item.getItemNumber(),
                    item.getQuantity(),
                    item.getCostPerItem().toPlainString(),
                    item.getLineTotal().toPlainString()
            });
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        return new JScrollPane(table);
    }

    private JPanel createFooterPanel(Order order) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        JLabel totalLabel = new JLabel("Balance Owing: " + order.getBalanceOwing().toPlainString());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> dispose());
        getRootPane().setDefaultButton(closeButton);

        panel.add(totalLabel, BorderLayout.WEST);
        panel.add(closeButton, BorderLayout.EAST);
        return panel;
    }

    private String buildPurchaserSummary(Purchaser purchaser) {
        return "Name: " + purchaser.getName() + "\n"
                + "Phone: " + purchaser.getPhone() + "\n"
                + "Postal Code: " + purchaser.getPostalCode() + "\n"
                + "Province: " + purchaser.getProvince() + "\n"
                + "City: " + purchaser.getCity() + "\n"
                + "Delivery Address: " + purchaser.getDeliveryAddress() + "\n"
                + "Today's Date: " + purchaser.getOrderDate() + "\n"
                + "Credit Card No: " + purchaser.getCreditCardNumber() + "\n"
                + "Validation Id: " + purchaser.getValidationId();
    }

    private String buildOrderSummary(Order order) {
        return "Items Entered: " + order.getItemCount() + "\n"
                + "Running Balance: " + order.getBalanceOwing().toPlainString() + "\n"
                + "Status: Ready for invoice confirmation";
    }
}
