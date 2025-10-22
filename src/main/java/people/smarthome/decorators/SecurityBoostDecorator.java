package people.smarthome.decorators;

import people.smarthome.models.Device;

public class SecurityBoostDecorator extends DeviceDecorator {

    public SecurityBoostDecorator(Device decoratedDevice) {
        super(decoratedDevice);

    }

    @Override
    public void performEnhancedAction() {
        enhanceSecurity();
    }

    @Override
    protected String getEnhancementStatus() {
        return " + 🔒 Security";
    }

    private void enhanceSecurity() {
        if ("DOOR".equals(getDeviceType())) {
            autoLock();
        } else if ("WINDOW".equals(getDeviceType())) {
            autoLock();
        }
    }

    private void autoLock() {
        if (!getIsLocked()) {
            setIsLocked(true);
        }
    }
}
