package org.springboot;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<UserScore, Long> {
    // Yeh method database se high scores ko descending order mein laayega
    List<UserScore> findAllByOrderByScoreDesc();
}