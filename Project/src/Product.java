import java.math.BigDecimal;
import java.math.RoundingMode;

public class Product {
    private final String id;
    private final String name;
    private final String category;
    private final String description;
    private final BigDecimal price;

    public Product(String id, String name, String category, String description, BigDecimal price) {
        this.id = ValidationUtils.requireText(id, "Product id");
        this.name = ValidationUtils.requireText(name, "Product name");
        this.category = ValidationUtils.requireText(category, "Product category");
        this.description = ValidationUtils.requireText(description, "Product description");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
