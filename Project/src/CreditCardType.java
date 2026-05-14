public enum CreditCardType {
    NONE("None"),
    VISA("Visa"),
    MASTERCARD("MasterCard"),
    DISCOVER("Discover");

    private final String displayName;

    CreditCardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static CreditCardType fromName(String value) {
        if (value == null) {
            return NONE;
        }
        for (CreditCardType type : values()) {
            if (type.name().equalsIgnoreCase(value.trim())
                    || type.displayName.equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        return NONE;
    }
}
