package fis.example.eventmatch.suggestion.model;

public class Suggestion {

    private int score;

    private String event;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "score=" + score +
                ", event='" + event + '\'' +
                '}';
    }
}
