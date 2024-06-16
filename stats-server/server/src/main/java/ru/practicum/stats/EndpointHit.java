package ru.practicum.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    private Integer id;
    private	String app;
    private String uri;
    private String ip;
    private Timestamp timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndpointHit)) return false;
        return id != null && id.equals(((EndpointHit) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}