package org.springboot;

public class QuizResult {
    private int totalQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private double percentage;
    private String performanceBadge;

    public QuizResult(int totalQuestions, int correctAnswers, int incorrectAnswers, double percentage, String performanceBadge) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.incorrectAnswers = incorrectAnswers;
        this.percentage = percentage;
        this.performanceBadge = performanceBadge;
    }

    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getIncorrectAnswers() { return incorrectAnswers; }
    public double getPercentage() { return percentage; }
    public String getPerformanceBadge() { return performanceBadge; }
}