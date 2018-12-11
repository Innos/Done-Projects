package Models;


public interface ModifiableBoundedObject extends BoundedObject {
    void setX(int x);
    void setY(int y);
    void setWidth(int width);
    void setHeight(int height);
}
