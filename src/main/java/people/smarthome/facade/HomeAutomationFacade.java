package people.smarthome.facade;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import people.smarthome.models.Device;
import org.springframework.stereotype.Component;
import people.smarthome.models.*;
import people.smarthome.service.SmartHomeService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HomeAutomationFacade {
    private static final Logger log = LoggerFactory.getLogger(HomeAutomationFacade.class);
    private final List<Device> devices;

    public HomeAutomationFacade() {
        this.devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void addDevices(List<Device> devices){
        this.devices.addAll(devices);
    }

    public void activateScene(String scene) {
        switch (scene.toUpperCase()) {
            case "NIGHT" -> activateNightMode();
            case "PARTY" -> activatePartyMode();
            case "AWAY" -> activateAwayMode();
            case "GARDEN" -> activateGardenMode();
            default -> throw new IllegalArgumentException("Unknown scene: " + scene);
        }
    }

    private void activateNightMode() {

        devices.forEach(device -> {
            Device baseDevice = SmartHomeService.getBaseDevice(device);
            baseDevice.setIsActive(false);
            if ("LIGHT".equals(baseDevice.getDeviceType()) && baseDevice instanceof Light light ) {
                light.setBrightness(10);
            }
        });
    }

    private void activatePartyMode() {
        devices.forEach(device -> {
            Device baseDevice = SmartHomeService.getBaseDevice(device);
            baseDevice.setIsActive(true);
            if ("LIGHT".equals(baseDevice.getDeviceType()) && baseDevice instanceof Light light ) {
                light.setBrightness(80);
            }
            if ("DOOR".equals(baseDevice.getDeviceType()) && baseDevice instanceof Door door) {
                door.setIsLocked(false);
            }
            if ("WINDOW".equals(baseDevice.getDeviceType()) && baseDevice instanceof Window window) {
                window.setIsLocked(false);
            }
        });
    }

    private void activateAwayMode() {
        devices.forEach(device -> {
            Device baseDevice = SmartHomeService.getBaseDevice(device);
            baseDevice.setIsActive(false);
            if ("DOOR".equals(baseDevice.getDeviceType()) && baseDevice instanceof Door door) {
                door.setIsLocked(true);
            }
            if ("WINDOW".equals(baseDevice.getDeviceType()) && baseDevice instanceof Window window) {
                window.setIsLocked(true);
            }
        });
    }

    private void activateGardenMode() {
        devices.forEach(device -> {
            Device baseDevice = SmartHomeService.getBaseDevice(device);
            if ("SPRINKLER".equals(baseDevice.getDeviceType()) && baseDevice instanceof Sprinkler sprinkler) {
                sprinkler.setIsActive(true);
                sprinkler.setWaterFlow(75);
            }
        });
    }


    public List<String> getDeviceStatuses() {
        devices.forEach(device -> log.info("Device: {} - Status: {}", device.getName(), device.getStatus()));
        return devices.stream()
                .map(Device::getStatus)
                .collect(Collectors.toList());
    }
}