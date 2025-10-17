package people.smarthome.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import people.smarthome.facade.HomeAutomationFacade;
import people.smarthome.service.SmartHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

@ShellComponent
@RequiredArgsConstructor
public class SmartHomeCommands {
    private static final Logger log = LoggerFactory.getLogger(SmartHomeCommands.class);
    private final SmartHomeService smartHomeService;

    @ShellMethod(key = "scene", value = "Activate a scene: night, party, away, garden")
    public String activateScene(@ShellOption String scene) {
        smartHomeService.activateScene(scene);
        return "🎭 " + scene + " scene activated!";
    }

    @ShellMethod(key = "devices", value = "Show all devices status")
    public String listDevices() {
        var statuses = smartHomeService.getDeviceStatuses();
        if (statuses.isEmpty()) {
            log.info("No devices found or devices are inactive.");
        } else {
            statuses.forEach(status -> log.info("Device status: {}", status));
        }
        var output = new StringBuilder("🏠 Smart Home Devices:\n");
        statuses.forEach(status -> output.append("• ").append(status).append("\n"));
        return output.toString();
    }



    @ShellMethod(key = "toggle", value = "Toggle device on/off")
    public String toggleDevice(@ShellOption String device) {
        smartHomeService.operateDevice(device);
        return "⚡ Toggled " + device;
    }

    @ShellMethod(key = "enhance", value = "Add enhancement: assistant, eco, cloud, security")
    public String enhanceDevice(
            @ShellOption String device,
            @ShellOption String enhancement) {
        smartHomeService.enhanceDevice(device, enhancement);
        return "✨ Added " + enhancement + " to " + device;
    }

    @ShellMethod(key = "add-device", value = "Add a new device")
    public String addDevice(
            @ShellOption String name,
            @ShellOption String type) {
        String deviceId = smartHomeService.addNewDevice(name, type);
        return "➕ Added new device: " + name + " (" + type + ") with ID: " + deviceId;
    }

    @ShellMethod(key = "remove-device", value = "Remove a device")
    public String removeDevice(@ShellOption String device) {
        smartHomeService.removeDevice(device);
        return "➖ Removed device: " + device;
    }

    @ShellMethod(key = "device-types", value = "Show supported device types")
    public String showDeviceTypes() {
        var types = smartHomeService.getSupportedDeviceTypes();
        var output = new StringBuilder("📋 Supported Device Types:\n");
        types.forEach(type -> output.append("• ").append(type).append("\n"));
        return output.toString();
    }

    @ShellMethod(key = "set-power-level", value = "Set the power level for a device")
    public String setPowerLevel(
            @ShellOption String deviceId,
            @ShellOption int powerLevel) {
        smartHomeService.setPowerLevel(deviceId, powerLevel);
        return "Set power level of device " + deviceId + " to " + powerLevel;
    }

    @ShellMethod(key = "set-brightness", value = "Set brightness for a light")
    public String setBrightness(
            @ShellOption String deviceId,
            @ShellOption int brightness) {
        smartHomeService.setBrightness(deviceId, brightness);
        return "Set brightness of light " + deviceId + " to " + brightness;
    }

    @ShellMethod(key = "set-water-flow", value = "Set water flow for a sprinkler")
    public String setWaterFlow(
            @ShellOption String deviceId,
            @ShellOption int waterFlow) {
        smartHomeService.setWaterFlow(deviceId, waterFlow);
        return "Set water flow of sprinkler " + deviceId + " to " + waterFlow;
    }

    @ShellMethod(key = "set-temperature", value = "Set temperature for a thermostat")
    public String setTemperature(
            @ShellOption String deviceId,
            @ShellOption int temperature) {
        smartHomeService.setTemperature(deviceId, temperature);
        return "Set temperature of thermostat " + deviceId + " to " + temperature;
    }

    @ShellMethod(key = "help", value = "Show available commands and devices")
    public String showHelp() {
        var devices = smartHomeService.getAvailableDevices();
        var help = new StringBuilder("""
            🏠 Smart Home System
            ===================

            Available Commands:
            -------------------
            • scene <night|party|away|garden>  - Activate a predefined scene
              Example: scene night
            • devices                           - Show the status of all devices
              Example: devices
            • toggle <device>                   - Toggle the device on or off
              Example: toggle door_1
            • enhance <device> <enhancement>    - Add an enhancement to a device
              Example: enhance door_1 security
            • add-device <name> <type>          - Add a new device to the system
              Example: add-device LivingRoomLight LIGHT
            • remove-device <device>            - Remove a device from the system
              Example: remove-device door_1
            • device-types                      - Show the supported device types
            • set-power-level <deviceId> <powerLevel>  - Set the power level for a device
              Example: set-power-level door_1 50
            • set-brightness <deviceId> <brightness>   - Set brightness for a light device
              Example: set-brightness light_1 75
            • set-water-flow <deviceId> <waterFlow>   - Set water flow for a sprinkler device
              Example: set-water-flow sprinkler_1 60
            • set-temperature <deviceId> <temperature> - Set temperature for a thermostat
              Example: set-temperature thermostat_1 22
            • help                              - Show this help message

            Available Devices:
            ------------------
            """);

        // List all available devices and their IDs
        devices.forEach((id, name) ->
                help.append("• ").append(id).append(" - ").append(name).append("\n"));

        help.append("""
            Device Types:
            -------------
            • LIGHT     - 💡 Smart lighting
            • THERMOSTAT- 🌡️ Temperature control
            • SPRINKLER - 💧 Garden irrigation
            • DOOR      - 🚪 Access control
            • WINDOW    - 🪟 Window management

            Enhancements:
            -------------
            • assistant - 🤖 Smart assistant integration
            • eco       - 🌿 Energy and resource optimization
            • cloud     - ☁️ Remote access and analytics
            • security  - 🔒 Advanced security features
            """);

        return help.toString();
    }
}