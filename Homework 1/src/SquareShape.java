import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

// Square shape.
public class SquareShape extends DrawableShape {
    private int x;
    private int y;
    private final int size;
    private final Color color;

    public SquareShape(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = new Color(52, 152, 219);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(x, y, size, size);
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, size, size);
    }

    @Override
    public boolean contains(Point point) {
        return point.x >= x && point.x <= x + size
                && point.y >= y && point.y <= y + size;
    }

    @Override
    public void moveBy(int dx, int dy) {
        x += dx;
        y += dy;
    }
}
