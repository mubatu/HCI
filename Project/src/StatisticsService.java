import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    public StatisticsSummary summarize(List<OrderRecord> orders) {
        BigDecimal revenue = BigDecimal.ZERO;
        Map<String, Integer> productQuantities = new HashMap<>();
        Map<String, String> productNames = new HashMap<>();
        EnumMap<CreditCardType, Integer> cardCounts = new EnumMap<>(CreditCardType.class);

        for (CreditCardType type : CreditCardType.values()) {
            cardCounts.put(type, 0);
        }

        if (orders != null) {
            for (OrderRecord order : orders) {
                revenue = revenue.add(order.getTotal());
                CreditCardType cardType = order.getPaymentInfo().getCardType();
                cardCounts.put(cardType, cardCounts.getOrDefault(cardType, 0) + 1);
                for (CartItem item : order.getItems()) {
                    String id = item.getProduct().getId();
                    productNames.put(id, item.getProduct().getName());
                    productQuantities.put(id, productQuantities.getOrDefault(id, 0) + item.getQuantity());
                }
            }
        }

        String mostPurchased = "None yet";
        int mostPurchasedQuantity = 0;
        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            if (entry.getValue() > mostPurchasedQuantity) {
                mostPurchasedQuantity = entry.getValue();
                mostPurchased = productNames.get(entry.getKey()) + " (" + entry.getValue() + " sold)";
            }
        }

        return new StatisticsSummary(
                orders == null ? 0 : orders.size(),
                revenue.setScale(2, RoundingMode.HALF_UP),
                mostPurchased,
                cardCounts
        );
    }

    public static class StatisticsSummary {
        private final int totalOrders;
        private final BigDecimal totalRevenue;
        private final String mostPurchasedProduct;
        private final EnumMap<CreditCardType, Integer> ordersByCardType;

        public StatisticsSummary(
                int totalOrders,
                BigDecimal totalRevenue,
                String mostPurchasedProduct,
                EnumMap<CreditCardType, Integer> ordersByCardType
        ) {
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.mostPurchasedProduct = mostPurchasedProduct;
            this.ordersByCardType = new EnumMap<>(ordersByCardType);
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public String getMostPurchasedProduct() {
            return mostPurchasedProduct;
        }

        public EnumMap<CreditCardType, Integer> getOrdersByCardType() {
            return new EnumMap<>(ordersByCardType);
        }
    }
}
