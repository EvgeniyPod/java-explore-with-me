package ru.practicum.place;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "places", schema = "public")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

    @NotNull
    private Float radius;

    @NotBlank
    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (getId() != place.getId()) return false;
        if (getLat() != null ? !getLat().equals(place.getLat()) : place.getLat() != null) return false;
        if (getLon() != null ? !getLon().equals(place.getLon()) : place.getLon() != null) return false;
        if (getRadius() != null ? !getRadius().equals(place.getRadius()) : place.getRadius() != null) return false;
        return getName() != null ? getName().equals(place.getName()) : place.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getLat() != null ? getLat().hashCode() : 0);
        result = 31 * result + (getLon() != null ? getLon().hashCode() : 0);
        result = 31 * result + (getRadius() != null ? getRadius().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
