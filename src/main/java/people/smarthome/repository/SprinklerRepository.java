package people.smarthome.repository;

import people.smarthome.models.Sprinkler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprinklerRepository extends JpaRepository<Sprinkler, Long> {
    List<Sprinkler> findByIsActive(boolean isActive);
}
