package people.smarthome.service;

import people.smarthome.models.Device;
import people.smarthome.repository.DeviceRepository;
import people.smarthome.factory.DeviceFactory;
import people.smarthome.facade.HomeAutomationFacade;
import people.smarthome.decorators.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmartHomeService {
    private final HomeAutomationFacade homeAutomationFacade;
    private final DeviceRepository deviceRepository;
    private final DeviceFactory deviceFactory;

    private final Map<String, Device> devices = new HashMap<>();

    @PostConstruct
    @Transactional
    public void initialize() {
        log.info("Initializing smart home system...");

        if (deviceRepository.count() == 0) {
            createDefaultDevices();
        }

        loadDevicesFromDatabase();
        log.info("System initialized with {} devices", devices.size());
    }

    private void createDefaultDevices() {
        List<Device> defaultDevices = List.of(
                deviceFactory.createLight("Living Room Light"),
                deviceFactory.createThermostat("Main Thermostat"),
                deviceFactory.createSprinkler("Garden Sprinkler"),
                deviceFactory.createDoor("Front Door"),
                deviceFactory.createDoor("Garage Door"),
                deviceFactory.createWindow("Kitchen Window")
        );

        deviceRepository.saveAll(defaultDevices);
        log.info("Created {} default devices", defaultDevices.size());
    }

    private void loadDevicesFromDatabase() {
        deviceRepository.findAll().forEach(device -> {
            String deviceId = generateDeviceId(device.getName());
            Device enhancedDevice = enhanceDevice(device);
            devices.put(deviceId, enhancedDevice);
            homeAutomationFacade.addDevice(enhancedDevice);
        });
    }

    private String generateDeviceId(String name) {
        return name.toLowerCase().replace(" ", "_");
    }

    private Device enhanceDevice(Device device) {
        return switch (device.getDeviceType()) {
            case "LIGHT" -> new SmartAssistantDecorator(new EcoFriendlyDecorator(device));
            case "THERMOSTAT" -> new CloudConnectDecorator(new SmartAssistantDecorator(device));
            case "DOOR" -> new SecurityBoostDecorator(new CloudConnectDecorator(device));
            case "SPRINKLER" -> new EcoFriendlyDecorator(device);
            default -> device;
        };
    }

    // Public API Methods
    public void activateScene(String scene) {
        homeAutomationFacade.activateScene(scene);
        saveDeviceStates();
    }

    public List<String> getDeviceStatuses() {
        return homeAutomationFacade.getDeviceStatuses();
    }

    public void operateDevice(String deviceId) {
        Device device = getDevice(deviceId);
        device.toggle();
        saveDeviceState(device);
    }

    public void enhanceDevice(String deviceId, String enhancement) {
        Device original = getBaseDevice(getDevice(deviceId));
        Device enhanced = applyEnhancement(original, enhancement);
        devices.put(deviceId, enhanced);
        updateFacade();
    }

    public Map<String, String> getAvailableDevices() {
        Map<String, String> available = new HashMap<>();
        devices.forEach((id, device) ->
                available.put(id, getBaseDevice(device).getName())
        );
        return available;
    }

    // Device Management Methods
    public String addNewDevice(String name, String deviceType) {
        Device newDevice = deviceFactory.createFromType(name, deviceType);
        Device savedDevice = deviceRepository.save(newDevice);

        String deviceId = generateDeviceId(name);
        Device enhancedDevice = enhanceDevice(savedDevice);
        devices.put(deviceId, enhancedDevice);
        homeAutomationFacade.addDevice(enhancedDevice);

        log.info("Added new device: {} ({})", name, deviceType);
        return deviceId;
    }

    public void removeDevice(String deviceId) {
        Device device = getDevice(deviceId);
        Device baseDevice = getBaseDevice(device);

        deviceRepository.delete(baseDevice);
        devices.remove(deviceId);
        updateFacade();

        log.info("Removed device: {}", deviceId);
    }

    public List<String> getSupportedDeviceTypes() {
        return List.of("LIGHT", "THERMOSTAT", "SPRINKLER", "DOOR", "WINDOW");
    }

    // Private helper methods
    private Device getDevice(String deviceId) {
        return devices.computeIfAbsent(deviceId,
                id -> { throw new IllegalArgumentException("Device not found: " + id); });
    }

    private Device getBaseDevice(Device device) {
        while (device instanceof DeviceDecorator) {
            device = ((DeviceDecorator) device).getDecoratedDevice();
        }
        return device;
    }

    private Device applyEnhancement(Device device, String enhancement) {
        return switch (enhancement.toLowerCase()) {
            case "assistant" -> new SmartAssistantDecorator(device);
            case "eco" -> new EcoFriendlyDecorator(device);
            case "cloud" -> new CloudConnectDecorator(device);
            case "security" -> new SecurityBoostDecorator(device);
            default -> throw new IllegalArgumentException("Unknown enhancement: " + enhancement);
        };
    }

    private void updateFacade() {
        HomeAutomationFacade newFacade = new HomeAutomationFacade();
        devices.values().forEach(newFacade::addDevice);
    }

    private void saveDeviceStates() {
        devices.values().forEach(this::saveDeviceState);
    }

    private void saveDeviceState(Device device) {
        try {
            deviceRepository.save(getBaseDevice(device));
        } catch (Exception e) {
            log.warn("Failed to save device state: {}", e.getMessage());
        }
    }
}