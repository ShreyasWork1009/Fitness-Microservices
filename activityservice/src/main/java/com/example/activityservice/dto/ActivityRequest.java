package com.example.activityservice.dto;

import com.example.activityservice.model.ActivityType;
import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityRequest
{
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,Object> additionalMetrics;

}
