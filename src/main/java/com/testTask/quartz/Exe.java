package com.testTask.quartz;

import com.testTask.entity.RequestPay;
import com.testTask.entity.Status;
import com.testTask.service.RequestPayService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@DisallowConcurrentExecution
public class Exe implements Job {
    @Autowired
    private RequestPayService requestPayService;

    @Override
    public void execute(JobExecutionContext context) {
        List<RequestPay> list = requestPayService.findAllByStatus(Status.PROCESSING);

        if (list.size() > 0) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/requestPay/getRandomStatus";
            String response = restTemplate.getForObject(url, String.class);
            try {
                JSONObject o = (JSONObject) new JSONParser().parse(response);
                Status status = Status.valueOf(o.get("status").toString());
                RequestPay rp = list.get(0);
                rp.setStatus(status);
                log.debug("Update RequestPay: 'id={}', is {}", rp.getId(), requestPayService.update(rp));
            } catch (Exception e) {
                log.error("{} \n Message: {} \n {}", e.getClass(), e.getMessage(), e.getStackTrace());
                e.printStackTrace();
            }
        }
        log.debug("Job '{}' completed.", context.getJobDetail().getKey().getName());
    }
}