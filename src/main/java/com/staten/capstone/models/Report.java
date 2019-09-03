package com.staten.capstone.models;

import javax.persistence.*;
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
    @Size(min = 3, max = 40, message = "Title must be between 3 and 40 characters")
    private String title;

    @NotNull
    @Size(min = 3, message = "Description must be at least 3 characters")
    private String description;

    @ManyToOne
    private User user;

    private ZonedDateTime dateTime;

    private ZonedDateTime lastEdit;

    @NotNull // new req, test
    private Integer urgency;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    public Report() {
        this.dateTime = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Report(String title, String description) {
        this.dateTime = ZonedDateTime.now(ZoneId.systemDefault());
        this.title = title;
        this.description = description;
    }

    public Report(String title, String description, User user) {
        this.dateTime = ZonedDateTime.now(ZoneId.systemDefault());
        this.title = title;
        this.description = description;
        this.user = user;
    }

    public Report(User user) {
        this.dateTime = ZonedDateTime.now(ZoneId.systemDefault());
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
        return dateTime;
    }

    public void setZonedDateTime(ZonedDateTime zonedDateTime) {
        this.dateTime = zonedDateTime;
    }

    public ZonedDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(ZonedDateTime lastEdit) {
        this.lastEdit = lastEdit;
    }

    public Integer getUrgency() {
        return urgency;
    }

    public void setUrgency(Integer urgency) {
        this.urgency = urgency;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /* Other methods */
    public String getFormattedDateTime() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(dateTime);
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
