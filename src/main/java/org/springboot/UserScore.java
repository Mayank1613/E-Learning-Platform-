package org.springboot;

import jakarta.persistence.*;

@Entity
@Table(name = "user_score")
public class UserScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String subject;
    private int score;
    private int total;

    public UserScore() {
    }

    public UserScore(String name, String subject, int score, int total) {
        this.name = name;
        this.subject = subject;
        this.score = score;
        this.total = total;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
}