import java.math.BigDecimal;
import java.math.RoundingMode;

public class CatalogItem {
    private final String itemNumber;
    private final int quantity;
    private final BigDecimal costPerItem;

    public CatalogItem(String itemNumber, int quantity, BigDecimal costPerItem) {
        if (itemNumber == null || itemNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Catalog item number is required.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        if (costPerItem == null || costPerItem.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Cost per item must be greater than zero.");
        }

        this.itemNumber = itemNumber.trim();
        this.quantity = quantity;
        this.costPerItem = costPerItem.setScale(2, RoundingMode.HALF_UP);
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getCostPerItem() {
        return costPerItem;
    }

    public BigDecimal getLineTotal() {
        return costPerItem.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
}
