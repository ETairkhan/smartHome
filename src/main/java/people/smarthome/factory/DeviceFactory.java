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
        light.setBrightness(50);
        light.setPowerLevel(60);
        light.setIsActive(true);
        return light;
    }

    public Device createThermostat(String name) {
        Thermostat thermostat = new Thermostat();
        thermostat.setName(name);
        thermostat.setDeviceType("THERMOSTAT");
        thermostat.setTemperature(22);
        thermostat.setPowerLevel(30);
        thermostat.setIsActive(true);
        return thermostat;
    }

    public Device createSprinkler(String name) {
        Sprinkler sprinkler = new Sprinkler();
        sprinkler.setName(name);
        sprinkler.setDeviceType("SPRINKLER");
        sprinkler.setWaterFlow(50);
        sprinkler.setPowerLevel(40);
        sprinkler.setIsActive(true);
        return sprinkler;
    }

    public Device createDoor(String name) {
        Door door = new Door();
        door.setName(name);
        door.setDeviceType("DOOR");
        door.setIsLocked(true);
        door.setPowerLevel(10);
        door.setIsActive(true);
        return door;
    }

    public Device createWindow(String name) {
        Window window = new Window();
        window.setName(name);
        window.setDeviceType("WINDOW");
        window.setIsLocked(true);
        window.setPowerLevel(5);
        window.setIsActive(true);
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
