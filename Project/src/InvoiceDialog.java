import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.format.DateTimeFormatter;
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
    public InvoiceDialog(java.awt.Window owner, OrderRecord order) {
        super(owner, "Invoice", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(12, 12));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createSummary(order), BorderLayout.NORTH);
        add(createItemTable(order), BorderLayout.CENTER);
        add(createFooter(order), BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(720, 430));
        setLocationRelativeTo(owner);
    }

    private JPanel createSummary(OrderRecord order) {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setText("Order ID: " + order.getOrderId() + "\n"
                + "Shopper: " + order.getUsername() + "\n"
                + "Date: " + order.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n"
                + "Payment: " + order.getPaymentInfo().getCardType().getDisplayName()
                + " " + order.getPaymentInfo().getMaskedCardNumber() + "\n"
                + "Receipt Email: " + order.getPaymentInfo().getEmail());
        text.setBorder(BorderFactory.createTitledBorder("Checkout Complete"));
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createItemTable(OrderRecord order) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Product", "Category", "Price", "Qty", "Line Total"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (CartItem item : order.getItems()) {
            model.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getProduct().getCategory(),
                    "$" + item.getProduct().getPrice().toPlainString(),
                    item.getQuantity(),
                    "$" + item.getLineTotal().toPlainString()
            });
        }
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        return new JScrollPane(table);
    }

    private JPanel createFooter(OrderRecord order) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel total = new JLabel("Order Total: $" + order.getTotal().toPlainString());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> dispose());
        getRootPane().setDefaultButton(closeButton);
        panel.add(total, BorderLayout.WEST);
        panel.add(closeButton, BorderLayout.EAST);
        return panel;
    }
}
