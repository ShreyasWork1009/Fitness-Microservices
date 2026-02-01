package com.example.activityservice.service;

import com.example.activityservice.dto.ActivityRequest;
import com.example.activityservice.dto.ActivityResponse;
import com.example.activityservice.model.Activity;
import com.example.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService
{

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;


    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public  ActivityResponse trackActivity(ActivityRequest request)
    {
        boolean isValidatedUser = userValidationService.validateUser(request.getUserId());

        if(!isValidatedUser)
        {
            throw new RuntimeException("The user is not a valid one");

        }

        Activity activity= Activity.builder()
                                   .userId(request.getUserId())
                                   .type(request.getType())
                                   .duration(request.getDuration())
                                   .caloriesBurned(request.getCaloriesBurned())
                                   .startTime(request.getStartTime())
                                   .additionalMetrics(request.getAdditionalMetrics())
                            .build();

        Activity savedActivity = activityRepository.save(activity);

        //publish to RabbitMQ for AI processing
        try {
          //rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);

        }
        catch(Exception e)
        {
            log.error("Failed to publish activity to Rabbit MQ:{}", String.valueOf(e));
        }

        return mapToResponse(activity);

    }

    public ActivityResponse mapToResponse(Activity activity)
    {
        ActivityResponse activityResponse=new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());

        return activityResponse;

    }

    public  List<ActivityResponse> getUserActivity(String userId)
    {
       List<Activity>activities = activityRepository.findByUserId(userId);
       return activities.stream()
               .map(this::mapToResponse)
               .collect(Collectors.toList());


    }

    public ActivityResponse getActivityById(String id)
    {
        return activityRepository.findById(id).map(this::mapToResponse).orElseThrow(()-> new RuntimeException("Activity not found with Id:"+id));

    }


}
