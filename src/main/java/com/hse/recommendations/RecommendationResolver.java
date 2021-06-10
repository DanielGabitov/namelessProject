package com.hse.recommendations;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.LikesDao;
import com.hse.DAOs.RecommendationDao;
import com.hse.DAOs.UserDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.primitives.Floats.min;
import static com.google.common.primitives.Ints.min;

@Component
public class RecommendationResolver {

    private final UserDao userDao;
    private final EventDao eventDao;
    private final LikesDao likesDao;
    private final RecommendationDao recommendationDao;

    private final static Double CLUSTER_PERCENTAGE = 0.05;
    //todo 15
    private final static int CLUSTER_LOWER_BOUND = 5;

    RecommendationResolver(UserDao userDao, LikesDao likesDao, EventDao eventDao, RecommendationDao recommendationDao) {
        this.userDao = userDao;
        this.likesDao = likesDao;
        this.eventDao = eventDao;
        this.recommendationDao = recommendationDao;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Pair implements Comparable<Pair>{
        private Long id;
        private float coefficient;

        @Override
        public int compareTo(Pair other) {
            return -Float.compare(coefficient, other.coefficient);
        }
    }

    private static float getOverlapCoefficient(Set<Long> a, Set<Long> b) {
        Set<Long> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        if (a.isEmpty() || b.isEmpty()){
            return 0;
        }
        return ((float) intersection.size()) / (min(a.size(), b.size()));
    }

    private static HashMap<Long, List<Pair>> calculateSimilarity(HashMap<Long, Set<Long>> userToLikes) {
        HashMap<Long, List<Pair>> calculatedMetrics = new HashMap<>();
        ArrayList<Long> users = new ArrayList<>(userToLikes.keySet());
        users.forEach(user -> calculatedMetrics.put(user, new ArrayList<>()));
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                Long firstUser = users.get(i);
                Long secondUser = users.get(j);
                float coefficient = getOverlapCoefficient(userToLikes.get(firstUser), userToLikes.get(secondUser));
                calculatedMetrics.get(firstUser).add(new Pair(secondUser, coefficient));
                calculatedMetrics.get(secondUser).add(new Pair(firstUser, coefficient));
            }
        }
        return calculatedMetrics;
    }

    private float calculateEventRecommendationCoefficient(Long eventId, List<Pair> userCluster,
                                                          HashMap<Long, Set<Long>> usersToLikes){
        float similaritySum = userCluster.stream()
                .filter((Pair elem) -> usersToLikes.get(elem.id).contains(eventId))
                .map(Pair::getCoefficient)
                .reduce((float) 0, Float::sum);
        return similaritySum / userCluster.size();
    }

    private HashMap<Long, List<Pair>> helper(List<Long> eventIds,
                                             HashMap<Long, List<Pair>> clusters,
                                             HashMap<Long, Set<Long>> usersToLikes){

        HashMap<Long, List<Pair>> result = new HashMap<>();
        for (Long userId : clusters.keySet()){
            List<Pair> userRecommendations = new ArrayList<>();
            for (Long eventId : eventIds){
                float eventCoefficient
                        = calculateEventRecommendationCoefficient(eventId, clusters.get(userId), usersToLikes);
                userRecommendations.add(new Pair(eventId, eventCoefficient));
            }
            userRecommendations = userRecommendations.stream().sorted().collect(Collectors.toList());
            result.put(userId, userRecommendations);
        }
        return result;
    }

    @Transactional
    public void putDataToDataBase(HashMap<Long, List<Pair>> calculatedRecommendations){
        recommendationDao.clearRecommendations();
        for (Long userId : calculatedRecommendations.keySet()){
            recommendationDao.markThatUserHasRecommendations(userId);
            calculatedRecommendations.get(userId).forEach((Pair elem) ->
                            recommendationDao.addEventRecommendation(userId, elem.id, elem.coefficient));
        }
    }

    //todo time in constant??
    @Scheduled(fixedDelay = 3600000, initialDelay = 1000)
    public void updateRecommendations() {
        List<Long> userIds = userDao.getAllUserIds();
        int clusterSize = Math.max((int) (CLUSTER_PERCENTAGE * userIds.size()), CLUSTER_LOWER_BOUND);
        HashMap<Long, Set<Long>> usersToLikes = new HashMap<>();
        userIds.forEach(userId -> usersToLikes.put(userId, new HashSet<>(likesDao.getUserLikes(userId))));
        HashMap<Long, List<Pair>> clusters = calculateSimilarity(usersToLikes);
        for (var elem : clusters.entrySet()){
            List<Pair> cluster = elem.getValue().stream().sorted().limit(clusterSize).collect(Collectors.toList());
            clusters.replace(elem.getKey(), cluster);
        }
        List<Long> eventIds = eventDao.getAllEventIds();
        HashMap<Long, List<Pair>> calculatedRecommendations = helper(eventIds, clusters, usersToLikes);
        putDataToDataBase(calculatedRecommendations);
    }
}
