# Homework 2 - Cheap Shop Dialogs

This project implements a Java Swing version of the Cheap Shop catalog ordering dialogs shown in the course handout examples.

## Included Deliverables

- A runnable desktop application with two shopping dialogs
- A packaged reusable control: `CatalogItemPanel`
- An API document for the control
- A short report with design details and shopping scenarios
- A portfolio summary for the dialog design

## Project Structure

- `src/`: Java source files
- `docs/API.md`: API description for `CatalogItemPanel`

## Compile and Run

```bash
javac src/*.java
java src CheapShopApplication
```

## Interaction Notes

- The first dialog collects purchaser details and the first catalog item.
- The second dialog lets the user keep adding items before creating the invoice.
- The line total updates automatically from quantity and cost per item.
- The balance owing updates as items are added.
- Function-key shortcuts follow the handout style:
  - Dialog 1: `F5` for next item, `F8` for invoice
  - Dialog 2: `F8` for next item, `F5` for invoice

## Validation Rules

- Required purchaser fields cannot be blank.
- The date must use `YYYY-MM-DD`.
- Quantity must be greater than zero.
- Cost per item must be a positive number.
