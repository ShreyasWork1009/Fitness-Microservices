package com.example.AIService.Service;

import com.example.AIService.model.Recommendation;
import com.example.AIService.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService
{
    private final RecommendationRepository recommendationRepository;

    public  List<Recommendation> getUserRecommendation(String userId)
    {
         return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId)
    {
        return recommendationRepository.findByActivityId(activityId);
    }

}
