import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

// Circle shape.
public class CircleShape extends DrawableShape {
    private int centerX;
    private int centerY;
    private final int radius;
    private final Color color;

    public CircleShape(int centerX, int centerY, int radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.color = new Color(231, 76, 60);
    }

    @Override
    public void draw(Graphics2D g2) {
        int diameter = radius * 2;
        int topLeftX = centerX - radius;
        int topLeftY = centerY - radius;

        g2.setColor(color);
        g2.fillOval(topLeftX, topLeftY, diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(topLeftX, topLeftY, diameter, diameter);
    }

    @Override
    public boolean contains(Point point) {
        int dx = point.x - centerX;
        int dy = point.y - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public void moveBy(int dx, int dy) {
        centerX += dx;
        centerY += dy;
    }
}
