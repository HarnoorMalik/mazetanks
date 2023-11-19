package WizardTD.data;

import processing.core.PVector;

/**
 * Represents Rectangle with the coordinates and the size
 */
public class RectangleF {
    public float x, y, width, height;

    public RectangleF(float x, float y, float width, float height) {
        setPosition(x, y);
        setSize(width, height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(float w, float h) {
        this.width = w;
        this.height = h;
    }

    public RectangleF copy() {
        return new RectangleF(x, y, width, height);
    }

    public PVector center() {
        return new PVector(x + width / 2f, y + height / 2f);
    }

}
