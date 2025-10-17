package people.smarthome.config;

import people.smarthome.models.Device;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import people.smarthome.models.Light;

@Configuration
public class DecoratorConfig {

    @Bean
    @Primary
    public Device baseDevice() {
        // Instantiate a concrete subclass (e.g., Light)
        Device device = new Light();  // Change this to any concrete subclass
        device.setName("Base Device");
        device.setDeviceType("BASE");
        return device;
    }
}