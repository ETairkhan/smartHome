package people.smarthome.decorators;

import lombok.Getter;
import people.smarthome.models.Device;
import lombok.AllArgsConstructor;

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
    protected abstract void performEnhancedAction();
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
    public boolean isTurnedOn() {
        return decoratedDevice.isTurnedOn();
    }

    @Override
    public void setTurnedOn(boolean turnedOn) {
        decoratedDevice.setTurnedOn(turnedOn);
    }

    @Override
    public String getDeviceType() {
        return decoratedDevice.getDeviceType();
    }

    @Override
    public void setDeviceType(String deviceType) {
        decoratedDevice.setDeviceType(deviceType);
    }

    @Override
    public Integer getBrightness() {
        return decoratedDevice.getBrightness();
    }

    @Override
    public void setBrightness(Integer brightness) {
        decoratedDevice.setBrightness(brightness);
    }

    @Override
    public Integer getTemperature() {
        return decoratedDevice.getTemperature();
    }

    @Override
    public void setTemperature(Integer temperature) {
        decoratedDevice.setTemperature(temperature);
    }

    @Override
    public Integer getWaterFlow() {
        return decoratedDevice.getWaterFlow();
    }

    @Override
    public void setWaterFlow(Integer waterFlow) {
        decoratedDevice.setWaterFlow(waterFlow);
    }

    @Override
    public Boolean getIsLocked() {
        return decoratedDevice.getIsLocked();
    }

    @Override
    public void setIsLocked(Boolean locked) {
        decoratedDevice.setIsLocked(locked);
    }

    @Override
    public String getZone() {
        return decoratedDevice.getZone();
    }

    @Override
    public void setZone(String zone) {
        decoratedDevice.setZone(zone);
    }

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
}