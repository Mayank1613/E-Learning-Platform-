package org.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(QuestionRepository questionRepository) {
        return args -> {
            if (questionRepository.count() == 0) {
                // Java Easy Questions
                questionRepository.save(new Question("Java", "Easy", "What is the extension of compiled Java classes?", ".txt", ".js", ".class", ".java", ".class"));
                questionRepository.save(new Question("Java", "Easy", "Which of the following is not an OOPS concept in Java?", "Inheritance", "Compilation", "Encapsulation", "Polymorphism", "Compilation"));
                questionRepository.save(new Question("Java", "Easy", "Which component is responsible for executing bytecode in Java?", "JDK", "JVM", "JRE", "JIT", "JVM"));

                // Python Easy Questions
                questionRepository.save(new Question("Python", "Easy", "Which keyword is used to define a function in Python?", "function", "def", "fun", "define", "def"));
                questionRepository.save(new Question("Python", "Easy", "What is the correct file extension for Python files?", ".pt", ".pyt", ".py", ".python", ".py"));

                // DBMS Easy Questions
                questionRepository.save(new Question("DBMS", "Easy", "Which SQL clause is used to filter records?", "GROUP BY", "ORDER BY", "WHERE", "HAVING", "WHERE"));
            }
        };
    }
}