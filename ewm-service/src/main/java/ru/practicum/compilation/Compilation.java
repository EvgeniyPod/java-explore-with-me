package ru.practicum.compilation;

import lombok.*;
import ru.practicum.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "pinned", nullable = false, columnDefinition = "boolean default false")
    private boolean pinned;

    @NotNull
    private String title;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(name = "compilations_events",
            joinColumns = { @JoinColumn(name = "compilation_id") },
            inverseJoinColumns = { @JoinColumn(name = "event_id") })
    @ToString.Exclude
    private Set<Event> events = new HashSet<>();

    /** Метод для добавления события к компиляции */
    public void addEvent(Event event) {
        this.events.add(event);
        event.getCompilations().add(this);
    }

    /** Метод для удаления события из компиляции */
    public void removeEvent(Event event) {
        this.events.remove(event);
        event.getCompilations().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compilation)) return false;
        return id == (((Compilation) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}