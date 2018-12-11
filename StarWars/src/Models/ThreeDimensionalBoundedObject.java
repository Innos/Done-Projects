package Models;

public interface ThreeDimensionalBoundedObject extends BoundedObject{

    int getZ();

    boolean intersects(ThreeDimensionalBoundedObject other);
}
