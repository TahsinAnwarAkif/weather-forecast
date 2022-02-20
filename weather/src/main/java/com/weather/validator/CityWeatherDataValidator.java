package com.weather.validator;

import com.weather.domain.CityWeatherData;
import com.weather.domain.WeatherSummary;
import com.weather.service.CityWeatherDataService;
import com.weather.util.CollectionUtils;
import com.weather.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author akif
 * @since 1/14/22
 */
@Component
public class CityWeatherDataValidator implements Validator {

    @Autowired
    private CityWeatherDataService cwdService;

    @Autowired
    private MessageSourceAccessor msa;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CityWeatherData.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CityWeatherData cwd = (CityWeatherData) target;

        validateWeatherSummaryList(cwd.getWeatherSummaryList(), errors);
        validateUnbindableInputs(cwd, errors);
    }

    private void validateWeatherSummaryList(List<WeatherSummary> weatherSummaryList, Errors errors) {
        String requiredErrorMsg = msa.getMessage("javax.validation.constraints.NotBlank.message");
        String fieldsInvalidSinceIdGivenMsg = msa.getMessage("error.cwd.fields.cannot.be.inputted");
        String duplicateIdsPresentMsg = msa.getMessage("error.weather.summary.ids.duplicate");

        /**
         * weatherSummaryList should be required for any CityWeatherData
         */
        if (CollectionUtils.isEmpty(weatherSummaryList)) {
            errors.rejectValue("weatherSummaryList", requiredErrorMsg, requiredErrorMsg);

            return;
        }

        /**
         * non-zero weatherSummary with same ids cannot be inputted
         */
        List<WeatherSummary> existingWeatherSummaryList = weatherSummaryList
                .stream()
                .filter(weatherSummary -> !weatherSummary.isNew())
                .collect(Collectors.toList());

        if (CollectionUtils.hasDuplicate(existingWeatherSummaryList)) {
            errors.rejectValue("weatherSummaryList", duplicateIdsPresentMsg, duplicateIdsPresentMsg);
        }

        IntStream.range(0, weatherSummaryList.size())
                .forEach(idx ->
                        validateWeatherSummaryFields(weatherSummaryList.get(idx), idx, errors,
                                requiredErrorMsg, fieldsInvalidSinceIdGivenMsg));
    }

    /**
     * Although @JsonIgnore is present in the unbindable fields, a sanity check  is kept to validate such unbindable fields
     */
    public void validateUnbindableInputs(CityWeatherData cwd, Errors errors) {
        String invalidInputMsg = msa.getMessage("error.cannot.be.inputted");

        if (cwd.getId() != 0L) {
            errors.rejectValue("id", invalidInputMsg, invalidInputMsg);
        }

        if (cwd.getCreated() != 0L) {
            errors.rejectValue("created", invalidInputMsg, invalidInputMsg);
        }

        if (cwd.getUpdated() != 0L) {
            errors.rejectValue("updated", invalidInputMsg, invalidInputMsg);
        }
    }

    private void validateWeatherSummaryFields(WeatherSummary weatherSummary, int idx, Errors errors,
                                              String requiredErrorMsg, String fieldsInvalidSinceIdGivenMsg) {
        /**
         * If weatherSummary.id is not given, then it is assumed that the consumer is trying to save a new WeatherSummary
         * entry, so the required fields should be validated
         */
        if (weatherSummary.isNew()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "weatherSummaryList[" + idx + "].summary", requiredErrorMsg, requiredErrorMsg);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "weatherSummaryList[" + idx + "].description", requiredErrorMsg, requiredErrorMsg);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "weatherSummaryList[" + idx + "].icon", requiredErrorMsg, requiredErrorMsg);
        } else {
            if (!cwdService.isWeatherSummaryExists(weatherSummary.getId())) {
                String invalidWeatherIdMsg = msa.getMessage("error.invalid.weather.id.passed", new Long[]{weatherSummary.getId()});

                errors.rejectValue("weatherSummaryList", invalidWeatherIdMsg, invalidWeatherIdMsg);
            }

            /**
             * If weatherSummary.id is given, then the rest of WeatherSummary fields should never be inputted
             */
            if (StringUtils.isNotEmpty(weatherSummary.getSummary())) {
                errors.rejectValue("weatherSummaryList[" + idx + "].summary", fieldsInvalidSinceIdGivenMsg, fieldsInvalidSinceIdGivenMsg);
            }

            if (StringUtils.isNotEmpty(weatherSummary.getDescription())) {
                errors.rejectValue("weatherSummaryList[" + idx + "].description", fieldsInvalidSinceIdGivenMsg, fieldsInvalidSinceIdGivenMsg);
            }

            if (StringUtils.isNotEmpty(weatherSummary.getIcon())) {
                errors.rejectValue("weatherSummaryList[" + idx + "].icon", fieldsInvalidSinceIdGivenMsg, fieldsInvalidSinceIdGivenMsg);
            }
        }
    }
}
