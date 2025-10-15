package people.smarthome.decorators;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

@Component
public class EcoFriendlyDecorator extends DeviceDecorator {

    public EcoFriendlyDecorator(Device decoratedDevice) {
        super(decoratedDevice);
    }

    @Override
    protected void performEnhancedAction() {
        applyEcoSettings();
    }

    @Override
    protected String getEnhancementStatus() {
        return " + 🌿 Eco";
    }

    private void applyEcoSettings() {
        switch (getDeviceType()) {
            case "LIGHT" -> optimizeLightEnergy();
            case "THERMOSTAT" -> optimizeTemperatureEnergy();
            case "SPRINKLER" -> optimizeWaterUsage();
        }
    }

    private void optimizeLightEnergy() {
        if (getBrightness() != null && getBrightness() > 60) {
            setBrightness(60);
        }
    }

    private void optimizeTemperatureEnergy() {
        if (getTemperature() != null && getTemperature() > 22) {
            setTemperature(22);
        }
    }

    private void optimizeWaterUsage() {
        if (getWaterFlow() != null && getWaterFlow() > 50) {
            setWaterFlow(50);
        }
    }
}