import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDatabase {
    private static final DateTimeFormatter ORDER_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path dataDirectory;
    private final Path usersFile;
    private final Path productsFile;
    private final Path ordersFile;

    public ShopDatabase() {
        this(Paths.get("data"));
    }

    public ShopDatabase(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.usersFile = dataDirectory.resolve("users.db");
        this.productsFile = dataDirectory.resolve("products.db");
        this.ordersFile = dataDirectory.resolve("orders.db");
    }

    public void initialize() {
        try {
            Files.createDirectories(dataDirectory);
            if (Files.notExists(usersFile)) {
                Files.write(usersFile, seedUsers(), StandardCharsets.UTF_8);
            }
            if (Files.notExists(productsFile)) {
                Files.write(productsFile, seedProducts(), StandardCharsets.UTF_8);
            }
            if (Files.notExists(ordersFile)) {
                Files.write(ordersFile, new ArrayList<String>(), StandardCharsets.UTF_8);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to initialize local database.", exception);
        }
    }

    public List<User> loadUsers() {
        initialize();
        List<User> users = new ArrayList<>();
        for (String line : readLines(usersFile)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length == 3) {
                users.add(new User(parts[0], parts[1], parts[2]));
            }
        }
        return users;
    }

    public List<Product> loadProducts() {
        initialize();
        List<Product> products = new ArrayList<>();
        for (String line : readLines(productsFile)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length == 5) {
                products.add(new Product(parts[0], parts[1], parts[2], parts[3], new BigDecimal(parts[4])));
            }
        }
        return products;
    }

    public List<OrderRecord> loadOrders() {
        initialize();
        List<Product> products = loadProducts();
        Map<String, Product> productById = new HashMap<>();
        for (Product product : products) {
            productById.put(product.getId(), product);
        }

        List<OrderRecord> orders = new ArrayList<>();
        for (String line : readLines(ordersFile)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", -1);
            if (parts.length == 7) {
                List<CartItem> items = decodeItems(parts[6], productById);
                if (!items.isEmpty()) {
                    orders.add(new OrderRecord(
                            parts[0],
                            parts[1],
                            LocalDateTime.parse(parts[2], ORDER_DATE_FORMAT),
                            PaymentInfo.fromStored(CreditCardType.fromName(parts[3]), parts[4], parts[5]),
                            items
                    ));
                }
            }
        }
        return orders;
    }

    public void saveOrder(OrderRecord order) {
        initialize();
        List<String> lines = readLines(ordersFile);
        lines.add(encodeOrder(order));
        writeLines(ordersFile, lines);
    }

    private List<String> seedUsers() {
        List<String> users = new ArrayList<>();
        users.add("student|hci123|HCI Student");
        users.add("shopper|music123|Demo Shopper");
        return users;
    }

    private List<String> seedProducts() {
        List<String> products = new ArrayList<>();
        products.add("M001|Acoustic Guitar|Instrument|Warm six-string acoustic guitar for practice and performance|249.99");
        products.add("M002|Electric Keyboard|Instrument|61-key portable keyboard with built-in learning modes|189.50");
        products.add("M003|Studio Headphones|Audio|Closed-back headphones for focused listening and recording|79.99");
        products.add("M004|Bluetooth Speaker|Audio|Compact wireless speaker with clear room-filling sound|54.99");
        products.add("M005|Vinyl Record Set|Albums|Classic rock vinyl starter set with three records|64.75");
        products.add("M006|Digital Piano Bench|Furniture|Adjustable padded bench for piano and keyboard players|89.00");
        products.add("M007|Microphone Kit|Recording|USB condenser microphone with stand and pop filter|119.95");
        products.add("M008|Guitar Strings Pack|Accessories|Three-pack of light gauge acoustic guitar strings|24.99");
        products.add("M009|Sheet Music Bundle|Books|Beginner-friendly piano and guitar sheet music collection|34.50");
        products.add("M010|Portable Music Stand|Accessories|Foldable metal music stand with carrying bag|29.99");
        return products;
    }

    private List<String> readLines(Path file) {
        try {
            return Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read " + file + ".", exception);
        }
    }

    private void writeLines(Path file, List<String> lines) {
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write " + file + ".", exception);
        }
    }

    private String encodeOrder(OrderRecord order) {
        return order.getOrderId()
                + "|" + order.getUsername()
                + "|" + ORDER_DATE_FORMAT.format(order.getCreatedAt())
                + "|" + order.getPaymentInfo().getCardType().name()
                + "|" + order.getPaymentInfo().getMaskedCardNumber()
                + "|" + order.getPaymentInfo().getEmail()
                + "|" + encodeItems(order.getItems());
    }

    private String encodeItems(List<CartItem> items) {
        List<String> encoded = new ArrayList<>();
        for (CartItem item : items) {
            encoded.add(item.getProduct().getId() + ":" + item.getQuantity());
        }
        return String.join(";", encoded);
    }

    private List<CartItem> decodeItems(String encodedItems, Map<String, Product> productById) {
        List<CartItem> items = new ArrayList<>();
        if (encodedItems == null || encodedItems.trim().isEmpty()) {
            return items;
        }
        String[] itemParts = encodedItems.split(";");
        for (String itemPart : itemParts) {
            String[] details = itemPart.split(":", -1);
            if (details.length == 2 && productById.containsKey(details[0])) {
                items.add(new CartItem(productById.get(details[0]), Integer.parseInt(details[1])));
            }
        }
        return items;
    }
}
