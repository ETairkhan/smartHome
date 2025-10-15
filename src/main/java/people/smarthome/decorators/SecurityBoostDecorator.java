package people.smarthome.decorators;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

@Component
public class SecurityBoostDecorator extends DeviceDecorator {

    public SecurityBoostDecorator(Device decoratedDevice) {
        super(decoratedDevice);
    }

    @Override
    protected void performEnhancedAction() {
        enhanceSecurity();
    }

    @Override
    protected String getEnhancementStatus() {
        return " + 🔒 Security";
    }

    private void enhanceSecurity() {
        if ("DOOR".equals(getDeviceType())) {
            autoLock();
        }
    }

    private void autoLock() {
        if (!getIsLocked()) {
            setIsLocked(true);
        }
    }
}