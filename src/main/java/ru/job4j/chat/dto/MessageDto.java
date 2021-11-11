package ru.job4j.chat.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class MessageDto {
    private int id;

    @NotBlank(message = "Text mustn't be empty")
    private String text;

    private LocalDateTime created;

    @Min(value = 1, message = "Person's id must be more than 0")
    private int personId;

    @Min(value = 1, message = "Room's id must be more than 0")
    private int roomId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
