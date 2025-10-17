package people.smarthome.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "thermostats")
public class Thermostat extends Device {
    protected String deviceType;

    @Column(name = "temperature")
    private Integer temperature;

    @Override
    public String getDeviceType() {
        return "THERMOSTAT";
    }

    @Override
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getTemperature(){
        return this.temperature;
    }

    @Override
    public void operate() {
        // Logic to operate the thermostat
        System.out.println("Operating thermostat: " + getName() + " with temperature: " + temperature);
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
