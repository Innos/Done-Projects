package Models;


public interface ModifiableThreeDimensionalBoundedObject extends ThreeDimensionalBoundedObject {
    void setX(int x);
    void setY(int y);
    void setWidth(int width);
    void setHeight(int height);
}
