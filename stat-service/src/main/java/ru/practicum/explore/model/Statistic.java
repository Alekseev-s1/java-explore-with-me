package ru.practicum.explore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "endpoint_hits")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Transient
    private long hits;
}