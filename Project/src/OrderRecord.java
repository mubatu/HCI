import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderRecord {
    private final String orderId;
    private final String username;
    private final LocalDateTime createdAt;
    private final PaymentInfo paymentInfo;
    private final List<CartItem> items;

    public OrderRecord(
            String orderId,
            String username,
            LocalDateTime createdAt,
            PaymentInfo paymentInfo,
            List<CartItem> items
    ) {
        this.orderId = ValidationUtils.requireText(orderId, "Order id");
        this.username = ValidationUtils.requireText(username, "Username");
        if (createdAt == null) {
            throw new IllegalArgumentException("Order date is required.");
        }
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment information is required.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("At least one item is required.");
        }
        this.createdAt = createdAt;
        this.paymentInfo = paymentInfo;
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getLineTotal());
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
