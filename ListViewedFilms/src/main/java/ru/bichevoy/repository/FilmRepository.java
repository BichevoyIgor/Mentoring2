package ru.bichevoy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("""
            SELECT f FROM Film f
            JOIN User u ON f.user.id = u.id
            WHERE u.name = :userName
            ORDER BY f.id DESC
            """)
    Page<Film> findAllByUserName(Pageable page, @Param("userName") String userName);

    @Query("""
            SELECT DISTINCT f FROM Film f
            JOIN f.user u
            JOIN f.genres g
            WHERE u.name = :userName
            AND g.title = :genre
            ORDER BY f.id DESC
            """)
    Page<Film> findAllByUserNameAndGenre(Pageable pageable, String userName, String genre);

    @Query("""
            SELECT DISTINCT f FROM Film f
            JOIN f.user u
            WHERE u.name = :userName
            AND f.share = true
            ORDER BY f.id DESC
            """)
    Page<Film> findAllSharedByUserName(Pageable pageable, String userName);
}
