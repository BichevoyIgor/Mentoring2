package ru.bichevoy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poster;
    private String title;
    private String year;
    private String description;
    private double rating;
    private boolean share;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private final Set<Actor> actors;

    @ManyToMany
    @JoinTable(name = "film_genre",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private final Set<Genre> genres;

    {
        this.actors = new HashSet<>();
        this.genres = new HashSet<>();
        this.share = false;
    }

    public Film(String poster, String title, String year, String description, double rating, Set<Actor> actors, Set<Genre> genres, User user) {
        this.poster = poster;
        this.title = title;
        this.year = year;
        this.description = description;
        this.rating = rating;
        this.actors.addAll(actors);
        this.genres.addAll(genres);
        this.user = user;
    }
}
