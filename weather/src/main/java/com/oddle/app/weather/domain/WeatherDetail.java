package com.oddle.app.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author akif
 * @since 1/13/22
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(nullable = false)
    @JsonAlias("temp")
    private Double temperature;

    @NotNull
    @Column(nullable = false)
    @JsonAlias("feels_like")
    private Double feelsLike;

    @NotNull
    @Column(nullable = false)
    @JsonAlias("temp_min")
    private Double minTemperature;

    @NotNull
    @Column(nullable = false)
    @JsonAlias("temp_max")
    private Double maxTemperature;

    @NotNull
    @Column(nullable = false)
    private Integer pressure;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer humidity;

    @JsonAlias("sea_level")
    private Integer seaLevel;

    @JsonAlias("grnd_level")
    private Integer groundLevel;

    public WeatherDetail() {
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Integer getGroundLevel() {
        return groundLevel;
    }

    public void setGroundLevel(Integer groundLevel) {
        this.groundLevel = groundLevel;
    }
}
