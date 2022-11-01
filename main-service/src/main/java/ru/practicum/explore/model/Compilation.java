package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private long id;
    private String title;
    private boolean pinned;

    @ManyToMany(mappedBy = "compilations")
    private List<Event> events;
}
