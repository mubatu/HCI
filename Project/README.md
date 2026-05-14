# Music Shop Java Swing Project

This project is a Java Swing online shopping interface for CmpE 496. It demonstrates common Java desktop controls, validation, popup dialogs, checkout flow, local persistence, and simple statistics.

## Features

- Login popup dialog before shoppers can access the store
- 10 seeded music products
- Product selection with checkboxes and quantity editing
- Running order total
- Credit card type selection with default value `None`
- Modal payment form for Visa, MasterCard, and Discover
- Validation for:
  - 16-digit credit card number
  - Expiry date in `mm/yy` format
  - Non-expired card date
  - Email address
- Invoice dialog after successful checkout
- Local file-backed database in `data/`
- Statistics dialog for:
  - Total orders
  - Total revenue
  - Most purchased product
  - Orders by credit card type

## Demo Login

Use either account:

```text
Username: student
Password: hci123
```

```text
Username: shopper
Password: music123
```

## Compile

From the Project folder:

```bash
cd src
javac *.java
```

## Run

```bash
java MusicShopApplication
```

## Data Files

The app creates and maintains these files in `data/`:

- `users.db`
- `products.db`
- `orders.db`

Users and products are seeded automatically on first run. Orders are appended after successful checkout, so statistics continue to work after the app restarts.