package com.staten.capstone.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Location {

    @Id
    @GeneratedValue
    private Integer id;

    private String latLng;

    @OneToOne(mappedBy = "location")
    private Report report;

    /* Constructors */
    public Location() {}

    public Location(String latLng) {
        this.latLng = latLng;
    }

    public Location(String latLng, Report report) {
        this.latLng = latLng;
        this.report = report;
    }

    /* Getters and Setters */
    public Integer getId() {
        return id;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
