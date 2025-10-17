package people.smarthome.service;


import jakarta.annotation.PostConstruct;
import people.smarthome.factory.DeviceFactory;
import people.smarthome.facade.HomeAutomationFacade;
import people.smarthome.decorators.*;
import people.smarthome.repository.*;
import people.smarthome.models.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmartHomeService {
    private final HomeAutomationFacade homeAutomationFacade;
    private final DoorRepository doorRepository;
    private final LightRepository lightRepository;
    private final SprinklerRepository sprinklerRepository;
    private final ThermostatRepository thermostatRepository;
    private final WindowRepository windowRepository;
    private final DeviceFactory deviceFactory;

    private final Map<String, Device> devices = new HashMap<>();

    @PostConstruct
    @Transactional
    public void initialize() {
        if (doorRepository.count() == 0 && lightRepository.count() == 0 &&
                sprinklerRepository.count() == 0 && thermostatRepository.count() == 0 && windowRepository.count() == 0) {
            createDefaultDevices();
        }
        log.info("Initializing Smart Home Service...");
        loadDevicesFromDatabase();


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

        log.info("Creating {} default devices", defaultDevices.size());

        defaultDevices.forEach(device -> {
            if (device instanceof Light) {
                lightRepository.save((Light) device);
                log.info("Saved default device: {}", device.getName());
            } else if (device instanceof Thermostat) {
                thermostatRepository.save((Thermostat) device);
                log.info("Saved default device: {}", device.getName());
            } else if (device instanceof Sprinkler) {
                sprinklerRepository.save((Sprinkler) device);
                log.info("Saved default device: {}", device.getName());
            } else if (device instanceof Door) {
                doorRepository.save((Door) device);
                log.info("Saved default device: {}", device.getName());
            } else if (device instanceof Window) {
                windowRepository.save((Window) device);
                log.info("Saved default device: {}", device.getName());
            }
        });

        log.info("Created {} default devices", defaultDevices.size());
    }


    private void loadDevicesFromDatabase() {
        // Fetch devices from specific repositories
        List<Door> doors = doorRepository.findAll();
        List<Light> lights = lightRepository.findAll();
        List<Sprinkler> sprinklers = sprinklerRepository.findAll();
        List<Thermostat> thermostats = thermostatRepository.findAll();
        List<Window> windows = windowRepository.findAll();

        doors.forEach(door -> {
            devices.put("doors_" + door.getId(), enhanceDevice(door));
            log.info("Loaded device: {} - {}", door.getName(), door.getStatus()); // Log each device
        });
        lights.forEach(light -> {
            devices.put("lights_" + light.getId(), enhanceDevice(light));
            log.info("Loaded device: {} - {}", light.getName(), light.getStatus()); // Log each device
        });
        sprinklers.forEach(sprinkler -> {
            devices.put("sprinklers_" + sprinkler.getId(), enhanceDevice(sprinkler));
            log.info("Loaded device: {} - {}", sprinkler.getName(), sprinkler.getStatus()); // Log each device
        });
        thermostats.forEach(thermostat -> {
            devices.put("thermostats_" + thermostat.getId(), enhanceDevice(thermostat));
            log.info("Loaded device: {} - {}", thermostat.getName(), thermostat.getStatus()); // Log each device
        });
        windows.forEach(window -> {
            devices.put("windows_" + window.getId(), enhanceDevice(window));
            log.info("Loaded device: {} - {}", window.getName(), window.getStatus()); // Log each device
        });

        homeAutomationFacade.addDevices(new ArrayList<>(devices.values()));
        log.info("Loaded {} devices from the database", devices.size());
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
            case "WINDOW" -> new SmartAssistantDecorator(device);
            default -> device;
        };
    }

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
        devices.forEach((id, device) -> {
            available.put(id, getBaseDevice(device).getName());
            log.info("Available Device: {} - {}", id, getBaseDevice(device).getName());  // Log each device
        });
        return available;
    }

    public String addNewDevice(String name, String deviceType) {
        Device newDevice = deviceFactory.createFromType(name, deviceType);

        if (newDevice instanceof Light) {
            newDevice = lightRepository.save((Light) newDevice);
        } else if (newDevice instanceof Thermostat) {
            newDevice = thermostatRepository.save((Thermostat) newDevice);
        } else if (newDevice instanceof Sprinkler) {
            newDevice = sprinklerRepository.save((Sprinkler) newDevice);
        } else if (newDevice instanceof Door) {
            newDevice = doorRepository.save((Door) newDevice);
        } else if (newDevice instanceof Window) {
            newDevice = windowRepository.save((Window) newDevice);
        }

        String deviceId = generateDeviceId(name);
        devices.put(deviceId, enhanceDevice(newDevice));
        homeAutomationFacade.addDevice(newDevice);

        log.info("Added new device: {} ({})", name, deviceType);
        return deviceId;
    }

    public void removeDevice(String deviceId) {
        Device device = getDevice(deviceId);
        Device baseDevice = getBaseDevice(device);
        System.out.println("Devices in the map: " + devices);
        if (baseDevice instanceof Light) {
            lightRepository.delete((Light) baseDevice);
        } else if (baseDevice instanceof Thermostat) {
            thermostatRepository.delete((Thermostat) baseDevice);
        } else if (baseDevice instanceof Sprinkler) {
            sprinklerRepository.delete((Sprinkler) baseDevice);
        } else if (baseDevice instanceof Door) {
            doorRepository.delete((Door) baseDevice);
        } else if (baseDevice instanceof Window) {
            windowRepository.delete((Window) baseDevice);
        }

        devices.remove(deviceId);
        updateFacade();
        log.info("Removed device: {}", deviceId);
    }

    public List<String> getSupportedDeviceTypes() {
        return List.of("LIGHT", "THERMOSTAT", "SPRINKLER", "DOOR", "WINDOW");
    }

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
            if (device instanceof Light) {
                lightRepository.save((Light) device);
            } else if (device instanceof Thermostat) {
                thermostatRepository.save((Thermostat) device);
            } else if (device instanceof Sprinkler) {
                sprinklerRepository.save((Sprinkler) device);
            } else if (device instanceof Door) {
                doorRepository.save((Door) device);
            } else if (device instanceof Window) {
                windowRepository.save((Window) device);
            }
        } catch (Exception e) {
            log.warn("Failed to save device state: {}", e.getMessage());
        }
    }
}
