import java.awt.Graphics2D;
import java.awt.Point;

// Base class for all shapes.
public abstract class DrawableShape {
    public abstract void draw(Graphics2D g2);

    public abstract boolean contains(Point point);

    public abstract void moveBy(int dx, int dy);
}
