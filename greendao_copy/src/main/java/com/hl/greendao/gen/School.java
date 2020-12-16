package com.hl.greendao.gen;

import com.hl.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "SCHOOL".
 */
@Entity
public class School {

    @Id
    private Long id;
    private String name;

    @Generated
    public School() {
    }

    @Generated
    public School(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}