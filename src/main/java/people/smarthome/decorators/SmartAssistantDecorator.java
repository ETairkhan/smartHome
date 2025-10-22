package people.smarthome.decorators;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

@Component
public class SmartAssistantDecorator extends DeviceDecorator {

    public SmartAssistantDecorator(Device decoratedDevice) {
        super(decoratedDevice);
    }

    @Override
    public void performEnhancedAction() {
        System.out.println("🤖 Assistant: " + getName() + " status updated");
    }

    @Override
    protected String getEnhancementStatus() {
        return " + 🤖 Assistant";
    }
}