package Models;

import processing.core.PImage;

public class DrawableImage implements ModifiableBoundedObject {
    private int x;
    private int y;
    private int width;
    private int height;
    private int imageX;
    private int imageY;
    private int imageWidth;
    private int imageHeight;
    private PImage image;


    public DrawableImage(int x, int y, int width, int height, int imageWidth, int imageHeight, PImage image){
        this.x = x;
        this.y = y;
        this.imageX = x;
        this.imageY = y;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.image = image;
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

    public void setImage(PImage image){ this.image = image; }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setImageX(int x) {
        this.imageX = x;
    }

    public void setImageY(int y) {
        this.imageY = y;
    }

    public int getImageX() {
        return this.imageX;
    }

    public int getImageY() {
        return this.imageY;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void setImageWidth(int width) {
        this.imageWidth = width;
    }

    public void setImageHeight(int height) {
        this.imageHeight = height;
    }

    public PImage getImage(){ return this.image; }

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
}
