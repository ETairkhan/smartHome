package people.smarthome.factory;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceFactory {

    public Device createLight(String name) {
        Device device = new Device();
        device.setName(name);
        device.setDeviceType("LIGHT");
        device.setBrightness(50);
        device.setPowerLevel(60);
        device.setIsActive(false);
        return device;
    }

    public Device createThermostat(String name) {
        Device device = new Device();
        device.setName(name);
        device.setDeviceType("THERMOSTAT");
        device.setTemperature(22);
        device.setPowerLevel(30);
        device.setIsActive(true);
        return device;
    }

    public Device createSprinkler(String name) {
        Device device = new Device();
        device.setName(name);
        device.setDeviceType("SPRINKLER");
        device.setWaterFlow(50);
        device.setZone("garden");
        device.setPowerLevel(40);
        device.setIsActive(false);
        return device;
    }

    public Device createDoor(String name) {
        Device device = new Device();
        device.setName(name);
        device.setDeviceType("DOOR");
        device.setIsLocked(true);
        device.setPowerLevel(10);
        device.setIsActive(false);
        return device;
    }

    public Device createWindow(String name) {
        Device device = new Device();
        device.setName(name);
        device.setDeviceType("WINDOW");
        device.setIsLocked(true);
        device.setPowerLevel(5);
        device.setIsActive(false);
        return device;
    }

    public Device createFromType(String name, String deviceType) {
        return switch (deviceType.toUpperCase()) {
            case "LIGHT" -> createLight(name);
            case "THERMOSTAT" -> createThermostat(name);
            case "SPRINKLER" -> createSprinkler(name);
            case "DOOR" -> createDoor(name);
            case "WINDOW" -> createWindow(name);
            default -> throw new IllegalArgumentException("Unknown device type: " + deviceType);
        };
    }
}