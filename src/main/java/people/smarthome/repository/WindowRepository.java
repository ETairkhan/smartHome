package people.smarthome.repository;

import people.smarthome.models.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends JpaRepository<Window, Long> {
    List<Window> findByIsActive(boolean isActive);
}
