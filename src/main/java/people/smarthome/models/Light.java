package people.smarthome.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "lights")
public class Light extends Device {
    protected String deviceType;

    @Column(name = "brightness")
    private Integer brightness;

    @Override
    public String getDeviceType() {
        return "LIGHT";
    }

    @Override
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }


    public void setBrightness(int  brightness){
        this.brightness = brightness;
    }

    public int getBrightness(){
        return this.brightness;
    }

    @Override
    public void operate() {
        // Logic to operate the light
        System.out.println("Operating light: " + getName() + " with brightness: " + brightness);
    }

}
