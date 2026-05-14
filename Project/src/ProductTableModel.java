import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ProductTableModel extends AbstractTableModel {
    private static final String[] COLUMNS = {
            "Select",
            "Product",
            "Category",
            "Description",
            "Price",
            "Qty",
            "Line Total"
    };

    private final List<Product> products;
    private final boolean[] selected;
    private final int[] quantities;

    public ProductTableModel(List<Product> products) {
        this.products = new ArrayList<>(products);
        this.selected = new boolean[products.size()];
        this.quantities = new int[products.size()];
        for (int index = 0; index < quantities.length; index++) {
            quantities[index] = 1;
        }
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        }
        if (columnIndex == 5) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0 || columnIndex == 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return selected[rowIndex];
            case 1:
                return product.getName();
            case 2:
                return product.getCategory();
            case 3:
                return product.getDescription();
            case 4:
                return "$" + product.getPrice().toPlainString();
            case 5:
                return quantities[rowIndex];
            case 6:
                return "$" + getLineTotal(rowIndex).toPlainString();
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            selected[rowIndex] = Boolean.TRUE.equals(value);
        } else if (columnIndex == 5) {
            quantities[rowIndex] = parseQuantity(value);
            selected[rowIndex] = true;
        }
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> items = new ArrayList<>();
        for (int index = 0; index < products.size(); index++) {
            if (selected[index]) {
                items.add(new CartItem(products.get(index), quantities[index]));
            }
        }
        return items;
    }

    public BigDecimal getSelectedTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : getSelectedItems()) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void clearSelections() {
        for (int index = 0; index < products.size(); index++) {
            selected[index] = false;
            quantities[index] = 1;
        }
        fireTableDataChanged();
    }

    private BigDecimal getLineTotal(int rowIndex) {
        if (!selected[rowIndex]) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return products.get(rowIndex)
                .getPrice()
                .multiply(BigDecimal.valueOf(quantities[rowIndex]))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private int parseQuantity(Object value) {
        if (value instanceof Number) {
            return clampQuantity(((Number) value).intValue());
        }
        try {
            return clampQuantity(Integer.parseInt(String.valueOf(value).trim()));
        } catch (NumberFormatException exception) {
            return 1;
        }
    }

    private int clampQuantity(int quantity) {
        if (quantity < 1) {
            return 1;
        }
        if (quantity > 10) {
            return 10;
        }
        return quantity;
    }
}
