package org.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotesController {

    @GetMapping("/notes")
    public String showNotes(@RequestParam(defaultValue = "Java") String subject,
                            @RequestParam(required = false, defaultValue = "") String query,
                            Model model) {
        
        String notesResult = fetchSmartNotes(subject, query);

        model.addAttribute("subject", subject);
        model.addAttribute("query", query);
        model.addAttribute("notesResult", notesResult);
        
        return "notes";
    }

    private String fetchSmartNotes(String subject, String query) {
        StringBuilder sb = new StringBuilder();

        if (query != null && !query.trim().isEmpty()) {
            sb.append("<div class='alert alert-info'>🔍 <strong>Search Result for:</strong> '")
              .append(query).append("' in ").append(subject).append("</div>");
        }

        if ("Python".equalsIgnoreCase(subject)) {
            sb.append("<h5>🐍 Python Core Reference & Notes</h5>")
              .append("<p><strong>Overview:</strong> High-level interpreted language focusing on code readability.</p>")
              .append("<ul>")
              .append("<li><a href='https://www.geeksforgeeks.org/python-programming-language/' target='_blank'>GeeksforGeeks: Python Programming Guide 🔗</a></li>")
              .append("<li><a href='https://docs.python.org/3/' target='_blank'>Official Python Documentation 🔗</a></li>")
              .append("</ul>")
              .append("<hr><p><strong>Key Concepts:</strong> Dynamic typing, list comprehensions, lambda functions, and exception handling blocks.</p>");
        } else if ("DBMS".equalsIgnoreCase(subject)) {
            sb.append("<h5>🗄️ DBMS & SQL Reference Notes</h5>")
              .append("<p><strong>Overview:</strong> Structured storage, ACID properties, and relational algebra.</p>")
              .append("<ul>")
              .append("<li><a href='https://www.geeksforgeeks.org/dbms/' target='_blank'>GeeksforGeeks: DBMS Tutorial 🔗</a></li>")
              .append("<li><a href='https://www.javatpoint.com/dbms-tutorial' target='_blank'>Javatpoint: SQL & Normalization 🔗</a></li>")
              .append("</ul>")
              .append("<hr><p><strong>Key Concepts:</strong> Primary/Foreign keys, 1NF to 3NF normalization, and complex JOIN operations.</p>");
        } else {
            sb.append("<h5>☕ Java Enterprise Reference Notes</h5>")
              .append("<p><strong>Overview:</strong> Object-oriented, platform-independent language powered by JVM.</p>")
              .append("<ul>")
              .append("<li><a href='https://www.geeksforgeeks.org/java-programming-language/' target='_blank'>GeeksforGeeks: Java Programming Tutorial 🔗</a></li>")
              .append("<li><a href='https://docs.oracle.com/en/java/' target='_blank'>Oracle Official Java Documentation 🔗</a></li>")
              .append("</ul>")
              .append("<hr><p><strong>Key Concepts:</strong> JVM architecture, Spring Boot beans, Collections framework (List, Map, Set), and multithreading.</p>");
        }

        return sb.toString();
    }
}