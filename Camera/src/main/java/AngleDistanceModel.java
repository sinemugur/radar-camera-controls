
import java.awt.geom.Point2D;

public class AngleDistanceModel {
	private double distanceOfTargetRadarTower;
	private double angleOfTargetRadarTower;
	private double angleOfTargetCameraTower;
	private Point2D.Double cameraTowerPosition;

	public double getDistanceOfTargetRadarTower() {
		return distanceOfTargetRadarTower;
	}

	public void setDistanceOfTargetRadarTower(double distanceOfTargetRadarTower) {
		this.distanceOfTargetRadarTower = distanceOfTargetRadarTower;
	}

	public double getAngleOfTargetRadarTower() {
		return angleOfTargetRadarTower;
	}

	public void setAngleOfTargetRadarTower(double angleOfTargetRadarTower) {
		this.angleOfTargetRadarTower = angleOfTargetRadarTower;
	}

	public double getAngleOfTargetCameraTower() {
		return angleOfTargetCameraTower;
	}

	public void setAngleOfTargetCameraTower(double angleOfTargetCameraTower) {
		this.angleOfTargetCameraTower = angleOfTargetCameraTower;
	}

	public Point2D.Double getCameraTowerPosition() {
		return cameraTowerPosition;
	}

	public void setCameraTowerPosition(Point2D.Double cameraTowerPosition) {
		this.cameraTowerPosition = cameraTowerPosition;
	}
}