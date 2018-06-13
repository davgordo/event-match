package fis.example.eventmatch.suggestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Entry {

    @JsonProperty
    private boolean available;

    @JsonProperty
    private Date start;

    @JsonProperty
    private Date end;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "available=" + available +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}
