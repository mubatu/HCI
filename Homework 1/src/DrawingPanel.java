import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// This is the drawing area.
public class DrawingPanel extends JPanel {
    // Stores all shapes.
    private final List<DrawableShape> shapes = new ArrayList<>();
    private Tool currentTool = Tool.SELECT;
    private DrawableShape selectedShape;
    private Point lastDragPoint;
    private Point lineStartPoint;
    private Point currentMousePoint;

    public DrawingPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 500));

        // One handler for mouse actions.
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                handleMousePressed(event.getPoint());
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                handleMouseDragged(event.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                handleMouseReleased(event.getPoint());
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                currentMousePoint = event.getPoint();
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setCurrentTool(Tool currentTool) {
        this.currentTool = currentTool;
        selectedShape = null;
        lastDragPoint = null;
        lineStartPoint = null;
        repaint();
    }

    private void handleMousePressed(Point point) {
        if (currentTool == Tool.SQUARE) {
            // Add a square.
            shapes.add(new SquareShape(point.x - 30, point.y - 30, 60));
            repaint();
            return;
        }

        if (currentTool == Tool.CIRCLE) {
            // Add a circle.
            shapes.add(new CircleShape(point.x, point.y, 30));
            repaint();
            return;
        }

        if (currentTool == Tool.LINE) {
            // Save the first point.
            lineStartPoint = point;
            currentMousePoint = point;
            repaint();
            return;
        }

        if (currentTool == Tool.ERASE) {
            // Remove the top shape.
            DrawableShape shapeToRemove = findShapeAt(point);
            if (shapeToRemove != null) {
                shapes.remove(shapeToRemove);
                repaint();
            }
            return;
        }

        // Pick a shape to move.
        selectedShape = findShapeAt(point);
        lastDragPoint = point;
    }

    private void handleMouseDragged(Point point) {
        currentMousePoint = point;

        if (currentTool == Tool.SELECT && selectedShape != null && lastDragPoint != null) {
            // Move the selected shape.
            int dx = point.x - lastDragPoint.x;
            int dy = point.y - lastDragPoint.y;
            selectedShape.moveBy(dx, dy);
            lastDragPoint = point;
        }

        repaint();
    }

    private void handleMouseReleased(Point point) {
        if (currentTool == Tool.LINE && lineStartPoint != null) {
            // Finish the line.
            shapes.add(new LineShape(lineStartPoint.x, lineStartPoint.y, point.x, point.y));
        }

        selectedShape = null;
        lastDragPoint = null;
        lineStartPoint = null;
        repaint();
    }

    private DrawableShape findShapeAt(Point point) {
        // Check from top to bottom.
        for (int i = shapes.size() - 1; i >= 0; i--) {
            DrawableShape shape = shapes.get(i);
            if (shape.contains(point)) {
                return shape;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (DrawableShape shape : shapes) {
            shape.draw(g2);
        }

        if (currentTool == Tool.LINE && lineStartPoint != null && currentMousePoint != null) {
            // Show a preview line.
            g2.setColor(Color.GRAY);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(lineStartPoint.x, lineStartPoint.y, currentMousePoint.x, currentMousePoint.y);
        }
    }
}
