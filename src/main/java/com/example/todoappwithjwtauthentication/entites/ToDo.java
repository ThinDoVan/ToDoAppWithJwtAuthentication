package com.example.todoappwithjwtauthentication.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer toDoId;
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private LocalDate deadline;
    @ManyToOne
    @JoinColumn(name = "priority_level_id")
    private PriorityLevel priorityLevel;
    @ManyToMany
    @JoinTable(name = "todo_user",
            joinColumns = {@JoinColumn(name = "to_do_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> userSet;
    @ManyToMany
    @JoinTable(name = "todo_tag",
            joinColumns = {@JoinColumn(name = "to_do_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tagSet;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public ToDo() {
    }

    public ToDo(String title, String description, LocalDate deadline, Set<User> userSet, Set<Tag> tagSet) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.userSet = userSet;
        this.tagSet = tagSet;
    }

    public Integer getToDoId() {
        return toDoId;
    }

    public void setToDoId(Integer toDoId) {
        this.toDoId = toDoId;
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
        return "ToDo{" +
                "toDoId=" + toDoId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priorityLevel=" + priorityLevel.getePriorityLevel().name() +
                ", userSet=" + userSet +
                ", tagSet=" + tagSet +
                ", status=" + status.geteStatus().name() +
                '}';
    }
}
