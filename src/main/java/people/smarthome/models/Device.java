package people.smarthome.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "devices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @Column(name = "brightness")
    private Integer brightness;

    @Column(name = "temperature")
    private Integer temperature;

    @Column(name = "water_flow")
    private Integer waterFlow;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "zone")
    private String zone;

    @Column(name = "power_level")
    private Integer powerLevel;

    public void toggle() {
        if (this.isActive == null) {
            this.isActive = true;
        } else {
            this.isActive = !this.isActive;
        }
    }

    public void operate() {
        toggle();
        System.out.println(name + " operated - Status: " + (isActive ? "ACTIVE" : "INACTIVE"));
    }

    public String getStatus() {
        return String.format("%s (%s): %s", name, deviceType,
                Boolean.TRUE.equals(isActive) ? "ACTIVE" : "INACTIVE");
    }

    public boolean isTurnedOn() {
        return Boolean.TRUE.equals(isActive);
    }

    public void setTurnedOn(boolean turnedOn) {
        this.isActive = turnedOn;
    }

}