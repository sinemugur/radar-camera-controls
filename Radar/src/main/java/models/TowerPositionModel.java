package models;


import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.kafka.common.serialization.Deserializer;

public class TowerPositionModel implements Deserializer{
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
    public TowerPositionModel deserialize(String topic, byte[] data) {
    	System.out.println(data);
        try {
            if (data == null) {
                return null;
            }
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
            System.out.println("1");
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
            System.out.println(2);
            TowerPositionModel myModel = (TowerPositionModel) objectInputStream.readObject();
            System.out.println(3);
            objectInputStream.close();
            return myModel;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("HATA HATA HATA", e);
        }
    }
}