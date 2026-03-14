import java.time.LocalDate;

public class Purchaser {
    private final String name;
    private final String phone;
    private final String postalCode;
    private final String province;
    private final String city;
    private final String deliveryAddress;
    private final LocalDate orderDate;
    private final String creditCardNumber;
    private final String validationId;

    public Purchaser(
            String name,
            String phone,
            String postalCode,
            String province,
            String city,
            String deliveryAddress,
            LocalDate orderDate,
            String creditCardNumber,
            String validationId
    ) {
        this.name = requireText(name, "Name");
        this.phone = requireText(phone, "Phone");
        this.postalCode = requireText(postalCode, "Postal code");
        this.province = requireText(province, "Province");
        this.city = requireText(city, "City");
        this.deliveryAddress = requireText(deliveryAddress, "Delivery address");
        if (orderDate == null) {
            throw new IllegalArgumentException("Today's date is required.");
        }
        this.orderDate = orderDate;
        this.creditCardNumber = requireText(creditCardNumber, "Credit card number");
        this.validationId = requireText(validationId, "Validation id");
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getValidationId() {
        return validationId;
    }
}
