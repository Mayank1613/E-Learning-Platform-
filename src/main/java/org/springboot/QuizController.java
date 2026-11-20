package org.springboot;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController implements WebMvcConfigurer {

    @Autowired(required = false)
    private QuestionRepository questionRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/");
    }

    public static class LeaderboardEntry {
        private String name;
        private int score;
        private int total;
        private String subject;

        public LeaderboardEntry(String name, int score, int total, String subject) {
            this.name = name;
            this.score = score;
            this.total = total;
            this.subject = subject;
        }

        public String getName() { return name; }
        public int getScore() { return score; }
        public int getTotal() { return total; }
        public String getSubject() { return subject; }
    }

    private static List<LeaderboardEntry> leaderboard = new ArrayList<>();

    private List<Question> getFallbackQuestions(String subject, String difficulty) {
        List<Question> questions = new ArrayList<>();
        
        if ("Python".equalsIgnoreCase(subject)) {
            questions.add(new Question("Python", difficulty, "Which symbol is used for comments in Python?", "//", "/*", "#", "<!--", "#"));
            questions.add(new Question("Python", difficulty, "What is the extension of Python files?", ".py", ".pyt", ".python", ".p", ".py"));
            questions.add(new Question("Python", difficulty, "What is the output of 2 ** 3 in Python?", "6", "8", "9", "12", "8"));
            questions.add(new Question("Python", difficulty, "Which collection type is ordered and immutable?", "List", "Set", "Tuple", "Dictionary", "Tuple"));
            questions.add(new Question("Python", difficulty, "Which keyword is used to create a generator in Python?", "yield", "return", "generate", "create", "yield"));
            questions.add(new Question("Python", difficulty, "What is GIL in Python?", "Global Interpreter Lock", "Global Import Logic", "General Interface Layer", "None", "Global Interpreter Lock"));
            questions.add(new Question("Python", difficulty, "Which function returns length of a list?", "length()", "len()", "count()", "size()", "len()"));
            questions.add(new Question("Python", difficulty, "How do you start a function definition in Python?", "function myFunc()", "def myFunc():", "func myFunc():", "create myFunc()", "def myFunc():"));
            questions.add(new Question("Python", difficulty, "Which of these is a mutable data type?", "Tuple", "String", "List", "Int", "List"));
            questions.add(new Question("Python", difficulty, "Which module is used for regular expressions in Python?", "regex", "pyregex", "re", "string", "re"));
            questions.add(new Question("Python", difficulty, "What is the maximum possible length of an identifier in Python?", "31 characters", "63 characters", "79 characters", "None of the above", "None of the above"));
            questions.add(new Question("Python", difficulty, "Which of the following is not a core data type in Python?", "List", "Dictionary", "Tuple", "Class", "Class"));
            questions.add(new Question("Python", difficulty, "What does pip stand for in Python?", "Pip Installs Python", "Preferred Installer Program", "Plugin Installation Package", "None", "Pip Installs Python"));
            questions.add(new Question("Python", difficulty, "Which function is used to open a file in Python?", "open()", "file()", "read()", "text()", "open()"));
            questions.add(new Question("Python", difficulty, "Which error occurs when a local variable is referenced before assignment?", "SyntaxError", "UnboundLocalError", "NameError", "TypeError", "UnboundLocalError"));
            questions.add(new Question("Python", difficulty, "What is the return type of id() function?", "Int", "Float", "String", "Bool", "Int"));
            questions.add(new Question("Python", difficulty, "Which keyword is used for exception handling in Python?", "try", "catch", "throw", "except", "except"));
            questions.add(new Question("Python", difficulty, "What is the output of type(1/1) in Python?", "<class 'int'>", "<class 'float'>", "<class 'double'>", "<class 'number'>", "<class 'float'>"));
            questions.add(new Question("Python", difficulty, "Which module in Python supports regular expressions?", "re", "regex", "py_regex", "match", "re"));
            questions.add(new Question("Python", difficulty, "What is the base class for all user-defined exceptions in Python?", "Error", "Exception", "BaseException", "SystemExit", "Exception"));
        } else if ("DBMS".equalsIgnoreCase(subject)) {
            questions.add(new Question("DBMS", difficulty, "SQL stands for?", "Structured Query Language", "Simple Query Language", "Sequential Query Language", "None", "Structured Query Language"));
            questions.add(new Question("DBMS", difficulty, "Which SQL command retrieves data?", "SELECT", "FETCH", "GET", "OPEN", "SELECT"));
            questions.add(new Question("DBMS", difficulty, "Which normal form removes partial dependency?", "1NF", "2NF", "3NF", "BCNF", "2NF"));
            questions.add(new Question("DBMS", difficulty, "Which key uniquely identifies each row in a database table?", "Foreign Key", "Primary Key", "Super Key", "Candidate Key", "Primary Key"));
            questions.add(new Question("DBMS", difficulty, "What does ACID stand for in DBMS?", "Atomicity, Consistency, Isolation, Durability", "Access, Control, Integrated, Data", "Auto, Check, Index, Data", "None", "Atomicity, Consistency, Isolation, Durability"));
            questions.add(new Question("DBMS", difficulty, "Which join returns all records when there is a match in either left or right table?", "INNER JOIN", "FULL OUTER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"));
            questions.add(new Question("DBMS", difficulty, "Which command is used to delete all rows from a table quickly?", "DELETE", "TRUNCATE", "DROP", "REMOVE", "TRUNCATE"));
            questions.add(new Question("DBMS", difficulty, "Which constraint ensures all values in a column are different?", "UNIQUE", "NOT NULL", "CHECK", "DEFAULT", "UNIQUE"));
            questions.add(new Question("DBMS", difficulty, "What is a dead lock in DBMS?", "Two transactions waiting for each other", "System crash", "Network issue", "Data loss", "Two transactions waiting for each other"));
            questions.add(new Question("DBMS", difficulty, "Which SQL clause is used to filter group results?", "WHERE", "HAVING", "GROUP BY", "ORDER BY", "HAVING"));
            questions.add(new Question("DBMS", difficulty, "Which language is used to define database schema?", "DML", "DDL", "DCL", "TCL", "DDL"));
            questions.add(new Question("DBMS", difficulty, "What is a relation in a relational database?", "A table", "A row", "A column", "A key", "A table"));
            questions.add(new Question("DBMS", difficulty, "Which level of abstraction describes how data is stored in the database?", "Physical Level", "Logical Level", "View Level", "External Level", "Physical Level"));
            questions.add(new Question("DBMS", difficulty, "What is the full form of BCNF?", "Boyce Codd Normal Form", "Basic Check Normal Form", "Binary Code Normal Form", "None", "Boyce Codd Normal Form"));
            questions.add(new Question("DBMS", difficulty, "Which statement is used to remove a table from database?", "DROP", "DELETE", "REMOVE", "CLEAR", "DROP"));
            questions.add(new Question("DBMS", difficulty, "What is an index in DBMS?", "A pointer to data", "A table backup", "A query optimizer", "A security key", "A pointer to data"));
            questions.add(new Question("DBMS", difficulty, "Which property ensures changes of a committed transaction are permanent?", "Atomicity", "Consistency", "Isolation", "Durability", "Durability"));
            questions.add(new Question("DBMS", difficulty, "What is a view in SQL?", "Virtual table based on result-set of SQL query", "Physical storage table", "Stored procedure", "Trigger", "Virtual table based on result-set of SQL query"));
            questions.add(new Question("DBMS", difficulty, "Which operator is used in SQL for pattern matching?", "LIKE", "IN", "BETWEEN", "EXISTS", "LIKE"));
            questions.add(new Question("DBMS", difficulty, "What is the maximum number of primary keys a table can have?", "1", "2", "4", "Unlimited", "1"));
        } else if ("C++".equalsIgnoreCase(subject)) {
            questions.add(new Question("C++", difficulty, "Which operator is used to allocate memory dynamically in C++?", "malloc", "alloc", "new", "create", "new"));
            questions.add(new Question("C++", difficulty, "Which concept allows the same function name to have different implementations?", "Encapsulation", "Polymorphism", "Abstraction", "Inheritance", "Polymorphism"));
            questions.add(new Question("C++", difficulty, "What is the default access specifier for members of a class in C++?", "public", "protected", "private", "internal", "private"));
            questions.add(new Question("C++", difficulty, "Which header file is required for cout and cin?", "<stdio.h>", "<iostream>", "<conio.h>", "<fstream>", "<iostream>"));
            questions.add(new Question("C++", difficulty, "What does STL stand for in C++?", "Standard Type Library", "Standard Template Library", "Simple Template Logic", "System Type Library", "Standard Template Library"));
            questions.add(new Question("C++", difficulty, "Which symbol is used for destructor in C++?", "!", "~", "#", "&", "~"));
            questions.add(new Question("C++", difficulty, "Which operator cannot be overloaded in C++?", "+", "==", "::", "[]", "::"));
            questions.add(new Question("C++", difficulty, "What is a pure virtual function?", "A function with no body assigned = 0", "A static function", "A friend function", "An inline function", "A function with no body assigned = 0"));
            questions.add(new Question("C++", difficulty, "Which keyword is used to handle exceptions in C++?", "try-catch", "error-handle", "throw-trap", "check-except", "try-catch"));
            questions.add(new Question("C++", difficulty, "Which pointer holds the address of the current object?", "super", "self", "this", "current", "this"));
            questions.add(new Question("C++", difficulty, "What is the size of bool in C++?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
            questions.add(new Question("C++", difficulty, "Which container stores elements in contiguous memory?", "list", "set", "vector", "map", "vector"));
            questions.add(new Question("C++", difficulty, "Which keyword prevents a class or virtual function from being overridden?", "static", "const", "final", "sealed", "final"));
            questions.add(new Question("C++", difficulty, "What is the extension of C++ source files?", ".c", ".cpp", ".cp", ".cxx", ".cpp"));
            questions.add(new Question("C++", difficulty, "Which function is used to release dynamically allocated memory?", "free()", "remove()", "delete", "clear()", "delete"));
            questions.add(new Question("C++", difficulty, "Who developed C++?", "Dennis Ritchie", "Bjarne Stroustrup", "James Gosling", "Guido van Rossum", "Bjarne Stroustrup"));
            questions.add(new Question("C++", difficulty, "Which type of inheritance leads to the Diamond Problem?", "Single", "Multilevel", "Multiple", "Hierarchical", "Multiple"));
            questions.add(new Question("C++", difficulty, "What is a friend function?", "Accesses private members of a class", "Inherits private members", "Constructor of class", "Destructor of class", "Accesses private members of a class"));
            questions.add(new Question("C++", difficulty, "Which stream class is used for both reading and writing files?", "ifstream", "ofstream", "fstream", "iostream", "fstream"));
            questions.add(new Question("C++", difficulty, "What does RAII stand for?", "Resource Acquisition Is Initialization", "Random Access In Memory", "Runtime Array Index", "None", "Resource Acquisition Is Initialization"));
        } else if ("C Lang".equalsIgnoreCase(subject)) {
            questions.add(new Question("C Lang", difficulty, "Who is the father of C language?", "James Gosling", "Dennis Ritchie", "Bjarne Stroustrup", "Ken Thompson", "Dennis Ritchie"));
            questions.add(new Question("C Lang", difficulty, "Which format specifier is used for printing an integer in C?", "%c", "%d", "%f", "%s", "%d"));
            questions.add(new Question("C Lang", difficulty, "Which function is used to allocate memory dynamically in C?", "new", "malloc()", "create()", "alloc()", "malloc()"));
            questions.add(new Question("C Lang", difficulty, "What is the size of char data type in C?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
            questions.add(new Question("C Lang", difficulty, "Which symbol is used to get the address of a variable?", "*", "&", "#", "@", "&"));
            questions.add(new Question("C Lang", difficulty, "Which loop is guaranteed to execute at least once?", "for", "while", "do-while", "None", "do-while"));
            questions.add(new Question("C Lang", difficulty, "What is the index of the first element in a C array?", "0", "1", "-1", "Depends on compiler", "0"));
            questions.add(new Question("C Lang", difficulty, "Which header file contains printf() and scanf()?", "<stdlib.h>", "<string.h>", "<stdio.h>", "<math.h>", "<stdio.h>"));
            questions.add(new Question("C Lang", difficulty, "What does NULL pointer point to?", "Garbage value", "Memory location 0", "Stack top", "Heap base", "Memory location 0"));
            questions.add(new Question("C Lang", difficulty, "Which keyword is used to define a structure in C?", "class", "struct", "union", "record", "struct"));
            questions.add(new Question("C Lang", difficulty, "Which function is used to find the length of a string in C?", "length()", "strlen()", "strsize()", "len()", "strlen()"));
            questions.add(new Question("C Lang", difficulty, "What is a union in C?", "Shares same memory location for all members", "Allocates separate memory for each member", "A type of array", "A pointer", "Shares same memory location for all members"));
            questions.add(new Question("C Lang", difficulty, "Which storage class stores variables in CPU registers?", "auto", "static", "extern", "register", "register"));
            questions.add(new Question("C Lang", difficulty, "What is the default return type of a function in C if not specified?", "void", "int", "float", "char", "int"));
            questions.add(new Question("C Lang", difficulty, "Which operator is used to access structure members through a pointer?", ".", "->", "::", "&", "->"));
            questions.add(new Question("C Lang", difficulty, "Which function frees dynamically allocated memory in C?", "delete()", "free()", "clear()", "remove()", "free()"));
            questions.add(new Question("C Lang", difficulty, "What is the terminating character of a string in C?", "\\n", "\\t", "\\0", "EOF", "\\0"));
            questions.add(new Question("C Lang", difficulty, "Which keyword is used to exit from a loop immediately?", "continue", "exit", "break", "return", "break"));
            questions.add(new Question("C Lang", difficulty, "Which bitwise operator performs bitwise AND?", "&&", "&", "|", "^", "&"));
            questions.add(new Question("C Lang", difficulty, "In C, parameters are passed by default using?", "Call by reference", "Call by value", "Call by name", "Call by pointer", "Call by value"));
        } else {
            questions.add(new Question("Java", difficulty, "What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "16 bytes", "4 bytes"));
            questions.add(new Question("Java", difficulty, "Which keyword is used to create a class in Java?", "class", "Class", "struct", "define", "class"));
            questions.add(new Question("Java", difficulty, "Which of the following is not an OOPS concept in Java?", "Inheritance", "Compilation", "Encapsulation", "Polymorphism", "Compilation"));
            questions.add(new Question("Java", difficulty, "Which interface is the root of the Java Collection hierarchy?", "Collection", "List", "Map", "Set", "Collection"));
            questions.add(new Question("Java", difficulty, "What is the default value of local variables in Java?", "0", "null", "Not assigned/Undefined", "Depends on data type", "Not assigned/Undefined"));
            questions.add(new Question("Java", difficulty, "Which memory space in JVM stores objects?", "Stack", "Heap", "Method Area", "PC Register", "Heap"));
            questions.add(new Question("Java", difficulty, "Which company originally developed Java?", "Microsoft", "Sun Microsystems", "Oracle", "Google", "Sun Microsystems"));
            questions.add(new Question("Java", difficulty, "What is string pool in Java?", "Heap Area for String literals", "Stack memory", "File System", "Database", "Heap Area for String literals"));
            questions.add(new Question("Java", difficulty, "Which feature allows multiple threads to run concurrently in Java?", "Multithreading", "Polymorphism", "Serialization", "Reflection", "Multithreading"));
            questions.add(new Question("Java", difficulty, "Which keyword is used to inherit a class in Java?", "implements", "extends", "inherits", "super", "extends"));
            questions.add(new Question("Java", difficulty, "Which exception is thrown when dividing by zero in Java?", "ArithmeticException", "NullPointerException", "IOException", "IndexOutOfBoundsException", "ArithmeticException"));
            questions.add(new Question("Java", difficulty, "What is the extension of compiled Java class files?", ".java", ".class", ".jv", ".jar", ".class"));
            questions.add(new Question("Java", difficulty, "Which method is the entry point of any Java application?", "start()", "init()", "main()", "run()", "main()"));
            questions.add(new Question("Java", difficulty, "Which package contains the Random class in Java?", "java.util", "java.lang", "java.io", "java.net", "java.util"));
            questions.add(new Question("Java", difficulty, "Which access modifier makes a variable accessible only within its own class?", "public", "protected", "private", "default", "private"));
            questions.add(new Question("Java", difficulty, "What is autoboxing in Java?", "Conversion of primitive type to wrapper object", "Conversion of wrapper to primitive", "Garbage collection", "Type casting", "Conversion of primitive type to wrapper object"));
            questions.add(new Question("Java", difficulty, "Which of these are not a valid keyword in Java?", "goto", "const", "volatile", "unsigned", "unsigned"));
            questions.add(new Question("Java", difficulty, "What is the superclass of all classes in Java?", "Object", "Class", "String", "System", "Object"));
            questions.add(new Question("Java", difficulty, "Which method is used to start a thread in Java?", "run()", "start()", "init()", "execute()", "start()"));
            questions.add(new Question("Java", difficulty, "Which data structure is synchronized in Java collections?", "ArrayList", "HashMap", "Vector", "HashSet", "Vector"));
        }
        
        return questions;
    }

    private List<Question> getQuestionsBySubjectAndDifficulty(String subject, String difficulty) {
        List<Question> questions = null;
        if (questionRepository != null) {
            try {
                questions = questionRepository.findBySubjectAndDifficulty(subject, difficulty);
            } catch (Exception e) {
                questions = null;
            }
        }
        if (questions == null || questions.size() < 20) {
            questions = getFallbackQuestions(subject, difficulty);
        }
        return questions;
    }

    @PostMapping("/api/upload-pdf")
    @ResponseBody
    public String handlePdfUpload(@RequestParam("file") MultipartFile file, @RequestParam("subject") String subject) {
        if (file.isEmpty()) {
            return "Error: Please select a valid PDF file.";
        }
        try {
            String subjectFolder = UPLOAD_DIR + subject + "/";
            File dir = new File(subjectFolder);
            if (!dir.exists()) dir.mkdirs();

            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(subjectFolder + fileName);
            Files.write(filePath, file.getBytes());

            return "Success: " + fileName + " uploaded successfully for " + subject + "!";
        } catch (IOException e) {
            return "Error during file upload: " + e.getMessage();
        }
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/notebook")
    public String notebookStudio(@RequestParam(defaultValue = "Java") String subject,
                                  @RequestParam(defaultValue = "Lecture Notes") String category,
                                  Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("selectedCategory", category);
        
        List<String> uploadedFiles = new ArrayList<>();
        try {
            Resource resource = resourceLoader.getResource("classpath:static/uploads/" + subject);
            if (resource.exists() && resource.getFile().exists() && resource.getFile().listFiles() != null) {
                for (File f : resource.getFile().listFiles()) {
                    if (f.isFile()) {
                        uploadedFiles.add(f.getName());
                    }
                }
            }
        } catch (Exception e) {
            // Fallback to physical path check if resource loader fails
            File dir = new File(UPLOAD_DIR + subject + "/");
            if (dir.exists() && dir.listFiles() != null) {
                for (File f : dir.listFiles()) {
                    if (f.isFile()) {
                        uploadedFiles.add(f.getName());
                    }
                }
            }
        }

        model.addAttribute("uploadedFiles", uploadedFiles);
        return "notebook";
    }

    @GetMapping("/api/ai-chat")
    @ResponseBody
    public String getAiChatResponse(@RequestParam String subject, @RequestParam String prompt) {
        String cleanTopic = prompt.trim();
        StringBuilder sb = new StringBuilder();
        sb.append("<div id='aiGeneratedContent' class='p-4 bg-dark border border-secondary rounded-4 shadow-lg text-light' style='line-height: 1.75;'>");
        sb.append("<div class='d-flex justify-content-between align-items-center border-bottom border-secondary pb-3 mb-4'>");
        sb.append("<div><span class='badge bg-warning text-dark mb-2'>✨ Deep AI Research Engine</span>");
        sb.append("<h3 class='text-info fw-bold mb-0'>Comprehensive Guide: ").append(cleanTopic).append(" (").append(subject).append(")</h3></div>");
        sb.append("<button type='button' onclick='speakAiResponse()' class='btn btn-outline-info btn-sm rounded-pill px-3'><i class='fas fa-volume-up me-1'></i> Listen Audio</button>");
        sb.append("</div>");
        sb.append("<h5 class='text-warning fw-bold mt-3'>1. Executive Summary & Core Concept of ").append(cleanTopic).append("</h5>");
        sb.append("<p><strong>").append(cleanTopic).append("</strong> is a foundational concept in <strong>").append(subject).append("</strong> software engineering. It provides developers with a structured mechanism to organize logic, manage system memory efficiently, and build scalable enterprise applications.</p>");
        sb.append("</div>");
        return sb.toString();
    }
}