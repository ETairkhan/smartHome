package people.smarthome.config;

import people.smarthome.models.Device;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DecoratorConfig {

    @Bean
    @Primary
    public Device baseDevice() {
        Device device = new Device();
        device.setName("Base Device");
        device.setDeviceType("BASE");
        return device;
    }
}