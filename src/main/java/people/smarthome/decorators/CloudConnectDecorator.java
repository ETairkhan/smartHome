package people.smarthome.decorators;

import people.smarthome.models.Device;
import org.springframework.stereotype.Component;

@Component
public class CloudConnectDecorator extends DeviceDecorator {

    public CloudConnectDecorator(Device decoratedDevice) {
        super(decoratedDevice);
    }

    @Override
    protected void performEnhancedAction() {
        syncWithCloud();
    }

    @Override
    protected String getEnhancementStatus() {
        return " + ☁️ Cloud";
    }

    private void syncWithCloud() {
        System.out.println("☁️ Syncing " + getName() + " with cloud services");
    }
}