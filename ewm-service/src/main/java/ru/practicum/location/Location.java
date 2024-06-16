package ru.practicum.location;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "locations", schema = "public")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private float lat;
    private float lon;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return getId() == location.getId() && Float.compare(location.getLat(), getLat()) == 0 && Float.compare(location.getLon(), getLon()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLat(), getLon());
    }
}
