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
public class Cloud implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    @JsonAlias("all")
    private Integer cloudiness;

    public Cloud() {
    }

    public Integer getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Integer cloudiness) {
        this.cloudiness = cloudiness;
    }
}
