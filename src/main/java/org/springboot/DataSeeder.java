package org.springboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(QuestionRepository repository) {
        return args -> {
            if (repository.count() < 10) {
                repository.deleteAll();

                // Java Questions
                repository.save(new Question("Java", "Easy", "What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "16 bytes", "4 bytes"));
                repository.save(new Question("Java", "Easy", "Which keyword is used to create a class in Java?", "class", "Class", "struct", "define", "class"));
                repository.save(new Question("Java", "Medium", "Which of the following is not an OOPS concept in Java?", "Inheritance", "Compilation", "Encapsulation", "Polymorphism", "Compilation"));
                repository.save(new Question("Java", "Medium", "Which interface is the root of the Java Collection hierarchy?", "Collection", "List", "Map", "Set", "Collection"));
                repository.save(new Question("Java", "Hard", "What is the default value of local variables in Java?", "0", "null", "Not assigned/Undefined", "Depends on data type", "Not assigned/Undefined"));
                repository.save(new Question("Java", "Hard", "Which memory space in JVM stores objects?", "Stack", "Heap", "Method Area", "PC Register", "Heap"));
                repository.save(new Question("Java", "Easy", "Which company originally developed Java?", "Microsoft", "Sun Microsystems", "Oracle", "Google", "Sun Microsystems"));
                repository.save(new Question("Java", "Medium", "What is string pool in Java?", "Heap Area for String literals", "Stack memory", "File System", "Database", "Heap Area for String literals"));
                repository.save(new Question("Java", "Hard", "Which feature allows multiple threads to run concurrently in Java?", "Multithreading", "Polymorphism", "Serialization", "Reflection", "Multithreading"));
                repository.save(new Question("Java", "Medium", "Which keyword is used to inherit a class in Java?", "implements", "extends", "inherits", "super", "extends"));

                // Python Questions
                repository.save(new Question("Python", "Easy", "Which symbol is used for comments in Python?", "//", "/*", "#", "<!--", "#"));
                repository.save(new Question("Python", "Easy", "What is the extension of Python files?", ".py", ".pyt", ".python", ".p", ".py"));
                repository.save(new Question("Python", "Medium", "What is the output of 2 ** 3 in Python?", "6", "8", "9", "12", "8"));
                repository.save(new Question("Python", "Medium", "Which collection type is ordered and immutable?", "List", "Set", "Tuple", "Dictionary", "Tuple"));
                repository.save(new Question("Python", "Hard", "Which keyword is used to create a generator in Python?", "yield", "return", "generate", "create", "yield"));
                repository.save(new Question("Python", "Hard", "What is GIL in Python?", "Global Interpreter Lock", "Global Import Logic", "General Interface Layer", "None", "Global Interpreter Lock"));
                repository.save(new Question("Python", "Easy", "Which function returns length of a list?", "length()", "len()", "count()", "size()", "len()"));
                repository.save(new Question("Python", "Medium", "How do you start a function definition in Python?", "function myFunc()", "def myFunc():", "func myFunc():", "create myFunc()", "def myFunc():"));
                repository.save(new Question("Python", "Medium", "Which of these is a mutable data type?", "Tuple", "String", "List", "Int", "List"));
                repository.save(new Question("Python", "Hard", "Which module is used for regular expressions in Python?", "regex", "pyregex", "re", "string", "re"));

                // DBMS Questions
                repository.save(new Question("DBMS", "Easy", "SQL stands for?", "Structured Query Language", "Simple Query Language", "Sequential Query Language", "None", "Structured Query Language"));
                repository.save(new Question("DBMS", "Easy", "Which SQL command retrieves data?", "SELECT", "FETCH", "GET", "OPEN", "SELECT"));
                repository.save(new Question("DBMS", "Medium", "Which normal form removes partial dependency?", "1NF", "2NF", "3NF", "BCNF", "2NF"));
                repository.save(new Question("DBMS", "Medium", "Which key uniquely identifies each row in a database table?", "Foreign Key", "Primary Key", "Super Key", "Candidate Key", "Primary Key"));
                repository.save(new Question("DBMS", "Hard", "What does ACID stand for in DBMS?", "Atomicity, Consistency, Isolation, Durability", "Access, Control, Integrated, Data", "Auto, Check, Index, Data", "None", "Atomicity, Consistency, Isolation, Durability"));
                repository.save(new Question("DBMS", "Hard", "Which join returns all records when there is a match in either left or right table?", "INNER JOIN", "FULL OUTER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"));
                repository.save(new Question("DBMS", "Easy", "Which command is used to delete all rows from a table quickly?", "DELETE", "TRUNCATE", "DROP", "REMOVE", "TRUNCATE"));
                repository.save(new Question("DBMS", "Medium", "Which constraint ensures all values in a column are different?", "UNIQUE", "NOT NULL", "CHECK", "DEFAULT", "UNIQUE"));
                repository.save(new Question("DBMS", "Hard", "What is a dead lock in DBMS?", "Two transactions waiting for each other", "System crash", "Network issue", "Data loss", "Two transactions waiting for each other"));
                repository.save(new Question("DBMS", "Medium", "Which SQL clause is used to filter group results?", "WHERE", "HAVING", "GROUP BY", "ORDER BY", "HAVING"));
            }
        };
    }
}