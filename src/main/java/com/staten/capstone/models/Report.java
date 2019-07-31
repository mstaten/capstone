package com.staten.capstone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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

    private ZonedDateTime zonedDateTime;

    private ZonedDateTime lastEdit;

    private Integer urgency;

    public Report() {
        this.zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Report(String title, String description) {
        this.zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        this.title = title;
        this.description = description;
    }

    public Report(String title, String description, User user) {
        this.zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Report(User user) {
        this.zonedDateTime = ZonedDateTime.now(ZoneId.systemDefault());
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

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
    }

    public String getFormattedZonedDateTime() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(zonedDateTime);
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
    }

    public ZonedDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(ZonedDateTime lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getFormattedLastEdit() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(lastEdit);
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
