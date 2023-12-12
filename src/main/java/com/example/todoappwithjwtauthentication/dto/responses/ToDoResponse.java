package com.example.todoappwithjwtauthentication.dto.responses;

import com.example.todoappwithjwtauthentication.entites.Tag;
import com.example.todoappwithjwtauthentication.entites.User;
import com.example.todoappwithjwtauthentication.enums.EPriorityLevel;
import com.example.todoappwithjwtauthentication.enums.EStatus;
import com.example.todoappwithjwtauthentication.enums.ETag;

import java.time.LocalDate;
import java.util.Set;

public class ToDoResponse {
    private String title;
    private String description;
    private LocalDate deadline;
    private EStatus status;
    private Set<Tag> tagSet;
    private EPriorityLevel priorityLevel;
    private Set<String> userNameSet;

    public ToDoResponse() {
    }

    public ToDoResponse(String title, String description, LocalDate deadline, EStatus status, Set<Tag> tagSet, EPriorityLevel priorityLevel, Set<String> userNameSet) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
        this.tagSet = tagSet;
        this.priorityLevel = priorityLevel;
        this.userNameSet = userNameSet;
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

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public EPriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(EPriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Set<String> getUserNameSet() {
        return userNameSet;
    }

    public void setUserNameSet(Set<String> userNameSet) {
        this.userNameSet = userNameSet;
    }

    @Override
    public String toString() {
        return "ToDoResponse{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", status=" + status +
                ", tagSet=" + tagSet +
                ", priorityLevel=" + priorityLevel +
                ", userNameSet=" + userNameSet +
                '}';
    }
}
