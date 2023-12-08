package com.example.todoappwithjwtauthentication.entites;

import com.example.todoappwithjwtauthentication.enums.ETag;
import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tagId;
    @Enumerated(EnumType.STRING)
    private ETag eTag;

    public Tag() {
    }

    public Tag(Integer tagId, ETag eTag) {
        this.tagId = tagId;
        this.eTag = eTag;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public ETag geteTag() {
        return eTag;
    }

    public void seteTag(ETag eTag) {
        this.eTag = eTag;
    }
}
