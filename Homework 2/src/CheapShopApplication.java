import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CheapShopApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            applySystemLookAndFeel();
            new CheapShopApplication().start();
        });
    }

    private static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to the default Swing look and feel.
        }
    }

    private void start() {
        Order order = runShoppingFlow();
        if (order != null) {
            InvoiceDialog invoiceDialog = new InvoiceDialog(null, order);
            invoiceDialog.setVisible(true);
        }
        System.exit(0);
    }

    private Order runShoppingFlow() {
        PurchaserDialog purchaserDialog = new PurchaserDialog(null);
        purchaserDialog.setVisible(true);

        if (!purchaserDialog.wasConfirmed()) {
            return null;
        }

        Order order = new Order(purchaserDialog.getPurchaser());
        order.addItem(purchaserDialog.getCatalogItem());

        if (purchaserDialog.getAction() == ShoppingAction.TRIGGER_INVOICE) {
            return order;
        }

        CatalogItemDialog catalogItemDialog = new CatalogItemDialog(null, order);
        try {
            while (true) {
                catalogItemDialog.resetForNextItem(order.getBalanceOwing());
                catalogItemDialog.setVisible(true);

                if (!catalogItemDialog.wasConfirmed()) {
                    return null;
                }

                order.addItem(catalogItemDialog.getCatalogItem());
                if (catalogItemDialog.getAction() == ShoppingAction.TRIGGER_INVOICE) {
                    return order;
                }
            }
        } finally {
            catalogItemDialog.dispose();
        }
    }
}
