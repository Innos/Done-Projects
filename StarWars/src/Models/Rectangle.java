package Models;

import static Constants.Constants.*;

public class Rectangle implements ModifiableThreeDimensionalBoundedObject {

    private int x;
    private int y;
    private int z;
    private int width;
    private int height;

    public Rectangle(int x, int y, int z, int width, int height){
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean intersects(BoundedObject other) {
        int rightX = this.x + this.width;
        int bottomY = this.y + this.height;
        int otherRightX = other.getX() + other.getWidth();
        int otherBottomY = other.getY() + other.getHeight();

        boolean intersectsX = (this.x <= other.getX() && rightX >= other.getX()) ||
                (other.getX() <= this.x && otherRightX >= this.x);

        boolean intersectsY = (this.y <= other.getY() && bottomY >= other.getY()) ||
                (other.getY() <= this.y && otherBottomY >= this.y);

        if(intersectsX && intersectsY){
            return true;
        }

        return false;
    }

    @Override
    public boolean intersects(ThreeDimensionalBoundedObject other) {
        boolean intersectsTwoDimensionally = this.intersects((BoundedObject)other);

        boolean hasDepthCollision = Math.abs(this.z - other.getZ()) <= allowedDepthCollisionDifference;
        if(intersectsTwoDimensionally && hasDepthCollision){
            return true;
        }

        return false;
    }
}
