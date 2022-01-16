CREATE DATABASE weather;

CREATE TABLE city_weather_data (
    id                                    BIGINT       NOT NULL AUTO_INCREMENT,
    lat                                   DOUBLE       NOT NULL,
    lang                                  DOUBLE       NOT NULL,
    temperature                           DOUBLE       NOT NULL,
    feels_like                            DOUBLE       NOT NULL,
    min_temperature                       DOUBLE       NOT NULL,
    max_temperature                       DOUBLE       NOT NULL,
    pressure                              INT          NOT NULL,
    humidity                              INT          NOT NULL,
    sea_level                             INT,
    ground_level                          INT,
    speed                                 DOUBLE       NOT NULL,
    degree                                INT          NOT NULL,
    gust                                  DOUBLE,
    cloudiness                            INT          NOT NULL,
    rain_volume_in_last1hr                DOUBLE,
    rain_volume_in_last3hrs               DOUBLE,
    snow_volume_in_last1hr                DOUBLE,
    snow_volume_in_last3hrs               DOUBLE,
    name                                  VARCHAR(255) NOT NULL,
    timezone                              BIGINT       NOT NULL,
    created                               BIGINT       NOT NULL,
    updated                               BIGINT       NOT NULL,
    CONSTRAINT pk_city_weather_data_id    PRIMARY KEY (id)
);

CREATE TABLE city_weather_summary (
    id          BIGINT                     NOT NULL AUTO_INCREMENT,
    summary     VARCHAR(255)               NOT NULL,
    description VARCHAR(1024)              NOT NULL,
    icon        VARCHAR(7)                 NOT NULL,
    CONSTRAINT pk_city_weather_summary_id  PRIMARY KEY (id)
);

CREATE TABLE city_weather_data_weather_summary (
    city_weather_data_id            BIGINT         NOT NULL,
    city_weather_summary_id         BIGINT         NOT NULL,
    IDX                             INT            NOT NULL,
    CONSTRAINT pk_cwdws_id          PRIMARY KEY (city_weather_data_id, idx),
    CONSTRAINT fk_cwdws_cwd_id      FOREIGN KEY (city_weather_data_id)      REFERENCES city_weather_data (id),
    CONSTRAINT fk_cwdws_cws_id      FOREIGN KEY (city_weather_summary_id)   REFERENCES city_weather_summary (id)
);
