import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.regex.Pattern;

public final class ValidationUtils {
    private static final Pattern CARD_PATTERN = Pattern.compile("\\d{16}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final DateTimeFormatter EXPIRY_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("MM/")
            .appendValueReduced(ChronoField.YEAR, 2, 2, 2000)
            .toFormatter(Locale.US);

    private ValidationUtils() {
    }

    public static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    public static String validateCardNumber(String value) {
        String text = requireText(value, "Credit card number");
        if (!CARD_PATTERN.matcher(text).matches()) {
            throw new IllegalArgumentException("Credit card number must contain exactly 16 digits.");
        }
        return text;
    }

    public static String validateExpiry(String value) {
        String text = requireText(value, "Expiry date");
        if (!text.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must use the format mm/yy.");
        }

        try {
            YearMonth expiry = YearMonth.parse(text, EXPIRY_FORMAT);
            if (expiry.isBefore(YearMonth.now())) {
                throw new IllegalArgumentException("Expiry date must not be in the past.");
            }
            return text;
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Expiry date must be a valid month in mm/yy format.");
        }
    }

    public static String validateEmail(String value) {
        String text = requireText(value, "Email address");
        if (!EMAIL_PATTERN.matcher(text).matches()) {
            throw new IllegalArgumentException("Email address must be valid.");
        }
        return text;
    }
}
