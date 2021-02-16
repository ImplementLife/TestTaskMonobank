package com.testTask.service;

import com.testTask.dao.RequestPayJPA;
import com.testTask.entity.RequestPay;
import com.testTask.entity.Status;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RequestPayService {
    @Autowired
    private RequestPayJPA requestPayJPA;

    private Validator validator;

    public RequestPayService() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public List<RequestPay> findAllByStatus(Status status) {
        return requestPayJPA.findAllByStatus(status);
    }

    public List<RequestPay> findAll() {
        return requestPayJPA.findAll();
    }

    /**
     * Makes a validation, if isValid successful, updated RequestPay in DB
     * @throws ValidationException
     * @param requestPay
     * @return updating is successful
     */
    public boolean update(RequestPay requestPay) {
        RequestPay savedRequestPay = null;
        if (isValid(requestPay)) {
            savedRequestPay = requestPayJPA.save(requestPay);
        }
        return savedRequestPay != null;
    }

    /**
     * Find by id in DB, throw NotFoundException if not find instance in DB
     *
     * @throws NotFoundException
     * @param id
     * @return requestPay
     */
    public RequestPay findById(Long id) {
        RequestPay requestPay = requestPayJPA.findById(id).orElse(null);
        if (requestPay == null) {
            throw new NotFoundException(String.format("Request Pay with id '%s' is Not Found.", id));
        }
        return requestPay;
    }

    /**
     * Makes a validation, if isValid successful, saved RequestPay in DB
     * @throws RuntimeException
     * @param requestPay
     * @return savedRequestPay.id
     */
    public Long saveNew(RequestPay requestPay) {
        requestPay.setStatus(Status.PROCESSING);
        RequestPay savedRequestPay = null;
        if (isValid(requestPay)) {
            savedRequestPay = requestPayJPA.save(requestPay);
        }
        if (savedRequestPay != null) {
            return savedRequestPay.getId();
        } else {
            throw new RuntimeException("Save");
        }
    }

    /**
     * @throws ValidationException
     * @param @NotNull requestPay
     * @return true
     */
    public boolean isValid(RequestPay requestPay) {
        Set<ConstraintViolation<RequestPay>> validateResult = validator.validate(requestPay);
        if (validateResult.size() > 0) {
            throw new ValidationException("One or more passed parameters is not valid.");
        }
        return true;
    }
}
