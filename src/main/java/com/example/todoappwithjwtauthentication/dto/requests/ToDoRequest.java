package com.example.todoappwithjwtauthentication.dto.requests;

import com.example.todoappwithjwtauthentication.entites.PriorityLevel;
import com.example.todoappwithjwtauthentication.entites.Status;
import com.example.todoappwithjwtauthentication.entites.Tag;
import com.example.todoappwithjwtauthentication.entites.User;

import java.time.LocalDate;
import java.util.Set;

public class ToDoRequest {
//    @NotBlank
    private String title;
    private String description;
//    @NotBlank
    private LocalDate deadline;
    private PriorityLevel priorityLevel;
    private Set<User> userSet;
    private Set<Tag> tagSet;
    private Status status;

    public ToDoRequest() {
    }

    public ToDoRequest(String title, String description, LocalDate deadline, PriorityLevel priorityLevel, Set<User> userSet, Set<Tag> tagSet, Status status) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.priorityLevel = priorityLevel;
        this.userSet = userSet;
        this.tagSet = tagSet;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ToDoRequest{" +
                "\n\ttitle: " + title +
                "\n\tdescription: " + description +
                "\n\tdeadline: " + deadline +
                "\n\tpriorityLevel: " + priorityLevel +
                "\n\tuserSet: " + userSet +
                "\n\ttagSet: " + tagSet +
                "\n\tstatus: " + status +
                "\n}";
    }
}
