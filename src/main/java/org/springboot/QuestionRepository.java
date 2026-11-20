package org.springboot;

import org.springboot.Question; // Agar Question class bhi org.springboot mein hai
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySubjectAndDifficulty(String subject, String difficulty);
}