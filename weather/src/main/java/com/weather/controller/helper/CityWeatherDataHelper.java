package com.weather.controller.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.controller.exception.RestTemplateExceptionResolver;
import com.weather.domain.CityWeatherData;
import com.weather.service.CityWeatherDataService;
import com.weather.util.DateUtils;
import com.weather.util.JSONObject;
import com.weather.util.ReflectionUtils;
import com.weather.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.weather.util.Constants.*;

/**
 * @author akif
 * @since 1/13/22
 */
@Component
public class CityWeatherDataHelper {

    @Autowired
    private CityWeatherDataService cwdService;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private MessageSourceAccessor msa;

    public CityWeatherData saveCurrentCityWeatherData(String city) throws JsonProcessingException {
        CityWeatherData cwd = JSONObject.getObjectFromJson(getCurrentCityWeatherDataJson(city), CityWeatherData.class);

        prepareBeforeSave(cwd, null);
        cwd = cwdService.saveOrUpdateCityWeatherData(cwd);

        return cwd;
    }

    /**
     * Prepares audit info a CityWeatherData entry for save
     * Prepares id and audit info for a CityWeatherData entry for update
     *
     * @param cwd
     * @param id
     */
    public void prepareBeforeSave(CityWeatherData cwd, Long id) {
        if (id != null) {
            prepareCityWeatherDataJsonIgnoredFields(cwd, id);
        }

        long currentDateTimestamp = DateUtils.getTimestampFromDate(new Date());

        if (cwd.isNew()) {
            cwd.setCreated(currentDateTimestamp);
        }

        cwd.setUpdated(currentDateTimestamp);
    }

    public String getCurrentCityWeatherDataJson(String city) {
        return getRestTemplate().getForObject(getWeatherApiUrlWithCity(city), String.class);
    }

    public Map<String, String> getFieldErrorMsgMap(Errors errors) {
        Map<String, String> fieldErrorMsgMap = new LinkedHashMap<>();

        errors.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();

            fieldErrorMsgMap.put(fieldName, error.getDefaultMessage());
        });

        return Collections.unmodifiableMap(fieldErrorMsgMap);
    }

    public String getSuccessMsg(Long id, String city, Date created, boolean isResultNotEmpty) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        switch (request.getRequestURI()) {
            case CURRENT_CWD_URL:
                return msa.getMessage("cwd.get.current.success", new String[]{city});

            case SAVED_CWD_URL:
                return isResultNotEmpty
                        ? msa.getMessage("cwd.get.success", new Object[]{city, created, id})
                        : msa.getMessage("cwd.get.fail", new Object[]{id});

            case SAVED_CWD_LIST_URL:
                String createdStr = (created == null ? msa.getMessage("all.time") : created.toString());

                return isResultNotEmpty
                        ? msa.getMessage("cwd.get.by.created.success", new Object[]{city, createdStr})
                        : msa.getMessage("cwd.get.by.created.fail", new Object[]{city, createdStr});

            case LATEST_SAVED_CWD_URL:
                return isResultNotEmpty
                        ? msa.getMessage("cwd.get.by.created.success", new Object[]{city, created})
                        : msa.getMessage("cwd.get.recent.fail", new String[]{city});

            case CURRENT_CWD_SAVE_URL:
                return msa.getMessage("cwd.save.success.current", new Object[]{city, created, id});

            case CWD_SAVE_URL:
                return msa.getMessage("cwd.save.success", new Object[]{city, created, id});

            case CWD_UPDATE_URL:
                return msa.getMessage("cwd.update.success", new Object[]{city, created, id});

            case CWD_DELETE_URL:
                return msa.getMessage("cwd.delete.success", new Long[]{id});

            default:
                throw new IllegalArgumentException("Invalid URL!");
        }
    }

    public RestTemplate getRestTemplate() {
        return restTemplateBuilder
                .errorHandler(new RestTemplateExceptionResolver())
                .build();
    }

    private void prepareCityWeatherDataJsonIgnoredFields(CityWeatherData cwd, long id) {
        CityWeatherData cwdBeforeUpdate = cwdService.getSavedCityWeatherData(id);

        ReflectionUtils.getJsonIgnoredFieldList(cwd).forEach(field -> {
            field.setAccessible(true);

            try {
                field.set(cwd, ReflectionUtils.getFieldValue(cwdBeforeUpdate, field.getName()));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        });
    }

    private String getWeatherApiUrlWithCity(String city) {
        return CWD_API_URL.replace("{API key}", CWD_API_KEY)
                .replace("{city name}", StringUtils.nullSafeString(city));
    }
}
