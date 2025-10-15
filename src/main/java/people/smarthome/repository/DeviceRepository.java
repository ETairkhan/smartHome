package people.smarthome.repository;

import people.smarthome.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByDeviceType(String deviceType);
    List<Device> findByIsActive(boolean isActive);
}