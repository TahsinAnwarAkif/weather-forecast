package com.oddle.app.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

import static com.oddle.app.weather.util.Constants.ICON_PATTERN;

/**
 * @author akif
 * @since 1/13/22
 */
@Entity
@Table(name = "city_weather_summary")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 1, max = 255)
    @Column(nullable = false, updatable = false)
    @JsonAlias("main")
    private String summary;

    @Size(min = 1, max = 1024)
    @Column(nullable = false, updatable = false)
    private String description;

    @Size(min = 7, max = 7)
    @Pattern(regexp = ICON_PATTERN)
    @Column(nullable = false, updatable = false)
    private String icon;

    public WeatherSummary() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherSummary)) {
            return false;
        }

        WeatherSummary that = (WeatherSummary) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == 0L;
    }
}
