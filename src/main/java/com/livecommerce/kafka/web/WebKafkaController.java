package com.livecommerce.kafka.web;

import com.livecommerce.kafka.dto.Message;
import com.livecommerce.kafka.services.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

@Slf4j
@RestController
public class WebKafkaController {
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    Logger logger = getLogger(WebKafkaController.class);

    @GetMapping("/")
    public String home(HttpServletRequest request){
        if(Objects.nonNull(loadBalancerClient.choose("Producer1"))){
            logger.info("getInstanceId: "+loadBalancerClient.choose("Producer1").getInstanceId());
        }
        if(Objects.nonNull(discoveryClient.getInstances("Producer1"))){
            logger.info("getInstance {}"+discoveryClient.getInstances("Producer1").size());
        }
        return this.reviewService.getValOnlineToken();
    }

    @GetMapping("/processor")
    public String processor(final @RequestBody Message message){
        this.processorTopic(message);
        return "got message";
    }

    @KafkaListener(topics="testprocessor", groupId="group1")
    private void processorTopic(Message message){
        if(Objects.isNull(message) || Objects.isNull(message.getMessage())){
            return;
        }
        ArrayList<String> kafkamsgs= new ArrayList<>();
        kafkamsgs.add(message.getMessage());
    }
}
