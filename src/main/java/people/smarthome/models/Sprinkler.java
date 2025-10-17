package people.smarthome.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "sprinklers")
public class Sprinkler extends Device {
    protected String deviceType;
    @Column(name = "water_flow")
    private Integer waterFlow;

    @Override
    public String getDeviceType() {
        return "SPRINKLER";
    }

    @Override
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public void operate() {
        System.out.println("Operating sprinkler: " + getName() + " with water flow: " + waterFlow);
    }

    public void setWaterFlow(int waterFlow) {
        this.waterFlow = waterFlow;
    }

    public int getWaterFlow() {
        return this.waterFlow;
    }
}
