
public class CameraControlService {
    public static double calculateAngle(AngleDistanceModel angleDistanceModel) {
        // Convert angle from degrees to radians
        double angleOfTargetRadarTowerRadians = Math.toRadians(angleDistanceModel.getAngleOfTargetRadarTower());

        // Calculate coordinates of target point relative to point 1
        double targetXRelativeToRadar = angleDistanceModel.getDistanceOfTargetRadarTower() * Math.cos(angleOfTargetRadarTowerRadians);
        double targetYRelativeToRadar = angleDistanceModel.getDistanceOfTargetRadarTower() * Math.sin(angleOfTargetRadarTowerRadians);

        // Calculate absolute coordinates of target point
        double targetX = angleDistanceModel.getCameraTowerPosition().getX() + targetXRelativeToRadar;
        double targetY = targetYRelativeToRadar; // Assuming the target is above point 1

        // Calculate angle between point 2 and target point
        double anglePoint2ToTargetRadians = Math.atan2(targetY - angleDistanceModel.getCameraTowerPosition().getY(),
                targetX - angleDistanceModel.getCameraTowerPosition().getX());
        double angleCameraToTargetDegrees = Math.toDegrees(anglePoint2ToTargetRadians);

        // Adjust angle to be positive and in the range [0, 360)
        if (angleCameraToTargetDegrees < 0) {
            angleCameraToTargetDegrees += 360;
        }

        return angleCameraToTargetDegrees;
    }
}