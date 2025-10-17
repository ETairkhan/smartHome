package people.smarthome.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "windows")
public class Window extends Device {
    protected String deviceType;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Override
    public String getDeviceType(){
        return "WINDOW";
    }

    @Override
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }

    @Override
    public void operate() {
        System.out.println("Operating window: " + getName());
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsLocked(){
        return this.isLocked;
    }
}
