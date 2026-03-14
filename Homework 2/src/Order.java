import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Purchaser purchaser;
    private final List<CatalogItem> items = new ArrayList<>();

    public Order(Purchaser purchaser) {
        this.purchaser = Objects.requireNonNull(purchaser, "Purchaser is required.");
    }

    public Purchaser getPurchaser() {
        return purchaser;
    }

    public void addItem(CatalogItem item) {
        items.add(Objects.requireNonNull(item, "Catalog item is required."));
    }

    public List<CatalogItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public BigDecimal getBalanceOwing() {
        BigDecimal total = BigDecimal.ZERO;
        for (CatalogItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
