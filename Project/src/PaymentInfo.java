public class PaymentInfo {
    private final CreditCardType cardType;
    private final String cardNumber;
    private final String maskedCardNumber;
    private final String expiry;
    private final String email;

    private PaymentInfo(
            CreditCardType cardType,
            String cardNumber,
            String maskedCardNumber,
            String expiry,
            String email
    ) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.maskedCardNumber = maskedCardNumber;
        this.expiry = expiry;
        this.email = email;
    }

    public static PaymentInfo create(
            CreditCardType cardType,
            String cardNumber,
            String expiry,
            String email
    ) {
        if (cardType == null || cardType == CreditCardType.NONE) {
            throw new IllegalArgumentException("Please select Visa, MasterCard, or Discover.");
        }
        String validNumber = ValidationUtils.validateCardNumber(cardNumber);
        String validExpiry = ValidationUtils.validateExpiry(expiry);
        String validEmail = ValidationUtils.validateEmail(email);
        return new PaymentInfo(
                cardType,
                validNumber,
                mask(validNumber),
                validExpiry,
                validEmail
        );
    }

    public static PaymentInfo fromStored(CreditCardType cardType, String maskedCardNumber, String email) {
        return new PaymentInfo(
                cardType == null ? CreditCardType.NONE : cardType,
                "",
                maskedCardNumber == null ? "" : maskedCardNumber,
                "",
                email == null ? "" : email
        );
    }

    public CreditCardType getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getEmail() {
        return email;
    }

    private static String mask(String cardNumber) {
        return "************" + cardNumber.substring(cardNumber.length() - 4);
    }
}
