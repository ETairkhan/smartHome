package people.smarthome.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "power_level")
    private Integer powerLevel;

    // Abstract methods to be implemented by subclasses
    public abstract String getDeviceType();
    public abstract void setDeviceType(String deviceType);

    public void operate() {
        toggle();
        System.out.println(name + " operated - Status: " + (isActive ? "ACTIVE" : "INACTIVE"));
    }

    public void toggle() {
        if (this.isActive == null) {
            this.isActive = true;
        } else {
            this.isActive = !this.isActive;
        }
    }

    public String getStatus() {
        return (isActive != null && isActive) ? "Active" : "Inactive";
    }

}
