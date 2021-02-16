package com.testTask.dao;

import com.testTask.entity.RequestPay;
import com.testTask.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestPayJPA extends JpaRepository<RequestPay, Long> {
    List<RequestPay> findAllByStatus(Status status);
}
