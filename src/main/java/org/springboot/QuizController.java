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

    private static ArrayList leaderboard = new ArrayList();
    
    private static List latestQuestions = new ArrayList();
    private static Map latestUserAnswers = new HashMap();
    private static String latestSubject = "Java";
    private static int latestScore = 0;
    private static int latestTotal = 20;

    private List getFallbackQuestions(String subject, String difficulty) {
        List questions = new ArrayList();
        String diff = (difficulty != null) ? difficulty.trim().toLowerCase() : "medium";
        
        if ("Java".equalsIgnoreCase(subject)) {
            if ("easy".equals(diff)) {
                questions.add(new Question("Java", "Easy", "Which of the following is not a Java keyword?", "static", "Boolean", "void", "private", "Boolean"));
                questions.add(new Question("Java", "Easy", "What is the extension of Java bytecode files?", ".java", ".class", ".txt", ".js", ".class"));
                questions.add(new Question("Java", "Easy", "Which component is responsible for executing Java bytecode?", "JDK", "JRE", "JVM", "JIT", "JVM"));
                questions.add(new Question("Java", "Easy", "Which data type is used to store text in Java?", "int", "String", "float", "boolean", "String"));
                questions.add(new Question("Java", "Easy", "Which symbol is used for single-line comments in Java?", "//", "/*", "#", "<!--", "//"));
                questions.add(new Question("Java", "Easy", "What is the default value of a boolean variable in Java?", "true", "false", "null", "0", "false"));
                questions.add(new Question("Java", "Easy", "Which method must be present in every Java application?", "start()", "run()", "main()", "init()", "main()"));
                questions.add(new Question("Java", "Easy", "Which package contains the Scanner class?", "java.io", "java.util", "java.net", "java.sql", "java.util"));
                questions.add(new Question("Java", "Easy", "Which operator is used to compare two values in Java?", "=", "==", ":=", "equals", "=="));
                questions.add(new Question("Java", "Easy", "What is the size of a float variable in Java?", "2 bytes", "4 bytes", "8 bytes", "16 bytes", "4 bytes"));
                questions.add(new Question("Java", "Easy", "Which loop executes at least once?", "for", "while", "do-while", "foreach", "do-while"));
                questions.add(new Question("Java", "Easy", "Which keyword is used to inherit a class in Java?", "implements", "inherits", "extends", "super", "extends"));
                questions.add(new Question("Java", "Easy", "What is the parent class of all classes in Java?", "Object", "System", "Class", "String", "Object"));
                questions.add(new Question("Java", "Easy", "Which access modifier makes a member accessible only within its own class?", "public", "protected", "private", "default", "private"));
                questions.add(new Question("Java", "Easy", "Which keyword is used to define constants in Java?", "const", "final", "static", "define", "final"));
                questions.add(new Question("Java", "Easy", "What is the default value of an object reference in Java?", "0", "false", "null", "undefined", "null"));
                questions.add(new Question("Java", "Easy", "Which exception is thrown when dividing by zero in integers?", "NullPointerException", "ArithmeticException", "IOException", "ClassCastException", "ArithmeticException"));
                questions.add(new Question("Java", "Easy", "Which keyword is used to handle exceptions?", "try-catch", "throw-trap", "handle-error", "catch-error", "try-catch"));
                questions.add(new Question("Java", "Easy", "Which of these is not an OOP principle?", "Encapsulation", "Compilation", "Inheritance", "Polymorphism", "Compilation"));
                questions.add(new Question("Java", "Easy", "What company originally created Java?", "Microsoft", "Sun Microsystems", "Oracle", "Google", "Sun Microsystems"));
            } else if ("hard".equals(diff)) {
                questions.add(new Question("Java", "Hard", "Which Garbage Collector is default in Java 9 and later?", "Serial GC", "Parallel GC", "CMS GC", "G1 GC", "G1 GC"));
                questions.add(new Question("Java", "Hard", "What is the internal data structure of ConcurrentHashMap in Java 8+?", "HashTable", "Balanced BST & LinkedList", "Array of buckets only", "SkipList", "Balanced BST & LinkedList"));
                questions.add(new Question("Java", "Hard", "Which reference type has the lowest priority and is collected first?", "SoftReference", "WeakReference", "PhantomReference", "StrongReference", "PhantomReference"));
                questions.add(new Question("Java", "Hard", "What does JVM option '-XX:+UseStringDeduplication' do?", "Deletes duplicate strings", "Removes duplicate chars across string arrays via G1 GC", "Clears String pool", "None", "Removes duplicate chars across string arrays via G1 GC"));
                questions.add(new Question("Java", "Hard", "Which interface allows custom sorting order in Java Collections?", "Comparable", "Comparator", "Sorted", "Ordered", "Comparator"));
                questions.add(new Question("Java", "Hard", "What is the purpose of transient keyword in Java serialization?", "Encrypts field", "Skips field during serialization", "Makes field static", "Makes field volatile", "Skips field during serialization"));
                questions.add(new Question("Java", "Hard", "What happens if an exception is thrown in a finally block?", "Ignored", "Overrides previous exception", "Caught by catch block", "Appends to stack trace", "Overrides previous exception"));
                questions.add(new Question("Java", "Hard", "Which concurrency utility allows one or more threads to wait until a set of operations completes?", "Semaphore", "CountDownLatch", "CyclicBarrier", "Exchanger", "CountDownLatch"));
                questions.add(new Question("Java", "Hard", "What is method hiding in Java?", "Overriding static method", "Overloading private method", "Hiding fields in subclass", "None", "Overriding static method"));
                questions.add(new Question("Java", "Hard", "Which class loader loads core Java API classes?", "Application ClassLoader", "Extension ClassLoader", "Bootstrap ClassLoader", "Custom ClassLoader", "Bootstrap ClassLoader"));
                questions.add(new Question("Java", "Hard", "What is the max size of an array in Java?", "Integer.MAX_VALUE", "Short.MAX_VALUE", "Long.MAX_VALUE", "Depends on RAM", "Integer.MAX_VALUE"));
                questions.add(new Question("Java", "Hard", "Which feature was introduced in Java 14 for immutable data carriers?", "Lambdas", "Records", "Sealed Classes", "Modules", "Records"));
                questions.add(new Question("Java", "Hard", "What is busy-spinning in multithreading?", "Sleeping thread", "Continuously checking condition in a loop", "Blocking queue wait", "Thread suspension", "Continuously checking condition in a loop"));
                questions.add(new Question("Java", "Hard", "Which annotation restricts class inheritance in Java 15+?", "@Final", "@Sealed", "@Restricted", "@Strict", "@Sealed"));
                questions.add(new Question("Java", "Hard", "What is the time complexity of searching in a standard HashSet?", "O(N)", "O(log N)", "O(1) average", "O(N log N)", "O(1) average"));
                questions.add(new Question("Java", "Hard", "What is tail-call optimization in JVM?", "Not supported natively by standard JVM", "Optimizes recursive calls", "Clears stack frame", "JIT compilation trick", "Not supported natively by standard JVM"));
                questions.add(new Question("Java", "Hard", "What is the main difference between CyclicBarrier and CountDownLatch?", "Latch can be reset, Barrier cannot", "Barrier can be reused, Latch cannot", "Both are identical", "Barrier uses threads, Latch uses processes", "Barrier can be reused, Latch cannot"));
                questions.add(new Question("Java", "Hard", "What is the memory footprint of an empty Object in 64-bit JVM with compressed oops?", "8 bytes", "16 bytes", "24 bytes", "32 bytes", "16 bytes"));
                questions.add(new Question("Java", "Hard", "Which JVM flag prints GC details?", "-verbose:gc", "-print:gc", "-gc:log", "-debug:gc", "-verbose:gc"));
                questions.add(new Question("Java", "Hard", "What is the difference between String and StringBuilder in memory?", "String is immutable, StringBuilder is mutable", "StringBuilder is immutable", "String is primitive", "No difference", "String is immutable, StringBuilder is mutable"));
            } else {
                questions.add(new Question("Java", "Medium", "What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "16 bytes", "4 bytes"));
                questions.add(new Question("Java", "Medium", "Which keyword is used to create a class in Java?", "class", "Class", "struct", "define", "class"));
                questions.add(new Question("Java", "Medium", "Which of the following is not an OOPS concept in Java?", "Inheritance", "Compilation", "Encapsulation", "Polymorphism", "Compilation"));
                questions.add(new Question("Java", "Medium", "Which interface is the root of the Java Collection hierarchy?", "Collection", "List", "Map", "Set", "Collection"));
                questions.add(new Question("Java", "Medium", "What is the default value of local variables in Java?", "0", "null", "Not assigned/Undefined", "Depends on data type", "Not assigned/Undefined"));
                questions.add(new Question("Java", "Medium", "Which memory space in JVM stores objects?", "Stack", "Heap", "Method Area", "PC Register", "Heap"));
                questions.add(new Question("Java", "Medium", "Which company originally developed Java?", "Microsoft", "Sun Microsystems", "Oracle", "Google", "Sun Microsystems"));
                questions.add(new Question("Java", "Medium", "What is string pool in Java?", "Heap Area for String literals", "Stack memory", "File System", "Database", "Heap Area for String literals"));
                questions.add(new Question("Java", "Medium", "Which feature allows multiple threads to run concurrently in Java?", "Multithreading", "Polymorphism", "Serialization", "Reflection", "Multithreading"));
                questions.add(new Question("Java", "Medium", "Which keyword is used to inherit a class in Java?", "implements", "extends", "inherits", "super", "extends"));
                questions.add(new Question("Java", "Medium", "What is the extension of compiled Java bytecode files?", ".java", ".class", ".obj", ".exe", ".class"));
                questions.add(new Question("Java", "Medium", "Which method is the entry point of a Java application?", "start()", "init()", "main()", "run()", "main()"));
                questions.add(new Question("Java", "Medium", "Which package contains the Random class in Java?", "java.util", "java.lang", "java.io", "java.net", "java.util"));
                questions.add(new Question("Java", "Medium", "Which of these access modifiers allows visibility only within the same class?", "public", "protected", "private", "default", "private"));
                questions.add(new Question("Java", "Medium", "What is method overriding in Java?", "Same method name and parameters in parent and child class", "Same name different parameters", "Static method binding", "None", "Same method name and parameters in parent and child class"));
                questions.add(new Question("Java", "Medium", "Which keyword is used to prevent method overriding in Java?", "final", "static", "private", "abstract", "final"));
                questions.add(new Question("Java", "Medium", "What is garbage collection in Java?", "Automatic memory management", "Manual memory free", "Thread synchronization", "Exception handling", "Automatic memory management"));
                questions.add(new Question("Java", "Medium", "Which exception is thrown when dividing by zero in integer arithmetic?", "NullPointerException", "ArithmeticException", "IOException", "IndexOutOfBoundsException", "ArithmeticException"));
                questions.add(new Question("Java", "Medium", "Which class is used to create strings in Java that can be modified?", "String", "StringBuilder", "System", "Scanner", "StringBuilder"));
                questions.add(new Question("Java", "Medium", "What is an interface in Java?", "A completely abstract class with abstract methods", "A concrete class", "A package", "An object", "A completely abstract class with abstract methods"));
            }
        } else if ("Python".equalsIgnoreCase(subject)) {
            if ("easy".equals(diff)) {
                questions.add(new Question("Python", "Easy", "Which symbol is used for comments in Python?", "//", "/*", "#", "<!--", "#"));
                questions.add(new Question("Python", "Easy", "What is the extension of Python files?", ".py", ".pyt", ".python", ".p", ".py"));
                questions.add(new Question("Python", "Easy", "Which function outputs text to console in Python?", "print()", "echo()", "output()", "console.log()", "print()"));
                questions.add(new Question("Python", "Easy", "How do you create a variable with numeric value 5 in Python?", "x = 5", "int x = 5", "num x = 5", "var x = 5", "x = 5"));
                questions.add(new Question("Python", "Easy", "Which data type is used to store true or false in Python?", "int", "bool", "string", "float", "bool"));
                questions.add(new Question("Python", "Easy", "How do you start a function in Python?", "function myFunc():", "def myFunc():", "create myFunc():", "func myFunc():", "def myFunc():"));
                questions.add(new Question("Python", "Easy", "Which collection is ordered and changeable in Python?", "Tuple", "List", "Set", "Dictionary", "List"));
                questions.add(new Question("Python", "Easy", "Which collection is unchangeable and indexed with parentheses?", "List", "Tuple", "Dictionary", "Set", "Tuple"));
                questions.add(new Question("Python", "Easy", "What is the result of 10 % 3 in Python?", "3", "1", "3.33", "0", "1"));
                questions.add(new Question("Python", "Easy", "Which keyword is used for conditional branches in Python?", "switch", "if", "case", "when", "if"));
                questions.add(new Question("Python", "Easy", "Which loop is used for iterating over a sequence in Python?", "for", "while", "repeat", "do-while", "for"));
                questions.add(new Question("Python", "Easy", "What built-in function returns the length of a list?", "size()", "length()", "len()", "count()", "len()"));
                questions.add(new Question("Python", "Easy", "How do you import a module named math in Python?", "include math", "import math", "use math", "require math", "import math"));
                questions.add(new Question("Python", "Easy", "Which operator is used for exponentiation in Python?", "^", "**", "*", "exp", "**"));
                questions.add(new Question("Python", "Easy", "What is the correct file mode to open a file for reading in Python?", "r", "w", "a", "x", "r"));
                questions.add(new Question("Python", "Easy", "Which keyword is used to handle exceptions in Python?", "try-catch", "try-except", "handle-error", "catch-error", "try-except"));
                questions.add(new Question("Python", "Easy", "What does pip stand for?", "Python Installer", "Preferred Installer Program", "Package Interface", "Program Index", "Preferred Installer Program"));
                questions.add(new Question("Python", "Easy", "Which method adds an element to the end of a list in Python?", "add()", "append()", "insert()", "push()", "append()"));
                questions.add(new Question("Python", "Easy", "How do you create an empty set in Python?", "{}", "set()", "[]", "()", "set()"));
                questions.add(new Question("Python", "Easy", "Who created Python?", "Guido van Rossum", "Dennis Ritchie", "James Gosling", "Bjarne Stroustrup", "Guido van Rossum"));
            } else if ("hard".equals(diff)) {
                questions.add(new Question("Python", "Hard", "What is the function of the __slots__ attribute in Python classes?", "Enables multi-inheritance", "Restricts dynamic attribute creation to save memory", "Defines private methods", "Speeds up execution", "Restricts dynamic attribute creation to save memory"));
                questions.add(new Question("Python", "Hard", "What is a metaclass in Python?", "Class of a class", "Child class", "Abstract class", "Decorator", "Class of a class"));
                questions.add(new Question("Python", "Hard", "What is the difference between yield and return in Python generators?", "return terminates function, yield pauses and returns value", "yield terminates", "No difference", "yield is deprecated", "return terminates function, yield pauses and returns value"));
                questions.add(new Question("Python", "Hard", "What does the GIL (Global Interpreter Lock) restrict in CPython?", "Multi-process execution", "Multi-threaded CPU-bound execution in single process", "Memory allocation", "File IO", "Multi-threaded CPU-bound execution in single process"));
                questions.add(new Question("Python", "Hard", "What is monkey patching in Python?", "Dynamic modification of a class or module at runtime", "Error handling technique", "Debugging tool", "Code optimization", "Dynamic modification of a class or module at runtime"));
                questions.add(new Question("Python", "Hard", "Which module provides support for asynchronous I/O in Python?", "threading", "asyncio", "multiprocessing", "concurrent", "asyncio"));
                questions.add(new Question("Python", "Hard", "What is method resolution order (MRO) algorithm used in Python?", "DFS", "BFS", "C3 Linearization", "Dijkstra", "C3 Linearization"));
                questions.add(new Question("Python", "Hard", "What is the use of functools.lru_cache?", "File caching", "Memoization of function results", "Database caching", "Memory cleaning", "Memoization of function results"));
                questions.add(new Question("Python", "Hard", "What is a descriptor in Python?", "Any object which defines methods __get__, __set__, or __delete__", "String formatter", "File descriptor", "Pointer", "Any object which defines methods __get__, __set__, or __delete__"));
                questions.add(new Question("Python", "Hard", "What is the difference between is and == operators?", "is checks memory address, == checks value equality", "is checks value, == checks memory", "Both are identical", "is is deprecated", "is checks memory address, == checks value equality"));
                questions.add(new Question("Python", "Hard", "How does Python handle garbage collection?", "Reference counting and cyclic garbage collector", "Manual deletion", "Mark and sweep only", "Tracing only", "Reference counting and cyclic garbage collector"));
                questions.add(new Question("Python", "Hard", "What is a context manager in Python?", "Object implementing __enter__ and __exit__ methods", "Thread manager", "GUI manager", "Process manager", "Object implementing __enter__ and __exit__ methods"));
                questions.add(new Question("Python", "Hard", "What is the purpose of weakref module?", "To create strong references", "To allow garbage collection of objects without reference count increment", "To speed up pointers", "Memory allocation", "To allow garbage collection of objects without reference count increment"));
                questions.add(new Question("Python", "Hard", "What does __name__ == '__main__' signify?", "Module is imported", "Script is executed directly", "Class definition", "Error state", "Script is executed directly"));
                questions.add(new Question("Python", "Hard", "What is a closure in Python?", "Nested function that remembers values from enclosing scope even after enclosing scope finishes", "Closed file", "Sealed class", "Private method", "Nested function that remembers values from enclosing scope even after enclosing scope finishes"));
                questions.add(new Question("Python", "Hard", "What is the difference between deepcopy and copy?", "copy is shallow, deepcopy recursively copies nested objects", "deepcopy is shallow", "No difference", "copy is faster for nested", "copy is shallow, deepcopy recursively copies nested objects"));
                questions.add(new Question("Python", "Hard", "What is duck typing in Python?", "Type checking based on presence of methods/properties rather than inheritance", "Animal classification", "Strict typing", "Static typing", "Type checking based on presence of methods/properties rather than inheritance"));
                questions.add(new Question("Python", "Hard", "What is the purpose of __init_subclass__?", "Initializes parent class", "Customizes subclass creation", "Deletes subclass", "None", "Customizes subclass creation"));
                questions.add(new Question("Python", "Hard", "Which built-in module inspects live objects?", "sys", "inspect", "os", "math", "inspect"));
                questions.add(new Question("Python", "Hard", "What is a coroutine in Python?", "Subroutine that can be paused and resumed, typically defined with async def", "Standard thread", "Process", "Daemon", "Subroutine that can be paused and resumed, typically defined with async def"));
            } else {
                questions.add(new Question("Python", "Medium", "Which symbol is used for comments in Python?", "//", "/*", "#", "<!--", "#"));
                questions.add(new Question("Python", "Medium", "What is the extension of Python files?", ".py", ".pyt", ".python", ".p", ".py"));
                questions.add(new Question("Python", "Medium", "What is the output of 2 ** 3 in Python?", "6", "8", "9", "12", "8"));
                questions.add(new Question("Python", "Medium", "Which collection type is ordered and immutable?", "List", "Set", "Tuple", "Dictionary", "Tuple"));
                questions.add(new Question("Python", "Medium", "Which keyword is used to create a generator in Python?", "yield", "return", "generate", "create", "yield"));
                questions.add(new Question("Python", "Medium", "What is GIL in Python?", "Global Interpreter Lock", "Global Import Logic", "General Interface Layer", "None", "Global Interpreter Lock"));
                questions.add(new Question("Python", "Medium", "Which function returns length of a list?", "length()", "len()", "count()", "size()", "len()"));
                questions.add(new Question("Python", "Medium", "How do you start a function definition in Python?", "function myFunc()", "def myFunc():", "func myFunc():", "create myFunc()", "def myFunc():"));
                questions.add(new Question("Python", "Medium", "Which of these is a mutable data type?", "Tuple", "String", "List", "Int", "List"));
                questions.add(new Question("Python", "Medium", "Which module is used for regular expressions in Python?", "regex", "pyregex", "re", "string", "re"));
                questions.add(new Question("Python", "Medium", "What is the maximum possible length of an identifier in Python?", "31 characters", "63 characters", "79 characters", "None of the above", "None of the above"));
                questions.add(new Question("Python", "Medium", "Which of the following is not a core data type in Python?", "List", "Dictionary", "Tuple", "Class", "Class"));
                questions.add(new Question("Python", "Medium", "What is the return type of function id()?", "Int", "Float", "Bool", "String", "Int"));
                questions.add(new Question("Python", "Medium", "Which of the following functions converts a string to float in Python?", "int()", "float()", "str()", "chr()", "float()"));
                questions.add(new Question("Python", "Medium", "Which operator is used for floor division in Python?", "/", "//", "%", "**", "//"));
                questions.add(new Question("Python", "Medium", "What is output of bool('False') in Python?", "True", "False", "None", "Error", "True"));
                questions.add(new Question("Python", "Medium", "Which method is used to remove an item from list by value?", "remove()", "pop()", "del", "clear()", "remove()"));
                questions.add(new Question("Python", "Medium", "What is the base class for all user-defined exceptions in Python?", "Exception", "Error", "BaseException", "StandardError", "Exception"));
                questions.add(new Question("Python", "Medium", "Which keyword is used for exception handling in Python?", "try-catch", "try-except", "handle", "except-finally", "try-except"));
                questions.add(new Question("Python", "Medium", "What does pip stand for?", "Preferred Installer Program", "Python Installation Process", "Plugin Interface Program", "None", "Preferred Installer Program"));
            }
        } else if ("DBMS".equalsIgnoreCase(subject)) {
            if ("easy".equals(diff)) {
                questions.add(new Question("DBMS", "Easy", "What does SQL stand for?", "Structured Query Language", "Simple Query", "Sequential Query", "System Query", "Structured Query Language"));
                questions.add(new Question("DBMS", "Easy", "Which command is used to retrieve data from a database?", "GET", "SELECT", "EXTRACT", "FETCH", "SELECT"));
                questions.add(new Question("DBMS", "Easy", "Which key uniquely identifies each record in a table?", "Foreign Key", "Primary Key", "Candidate Key", "Super Key", "Primary Key"));
                questions.add(new Question("DBMS", "Easy", "What does DBMS stand for?", "Database Management System", "Data Backup Management", "Database Maintenance Software", "Data Binary Management", "Database Management System"));
                questions.add(new Question("DBMS", "Easy", "Which SQL clause is used to filter records?", "HAVING", "WHERE", "GROUP BY", "ORDER BY", "WHERE"));
                questions.add(new Question("DBMS", "Easy", "Which command is used to delete a table from database?", "DELETE", "DROP", "REMOVE", "TRUNCATE", "DROP"));
                questions.add(new Question("DBMS", "Easy", "Which symbol is used in SQL wildcard queries for multiple characters?", "#", "%", "_", "*", "%"));
                questions.add(new Question("DBMS", "Easy", "Which language is used to modify database data (INSERT, UPDATE)?", "DDL", "DML", "DCL", "TCL", "DML"));
                questions.add(new Question("DBMS", "Easy", "What is a row in a relational database table called?", "Field", "Tuple/Record", "Attribute", "Column", "Tuple/Record"));
                questions.add(new Question("DBMS", "Easy", "What is a column in a relational database table called?", "Tuple", "Attribute/Field", "Record", "Instance", "Attribute/Field"));
                questions.add(new Question("DBMS", "Easy", "Which SQL function counts the number of rows?", "TOTAL()", "COUNT()", "SUM()", "NUMBER()", "COUNT()"));
                questions.add(new Question("DBMS", "Easy", "Which operator is used in SQL to search for a specified pattern?", "LIKE", "IN", "BETWEEN", "IS", "LIKE"));
                questions.add(new Question("DBMS", "Easy", "Which join returns all records from both tables when there is a match?", "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN", "INNER JOIN"));
                questions.add(new Question("DBMS", "Easy", "Which command is used to save changes permanently in a transaction?", "ROLLBACK", "COMMIT", "SAVEPOINT", "RETAIN", "COMMIT"));
                questions.add(new Question("DBMS", "Easy", "Which constraint ensures all values in a column are different?", "NOT NULL", "UNIQUE", "CHECK", "DEFAULT", "UNIQUE"));
                questions.add(new Question("DBMS", "Easy", "Which data model is most widely used today?", "Hierarchical", "Network", "Relational", "Object-Oriented", "Relational"));
                questions.add(new Question("DBMS", "Easy", "What is the term for a permanent storage unit in a database?", "File", "Table", "View", "Schema", "Table"));
                questions.add(new Question("DBMS", "Easy", "Which keyword is used to sort the result-set?", "SORT BY", "ORDER BY", "GROUP BY", "ALIGN BY", "ORDER BY"));
                questions.add(new Question("DBMS", "Easy", "Which statement is true about NULL in SQL?", "NULL equals zero", "NULL is a blank space", "NULL represents missing or unknown data", "NULL is false", "NULL represents missing or unknown data"));
                questions.add(new Question("DBMS", "Easy", "Which type of database is MongoDB?", "Relational", "NoSQL / Document-oriented", "Hierarchical", "Network", "NoSQL / Document-oriented"));
            } else if ("hard".equals(diff)) {
                questions.add(new Question("DBMS", "Hard", "What is serializability in transaction processing?", "Ensuring concurrent execution yields same result as serial execution", "Fast data retrieval", "Disk backup", "Table locking", "Ensuring concurrent execution yields same result as serial execution"));
                questions.add(new Question("DBMS", "Hard", "What is a B+ tree used for in database indexing?", "Faster sequential and random access on disk", "Encryption", "Compression", "Data normalization", "Faster sequential and random access on disk"));
                questions.add(new Question("DBMS", "Hard", "What is the phantom read anomaly?", "Reading uncommitted data", "Retrieving different set of rows in repeated queries due to concurrent insertions", "Missing rows in join", "Duplicate primary key", "Retrieving different set of rows in repeated queries due to concurrent insertions"));
                questions.add(new Question("DBMS", "Hard", "What is Aries recovery algorithm based on?", "Write-ahead logging (WAL) with repeating history", "Checkpointing only", "Rollback segments", "Mirroring", "Write-ahead logging (WAL) with repeating history"));
                questions.add(new Question("DBMS", "Hard", "What is Boyce-Codd Normal Form (BCNF) condition?", "Every determinant must be a candidate key", "No multi-valued dependencies", "No partial dependency", "1NF satisfied", "Every determinant must be a candidate key"));
                questions.add(new Question("DBMS", "Hard", "What is distributed consensus protocol commonly used in distributed databases?", "Two-Phase Commit (2PC) / Paxos / Raft", "FTP", "HTTP", "SMTP", "Two-Phase Commit (2PC) / Paxos / Raft"));
                questions.add(new Question("DBMS", "Hard", "What is cost-based query optimization?", "Selecting execution plan with lowest estimated resource consumption", "Choosing fastest CPU", "Random execution", "Fixed index lookup", "Selecting execution plan with lowest estimated resource consumption"));
                questions.add(new Question("DBMS", "Hard", "What is a clustered index?", "Index that determines physical storage order of data rows in a table", "Secondary index", "Hash index", "View index", "Index that determines physical storage order of data rows in a table"));
                questions.add(new Question("DBMS", "Hard", "What is write-ahead logging (WAL) property?", "Changes must be written to log on disk before database is modified", "Logs written after commit", "Optional logging", "InMemory logging", "Changes must be written to log on disk before database is modified"));
                questions.add(new Question("DBMS", "Hard", "What is database sharding?", "Horizontal partitioning of data across multiple independent database instances", "Vertical partitioning", "Data replication", "Index creation", "Horizontal partitioning of data across multiple independent database instances"));
                questions.add(new Question("DBMS", "Hard", "What does CAP theorem state for distributed systems?", "Can only guarantee two of Consistency, Availability, and Partition tolerance", "CPU, Access, Power", "Concurrency, Atomicity, Performance", "None", "Can only guarantee two of Consistency, Availability, and Partition tolerance"));
                questions.add(new Question("DBMS", "Hard", "What is latch vs lock in database engines?", "Latches protect internal data structures, locks protect logical database objects", "Identical terms", "Locks are faster", "Latches are persistent", "Latches protect internal data structures, locks protect logical database objects"));
                questions.add(new Question("DBMS", "Hard", "What is multi-version concurrency control (MVCC)?", "Allows simultaneous read and write without blocking by maintaining multiple snapshots", "Single version storage", "Locking all tables", "Sequential execution", "Allows simultaneous read and write without blocking by maintaining multiple snapshots"));
                questions.add(new Question("DBMS", "Hard", "What is a bitmap index best suited for?", "Columns with very low cardinality (few unique values like gender or status)", "Primary keys", "Foreign keys", "Large text columns", "Columns with very low cardinality (few unique values like gender or status)"));
                questions.add(new Question("DBMS", "Hard", "What is deadlock prevention via timestamp ordering (Wait-Die / Wound-Wait)?", "Schemes to order transactions by timestamp to prevent deadlocks", "Killing all queries", "Rebooting DB", "Timeout aborts", "Schemes to order transactions by timestamp to prevent deadlocks"));
                questions.add(new Question("DBMS", "Hard", "What is functional dependency in relational databases?", "Constraint where value of one attribute uniquely determines another", "Arithmetic relation", "Table join", "Primary key constraint", "Constraint where value of one attribute uniquely determines another"));
                questions.add(new Question("DBMS", "Hard", "What is the difference between OLTP and OLAP?", "OLTP is transactional (fast writes/reads), OLAP is analytical (complex aggregations)", "OLTP is analytical", "No difference", "OLAP is for daily logs", "OLTP is transactional (fast writes/reads), OLAP is analytical (complex aggregations)"));
                questions.add(new Question("DBMS", "Hard", "What is a materialized view?", "Pre-computed query result stored physically as a table for fast access", "Virtual table updated live", "Temporary table", "Index view", "Pre-computed query result stored physically as a table for fast access"));
                questions.add(new Question("DBMS", "Hard", "What is cascading rollback?", "Aborting one transaction forces abortion of dependent uncommitted transactions", "Normal commit", "Backup failure", "Deadlock recovery", "Aborting one transaction forces abortion of dependent uncommitted transactions"));
                questions.add(new Question("DBMS", "Hard", "What is query cost estimation using histograms?", "Estimating data distribution in columns to help query optimizer choose best plan", "Graphical report", "Disk sizing", "Security check", "Estimating data distribution in columns to help query optimizer choose best plan"));
            } else {
                questions.add(new Question("DBMS", "Medium", "SQL stands for?", "Structured Query Language", "Simple Query Language", "Sequential Query Language", "None", "Structured Query Language"));
                questions.add(new Question("DBMS", "Medium", "Which SQL command retrieves data?", "SELECT", "FETCH", "GET", "OPEN", "SELECT"));
                questions.add(new Question("DBMS", "Medium", "Which normal form removes partial dependency?", "1NF", "2NF", "3NF", "BCNF", "2NF"));
                questions.add(new Question("DBMS", "Medium", "Which key uniquely identifies each row in a database table?", "Foreign Key", "Primary Key", "Super Key", "Candidate Key", "Primary Key"));
                questions.add(new Question("DBMS", "Medium", "What does ACID stand for in DBMS?", "Atomicity, Consistency, Isolation, Durability", "Access, Control, Integrated, Data", "Auto, Check, Index, Data", "None", "Atomicity, Consistency, Isolation, Durability"));
                questions.add(new Question("DBMS", "Medium", "Which join returns all records when there is a match in either left or right table?", "INNER JOIN", "FULL OUTER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN"));
                questions.add(new Question("DBMS", "Medium", "Which command is used to delete all rows from a table quickly?", "DELETE", "TRUNCATE", "DROP", "REMOVE", "TRUNCATE"));
                questions.add(new Question("DBMS", "Medium", "Which constraint ensures all values in a column are different?", "UNIQUE", "NOT NULL", "CHECK", "DEFAULT", "UNIQUE"));
                questions.add(new Question("DBMS", "Medium", "What is a dead lock in DBMS?", "Two transactions waiting for each other", "System crash", "Network issue", "Data loss", "Two transactions waiting for each other"));
                questions.add(new Question("DBMS", "Medium", "Which SQL clause is used to filter group results?", "WHERE", "HAVING", "GROUP BY", "ORDER BY", "HAVING"));
                questions.add(new Question("DBMS", "Medium", "What is a view in SQL?", "A virtual table based on result-set of SQL query", "Physical table", "Stored procedure", "Index", "A virtual table based on result-set of SQL query"));
                questions.add(new Question("DBMS", "Medium", "Which language is used to define database schema?", "DML", "DDL", "DCL", "TCL", "DDL"));
                questions.add(new Question("DBMS", "Medium", "What is BCNF short for?", "Boyce-Codd Normal Form", "Basic Check Normal Form", "Binary Code Normal Form", "None", "Boyce-Codd Normal Form"));
                questions.add(new Question("DBMS", "Medium", "Which of the following is DML command?", "CREATE", "DROP", "UPDATE", "ALTER", "UPDATE"));
                questions.add(new Question("DBMS", "Medium", "What is a transaction in DBMS?", "Single logical unit of work", "Table row", "Query execution", "Backup process", "Single logical unit of work"));
                questions.add(new Question("DBMS", "Medium", "Which type of database model is MySQL?", "Hierarchical", "Network", "Relational", "Object-Oriented", "Relational"));
                questions.add(new Question("DBMS", "Medium", "What is indexing used for in databases?", "Faster data retrieval", "Data security", "Data compression", "Backup", "Faster data retrieval"));
                questions.add(new Question("DBMS", "Medium", "Which operator is used in SQL for pattern matching?", "LIKE", "IN", "BETWEEN", "EXISTS", "LIKE"));
                questions.add(new Question("DBMS", "Medium", "What is primary key constraint combination?", "UNIQUE and NOT NULL", "NULL and DEFAULT", "CHECK and FOREIGN", "None", "UNIQUE and NOT NULL"));
                questions.add(new Question("DBMS", "Medium", "Which property ensures changes made by committed transactions are permanent?", "Atomicity", "Consistency", "Isolation", "Durability", "Durability"));
            }
        } else if ("C++".equalsIgnoreCase(subject)) {
            if ("easy".equals(diff)) {
                questions.add(new Question("C++", "Easy", "Who created C++?", "Dennis Ritchie", "Bjarne Stroustrup", "James Gosling", "Guido van Rossum", "Bjarne Stroustrup"));
                questions.add(new Question("C++", "Easy", "Which header file is required for input/output in C++?", "<stdio.h>", "<iostream>", "<conio.h>", "<stdlib.h>", "<iostream>"));
                questions.add(new Question("C++", "Easy", "What is the extension of C++ source files?", ".c", ".cpp", ".cp", ".cs", ".cpp"));
                questions.add(new Question("C++", "Easy", "Which object is used to print output in C++?", "cin", "cout", "print", "scan", "cout"));
                questions.add(new Question("C++", "Easy", "Which object is used to take input in C++?", "cin", "cout", "input", "get", "cin"));
                questions.add(new Question("C++", "Easy", "What is the default access specifier for members of a class in C++?", "public", "protected", "private", "internal", "private"));
                questions.add(new Question("C++", "Easy", "Which operator is used for dynamic memory allocation in C++?", "malloc", "alloc", "new", "calloc", "new"));
                questions.add(new Question("C++", "Easy", "Which operator is used to deallocate dynamic memory in C++?", "free", "delete", "remove", "clear", "delete"));
                questions.add(new Question("C++", "Easy", "What is the size of boolean data type in C++?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
                questions.add(new Question("C++", "Easy", "Which symbol is used for scope resolution operator in C++?", ".", "->", "::", ":", "::"));
                questions.add(new Question("C++", "Easy", "What is the entry point function of a C++ program?", "start()", "begin()", "main()", "run()", "main()"));
                questions.add(new Question("C++", "Easy", "Which keyword is used to define a constant in C++?", "constant", "const", "static", "define", "const"));
                questions.add(new Question("C++", "Easy", "Which loop is guaranteed to execute at least once in C++?", "for", "while", "do-while", "foreach", "do-while"));
                questions.add(new Question("C++", "Easy", "What is polymorphism an example of in C++?", "OOP feature", "Procedural feature", "Template feature", "Storage class", "OOP feature"));
                questions.add(new Question("C++", "Easy", "Which keyword is used to inherit a class in C++?", "inherits", "extends", ":", "implements", ":"));
                questions.add(new Question("C++", "Easy", "What is a destructor in C++?", "Function called when object is created", "Function called when object is destroyed", "Virtual function", "Static function", "Function called when object is destroyed"));
                questions.add(new Question("C++", "Easy", "Which symbol is prefixed before a destructor name?", "!", "~", "#", "&", "~"));
                questions.add(new Question("C++", "Easy", "What is the base class of all standard exception classes in C++?", "Error", "Exception", "std::exception", "BaseException", "std::exception"));
                questions.add(new Question("C++", "Easy", "Which namespace is used by the C++ Standard Library?", "std", "cplus", "lib", "standard", "std"));
                questions.add(new Question("C++", "Easy", "Which operator cannot be overloaded in C++?", "+", "==", "::", "[]", "::"));
            } else if ("hard".equals(diff)) {
                questions.add(new Question("C++", "Hard", "What is RAII (Resource Acquisition Is Initialization) in C++?", "Garbage collection technique", "Binding resource lifecycle to object lifetime with constructor/destructor", "Template metaprogramming pattern", "Exception handling rule", "Binding resource lifecycle to object lifetime with constructor/destructor"));
                questions.add(new Question("C++", "Hard", "What is perfect forwarding in C++11?", "Passing arguments to another function while preserving their value category", "Copying objects efficiently", "Virtual inheritance", "Pointer arithmetic", "Passing arguments to another function while preserving their value category"));
                questions.add(new Question("C++", "Hard", "What is the purpose of std::move?", "Moves object in memory", "Unconditionally casts an expression to an rvalue reference", "Deallocates memory", "Creates a deep copy", "Unconditionally casts an expression to an rvalue reference"));
                questions.add(new Question("C++", "Hard", "What is SFINAE?", "Template metaprogramming rule where invalid template argument substitution is ignored", "Runtime error handler", "Exception specifier", "Virtual table mechanism", "Template metaprogramming rule where invalid template argument substitution is ignored"));
                questions.add(new Question("C++", "Hard", "What is the diamond problem in multiple inheritance and how is it solved?", "Ambiguity when two base classes inherit from same grandparent; solved via virtual inheritance", "Memory leak; solved via smart pointers", "Name clash; solved via namespaces", "Syntax error; solved via casting", "Ambiguity when two base classes inherit from same grandparent; solved via virtual inheritance"));
                questions.add(new Question("C++", "Hard", "What is a vtable (Virtual Table) in C++?", "Table of function pointers used to support runtime polymorphism", "Table of variables", "Template lookup table", "Exception table", "Table of function pointers used to support runtime polymorphism"));
                questions.add(new Question("C++", "Hard", "What is the rule of three/five in C++?", "Defines when you should explicitly write destructor, copy constructor, and copy assignment", "Rule for class inheritance", "Template rule", "Namespace rule", "Defines when you should explicitly write destructor, copy constructor, and copy assignment"));
                questions.add(new Question("C++", "Hard", "What is std::unique_ptr?", "Shared pointer", "Smart pointer that exclusively owns a dynamically allocated object", "Raw pointer wrapper", "Weak pointer", "Smart pointer that exclusively owns a dynamically allocated object"));
                questions.add(new Question("C++", "Hard", "What is constexpr specifier introduced in C++11?", "Specifies that value of a function or variable can be evaluated at compile-time", "Runtime constant", "Inline function", "Volatile variable", "Specifies that value of a function or variable can be evaluated at compile-time"));
                questions.add(new Question("C++", "Hard", "What is copy elision (e.g., Return Value Optimization - RVO)?", "Compiler optimization that eliminates unnecessary copy and move constructor calls", "Copying objects faster", "Garbage collection", "Memory alignment", "Compiler optimization that eliminates unnecessary copy and move constructor calls"));
                questions.add(new Question("C++", "Hard", "What is undefined behavior in C++?", "Behavior for which standard imposes no requirements, leading to unpredictable results", "Compile error", "Logical bug", "Warning", "Behavior for which standard imposes no requirements, leading to unpredictable results"));
                questions.add(new Question("C++", "Hard", "What is the difference between std::shared_ptr and std::weak_ptr?", "weak_ptr does not increase reference count and prevents cyclic dependencies", "weak_ptr owns object exclusively", "shared_ptr is unsafe", "No difference", "weak_ptr does not increase reference count and prevents cyclic dependencies"));
                questions.add(new Question("C++", "Hard", "What are user-defined literals in C++11?", "Custom suffixes for literals like 12_km", "String constants", "Macro definitions", "Type aliases", "Custom suffixes for literals like 12_km"));
                questions.add(new Question("C++", "Hard", "What is type punning and why is it dangerous in C++?", "Accessing object of one type through pointer of another type, violating strict aliasing rule", "Casting int to float safely", "Printing variable types", "Template casting", "Accessing object of one type through pointer of another type, violating strict aliasing rule"));
                questions.add(new Question("C++", "Hard", "What is a lambda expression in C++11?", "Anonymous function object capable of capturing variables in scope", "Named function", "Inline macro", "Virtual method", "Anonymous function object capable of capturing variables in scope"));
                questions.add(new Question("C++", "Hard", "What is the purpose of std::initializer_list?", "Enables list initialization syntax like {1, 2, 3} for containers and functions", "Array sorting", "Vector allocation", "Loop construct", "Enables list initialization syntax like {1, 2, 3} for containers and functions"));
                questions.add(new Question("C++", "Hard", "What is thread safety in C++11 standard memory model?", "Guaranteed atomic operations and synchronization primitives like std::mutex and std::atomic", "Automatic thread creation", "No thread support", "Single thread enforcement", "Guaranteed atomic operations and synchronization primitives like std::mutex and std::atomic"));
                questions.add(new Question("C++", "Hard", "What is coroutine support introduced in C++20?", "Functions that can be suspended and resumed using co_await, co_yield, and co_return", "Standard threads", "Async IO library", "Lambda feature", "Functions that can be suspended and resumed using co_await, co_yield, and co_return"));
                questions.add(new Question("C++", "Hard", "What are concepts in C++20?", "Named constraints on template type arguments that constrain template metaprogramming", "Class design patterns", "Namespaces", "Virtual interfaces", "Named constraints on template type arguments that constrain template metaprogramming"));
                questions.add(new Question("C++", "Hard", "What is static assertion (static_assert)?", "Compile-time assertion check", "Runtime assertion", "Debug macro", "Exception check", "Compile-time assertion check"));
            } else {
                questions.add(new Question("C++", "Medium", "Which operator is used to allocate memory dynamically in C++?", "malloc", "alloc", "new", "create", "new"));
                questions.add(new Question("C++", "Medium", "Which concept allows the same function name to have different implementations?", "Encapsulation", "Polymorphism", "Abstraction", "Inheritance", "Polymorphism"));
                questions.add(new Question("C++", "Medium", "What is the default access specifier for members of a class in C++?", "public", "protected", "private", "internal", "private"));
                questions.add(new Question("C++", "Medium", "Which header file is required for cout and cin?", "<stdio.h>", "<iostream>", "<conio.h>", "<fstream>", "<iostream>"));
                questions.add(new Question("C++", "Medium", "What does STL stand for in C++?", "Standard Type Library", "Standard Template Library", "Simple Template Logic", "System Type Library", "Standard Template Library"));
                questions.add(new Question("C++", "Medium", "Which symbol is used for destructor in C++?", "!", "~", "#", "&", "~"));
                questions.add(new Question("C++", "Medium", "Which operator cannot be overloaded in C++?", "+", "==", "::", "[]", "::"));
                questions.add(new Question("C++", "Medium", "What is a pure virtual function?", "A function with no body assigned = 0", "A static function", "A friend function", "An inline function", "A function with no body assigned = 0"));
                questions.add(new Question("C++", "Medium", "Which keyword is used to handle exceptions in C++?", "try-catch", "error-handle", "throw-trap", "check-except", "try-catch"));
                questions.add(new Question("C++", "Medium", "Which pointer holds the address of the current object?", "super", "self", "this", "current", "this"));
                questions.add(new Question("C++", "Medium", "Which of the following is a correct file extension for C++ files?", ".c", ".cpp", ".cp", ".cs", ".cpp"));
                questions.add(new Question("C++", "Medium", "What is method overloading an example of?", "Compile-time polymorphism", "Run-time polymorphism", "Inheritance", "Encapsulation", "Compile-time polymorphism"));
                questions.add(new Question("C++", "Medium", "Which feature of OOP indicates code reusability?", "Polymorphism", "Inheritance", "Abstraction", "Encapsulation", "Inheritance"));
                questions.add(new Question("C++", "Medium", "What is inline function in C++?", "Function expanded in line when called", "Recursive function", "Virtual function", "Friend function", "Function expanded in line when called"));
                questions.add(new Question("C++", "Medium", "Which casting operator is introduced in modern C++?", "static_cast", "dynamic_cast", "reinterpret_cast", "All of the above", "All of the above"));
                questions.add(new Question("C++", "Medium", "What is a friend function in C++?", "A function that can access private/protected members", "A member function", "A virtual function", "A constructor", "A function that can access private/protected members"));
                questions.add(new Question("C++", "Medium", "What is the size of boolean data type in C++?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
                questions.add(new Question("C++", "Medium", "Which loop is best when number of iterations is known?", "while", "do-while", "for", "None", "for"));
                questions.add(new Question("C++", "Medium", "What is constructor?", "Special function called when object is created", "Function to delete object", "Virtual method", "Static method", "Special function called when object is created"));
                questions.add(new Question("C++", "Medium", "Which container in STL is implemented as double-ended queue?", "vector", "list", "deque", "stack", "deque"));
            }
        } else if ("C Lang".equalsIgnoreCase(subject)) {
            if ("easy".equals(diff)) {
                questions.add(new Question("C Lang", "Easy", "Who is known as the father of C language?", "James Gosling", "Dennis Ritchie", "Bjarne Stroustrup", "Ken Thompson", "Dennis Ritchie"));
                questions.add(new Question("C Lang", "Easy", "In which year was C language developed?", "1970", "1972", "1980", "1985", "1972"));
                questions.add(new Question("C Lang", "Easy", "Which header file is essential for printf() and scanf()?", "<stdlib.h>", "<string.h>", "<stdio.h>", "<math.h>", "<stdio.h>"));
                questions.add(new Question("C Lang", "Easy", "What is the extension of C language source files?", ".cpp", ".c", ".java", ".exe", ".c"));
                questions.add(new Question("C Lang", "Easy", "What is the format specifier for printing an integer in C?", "%c", "%d", "%f", "%s", "%d"));
                questions.add(new Question("C Lang", "Easy", "What is the format specifier for printing a float value in C?", "%d", "%c", "%f", "%s", "%f"));
                questions.add(new Question("C Lang", "Easy", "What is the size of char data type in C?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
                questions.add(new Question("C Lang", "Easy", "Which symbol is used for addressing or getting memory address of a variable?", "*", "&", "#", "@", "&"));
                questions.add(new Question("C Lang", "Easy", "Which symbol is used for pointer declaration or dereferencing in C?", "&", "*", "->", ".", "*"));
                questions.add(new Question("C Lang", "Easy", "What is the index of the first element in a C array?", "1", "0", "-1", "Depends on OS", "0"));
                questions.add(new Question("C Lang", "Easy", "Which keyword is used to define a structure in C?", "class", "struct", "union", "record", "struct"));
                questions.add(new Question("C Lang", "Easy", "Which function is used to allocate memory dynamically in C?", "new", "malloc()", "alloc()", "create()", "malloc()"));
                questions.add(new Question("C Lang", "Easy", "Which function is used to release allocated memory in C?", "free()", "delete()", "dealloc()", "clear()", "free()"));
                questions.add(new Question("C Lang", "Easy", "What does NULL pointer point to?", "Garbage value", "Memory location 0", "Stack top", "Heap base", "Memory location 0"));
                questions.add(new Question("C Lang", "Easy", "Which loop runs at least once in C?", "for", "while", "do-while", "None", "do-while"));
                questions.add(new Question("C Lang", "Easy", "What is the default return type of functions in C if not specified?", "void", "int", "float", "double", "int"));
                questions.add(new Question("C Lang", "Easy", "Which keyword is used to prevent modification of a variable value in C?", "const", "static", "volatile", "immutable", "const"));
                questions.add(new Question("C Lang", "Easy", "Which operator is used to access structure members using a pointer in C?", ".", "->", "*", "&", "->"));
                questions.add(new Question("C Lang", "Easy", "What is recursion in C?", "Looping structure", "Function calling itself", "Pointer arithmetic", "File handling", "Function calling itself"));
                questions.add(new Question("C Lang", "Easy", "Which function opens a file in C?", "open()", "fopen()", "file_open()", "newfile()", "fopen()"));
            } else if ("hard".equals(diff)) {
                questions.add(new Question("C Lang", "Hard", "What is undefined behavior regarding sequence points in C?", "Evaluating a variable twice in same expression with side effects", "Syntax error", "Memory leak", "Linker error", "Evaluating a variable twice in same expression with side effects"));
                questions.add(new Question("C Lang", "Hard", "What is the strict aliasing rule in C?", "Compiler assumption that pointers of different types do not point to same memory location", "Pointer casting rule", "Struct padding rule", "Array bounds rule", "Compiler assumption that pointers of different types do not point to same memory location"));
                questions.add(new Question("C Lang", "Hard", "What is structure padding in C?", "Compiler inserting unused bytes between struct members to align data for CPU architecture", "Data compression", "Memory corruption", "Dynamic allocation", "Compiler inserting unused bytes between struct members to align data for CPU architecture"));
                questions.add(new Question("C Lang", "Hard", "How can you prevent structure padding in C?", "Using #pragma pack or __attribute__((packed))", "Using const", "Using pointers", "Using volatile", "Using #pragma pack or __attribute__((packed))"));
                questions.add(new Question("C Lang", "Hard", "What is the difference between malloc and calloc?", "malloc initializes memory with garbage values, calloc initializes with zero", "malloc is faster", "calloc is for strings only", "No difference", "malloc initializes memory with garbage values, calloc initializes with zero"));
                questions.add(new Question("C Lang", "Hard", "What is a function pointer in C?", "Pointer variable that stores the memory address of a function", "Pointer to integer", "Callback variable", "Macro", "Pointer variable that stores the memory address of a function"));
                questions.add(new Question("C Lang", "Hard", "What is volatile keyword used for in C?", "Tells compiler not to optimize variable because its value can change asynchronously", "Makes variable constant", "Makes variable global", "Thread synchronization", "Tells compiler not to optimize variable because its value can change asynchronously"));
                questions.add(new Question("C Lang", "Hard", "What is setjmp and longjmp in C?", "Mechanism for non-local jumps across stack frames", "Loop constructs", "File operations", "Thread control", "Mechanism for non-local jumps across stack frames"));
                questions.add(new Question("C Lang", "Hard", "What is the significance of extern keyword?", "Declares a global variable or function defined in another source file", "Local variable", "Static variable", "Register variable", "Declares a global variable or function defined in another source file"));
                questions.add(new Question("C Lang", "Hard", "What is a flexible array member in C99 structs?", "Array with unspecified size as the last member of a struct", "Dynamic array pointer", "Fixed array", "Pointer array", "Array with unspecified size as the last member of a struct"));
                questions.add(new Question("C Lang", "Hard", "What is integer promotion in C expression evaluation?", "Converting smaller integer types (char, short) to int or unsigned int during expression evaluation", "Overflow check", "Type casting", "Multiplying integers", "Converting smaller integer types (char, short) to int or unsigned int during expression evaluation"));
                questions.add(new Question("C Lang", "Hard", "What is the difference between macro (#define) and inline function?", "Macros do no type checking and text replacement, inline functions do type checking and compiler optimization", "Macros are slower", "Inline is preprocessor directive", "No difference", "Macros do no type checking and text replacement, inline functions do type checking and compiler optimization"));
                questions.add(new Question("C Lang", "Hard", "What is a dangling pointer?", "Pointer pointing to a memory location that has been freed or deallocated", "Null pointer", "Uninitialized pointer", "Wild pointer", "Pointer pointing to a memory location that has been freed or deallocated"));
                questions.add(new Question("C Lang", "Hard", "What is a memory leak in C?", "Allocating dynamic memory using malloc/calloc and failing to free it", "Stack overflow", "Buffer overflow", "Segmentation fault", "Allocating dynamic memory using malloc/calloc and failing to free it"));
                questions.add(new Question("C Lang", "Hard", "What causes a segmentation fault (SIGSEGV)?", "Attempting to access CPU restricted memory or dereferencing null/invalid pointer", "Arithmetic overflow", "Infinite loop", "File read error", "Attempting to access CPU restricted memory or dereferencing null/invalid pointer"));
                questions.add(new Question("C Lang", "Hard", "What is the role of the linker in C compilation?", "Combines object files and resolves external references to produce executable binary", "Translates C to assembly", "Preprocesses headers", "Syntax checking", "Combines object files and resolves external references to produce executable binary"));
                questions.add(new Question("C Lang", "Hard", "What are bit fields in C structures?", "Allowing specification of exact bit widths for struct members to save memory", "Bitwise operators", "Byte arrays", "Pointers", "Allowing specification of exact bit widths for struct members to save memory"));
                questions.add(new Question("C Lang", "Hard", "What is the comma operator in C?", "Evaluates operands sequentially and returns value of rightmost operand", "Separates function parameters", "Logical AND", "Bitwise OR", "Evaluates operands sequentially and returns value of rightmost operand"));
                questions.add(new Question("C Lang", "Hard", "What is realloc and how does it behave?", "Resizes previously allocated memory block, preserving existing content if possible", "Allocates new block without copying", "Frees memory", "Duplicates memory", "Resizes previously allocated memory block, preserving existing content if possible"));
                questions.add(new Question("C Lang", "Hard", "What is restricted pointer (restrict keyword in C99)?", "Optimization hint telling compiler that pointer is sole means of accessing object", "Read-only pointer", "Null-terminated pointer", "Const pointer", "Optimization hint telling compiler that pointer is sole means of accessing object"));
            } else {
                questions.add(new Question("C Lang", "Medium", "Who is the father of C language?", "James Gosling", "Dennis Ritchie", "Bjarne Stroustrup", "Ken Thompson", "Dennis Ritchie"));
                questions.add(new Question("C Lang", "Medium", "Which format specifier is used for printing an integer in C?", "%c", "%d", "%f", "%s", "%d"));
                questions.add(new Question("C Lang", "Medium", "Which function is used to allocate memory dynamically in C?", "new", "malloc()", "create()", "alloc()", "malloc()"));
                questions.add(new Question("C Lang", "Medium", "What is the size of char data type in C?", "1 byte", "2 bytes", "4 bytes", "8 bytes", "1 byte"));
                questions.add(new Question("C Lang", "Medium", "Which symbol is used to get the address of a variable?", "*", "&", "#", "@", "&"));
                questions.add(new Question("C Lang", "Medium", "Which loop is guaranteed to execute at least once?", "for", "while", "do-while", "None", "do-while"));
                questions.add(new Question("C Lang", "Medium", "What is the index of the first element in a C array?", "0", "1", "-1", "Depends on compiler", "0"));
                questions.add(new Question("C Lang", "Medium", "Which header file contains printf() and scanf()?", "<stdlib.h>", "<string.h>", "<stdio.h>", "<math.h>", "<stdio.h>"));
                questions.add(new Question("C Lang", "Medium", "What does NULL pointer point to?", "Garbage value", "Memory location 0", "Stack top", "Heap base", "Memory location 0"));
                questions.add(new Question("C Lang", "Medium", "Which keyword is used to define a structure in C?", "class", "struct", "union", "record", "struct"));
                questions.add(new Question("C Lang", "Medium", "What is recursion in C?", "Function calling itself", "Looping structure", "Pointer arithmetic", "File handling", "Function calling itself"));
                questions.add(new Question("C Lang", "Medium", "Which function releases allocated memory in C?", "free()", "delete()", "dealloc()", "clear()", "free()"));
                questions.add(new Question("C Lang", "Medium", "What is the return type of getchar() function?", "int", "char", "void", "float", "int"));
                questions.add(new Question("C Lang", "Medium", "Which operator is used to access members of a structure using pointer?", ".", "->", "*", "&", "->"));
                questions.add(new Question("C Lang", "Medium", "What is the default return type of functions in C if not specified?", "void", "int", "float", "double", "int"));
                questions.add(new Question("C Lang", "Medium", "Which storage class has local scope and lifetime till program ends?", "auto", "static", "register", "extern", "static"));
                questions.add(new Question("C Lang", "Medium", "What is the maximum value of unsigned char in C?", "127", "255", "32767", "65535", "255"));
                questions.add(new Question("C Lang", "Medium", "Which keyword prevents modification of variable value in C?", "const", "static", "volatile", "immutable", "const"));
                questions.add(new Question("C Lang", "Medium", "What is a macro in C?", "A fragment of code given a symbolic name", "A function", "A pointer", "A variable", "A fragment of code given a symbolic name"));
                questions.add(new Question("C Lang", "Medium", "Which function opens a file in C?", "open()", "fopen()", "file_open()", "newfile()", "fopen()"));
            }
        }
        
        return questions;
    }

    private List getQuestionsBySubjectAndDifficulty(String subject, String difficulty) {
        List questions = null;
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

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/startQuiz")
    public String startQuiz(@RequestParam(defaultValue = "Java") String subject, 
                            @RequestParam(defaultValue = "Medium") String difficulty, 
                            Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("questions", getQuestionsBySubjectAndDifficulty(subject, difficulty));
        return "quiz";
    }

    @GetMapping("/quiz")
    public String quizPage(@RequestParam(defaultValue = "Java") String subject, 
                           @RequestParam(defaultValue = "Medium") String difficulty, 
                           Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("questions", getQuestionsBySubjectAndDifficulty(subject, difficulty));
        return "quiz";
    }

    @GetMapping({"/quiz/search", "/quiz/filter"})
    public String quizSearchGet(@RequestParam(defaultValue = "Java") String subject, 
                                @RequestParam(defaultValue = "Medium") String difficulty, 
                                Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("questions", getQuestionsBySubjectAndDifficulty(subject, difficulty));
        return "quiz";
    }

    @PostMapping({"/quiz/search", "/quiz/filter", "/quiz"})
    public String quizSearchPost(@RequestParam(defaultValue = "Java") String subject, 
                                 @RequestParam(defaultValue = "Medium") String difficulty, 
                                 Model model) {
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("questions", getQuestionsBySubjectAndDifficulty(subject, difficulty));
        return "quiz";
    }

    @PostMapping("/submitQuiz")
    public String submitQuiz(@RequestParam Map<String, String> allParams,
                             @RequestParam(value = "subject", defaultValue = "Java") String subject,
                             @RequestParam(value = "difficulty", defaultValue = "Medium") String difficulty,
                             Model model) {
        
        List questions = getQuestionsBySubjectAndDifficulty(subject, difficulty);
        int score = 0;
        int total = questions.size();
        int attempted = 0;

        Map<String, String> userAnswersMap = new HashMap<>();

        for (int i = 0; i < total; i++) {
            Question q = (Question) questions.get(i);
            String userSelectedAnswer = allParams.get("q_" + i);
            
            boolean isAttempted = (userSelectedAnswer != null && !userSelectedAnswer.trim().isEmpty());
            if (isAttempted) {
                attempted++;
                userAnswersMap.put("q_" + i, userSelectedAnswer.trim());
            }

            String correctAnswer = (q.getCorrectAnswer() != null) ? q.getCorrectAnswer().trim() : "";
            String selectedAnswer = isAttempted ? userSelectedAnswer.trim() : "";

            System.out.println("Question " + (i+1) + " -> User Selected: '" + selectedAnswer + "' | Correct Answer: '" + correctAnswer + "'");

            if (isAttempted && selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
            }
        }

        int unattempted = total - attempted;
        int percentage = (total > 0) ? (score * 100) / total : 0;
        
        String badge = "Practice Learner 🌱";
        if (percentage >= 80) badge = "Expert Scholar 🏆";
        else if (percentage >= 50) badge = "Proficient Achiever 🥈";

        latestQuestions = questions;
        latestUserAnswers = userAnswersMap;
        latestSubject = subject;
        latestScore = score;
        latestTotal = total;

        model.addAttribute("score", score);
        model.addAttribute("total", total);
        model.addAttribute("subject", subject);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("percentage", percentage);
        model.addAttribute("attempted", attempted);
        model.addAttribute("unattempted", unattempted);
        model.addAttribute("badge", badge);
        
        return "result"; 
    }

    @PostMapping("/saveScore")
    public String saveScore(@RequestParam("playerName") String playerName,
                            @RequestParam(defaultValue = "0") int score,
                            @RequestParam(defaultValue = "20") int total,
                            @RequestParam(defaultValue = "Java") String subject) {
        leaderboard.add(new LeaderboardEntry(playerName, score, total, subject));
        return "redirect:/leaderboard";
    }

    @GetMapping("/review")
    public String reviewPage(Model model) {
        model.addAttribute("questions", latestQuestions);
        model.addAttribute("userAnswers", latestUserAnswers);
        model.addAttribute("subject", latestSubject);
        return "review";
    }

    @GetMapping("/result")
    public String resultPage(Model model) {
        int percentage = (latestTotal > 0) ? (latestScore * 100) / latestTotal : 0;
        String badge = "Practice Learner 🌱";
        if (percentage >= 80) badge = "Expert Scholar 🏆";
        else if (percentage >= 50) badge = "Proficient Achiever 🥈";

        model.addAttribute("score", latestScore);
        model.addAttribute("total", latestTotal);
        model.addAttribute("subject", latestSubject);
        model.addAttribute("percentage", percentage);
        model.addAttribute("attempted", latestScore);
        model.addAttribute("unattempted", latestTotal - latestScore);
        model.addAttribute("badge", badge);
        return "result";
    }

    @GetMapping("/leaderboard")
    public String leaderboardPage(Model model) {
        model.addAttribute("leaderboard", leaderboard);
        return "leaderboard";
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

        ArrayList uploadedFiles = new ArrayList();
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

    @GetMapping("/api/ai-chat")
    @ResponseBody
    public String getAiChatResponse(@RequestParam String subject, @RequestParam String prompt) {
        String cleanTopic = prompt.trim();
        StringBuilder sb = new StringBuilder();
        
        sb.append("<div id='aiGeneratedContent' class='p-5 bg-dark border border-secondary rounded-4 shadow-lg text-light' style='line-height: 1.9; font-size: 1.05rem;'>");
        
        sb.append("<div class='d-flex justify-content-between align-items-center border-bottom border-secondary pb-3 mb-4'>");
        sb.append("<div><span class='badge bg-warning text-dark mb-2 px-3 py-2 fw-bold'>✨ Comprehensive AI Masterclass & Research Engine</span>");
        sb.append("<h2 class='text-info fw-bold mb-0 mt-1'>Deep Dive Study Guide: ").append(cleanTopic).append(" (").append(subject).append(")</h2></div>");
        sb.append("<button type='button' onclick='speakAiResponse()' class='btn btn-outline-info btn-sm rounded-pill px-4 py-2'><i class='fas fa-volume-up me-2'></i> Listen Audio</button>");
        sb.append("</div>");

        sb.append("<h4 class='text-warning fw-bold mt-4'><i class='fas fa-landmark me-2'></i> 1. Executive Summary & Core Architectural Paradigm</h4>");
        sb.append("<p>Welcome to the comprehensive masterclass on <strong>").append(cleanTopic).append("</strong> within the ecosystem of <strong>").append(subject).append("</strong>. In modern enterprise software development, mastering this concept is critical for building scalable, high-performance, and resilient applications. At its core, ").append(cleanTopic).append(" provides a structured abstraction layer that bridges high-level business logic with low-level system execution, memory management, and data persistence.</p>");
        sb.append("<p>Architecturally, applications utilizing ").append(cleanTopic).append(" follow strict design patterns to isolate responsibilities, decouple dependencies, and maintain thread safety across distributed worker nodes or concurrent execution threads.</p>");

        sb.append("<h4 class='text-warning fw-bold mt-4'><i class='fas fa-cogs me-2'></i> 2. Internal Mechanics & Execution Lifecycle</h4>");
        sb.append("<p>Understanding the internal lifecycle and state transitions of ").append(cleanTopic).append(" involves analyzing several key phases:</p>");
        sb.append("<ul>");
        sb.append("<li class='mb-2'><strong>Initialization & Configuration:</strong> How parameters are injected, environment context is bound, and system components are instantiated during startup.</li>");
        sb.append("<li class='mb-2'><strong>Runtime Execution & Data Flow:</strong> Managing transactional boundaries, request-response payloads, and state mutations efficiently without blocking CPU threads.</li>");
        sb.append("<li class='mb-2'><strong>Resource Cleanup & Garbage Collection:</strong> Ensuring proper closure of network streams, database connections, and memory buffers to prevent resource leaks under heavy load.</li>");
        sb.append("</ul>");

        sb.append("<h4 class='text-warning fw-bold mt-4'><i class='fas fa-code me-2'></i> 3. Advanced Implementation Blueprint</h4>");
        sb.append("<p>Below is a production-grade code pattern demonstrating how ").append(cleanTopic).append(" is implemented in enterprise environments:</p>");
        sb.append("<div class='p-3 bg-black rounded border border-secondary font-monospace text-success small mb-3 shadow-sm' style='overflow-x: auto;'>");
        sb.append("<code>// Production-Grade Implementation Blueprint for ").append(cleanTopic).append("<br>");
        sb.append("package org.springboot.masterclass;<br><br>");
        sb.append("public class ").append(cleanTopic.replaceAll("[^a-zA-Z0-9]", "")).append("Engine {<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;private final String moduleName = \"").append(cleanTopic).append("\";<br><br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;public void executeWorkflow() {<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(\"[INFO] Initializing module: \" + moduleName);<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;// Core business logic execution & exception handling<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;try {<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;processCoreOperations();<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;} catch (Exception e) {<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.err.println(\"[ERROR] Execution failed: \" + e.getMessage());<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;}<br><br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;private void processCoreOperations() {<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(\"[SUCCESS] ").append(cleanTopic).append(" pipeline executed successfully.\");<br>");
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;}<br>");
        sb.append("}</code>");
        sb.append("</div>");

        sb.append("<h4 class='text-warning fw-bold mt-4'><i class='fas fa-shield-alt me-2'></i> 4. Enterprise Best Practices & Anti-Patterns</h4>");
        sb.append("<p>When deploying ").append(cleanTopic).append(" in production systems, engineers should adhere to these industry standards:</p>");
        sb.append("<ul>");
        sb.append("<li class='mb-2'><strong>Avoid Tight Coupling:</strong> Always use interfaces and dependency injection containers rather than hardcoding concrete instantiations.</li>");
        sb.append("<li class='mb-2'><strong>Robust Exception Handling:</strong> Never swallow caught exceptions silently; log them with appropriate context or wrap them in custom runtime exceptions.</li>");
        sb.append("<li class='mb-2'><strong>Concurrency Safety:</strong> Ensure shared state variables are marked volatile or guarded using synchronization blocks to avoid race conditions.</li>");
        sb.append("</ul>");

        sb.append("<h4 class='text-warning fw-bold mt-4'><i class='fas fa-external-link-alt me-2'></i> 5. Official Documentation & Verified References</h4>");
        sb.append("<p class='text-light'>Explore these curated external references, documentation portals, and research repositories to deepen your expertise on <strong>").append(cleanTopic).append("</strong>:</p>");
        
        sb.append("<div class='list-group bg-transparent mb-3'>");
        
        sb.append("<a href='https://docs.oracle.com/en/' target='_blank' class='list-group-item list-group-item-action bg-secondary text-light border-dark mb-2 rounded'>");
        sb.append("<i class='fas fa-book text-warning me-2'></i> <strong>Official Oracle Specification Guide</strong> — Comprehensive reference manual for core architecture and specifications.");
        sb.append("</a>");

        sb.append("<a href='https://spring.io/guides' target='_blank' class='list-group-item list-group-item-action bg-secondary text-light border-dark mb-2 rounded'>");
        sb.append("<i class='fas fa-leaf text-success me-2'></i> <strong>Spring Framework Official Guides</strong> — Best practices, integration patterns, and enterprise blueprints.");
        sb.append("</a>");

        sb.append("<a href='https://github.com/trending' target='_blank' class='list-group-item list-group-item-action bg-secondary text-light border-dark mb-2 rounded'>");
        sb.append("<i class='fab fa-github text-info me-2'></i> <strong>Open Source GitHub Repositories</strong> — Real-world production code implementations and community modules.");
        sb.append("</a>");

        sb.append("<a href='https://stackoverflow.com/questions' target='_blank' class='list-group-item list-group-item-action bg-secondary text-light border-dark mb-2 rounded'>");
        sb.append("<i class='fab fa-stack-overflow text-danger me-2'></i> <strong>StackOverflow Expert Discussions</strong> — Troubleshooting common runtime exceptions, edge cases, and performance tunings.");
        sb.append("</a>");

        sb.append("<a href='https://www.baeldung.com/' target='_blank' class='list-group-item list-group-item-action bg-secondary text-light border-dark mb-2 rounded'>");
        sb.append("<i class='fas fa-graduation-cap text-warning me-2'></i> <strong>Baeldung Technical Tutorials</strong> — In-depth architectural articles and step-by-step developer guides.");
        sb.append("</a>");

        sb.append("</div>");
        sb.append("</div>");
        
        return sb.toString();
    }

    @GetMapping("/api/quiz-reference")
    @ResponseBody
    public String getQuizReference(@RequestParam String topic) {
        return "<div class='p-3 bg-secondary rounded text-light'>" +
               "<h5>📚 Verified Reference & Documentation</h5>" +
               "<p>Explore official specifications and deep architectural patterns for <strong>" + topic + "</strong> to master interview-level concepts.</p>" +
               "<a href='https://docs.oracle.com/en/java/' target='_blank' class='btn btn-sm btn-outline-light'>Official Docs Reference</a>" +
               "</div>";
    }
}