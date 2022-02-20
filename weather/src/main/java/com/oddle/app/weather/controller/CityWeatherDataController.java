package com.oddle.app.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oddle.app.weather.controller.helper.CityWeatherDataHelper;
import com.oddle.app.weather.domain.CityWeatherData;
import com.oddle.app.weather.exception.InvalidIdException;
import com.oddle.app.weather.service.CityWeatherDataService;
import com.oddle.app.weather.util.JSONObject;
import com.oddle.app.weather.validator.CityWeatherDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.oddle.app.weather.util.CollectionUtils.isNotEmpty;
import static com.oddle.app.weather.util.Constants.*;
import static com.oddle.app.weather.util.DateUtils.getDateFromTimestamp;
import static com.oddle.app.weather.util.JSONObject.getObjectFromJson;

/**
 * @author akif
 * @since 1/13/22
 */
@RestController
public class CityWeatherDataController {

    @Autowired
    private CityWeatherDataService cwdService;

    @Autowired
    private CityWeatherDataHelper cwdHelper;

    @Autowired
    private CityWeatherDataValidator cwdValidator;

    @Autowired
    private MessageSourceAccessor msa;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(cwdValidator);
    }

    /**
     * Gets the current CityWeatherData by city name directly from OpenWeatherMap API
     *
     * @param city
     * @return JSONObject
     * @throws JsonProcessingException
     */
    @GetMapping(CURRENT_CWD_URL)
    public JSONObject getCurrentCityWeatherData(@RequestParam String city) throws JsonProcessingException {
        JSONObject json = new JSONObject();
        json.put("cityWeatherData", getObjectFromJson(cwdHelper.getCurrentCityWeatherDataJson(city), CityWeatherData.class));
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(null, city, null, true));

        return json;
    }

    /**
     * Gets a saved CityWeatherData by id
     *
     * @param id
     * @return JSONObject
     */
    @GetMapping(SAVED_CWD_URL)
    public JSONObject getSavedCityWeatherData(@RequestParam long id) {
        CityWeatherData cwd = cwdService.getSavedCityWeatherData(id);
        boolean isFound = cwd != null;

        JSONObject json = new JSONObject();
        json.put("cityWeatherData", cwdService.getSavedCityWeatherData(id));
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(id, isFound ? cwd.getName() : null,
                isFound ? getDateFromTimestamp(cwd.getCreated()) : null, isFound));

        return json;
    }

    /**
     * Gets saved CityWeatherData entries for a specific city, optionally filtered by the generation date (i.e. created)
     *
     * @param city
     * @param created (i.e. generation date)
     * @return JSONObject
     */
    @GetMapping(SAVED_CWD_LIST_URL)
    public JSONObject getSavedCityWeatherDataList(@RequestParam String city,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(pattern = DATE_PATTERN) Date created) {

        List<CityWeatherData> cwdList = cwdService.getLatestSavedCityWeatherData(city, created);
        boolean isFound = isNotEmpty(cwdList);

        JSONObject json = new JSONObject();
        json.put("cityWeatherDataList", cwdList);
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(null, city, created, isFound));

        return json;
    }

    /**
     * Gets the latest saved CityWeatherData entry for a specific city
     *
     * @param city
     * @return JSONObject
     */
    @GetMapping(LATEST_SAVED_CWD_URL)
    public JSONObject getLatestSavedCityWeatherData(@RequestParam String city) {
        CityWeatherData cwd = cwdService.getLatestSavedCityWeatherData(city);
        boolean isFound = cwd != null;

        JSONObject json = new JSONObject();
        json.put("cityWeatherData", cwd);
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(isFound ? cwd.getId() : null, city,
                isFound ? getDateFromTimestamp(cwd.getCreated()) : null, isFound));

        return json;
    }

    /**
     * Saves a CityWeatherData object upon validation
     *
     * @param cwd
     * @param errors
     * @return JSONObject
     */
    @PostMapping(CWD_SAVE_URL)
    public JSONObject saveCityWeatherData(@Valid @RequestBody CityWeatherData cwd,
                                          Errors errors) {

        JSONObject json = new JSONObject();

        if (errors.hasErrors()) {
            json.put("cityWeatherData", cwd);
            json.put("errors", cwdHelper.getFieldErrorMsgMap(errors));
            json.put("status", msa.getMessage("error.label"));
            json.put("errorMsg", msa.getMessage("validation.errors.alert"));

            return json;
        }

        cwdHelper.prepareBeforeSave(cwd, null);
        cwd = cwdService.saveOrUpdateCityWeatherData(cwd);

        json.put("cityWeatherData", cwd);
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(cwd.getId(), cwd.getName(),
                getDateFromTimestamp(cwd.getCreated()), true));

        return json;
    }

    /**
     * Saves the current CityWeatherData directly, derived from the OpenWeatherMap API
     *
     * @param city
     * @return JSONObject
     */
    @PostMapping(CURRENT_CWD_SAVE_URL)
    public JSONObject saveCurrentCityWeatherData(@RequestParam String city) throws JsonProcessingException {
        CityWeatherData cwd = cwdHelper.saveCurrentCityWeatherData(city);

        JSONObject json = new JSONObject();
        json.put("cityWeatherData", cwd);
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(cwd.getId(), cwd.getName(),
                getDateFromTimestamp(cwd.getCreated()), true));

        return json;
    }

    /**
     * Updates a CityWeatherData entry with respect to its id
     *
     * @param cwd
     * @param errors
     * @param id
     * @return JSONObject
     */
    @PutMapping(CWD_UPDATE_URL)
    public JSONObject updateCityWeatherData(@Valid @RequestBody CityWeatherData cwd,
                                            Errors errors,
                                            @RequestParam long id) {

        if (!cwdService.isCityWeatherDataExists(id)) {
            throw new InvalidIdException("error.invalid.id.passed");
        }

        JSONObject json = new JSONObject();

        if (errors.hasErrors()) {
            json.put("cityWeatherData", cwd);
            json.put("errors", cwdHelper.getFieldErrorMsgMap(errors));
            json.put("status", msa.getMessage("error.label"));
            json.put("errorMsg", msa.getMessage("validation.errors.alert"));

            return json;
        }

        cwdHelper.prepareBeforeSave(cwd, id);
        cwd = cwdService.saveOrUpdateCityWeatherData(cwd);

        json.put("cityWeatherData", cwd);
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(cwd.getId(), cwd.getName(),
                getDateFromTimestamp(cwd.getCreated()), true));

        return json;
    }

    /**
     * Deletes a CityWeatherData with respect to its id
     *
     * @param id
     * @return JSONObject
     */
    @DeleteMapping(CWD_DELETE_URL)
    public JSONObject deleteCityWeatherData(@RequestParam long id) {
        if (!cwdService.isCityWeatherDataExists(id)) {
            throw new InvalidIdException("error.invalid.id.passed");
        }

        cwdService.deleteCityWeatherData(id);

        JSONObject json = new JSONObject();
        json.put("status", msa.getMessage("success.label"));
        json.put("successMsg", cwdHelper.getSuccessMsg(id, null, null, true));

        return json;
    }
}
