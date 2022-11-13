package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private long id;
    private String title;
    private String annotation;
    private String description;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "user_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;
    private boolean paid;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "confirmedRequests")
    private long confirmedRequests;

    @Column(name = "participant_limit")
    private long participantLimit;
    private long views;

    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    private Location location;

    @ManyToMany
    @JoinTable(name = "compilation_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id"))
    private List<Compilation> compilations;
}
