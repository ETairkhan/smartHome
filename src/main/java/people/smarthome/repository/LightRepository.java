package people.smarthome.repository;

import people.smarthome.models.Light;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LightRepository extends JpaRepository<Light, Long> {
    List<Light> findByIsActive(boolean isActive);
}
