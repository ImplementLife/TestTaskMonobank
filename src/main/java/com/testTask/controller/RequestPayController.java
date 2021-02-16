package com.testTask.controller;

import com.testTask.entity.RequestPay;
import com.testTask.entity.Status;
import com.testTask.service.RequestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/requestPay")
public class RequestPayController {
    @Autowired
    private RequestPayService requestPayService;

    @GetMapping("/getRandomStatus")
    public ResponseEntity<Object> getRandomStatus() {
        log.trace("/requestPay/getRandomStatus is calling");
        Status status = Status.PROCESSING;
        int i = (int) (Math.random() * 30);
        if (i < 10) status = Status.ERROR;
        else if (i < 20) status = Status.COMPLETED;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getStatus")
    public ResponseEntity<Object> getStatus(@RequestParam String id) {
        log.trace("/requestPay/getStatus is calling");
        Map<String, Object> response = new HashMap<>();
        RequestPay requestPay = requestPayService.findById(Long.parseLong(id));
        response.put("status", requestPay.getStatus());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestParam String number, @RequestParam String date) {
        log.trace("/requestPay/create is calling");
        RequestPay requestPay = new RequestPay();
        requestPay.setNumber(number);
        requestPay.setDate(Date.valueOf(date));
        Map<String, Object> response = new HashMap<>();
        response.put("idRequest", requestPayService.saveNew(requestPay));
        return ResponseEntity.ok(response);
    }

}
