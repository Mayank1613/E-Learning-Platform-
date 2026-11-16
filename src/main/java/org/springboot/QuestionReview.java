package org.springboot;

public class QuestionReview {
    private String questionText;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean correct;

    public QuestionReview(String questionText, String selectedAnswer, String correctAnswer, boolean correct) {
        this.questionText = questionText;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }
}