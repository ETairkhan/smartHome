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
            // Check if the device is decorated, if yes, delegate the save operation to the decorated device
            Device baseDevice = getBaseDevice(device);  // Get the actual base device

            if (baseDevice instanceof Light) {
                lightRepository.save((Light) baseDevice);
                lightRepository.flush();  // Ensure the changes are immediately flushed to the database
                log.info("Saved Light device '{}' with power level {}", baseDevice.getName(), baseDevice.getPowerLevel());
            } else if (baseDevice instanceof Thermostat) {
                thermostatRepository.save((Thermostat) baseDevice);
                thermostatRepository.flush();  // Ensure the changes are immediately flushed to the database
                log.info("Saved Thermostat device '{}' with temperature {}", baseDevice.getName(), ((Thermostat) baseDevice).getTemperature());
            } else if (baseDevice instanceof Sprinkler) {
                sprinklerRepository.save((Sprinkler) baseDevice);
                sprinklerRepository.flush();  // Ensure the changes are immediately flushed to the database
                log.info("Saved Sprinkler device '{}' with water flow {}", baseDevice.getName(), ((Sprinkler) baseDevice).getWaterFlow());
            } else if (baseDevice instanceof Door) {
                doorRepository.save((Door) baseDevice);
                doorRepository.flush();  // Ensure the changes are immediately flushed to the database
                log.info("Saved Door device '{}' with power level {}", baseDevice.getName(), baseDevice.getPowerLevel());
            } else if (baseDevice instanceof Window) {
                windowRepository.save((Window) baseDevice);
                windowRepository.flush();  // Ensure the changes are immediately flushed to the database
                log.info("Saved Window device '{}' with state {}", baseDevice.getName(), baseDevice.getIsActive());
            }
        } catch (Exception e) {
            log.warn("Failed to save device state for device '{}': {}", device.getName(), e.getMessage());
        }
    }




    public String getDeviceIdByName(String deviceName) {
        log.info("Searching for device with name: {}", deviceName);  // Log the device name being searched
        // Iterate through all devices and compare names (case-insensitive)
        for (Map.Entry<String, Device> entry : devices.entrySet()) {
            log.debug("Checking device: {} with name: {}", entry.getKey(), entry.getValue().getName()); // Log each device checked
            if (entry.getValue().getName().equalsIgnoreCase(deviceName.trim())) {
                log.info("Device found: {} with ID: {}", deviceName, entry.getKey()); // Log successful match
                return entry.getKey(); // Return the device ID if names match
            }
        }
        log.warn("Device with name '{}' not found", deviceName);  // Log if the device was not found
        throw new IllegalArgumentException("Device with name '" + deviceName + "' not found.");
    }

    public void setPowerLevel(String deviceId, int powerLevel) {
        log.info("Setting power level for device {} to {}", deviceId, powerLevel);
        Device device = getDevice(deviceId);
        device.setPowerLevel(powerLevel);
        saveDeviceState(device);
        log.info("Power level for device {} set to {}", deviceId, powerLevel);
    }


    @Transactional
    public void setBrightness(String deviceId, int brightness) {
        log.info("Setting brightness for device {} to {}", deviceId, brightness);

        Device device = getDevice(deviceId);  // Get the decorated device
        Device baseDevice = getBaseDevice(device);  // Get the base device (unwrapped from the decorator)

        if (baseDevice instanceof Light) {
            ((Light) baseDevice).setBrightness(brightness);  // Set brightness for the base Light device
            saveDeviceState(baseDevice);  // Save the device state to the database
            log.info("Brightness for device {} set to {}", deviceId, brightness);
        } else {
            throw new IllegalArgumentException("Device is not a light.");
        }
    }



    @Transactional
    public void setWaterFlow(String deviceId, int waterFlow) {
        log.info("Setting water flow for device {} to {}", deviceId, waterFlow);

        Device device = getDevice(deviceId);  // Get the decorated device
        Device baseDevice = getBaseDevice(device);  // Get the base device (unwrapped from the decorator)

        if (baseDevice instanceof Sprinkler) {
            ((Sprinkler) baseDevice).setWaterFlow(waterFlow);  // Set water flow for the base Sprinkler device
            saveDeviceState(baseDevice);  // Save the device state to the database
            log.info("Water flow for device {} set to {}", deviceId, waterFlow);
        } else {
            throw new IllegalArgumentException("Device is not a sprinkler.");
        }
    }

    @Transactional
    public void setTemperature(String deviceId, int temperature) {
        log.info("Setting temperature for device {} to {}", deviceId, temperature);

        // Get the decorated device first
        Device device = getDevice(deviceId);

        // Unwrap the decorator and get the base device
        Device baseDevice = getBaseDevice(device);

        // Check if the base device is a Thermostat
        if (baseDevice instanceof Thermostat) {
            ((Thermostat) baseDevice).setTemperature(temperature);  // Set temperature for the base Thermostat device
            saveDeviceState(baseDevice);  // Save the device state to the database
            log.info("Temperature for device {} set to {}", deviceId, temperature);
        } else {
            throw new IllegalArgumentException("Device is not a thermostat.");
        }
    }



    // Helper method to get device by ID
    private Device getDevice(String deviceId) {
        Device device = devices.get(deviceId);
        if (device == null) {
            throw new IllegalArgumentException("Device not found: " + deviceId);
        }
        return device;
    }


}
