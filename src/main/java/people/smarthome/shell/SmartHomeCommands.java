package people.smarthome.shell;

import people.smarthome.service.SmartHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Map;

@ShellComponent
@RequiredArgsConstructor
public class SmartHomeCommands {
    private final SmartHomeService smartHomeService;

    @ShellMethod(key = "scene", value = "Activate a scene: night, party, away, garden")
    public String activateScene(@ShellOption String scene) {
        smartHomeService.activateScene(scene);
        return "🎭 " + scene + " scene activated!";
    }

    @ShellMethod(key = "devices", value = "Show all devices status")
    public String listDevices() {
        var statuses = smartHomeService.getDeviceStatuses();
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

    @ShellMethod(key = "help", value = "Show available commands and devices")
    public String showHelp() {
        var devices = smartHomeService.getAvailableDevices();
        var help = new StringBuilder("""
            🏠 Smart Home System
            ===================

            Available Commands:
            • scene <night|party|away|garden> - Activate scene
            • devices                          - Show device status
            • toggle <device>                  - Toggle device
            • enhance <device> <enhancement>   - Add enhancement
            • add-device <name> <type>         - Add new device
            • remove-device <device>           - Remove device
            • device-types                     - Show supported types
            • help                             - This message

            Available Devices:
            """);

        devices.forEach((id, name) ->
                help.append("• ").append(id).append(" - ").append(name).append("\n"));

        help.append("""
            
            Device Types:
            • LIGHT     - 💡 Smart lighting
            • THERMOSTAT- 🌡️ Temperature control
            • SPRINKLER - 💧 Garden irrigation
            • DOOR      - 🚪 Access control
            • WINDOW    - 🪟 Window management

            Enhancements:
            • assistant - 🤖 Smart assistant integration
            • eco       - 🌿 Energy and resource optimization  
            • cloud     - ☁️ Remote access and analytics
            • security  - 🔒 Advanced security features
            """);

        return help.toString();
    }
}