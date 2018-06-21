package fis.example.eventmatch.suggestion.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {

    @JsonProperty
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "userId='" + userId + '\'' +
                '}';
    }

}
