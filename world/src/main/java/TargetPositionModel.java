


import java.awt.geom.Point2D;
import java.io.Serializable;

public class TargetPositionModel implements Serializable{
    private Point2D.Double targetPosition;

    public Point2D.Double getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Point2D.Double targetPosition) {
        this.targetPosition = targetPosition;
    }
}