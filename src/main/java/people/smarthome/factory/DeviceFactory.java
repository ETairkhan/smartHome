package people.smarthome.factory;

import people.smarthome.models.Device;
import people.smarthome.models.Light;
import people.smarthome.models.Sprinkler;
import people.smarthome.models.Thermostat;
import people.smarthome.models.Door;
import people.smarthome.models.Window;
import org.springframework.stereotype.Component;

@Component
public class DeviceFactory {

    public Device createLight(String name) {
        Light light = new Light();
        light.setName(name);
        light.setDeviceType("LIGHT");
        light.setBrightness(50); // Set default brightness
        light.setPowerLevel(60); // Default power level
        light.setIsActive(false); // Default state is inactive
        return light;
    }

    public Device createThermostat(String name) {
        Thermostat thermostat = new Thermostat();
        thermostat.setName(name);
        thermostat.setDeviceType("THERMOSTAT");
        thermostat.setTemperature(22); // Default temperature
        thermostat.setPowerLevel(30); // Default power level
        thermostat.setIsActive(true); // Default state is active
        return thermostat;
    }

    public Device createSprinkler(String name) {
        Sprinkler sprinkler = new Sprinkler();
        sprinkler.setName(name);
        sprinkler.setDeviceType("SPRINKLER");
        sprinkler.setWaterFlow(50); // Default water flow
        sprinkler.setPowerLevel(40); // Default power level
        sprinkler.setIsActive(false); // Default state is inactive
        return sprinkler;
    }

    public Device createDoor(String name) {
        Door door = new Door();
        door.setName(name);
        door.setDeviceType("DOOR");
        door.setIsLocked(true); // Default door is locked
        door.setPowerLevel(10); // Default power level
        door.setIsActive(false); // Default state is inactive
        return door;
    }

    public Device createWindow(String name) {
        Window window = new Window();
        window.setName(name);
        window.setDeviceType("WINDOW");
        window.setIsLocked(true); // Default window is locked
        window.setPowerLevel(5); // Default power level
        window.setIsActive(false); // Default state is inactive
        return window;
    }

    public Device createFromType(String name, String deviceType) {
        switch (deviceType.toUpperCase()) {
            case "LIGHT":
                return createLight(name);
            case "THERMOSTAT":
                return createThermostat(name);
            case "SPRINKLER":
                return createSprinkler(name);
            case "DOOR":
                return createDoor(name);
            case "WINDOW":
                return createWindow(name);
            default:
                throw new IllegalArgumentException("Unknown device type: " + deviceType);
        }
    }
}
