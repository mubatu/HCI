import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class StatisticsDialog extends JDialog {
    public StatisticsDialog(java.awt.Window owner, StatisticsService.StatisticsSummary summary) {
        super(owner, "Shopping Statistics", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout(12, 12));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        add(createSummaryPanel(summary), BorderLayout.NORTH);
        add(createCardTable(summary), BorderLayout.CENTER);
        add(createClosePanel(), BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(520, 340));
        setLocationRelativeTo(owner);
    }

    private JPanel createSummaryPanel(StatisticsService.StatisticsSummary summary) {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setOpaque(false);
        text.setText("Total Orders: " + summary.getTotalOrders() + "\n"
                + "Total Revenue: $" + summary.getTotalRevenue().toPlainString() + "\n"
                + "Most Purchased Product: " + summary.getMostPurchasedProduct());
        text.setBorder(BorderFactory.createTitledBorder("Overview"));
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createCardTable(StatisticsService.StatisticsSummary summary) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Credit Card Type", "Orders"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (CreditCardType type : CreditCardType.values()) {
            if (type != CreditCardType.NONE) {
                model.addRow(new Object[]{type.getDisplayName(), summary.getOrdersByCardType().get(type)});
            }
        }
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        return new JScrollPane(table);
    }

    private JPanel createClosePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event -> dispose());
        getRootPane().setDefaultButton(closeButton);
        panel.add(new JLabel("Statistics are calculated from saved orders."), BorderLayout.WEST);
        panel.add(closeButton, BorderLayout.EAST);
        return panel;
    }
}
