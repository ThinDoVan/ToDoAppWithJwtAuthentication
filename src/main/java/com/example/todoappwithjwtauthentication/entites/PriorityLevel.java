package com.example.todoappwithjwtauthentication.entites;

import com.example.todoappwithjwtauthentication.enums.EPriorityLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "prioritylevels")
public class PriorityLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer priorityLevelId;

    @Enumerated(EnumType.STRING)
    private EPriorityLevel ePriorityLevel;

    public PriorityLevel() {
    }

    public PriorityLevel(EPriorityLevel ePriorityLevel) {
        this.ePriorityLevel = ePriorityLevel;
    }

    public Integer getPriorityLevelId() {
        return priorityLevelId;
    }

    public void setPriorityLevelId(Integer priorityLevelId) {
        this.priorityLevelId = priorityLevelId;
    }

    public EPriorityLevel getePriorityLevel() {
        return ePriorityLevel;
    }

    public void setePriorityLevel(EPriorityLevel ePriorityLevel) {
        this.ePriorityLevel = ePriorityLevel;
    }
}
