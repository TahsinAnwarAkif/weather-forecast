package com.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akif
 * @since 1/13/22
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityWeatherData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Valid
    @Embedded
    private Coord coord;

    @Valid
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "city_weather_data_weather_summary",
            joinColumns = @JoinColumn(name = "city_weather_data_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "city_weather_summary_id", nullable = false))
    @OrderColumn(name = "idx", nullable = false)
    @Fetch(FetchMode.SELECT)
    @JsonAlias("weather")
    private List<WeatherSummary> weatherSummaryList;

    @Valid
    @Embedded
    @JsonAlias("main")
    private WeatherDetail weatherDetail;

    @Valid
    @Embedded
    private Wind wind;

    @Valid
    @Embedded
    @JsonAlias("clouds")
    private Cloud cloud;

    @Embedded
    private Rain rain;

    @Embedded
    private Snow snow;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long timezone;

    @Column(nullable = false)
    @JsonIgnore
    private long created;

    @Column(nullable = false)
    @JsonIgnore
    private long updated;

    public CityWeatherData() {
        this.coord = new Coord();
        this.weatherDetail = new WeatherDetail();
        this.wind = new Wind();
        this.cloud = new Cloud();
        this.rain = new Rain();
        this.snow = new Snow();

        this.weatherSummaryList = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<WeatherSummary> getWeatherSummaryList() {
        return weatherSummaryList;
    }

    public void setWeatherSummaryList(List<WeatherSummary> weatherSummaryList) {
        this.weatherSummaryList = weatherSummaryList;
    }

    public WeatherDetail getWeatherDetail() {
        return weatherDetail;
    }

    public void setWeatherDetail(WeatherDetail weatherDetail) {
        this.weatherDetail = weatherDetail;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(Long timezone) {
        this.timezone = timezone;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == 0L;
    }
}
