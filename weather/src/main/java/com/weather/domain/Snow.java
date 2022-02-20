package com.weather.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author akif
 * @since 1/13/22
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Snow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "snow_volume_in_last1hr")
    @JsonAlias("1h")
    private Double volumeInLastHr;

    @Column(name = "snow_volume_in_last3hrs")
    @JsonAlias("3h")
    private Double volumeInLast3Hrs;

    public Snow() {
    }

    public Double getVolumeInLastHr() {
        return volumeInLastHr;
    }

    public void setVolumeInLastHr(Double volumeInLastHr) {
        this.volumeInLastHr = volumeInLastHr;
    }

    public Double getVolumeInLast3Hrs() {
        return volumeInLast3Hrs;
    }

    public void setVolumeInLast3Hrs(Double volumeInLast3Hrs) {
        this.volumeInLast3Hrs = volumeInLast3Hrs;
    }
}
