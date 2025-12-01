package sk.uniba.fmph.dcs.terra_futura;

public class GridPosition {
    private int x, y;
    private final int leftConstraint = -2;
    private final int rightConstraint = 2;

    private void checkArg(Integer c) {
        if(c < leftConstraint || c > rightConstraint)
            throw new IllegalArgumentException("Coordinate should be of range: " + leftConstraint + " to " + rightConstraint);
    }

    public GridPosition(Integer x, Integer y) {
        checkArg(x);
        checkArg(y);
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        checkArg(x);
        this.x = x;
    }

    public void setY(Integer y) {
        checkArg(y);
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GridPosition))
            return false;
        return ((GridPosition) o).y == this.y && ((GridPosition) o).x == this.x;
    }

    @Override
    public int hashCode() {
        return 5 * x + y;
    }
}
