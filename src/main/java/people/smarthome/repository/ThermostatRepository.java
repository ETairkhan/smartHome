package people.smarthome.repository;

import people.smarthome.models.Thermostat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThermostatRepository extends JpaRepository<Thermostat, Long> {
    List<Thermostat> findByIsActive(boolean isActive);
}
