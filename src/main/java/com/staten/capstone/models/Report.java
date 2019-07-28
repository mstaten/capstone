package com.staten.capstone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Report {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(min = 3, max = 60, message = "Title must be between 3 and 60 characters")
    private String title;

    @NotNull
    @Size(min = 3, message = "Description must be at least 3 characters")
    private String description;

    @ManyToOne
    private User user;

    public Report() {}

    public Report(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Report(String title, String description, User user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Report(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
