package people.smarthome.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doors")
public class Door extends Device {
    protected String deviceType;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Override
    public String getDeviceType(){
        return "DOOR";
    }

    @Override
    public void setDeviceType(String deviceType){
        this.deviceType = deviceType;
    }

    @Override
    public void operate() {
        System.out.println("Operating door: " + getName());
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsLocked() {
        return isLocked;
    }
}
