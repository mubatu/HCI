# Homework 1 - Simple Drawing Editor

This is a very small Java Swing starter project for an object-oriented drawing editor.

## Class Structure

- `DrawingEditor`: starts the program
- `EditorFrame`: main window and tool buttons
- `DrawingPanel`: drawing area and mouse actions
- `DrawableShape`: base class for all shapes
- `SquareShape`, `CircleShape`, `LineShape`: concrete shape classes
- `Tool`: keeps the selected tool name

## Current Features

- Create squares
- Create circles
- Create lines
- Move shapes with the select tool
- Erase shapes

## Compile and Run

```bash
javac src/*.java
java -cp src DrawingEditor
```
