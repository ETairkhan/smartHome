package people.smarthome.facade;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HomeAutomationFacade {
    private final List<Device> devices;

    public HomeAutomationFacade() {
        this.devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
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
            device.setIsActive(false);
            if ("LIGHT".equals(device.getDeviceType()) && device.getBrightness() != null) {
                device.setBrightness(10);
            }
        });
    }

    private void activatePartyMode() {
        devices.forEach(device -> {
            if ("LIGHT".equals(device.getDeviceType())) {
                device.setIsActive(true);
                device.setBrightness(80);
            }
        });
    }

    private void activateAwayMode() {
        devices.forEach(device -> {
            device.setIsActive(false);
            if ("DOOR".equals(device.getDeviceType())) {
                device.setIsLocked(true);
            }
        });
    }

    private void activateGardenMode() {
        devices.forEach(device -> {
            if ("SPRINKLER".equals(device.getDeviceType())) {
                device.setIsActive(true);
                device.setWaterFlow(75);
            }
        });
    }

    public List<String> getDeviceStatuses() {
        return devices.stream()
                .map(Device::getStatus)
                .collect(Collectors.toList());
    }
}