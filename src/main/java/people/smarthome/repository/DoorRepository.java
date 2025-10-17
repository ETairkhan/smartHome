package people.smarthome.repository;

import people.smarthome.models.Door;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoorRepository extends JpaRepository<Door, Long> {
    List<Door> findByIsActive(boolean isActive);
}
