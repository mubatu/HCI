import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;

// Line shape.
public class LineShape extends DrawableShape {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private final Color color;

    public LineShape(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = new Color(46, 204, 113);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x1, y1, x2, y2);
    }

    @Override
    public boolean contains(Point point) {
        return Line2D.ptSegDist(x1, y1, x2, y2, point.x, point.y) <= 5;
    }

    @Override
    public void moveBy(int dx, int dy) {
        x1 += dx;
        y1 += dy;
        x2 += dx;
        y2 += dy;
    }
}
