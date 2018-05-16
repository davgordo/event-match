package fis.example.eventmatch.calendar.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;

public class Entry {

    @JsonProperty
    private boolean available;

    @JsonProperty
    private DateTime start;

    @JsonProperty
    private DateTime end;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
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
