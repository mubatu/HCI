import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class CatalogItemDialog extends JDialog {
    private final CatalogItemPanel catalogItemPanel;
    private final JTextField balanceField;

    private BigDecimal baseBalance = BigDecimal.ZERO;
    private ShoppingAction action = ShoppingAction.CANCEL;
    private CatalogItem catalogItem;

    public CatalogItemDialog(Window owner, Order order) {
        super(owner, "Cheap Shop Catalog Store", ModalityType.APPLICATION_MODAL);

        catalogItemPanel = new CatalogItemPanel();
        balanceField = new JTextField(10);
        balanceField.setEditable(false);

        setLayout(new BorderLayout(10, 10));
        add(catalogItemPanel, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        catalogItemPanel.addValueChangeListener(this::updateBalancePreview);
        registerShortcuts();
        getRootPane().setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        resetForNextItem(order.getBalanceOwing());
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public boolean wasConfirmed() {
        return action != ShoppingAction.CANCEL;
    }

    public ShoppingAction getAction() {
        return action;
    }

    public CatalogItem getCatalogItem() {
        return catalogItem;
    }

    public void resetForNextItem(BigDecimal currentBalance) {
        baseBalance = currentBalance == null ? BigDecimal.ZERO : currentBalance;
        action = ShoppingAction.CANCEL;
        catalogItem = null;
        catalogItemPanel.clear();
        updateBalancePreview();
        javax.swing.SwingUtilities.invokeLater(catalogItemPanel::requestItemNumberFocus);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel balancePanel = new JPanel(new BorderLayout(6, 6));
        balancePanel.add(new JLabel("Balance Owing:"), BorderLayout.WEST);
        balancePanel.add(balanceField, BorderLayout.CENTER);

        JButton nextButton = new JButton("Next Catalog Item (F8)");
        nextButton.addActionListener(event -> submit(ShoppingAction.NEXT_ITEM));

        JButton invoiceButton = new JButton("Trigger Invoice (F5)");
        invoiceButton.addActionListener(event -> submit(ShoppingAction.TRIGGER_INVOICE));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        buttonPanel.add(nextButton);
        buttonPanel.add(invoiceButton);

        panel.add(balancePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);
        getRootPane().setDefaultButton(nextButton);
        return panel;
    }

    private void registerShortcuts() {
        bindAction("submitNext", KeyStroke.getKeyStroke("F8"), () -> submit(ShoppingAction.NEXT_ITEM));
        bindAction("submitInvoice", KeyStroke.getKeyStroke("F5"), () -> submit(ShoppingAction.TRIGGER_INVOICE));
        bindAction("closeDialog", KeyStroke.getKeyStroke("ESCAPE"), this::dispose);
    }

    private void bindAction(String key, KeyStroke keyStroke, Runnable runnable) {
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, key);
        getRootPane().getActionMap().put(key, new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                runnable.run();
            }
        });
    }

    private void submit(ShoppingAction selectedAction) {
        try {
            catalogItem = catalogItemPanel.getCatalogItem();
            action = selectedAction;
            setVisible(false);
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateBalancePreview() {
        balanceField.setText(baseBalance.add(catalogItemPanel.getLineTotal()).toPlainString());
    }
}
