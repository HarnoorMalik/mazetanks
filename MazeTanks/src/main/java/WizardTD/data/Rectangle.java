package WizardTD.data;

/**
 * Represents Rectangle with the coordinates and the size
 */
public class Rectangle {
    public int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    public boolean contains(int x1, int y1) {
        boolean inXBounds = x1 >= x && x1 <= x + width;
        boolean inYBounds = y1 >= y && y1 <= y + width;
        return inXBounds && inYBounds;
    }

    public Point center() {
        return new Point(Math.round(x + width / 2f), Math.round(y + height / 2f));
    }

    public Rectangle copy() {
        return new Rectangle(x, y, width, height);
    }

}
