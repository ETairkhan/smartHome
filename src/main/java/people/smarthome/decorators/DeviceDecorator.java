package people.smarthome.decorators;

import lombok.Getter;
import people.smarthome.models.Device;
import lombok.AllArgsConstructor;
import people.smarthome.models.Light;
import people.smarthome.models.Door;
import people.smarthome.models.Sprinkler;
import people.smarthome.models.Thermostat;
import people.smarthome.models.Window;

@AllArgsConstructor
public abstract class DeviceDecorator extends Device {
    @Getter
    protected Device decoratedDevice;

    @Override
    public void operate() {
        decoratedDevice.operate();
        performEnhancedAction();
    }

    @Override
    public String getStatus() {
        return decoratedDevice.getStatus() + getEnhancementStatus();
    }

    // Abstract methods that subclasses must implement
    public abstract void performEnhancedAction();
    protected abstract String getEnhancementStatus();

    // Delegation methods for all Device properties
    @Override
    public Long getId() {
        return decoratedDevice.getId();
    }

    @Override
    public String getName() {
        return decoratedDevice.getName();
    }

    @Override
    public void setName(String name) {
        decoratedDevice.setName(name);
    }

    @Override
    public Boolean getIsActive() {
        return decoratedDevice.getIsActive();
    }

    @Override
    public void setIsActive(Boolean active) {
        decoratedDevice.setIsActive(active);
    }

    @Override
    public String getDeviceType() {
        return decoratedDevice.getDeviceType();
    }

    @Override
    public void setDeviceType(String deviceType) {
        decoratedDevice.setDeviceType(deviceType);
    }

    public Integer getBrightness() {
        if (decoratedDevice instanceof Light light) {
            return light.getBrightness();
        }
        return null;
    }

    public void setBrightness(Integer brightness) {
        if (decoratedDevice instanceof Light light) {
            light.setBrightness(brightness);
        }
    }


    public Integer getTemperature() {
        if (decoratedDevice instanceof Thermostat thermostat) {
            return thermostat.getTemperature();
        }
        return null;
    }

    public void setTemperature(Integer temperature) {
        if (decoratedDevice instanceof Thermostat thermostat) {
            thermostat.setTemperature(temperature);
        }
    }

    public Integer getWaterFlow() {
        if (decoratedDevice instanceof Sprinkler sprinkler) {
            return sprinkler.getWaterFlow();
        }
        return null;
    }

    public void setWaterFlow(Integer waterFlow) {
        if (decoratedDevice instanceof Sprinkler sprinkler) {
            sprinkler.setWaterFlow(waterFlow);
        }
    }

    public Boolean getIsLocked() {
        if (decoratedDevice instanceof Door door) {
            return door.getIsLocked();
        }
        if (decoratedDevice instanceof Window window) {
            return window.getIsLocked();
        }
        return null;
    }

    public void setIsLocked(Boolean locked) {
        if (decoratedDevice instanceof Door door) {
            door.setIsLocked(locked);
        }
        if (decoratedDevice instanceof Window window) {
            window.setIsLocked(locked);
        }
    }

//    @Override
//    public String getZone() {
//        return decoratedDevice.getZone();
//    }
//
//    @Override
//    public void setZone(String zone) {
//        decoratedDevice.setZone(zone);
//    }

    @Override
    public Integer getPowerLevel() {
        return decoratedDevice.getPowerLevel();
    }

    @Override
    public void setPowerLevel(Integer powerLevel) {
        decoratedDevice.setPowerLevel(powerLevel);
    }

    @Override
    public void toggle() {
        decoratedDevice.toggle();
    }

    // Method to handle decorated devices
    private Device getBaseDevice(Device device) {
        while (device instanceof DeviceDecorator) {
            device = ((DeviceDecorator) device).getDecoratedDevice();  // Unwrap the decorated device
        }
        return device;  // Return the original device
    }

}
