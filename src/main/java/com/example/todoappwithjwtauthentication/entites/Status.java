package com.example.todoappwithjwtauthentication.entites;

import com.example.todoappwithjwtauthentication.enums.EStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusId;
    @Enumerated(EnumType.STRING)
    private EStatus eStatus;

    public Status() {
    }

    public Status(Integer statusId, EStatus eStatus) {
        this.statusId = statusId;
        this.eStatus = eStatus;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public EStatus geteStatus() {
        return eStatus;
    }

    public void seteStatus(EStatus eStatus) {
        this.eStatus = eStatus;
    }
}
