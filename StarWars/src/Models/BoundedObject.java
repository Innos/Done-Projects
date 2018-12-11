package Models;


public interface BoundedObject {
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    boolean intersects(BoundedObject other);
}
