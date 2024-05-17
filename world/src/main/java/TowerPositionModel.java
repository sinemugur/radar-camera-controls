


import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.kafka.common.serialization.Serializer;

public class TowerPositionModel implements Serializable, Serializer{
    private Point2D.Double radarTowerPosition;
    private Point2D.Double cameraTowerPosition;

    public Point2D.Double getRadarTowerPosition() {
        return radarTowerPosition;
    }

    public void setRadarTowerPosition(Point2D.Double radarTowerPosition) {
        this.radarTowerPosition = radarTowerPosition;
    }

    public Point2D.Double getCameraTowerPosition() {
        return cameraTowerPosition;
    }

    public void setCameraTowerPosition(Point2D.Double cameraTowerPosition) {
        this.cameraTowerPosition = cameraTowerPosition;
    }

	@Override
	public byte[] serialize(String topic, Object data) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(data);
	        oos.flush();
	        return bos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		// TODO Auto-generated method stub
		
	}
}