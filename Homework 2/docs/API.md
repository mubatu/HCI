# API Document - CatalogItemPanel

## Purpose

`CatalogItemPanel` is a reusable Swing control that encapsulates the catalog item entry portion of the Cheap Shop interface. It is designed to be embedded in multiple dialogs while keeping item input, validation, and total calculation logic in one place.

## Class Signature

```java
public class CatalogItemPanel extends JPanel
```

## Responsibilities

- Display the item number, quantity, cost/item, and total fields
- Recalculate the line total when quantity or cost changes
- Validate user input before creating a `CatalogItem`
- Reset or prefill the control when reused in different dialogs

## Public Methods

### `CatalogItemPanel()`

Creates the panel and initializes all entry fields, the quantity spinner, and the live total behavior.

### `String getItemNumber()`

Returns the trimmed item number currently shown in the control.

### `void setItemNumber(String itemNumber)`

Prefills the item number field.

### `int getQuantity()`

Returns the current quantity selected in the spinner.

### `void setQuantity(int quantity)`

Updates the quantity. Values below `1` are clamped to `1`.

### `BigDecimal getCostPerItem()`

Parses and returns the cost/item value. Throws `IllegalArgumentException` if the field is blank, non-numeric, or not positive.

### `void setCostPerItem(BigDecimal costPerItem)`

Prefills the cost/item field and refreshes the displayed total.

### `BigDecimal getLineTotal()`

Returns the current computed line total. If the cost field is currently invalid or blank, the method returns `0.00`.

### `CatalogItem getCatalogItem()`

Validates the current inputs and returns a fully constructed `CatalogItem` object.

### `void setCatalogItem(CatalogItem item)`

Loads an existing `CatalogItem` into the control.

### `void clear()`

Clears the item number and cost fields, resets quantity to `1`, and clears the displayed total.

### `void updateTotal()`

Refreshes the total field based on the current quantity and cost/item values.

### `void addValueChangeListener(Runnable listener)`

Registers a listener that is called whenever the item number, quantity, or cost/item field changes. The dialogs use this hook to update the running balance preview.

## Validation Behavior

- Item number is required
- Quantity must be greater than zero
- Cost per item must be a positive numeric value
- Monetary values are rounded to two decimal places

## Example Usage

```java
CatalogItemPanel panel = new CatalogItemPanel();
panel.setItemNumber("A-100");
panel.setQuantity(2);
panel.setCostPerItem(new BigDecimal("19.95"));
CatalogItem item = panel.getCatalogItem();
```
