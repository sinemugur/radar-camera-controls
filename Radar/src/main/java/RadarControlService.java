
import models.AngleDistanceModel;
import models.TargetPositionModel;
import models.TowerPositionModel;

public class RadarControlService {
	public static AngleDistanceModel getAngleDistance(TowerPositionModel towerPositionModel,
			TargetPositionModel targetPositionModel) {
		AngleDistanceModel returnModel = new AngleDistanceModel();
		returnModel.setCameraTowerPosition(towerPositionModel.getCameraTowerPosition());

		try {
			returnModel.setDistanceOfTargetRadarTower(calculateDistance(towerPositionModel, targetPositionModel));
		} catch (Exception e) {
			System.out.println("Calculation of distance failed.");
		}

		try {
			returnModel.setAngleOfTargetRadarTower(calculateAngle(towerPositionModel, targetPositionModel));
		} catch (Exception e) {
			System.out.println("Calculating of angle failed.");
		}
		return returnModel;
	}

	private static double calculateDistance(TowerPositionModel point1, TargetPositionModel point2) {
		double deltaX = point2.getTargetPosition().getX() - point1.getRadarTowerPosition().getX();
		double deltaY = point2.getTargetPosition().getY() - point1.getRadarTowerPosition().getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	private static double calculateAngle(TowerPositionModel point1, TargetPositionModel point2) {
		double angle = Math.atan2(point2.getTargetPosition().getY() - point1.getRadarTowerPosition().getY(),
				point2.getTargetPosition().getX() - point1.getRadarTowerPosition().getX());
		return Math.toDegrees(angle);
	}
}