package com.oddle.app.weather.service;

import com.oddle.app.weather.domain.CityWeatherData;
import com.oddle.app.weather.domain.WeatherSummary;
import com.oddle.app.weather.util.CollectionUtils;
import com.oddle.app.weather.util.DateUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author akif
 * @since 1/13/22
 */
@Repository
public class CityWeatherDataService {

    private static final String CITY_WEATHER_DATA_SQL = "FROM CityWeatherData cwd"
            + " WHERE UPPER(cwd.name) = UPPER(:city)";

    @PersistenceContext
    private EntityManager em;

    public CityWeatherData getSavedCityWeatherData(long id) {
        return em.find(CityWeatherData.class, id);
    }

    public CityWeatherData getLatestSavedCityWeatherData(String city) {
        try {
            return em.createQuery(CITY_WEATHER_DATA_SQL
                            + " ORDER BY created DESC", CityWeatherData.class)
                    .setParameter("city", city)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<CityWeatherData> getLatestSavedCityWeatherData(String city, Date created) {
        String sql = CITY_WEATHER_DATA_SQL + (created != null ? " AND cwd.created BETWEEN :createdFrom AND :createdTo" : "");
        TypedQuery<CityWeatherData> query = em.createQuery(sql, CityWeatherData.class)
                .setParameter("city", city);

        if (created != null) {
            long createdFrom = DateUtils.getTimestampFromDate(created);
            long createdTo = DateUtils.getNextDayTimestamp(createdFrom);

            query.setParameter("createdFrom", createdFrom)
                    .setParameter("createdTo", createdTo);
        }

        return query.getResultList();
    }

    public boolean isCityWeatherDataExists(long id) {
        try {
            return em.createQuery("SELECT 1"
                            + " FROM CityWeatherData"
                            + " WHERE id = :id", Integer.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult() != null;
        } catch (NoResultException nre) {
            return false;
        }
    }

    public boolean isWeatherSummaryExists(long id) {
        try {
            return em.createQuery("SELECT 1"
                            + " FROM WeatherSummary"
                            + " WHERE id = :id", Integer.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getSingleResult() != null;
        } catch (NoResultException nre) {
            return false;
        }
    }

    @Transactional
    public CityWeatherData saveOrUpdateCityWeatherData(CityWeatherData cwd) {
        initializeWeatherSummaryList(cwd);

        if (cwd.isNew()) {
            em.persist(cwd);
            em.flush();
        } else {
            saveNewWeatherSummaryListBeforeUpdate(cwd.getWeatherSummaryList());

            cwd = em.merge(cwd);
        }

        return cwd;
    }

    @Transactional
    public void deleteCityWeatherData(long id) {
        em.remove(em.getReference(CityWeatherData.class, id));
    }

    /**
     * This is done to avoid mentioning CasCadeType.MERGE above CityWeatherData#weatherSummaryList. CasCadeType.MERGE
     * would enforce a system generated WeatherSummary to be updated with the specified value inside parent, which
     * we don't want. However, we want a new WeatherSummary to be created during update
     */
    private void saveNewWeatherSummaryListBeforeUpdate(List<WeatherSummary> weatherSummaryList) {
        List<WeatherSummary> newWeatherSummaryList = weatherSummaryList
                .stream()
                .filter(WeatherSummary::isNew)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newWeatherSummaryList)) {
            return;
        }

        newWeatherSummaryList.forEach(em::persist);
        em.flush();
    }

    /**
     * Initializes the detached WeatherSummary objects specified with id only, sets the actual object in the actual idx
     * of cwd.weatherSummaryList
     */
    private void initializeWeatherSummaryList(CityWeatherData cwd) {
        List<Long> existingWeatherSummaryIdList = cwd.getWeatherSummaryList()
                .stream()
                .filter(weatherSummary -> !weatherSummary.isNew())
                .map(WeatherSummary::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(existingWeatherSummaryIdList)) {
            return;
        }

        existingWeatherSummaryIdList.forEach(id -> {
            WeatherSummary weatherSummary = em.find(WeatherSummary.class, id);
            int idx = cwd.getWeatherSummaryList().indexOf(weatherSummary);

            cwd.getWeatherSummaryList().set(idx, weatherSummary);
        });
    }
}
