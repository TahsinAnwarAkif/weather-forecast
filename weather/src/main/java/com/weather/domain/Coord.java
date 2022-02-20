package com.weather.domain;

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
public class Coord implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(-90)
    @Max(90)
    @Column(nullable = false)
    private Double lat;

    @NotNull
    @Min(-180)
    @Max(180)
    @Column(nullable = false)
    @JsonAlias("lon")
    private Double lang;

    public Coord() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }
}
