package fis.example.eventmatch.calendar.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Calendar {

    @JsonProperty
    List<Entry> entryList = new ArrayList<Entry>();

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "entryList=" + entryList +
                '}';
    }

}
