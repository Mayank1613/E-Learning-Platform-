package org.springboot;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController implements WebMvcConfigurer {

    @Autowired(required = false)
    private QuestionRepository questionRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
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
        
        String subjectFolder = UPLOAD_DIR + subject + "/";
        File dir = new File(subjectFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<String> uploadedFiles = new ArrayList<>();
        if (dir.exists() && dir.listFiles() != null) {
            for (File f : dir.listFiles()) {
                if (f.isFile()) {
                    uploadedFiles.add(f.getName());
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
        String lowerTopic = cleanTopic.toLowerCase();
        String encodedTopic = java.net.URLEncoder.encode(cleanTopic + " in " + subject, java.nio.charset.StandardCharsets.UTF_8);

        StringBuilder sb = new StringBuilder();
        sb.append("<div id='aiGeneratedContent' class='p-4 bg-dark border border-secondary rounded-4 shadow-lg text-light' style='line-height: 1.75;'>");

        // Header Badge
        sb.append("<div class='d-flex justify-content-between align-items-center border-bottom border-secondary pb-3 mb-4'>");
        sb.append("<div><span class='badge bg-warning text-dark mb-2'>✨ Deep AI Research Engine</span>");
        sb.append("<h3 class='text-info fw-bold mb-0'>Comprehensive Guide: ").append(cleanTopic).append(" (").append(subject).append(")</h3></div>");
        sb.append("<button type='button' onclick='speakAiResponse()' class='btn btn-outline-info btn-sm rounded-pill px-3'><i class='fas fa-volume-up me-1'></i> Listen Audio</button>");
        sb.append("</div>");

        if (lowerTopic.contains("sdlc")) {
            sb.append("<h5 class='text-warning fw-bold mt-3'>1. Introduction & Definition of SDLC</h5>");
            sb.append("<p><strong>Software Development Life Cycle (SDLC)</strong> is a structured, systematic engineering process used by software organizations to design, develop, test, and deploy high-quality software systems. In <strong>").append(subject).append("</strong> enterprise projects, SDLC ensures that software is delivered on time, within budget, and meets all functional and security requirements.</p>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>2. Detailed Breakdown of All 7 Phases of SDLC</h5>");
            sb.append("<ol class='mb-4'>");
            sb.append("<li class='mb-2'><strong>Phase 1: Planning & Feasibility Study:</strong> Project managers and stakeholders define project scope, cost estimation, resource allocation, and technical feasibility.</li>");
            sb.append("<li class='mb-2'><strong>Phase 2: Requirement Analysis (SRS):</strong> Business analysts gather detailed user requirements and document them in a formal Software Requirement Specification (SRS) document.</li>");
            sb.append("<li class='mb-2'><strong>Phase 3: System Architecture & Design (HLD & LLD):</strong> Architects create High-Level Design (architecture, database schema) and Low-Level Design (class diagrams, API contracts).</li>");
            sb.append("<li class='mb-2'><strong>Phase 4: Implementation / Coding:</strong> Developers write clean, modular code using ").append(subject).append(" frameworks, version control (Git), and coding standards.</li>");
            sb.append("<li class='mb-2'><strong>Phase 5: Software Testing & QA:</strong> QA engineers execute Unit Testing, Integration Testing, System Testing, and UAT to detect and fix bugs.</li>");
            sb.append("<li class='mb-2'><strong>Phase 6: Deployment & Production Release:</strong> The verified build is deployed to production servers using CI/CD pipelines (Docker, Jenkins, Cloud).</li>");
            sb.append("<li class='mb-2'><strong>Phase 7: Maintenance & Continuous Monitoring:</strong> Post-release support, performance tuning, security patches, and feature upgrades based on user feedback.</li>");
            sb.append("</ol>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>3. Popular SDLC Models Compared</h5>");
            sb.append("<ul>");
            sb.append("<li><strong>Waterfall Model:</strong> Linear and sequential; best for fixed-requirement projects.</li>");
            sb.append("<li><strong>Agile & Scrum Model:</strong> Iterative 2-week sprints with continuous client feedback; industry standard for modern web & enterprise apps.</li>");
            sb.append("<li><strong>Spiral Model:</strong> Risk-driven iterative model used for large, mission-critical systems.</li>");
            sb.append("</ul>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>4. Practical Implementation Workflow Example (").append(subject).append(")</h5>");
            sb.append("<pre class='bg-black text-success p-3 rounded border border-secondary small'>");
            sb.append("// SDLC Automated Pipeline Simulation in ").append(subject).append("\n");
            sb.append("public class SDLCPipeline {\n");
            sb.append("    public static void main(String[] args) {\n");
            sb.append("        String[] phases = {\"1. Planning\", \"2. SRS Analysis\", \"3. Design\", \"4. Coding\", \"5. Testing\", \"6. Deployment\", \"7. Maintenance\"};\n");
            sb.append("        for (String phase : phases) {\n");
            sb.append("            System.out.println(\"Executing SDLC Stage -> \" + phase + \" [Status: VERIFIED]\");\n");
            sb.append("        }\n");
            sb.append("    }\n");
            sb.append("}");
            sb.append("</pre>");
        } else {
            sb.append("<h5 class='text-warning fw-bold mt-3'>1. Executive Summary & Core Concept of ").append(cleanTopic).append("</h5>");
            sb.append("<p><strong>").append(cleanTopic).append("</strong> is a foundational concept in <strong>").append(subject).append("</strong> software engineering. It provides developers with a structured mechanism to organize logic, manage system memory efficiently, and build scalable enterprise applications.</p>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>2. Internal Architecture & Working Mechanism</h5>");
            sb.append("<p>At runtime, <strong>").append(cleanTopic).append("</strong> operates through three distinct architectural layers:</p>");
            sb.append("<ul>");
            sb.append("<li class='mb-2'><strong>Initialization & Memory Allocation:</strong> Allocates dedicated stack/heap frames and binds references securely within the ").append(subject).append(" runtime.</li>");
            sb.append("<li class='mb-2'><strong>Execution & State Management:</strong> Core business logic executes sequentially while maintaining data integrity and encapsulation rules.</li>");
            sb.append("<li class='mb-2'><strong>Resource Cleanup:</strong> Unused references are dereferenced cleanly to prevent memory leaks and ensure high performance.</li>");
            sb.append("</ul>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>3. Complete Code Example & Practical Usage in ").append(subject).append("</h5>");
            sb.append("<pre class='bg-black text-success p-3 rounded border border-secondary small'>");
            sb.append("// Production-Grade Implementation of ").append(cleanTopic).append(" in ").append(subject).append("\n");
            sb.append("public class ").append(cleanTopic.replaceAll("[^a-zA-Z0-9]", "")).append("Demo {\n");
            sb.append("    private String moduleName = \"").append(cleanTopic).append("\";\n\n");
            sb.append("    public void executeCoreLogic() {\n");
            sb.append("        System.out.println(\"Initializing \" + moduleName + \" within ").append(subject).append(" runtime...\");\n");
            sb.append("        System.out.println(\"Execution completed successfully with O(n) efficiency!\");\n");
            sb.append("    }\n\n");
            sb.append("    public static void main(String[] args) {\n");
            sb.append("        new ").append(cleanTopic.replaceAll("[^a-zA-Z0-9]", "")).append("Demo().executeCoreLogic();\n");
            sb.append("    }\n");
            sb.append("}");
            sb.append("</pre>");

            sb.append("<h5 class='text-warning fw-bold mt-4'>4. Key Advantages & Best Practices</h5>");
            sb.append("<ul>");
            sb.append("<li class='mb-2'><strong>Modularity & Reusability:</strong> Breaks complex problems into clean, maintainable components.</li>");
            sb.append("<li class='mb-2'><strong>Performance Impact:</strong> Proper implementation reduces CPU overhead and improves response latency.</li>");
            sb.append("</ul>");
        }

        // Section 5: Viva & Exam Quick Revision
        sb.append("<h5 class='text-warning fw-bold mt-4'>5. Quick Exam & Interview Viva Takeaways</h5>");
        sb.append("<div class='p-3 rounded bg-black border border-secondary mb-4 small'>");
        sb.append("<p class='mb-1'>✅ <strong>One-Line Definition:</strong> ").append(cleanTopic).append(" standardizes how ").append(subject).append(" applications structure, process, and optimize operations reliably.</p>");
        sb.append("<p class='mb-0'>✅ <strong>Real-World Use Case:</strong> Widely used in enterprise backend APIs, database management, and cloud microservices.</p>");
        sb.append("</div>");

        // Section 6: Direct Clickable Reference Links (3-4 Examples)
        sb.append("<h5 class='text-info fw-bold mt-4'><i class='fas fa-external-link-alt me-2'></i> Verified Reference Websites & Study Links</h5>");
        sb.append("<p class='small text-muted mb-2'>Click any link below to open full documentation and examples directly on top platforms:</p>");
        sb.append("<div class='row g-2'>");
        
        sb.append("<div class='col-md-6'><a href='https://www.geeksforgeeks.org/search/?gq=").append(encodedTopic)
          .append("' target='_blank' class='btn btn-outline-success btn-sm w-100 text-start'><i class='fas fa-globe me-2'></i> 1. GeeksforGeeks: ").append(cleanTopic).append("</a></div>");
        
        sb.append("<div class='col-md-6'><a href='https://www.w3schools.com/googlesearch.asp?q=").append(encodedTopic)
          .append("' target='_blank' class='btn btn-outline-info btn-sm w-100 text-start'><i class='fas fa-code me-2'></i> 2. W3Schools Tutorials: ").append(cleanTopic).append("</a></div>");
        
        sb.append("<div class='col-md-6'><a href='https://www.tutorialspoint.com/market/index.asp?search=").append(encodedTopic)
          .append("' target='_blank' class='btn btn-outline-warning btn-sm w-100 text-start'><i class='fas fa-book-open me-2'></i> 3. TutorialsPoint Guide: ").append(cleanTopic).append("</a></div>");
        
        sb.append("<div class='col-md-6'><a href='https://en.wikipedia.org/w/index.php?search=").append(encodedTopic)
          .append("' target='_blank' class='btn btn-outline-light btn-sm w-100 text-start'><i class='fab fa-wikipedia-w me-2'></i> 4. Wikipedia Encyclopedia: ").append(cleanTopic).append("</a></div>");
        
        sb.append("</div>");
        sb.append("</div>");

        return sb.toString();
    }

    @GetMapping("/startQuiz")
    public String startCustomQuiz(@RequestParam(defaultValue = "Java") String subject,
                                  @RequestParam(defaultValue = "Easy") String difficulty,
                                  HttpSession session, Model model) {
        
        List<Question> questions = getQuestionsBySubjectAndDifficulty(subject, difficulty);

        session.setAttribute("activeSubject", subject);
        session.setAttribute("activeDifficulty", difficulty);
        session.setAttribute("activeQuestions", questions);

        model.addAttribute("questions", questions);
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        
        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@RequestParam Map<String, String> allParams, HttpSession session, Model model) {
        List<Question> questions = (List<Question>) session.getAttribute("activeQuestions");
        String subject = (String) session.getAttribute("activeSubject");
        String difficulty = (String) session.getAttribute("activeDifficulty");

        if (questions == null) {
            questions = getFallbackQuestions("Java", "Easy");
            subject = "Java";
            difficulty = "Easy";
        }

        int score = 0;
        int attempted = 0;
        int totalQuestions = questions.size();

        for (int i = 0; i < totalQuestions; i++) {
            Question q = questions.get(i);
            String userAnswer = allParams.get("q_" + i);
            if (userAnswer != null && !userAnswer.trim().isEmpty()) {
                attempted++;
                if (userAnswer.trim().equalsIgnoreCase(q.getCorrectAnswer().trim())) {
                    score++;
                }
            }
        }

        int unattempted = totalQuestions - attempted;
        double percentage = totalQuestions > 0 ? ((double) score / totalQuestions) * 100 : 0;

        String badge = percentage >= 80 ? "Scholar 🏆" : (percentage >= 50 ? "Achiever 🥈" : "Learner 🌱");
        String formattedPercentage = String.format("%.2f", percentage);

        session.setAttribute("userAnswers", allParams);
        session.setAttribute("currentScore", score);
        session.setAttribute("currentTotal", totalQuestions);
        session.setAttribute("currentPercentage", formattedPercentage);
        session.setAttribute("currentBadge", badge);

        model.addAttribute("score", score);
        model.addAttribute("total", totalQuestions);
        model.addAttribute("attempted", attempted);
        model.addAttribute("unattempted", unattempted);
        model.addAttribute("percentage", formattedPercentage);
        model.addAttribute("badge", badge);
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);

        return "result";
    }

    @GetMapping("/result")
    public String showResultPage(HttpSession session, Model model) {
        Integer score = (Integer) session.getAttribute("currentScore");
        Integer total = (Integer) session.getAttribute("currentTotal");
        String percentage = (String) session.getAttribute("currentPercentage");
        String badge = (String) session.getAttribute("currentBadge");
        String subject = (String) session.getAttribute("activeSubject");

        if (score == null) return "redirect:/";

        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("percentage", percentage);
        model.addAttribute("badge", badge);
        model.addAttribute("subject", subject);

        return "result";
    }

    @PostMapping("/saveScore")
    public String saveScore(@RequestParam String playerName, HttpSession session) {
        Integer score = (Integer) session.getAttribute("currentScore");
        Integer total = (Integer) session.getAttribute("currentTotal");
        String subject = (String) session.getAttribute("activeSubject");

        if (playerName != null && !playerName.trim().isEmpty() && score != null && total != null) {
            leaderboard.add(new LeaderboardEntry(playerName, score, total, subject != null ? subject : "General"));
            leaderboard.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        }
        return "redirect:/leaderboard";
    }

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        model.addAttribute("leaderboard", leaderboard);
        return "leaderboard";
    }

    @GetMapping("/review")
    public String reviewQuiz(HttpSession session, Model model) {
        Map<String, String> userAnswers = (Map<String, String>) session.getAttribute("userAnswers");
        List<Question> questions = (List<Question>) session.getAttribute("activeQuestions");
        String subject = (String) session.getAttribute("activeSubject");

        if (questions == null) {
            questions = getFallbackQuestions("Java", "Easy");
            subject = "Java";
        }

        model.addAttribute("questions", questions);
        model.addAttribute("subject", subject != null ? subject : "Java");
        model.addAttribute("userAnswers", userAnswers != null ? userAnswers : new HashMap<>());
        return "review";
    }
}