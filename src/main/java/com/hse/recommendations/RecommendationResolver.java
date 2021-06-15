package com.hse.recommendations;

import com.hse.DAOs.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecommendationResolver {

    private final UserDao userDao;
    private final EventDao eventDao;
    private final LikesDao likesDao;
    private final EventToParticipantDao eventToParticipantDao;
    private final RecommendationDao recommendationDao;

    private final static double CLUSTER_PERCENTAGE = 0.05;
    private final static int CLUSTER_LOWER_BOUND = 5;

    private final static double VIEW_WEIGHT = 1.0;
    private final static double LIKE_WEIGHT = 3.0;
    private final static double PARTICIPATION_WEIGHT = 5.0;
    private final static double shiftValue = 2.5;

    private final static Random random = new Random();
    private final static int LSH_ITERATIONS_AMOUNT = 10;
    private final static int GEN_VECTORS_AMOUNT_LOWER_BOUND = 5;

    RecommendationResolver(UserDao userDao, LikesDao likesDao, EventDao eventDao,
                           RecommendationDao recommendationDao, EventToParticipantDao eventToParticipantDao) {
        this.userDao = userDao;
        this.likesDao = likesDao;
        this.eventDao = eventDao;
        this.eventToParticipantDao = eventToParticipantDao;
        this.recommendationDao = recommendationDao;
    }

    private HashMap<Long, Double> calculateUserVector(Long userId) {
        var userVector = new HashMap<Long, Double>();
        List<Long> viewedEvents = eventDao.getUserViewedEvents(userId);
        List<Long> likedEvents = likesDao.getUserLikes(userId);
        List<Long> participatedEvents = eventToParticipantDao.getUserParticipations(userId);

        // Порядок проставления весов важен - в порядке возрастания
        for (Long eventId : viewedEvents) {
            userVector.put(eventId, VIEW_WEIGHT);
        }
        for (Long eventId : likedEvents) {
            userVector.put(eventId, LIKE_WEIGHT);
        }
        for (Long eventId : participatedEvents) {
            userVector.put(eventId, PARTICIPATION_WEIGHT);
        }

        return userVector;
    }

    private Map<Long, Double> generateVector(List<Long> eventIds) {
        var vector = new HashMap<Long, Double>();
        eventIds.forEach(id -> vector.put(id, random.nextGaussian()));
        double magnitude = Math.sqrt(vector.values().stream().reduce(0.0, (a, b) -> a + b * b));
        return vector.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() / magnitude));
    }

    private Pair<Map<String, List<Long>>, Map<Long, String>> LHS(List<Long> userIds,
                                                                 List<Long> eventIds,
                                                                 int dimensionsAmount,
                                                                 Map<Long, Map<Long, Double>> usersVectors) {
        int vectorsToGenerateNumber = Math.max(GEN_VECTORS_AMOUNT_LOWER_BOUND, (int) (2 * Math.log10(dimensionsAmount) / Math.log10(2)));
        var userIdToHashBuilder = new HashMap<Long, StringBuilder>();
        userIds.forEach(id -> userIdToHashBuilder.put(id, new StringBuilder()));
        for (int i = 0; i < vectorsToGenerateNumber; i++) {
            var vector = generateVector(eventIds);
            for (Long userId : userIds) {
                if (calculateScalarProduct(vector, usersVectors.get(userId)) >= 0) {
                    userIdToHashBuilder.get(userId).append(1);
                } else {
                    userIdToHashBuilder.get(userId).append(0);
                }
            }
        }
        var lhsResult = new HashMap<String, List<Long>>();

        var userIdToHash = userIdToHashBuilder.entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().toString()));

        userIdToHash.values().forEach(hash -> lhsResult.put(hash, new ArrayList<>()));

        for (var userHash : userIdToHash.entrySet()) {
            lhsResult.get(userHash.getValue()).add(userHash.getKey());
        }
        return Pair.of(lhsResult, userIdToHash);
    }

    //todo shift vectors
    private Map<Long, Set<Long>> calculateClusters(List<Long> userIds,
                                                   List<Long> eventIds,
                                                   int dimensionsAmount,
                                                   Map<Long, Map<Long, Double>> usersVectors) {
        var iterationResults = new ArrayList<Pair<Map<String, List<Long>>, Map<Long, String>>>();
        var shiftedUsersVectors = new HashMap<Long, Map<Long, Double>>();
        for (Long userId : userIds) {
            shiftedUsersVectors.put(
                    userId,
                    usersVectors.get(userId).entrySet().stream().
                            collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue() - shiftValue)));
        }
        for (int i = 0; i < LSH_ITERATIONS_AMOUNT; i++) {
            iterationResults.add(LHS(userIds, eventIds, dimensionsAmount, shiftedUsersVectors));
        }
        var userToCluster = new HashMap<Long, Set<Long>>();
        for (Long userId : userIds) {
            var userCluster = new HashSet<Long>();
            for (var iterationResult : iterationResults) {
                String iterationHash = iterationResult.getRight().get(userId);
                userCluster.addAll(iterationResult.getLeft().get(iterationHash));
            }
            userToCluster.put(userId, userCluster);
        }
        return userToCluster;
    }

    private double calculateEventRecommendationCoefficient(Long eventId,
                                                           List<Pair<Long, Double>> userCluster,
                                                           Map<Long, Map<Long, Double>> userVectors) {

        double numerator = 0.0;
        double denominator = 0.0;

        for (var userAndCoefficient : userCluster) {
            long clusterUserId = userAndCoefficient.getLeft();
            double weight = userAndCoefficient.getRight();

            if (userVectors.get(clusterUserId).containsKey(eventId)) {
                numerator += weight * userVectors.get(clusterUserId).get(eventId);
            }

            denominator += Math.abs(weight);
        }
        if (denominator == 0) {
            return 0;
        }
        return numerator / denominator;
    }

    private List<Pair<Long, Double>> calculateUserRecommendations(List<Long> eventIds,
                                                                  List<Pair<Long, Double>> userCluster,
                                                                  Map<Long, Map<Long, Double>> userVectors) {

        return eventIds.stream()
                .map(eventId ->
                        Pair.of(eventId,
                                calculateEventRecommendationCoefficient(eventId, userCluster, userVectors)))
                .collect(Collectors.toList());
    }

    private Map<Long, List<Pair<Long, Double>>> calculateRecommendations(List<Long> eventIds,
                                                                         Map<Long, List<Pair<Long, Double>>> userToCluster,
                                                                         Map<Long, Map<Long, Double>> usersVectors) {

        return userToCluster.entrySet().
                stream().
                collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> calculateUserRecommendations(eventIds, entry.getValue(), usersVectors))
                );
    }

    private HashMap<Long, Map<Long, Double>> calculateUserVectors(List<Long> userIds) {
        var userVectors = new HashMap<Long, Map<Long, Double>>();
        for (Long userId : userIds) {
            userVectors.put(userId, calculateUserVector(userId));
        }
        return userVectors;
    }

    @Scheduled(fixedDelayString = "${recommendation.update.delay}")
    public void updateRecommendationsForAllUsers() {
        List<Long> userIds = userDao.getAllUserIds();
        List<Long> eventIds = eventDao.getAllEventIds();

        var usersVectors
                = calculateUserVectors(userIds);

        var userToCluster
                = calculateClusters(userIds, eventIds, eventIds.size(), usersVectors);

        var userToClusterWithWeights
                = calculateClusterWeightsAndTakeMostSimilarUsers(userToCluster, usersVectors);

        var usersRecommendations
                = calculateRecommendations(eventIds, userToClusterWithWeights, usersVectors);

        putDataToDataBase(usersRecommendations);
    }

    private Map<Long, List<Pair<Long, Double>>> calculateClusterWeightsAndTakeMostSimilarUsers(Map<Long, Set<Long>> userToCluster,
                                                                                               Map<Long, Map<Long, Double>> usersVectors) {
        var userToClusterWithWeights = new HashMap<Long, List<Pair<Long, Double>>>();
        for (Long userId : usersVectors.keySet()) {
            System.out.println(userId + " " + userToCluster.get(userId));
            var weightedCluster = userToCluster.get(userId).stream()
                    .map(id ->
                            Pair.of(id, calculateSimilarityCoefficient(usersVectors.get(userId), usersVectors.get(id))))
                    .filter(x -> !x.getLeft().equals(userId))
                    .sorted((a, b) -> -Double.compare(a.getRight(), b.getRight()))
                    .limit(Math.max(CLUSTER_LOWER_BOUND, (int)(CLUSTER_PERCENTAGE * usersVectors.size())))
                    .collect(Collectors.toList());
            userToClusterWithWeights.put(userId, weightedCluster);
        }
        return userToClusterWithWeights;
    }

    @Transactional
    public void putDataToDataBase(Map<Long, List<Pair<Long, Double>>> calculatedRecommendations) {
        recommendationDao.clearRecommendations();
        for (Long userId : calculatedRecommendations.keySet()) {
            recommendationDao.markThatUserHasRecommendations(userId);
            calculatedRecommendations.get(userId).forEach((Pair<Long, Double> elem) ->
                    recommendationDao.addEventRecommendation(userId, elem.getLeft(), elem.getRight()));
        }
    }

    private Double calculateSimilarityCoefficient(Map<Long, Double> user, Map<Long, Double> anotherUser) {

        Set<Long> commonRatedEvents = user.keySet().stream()
                .filter(anotherUser::containsKey)
                .collect(Collectors.toSet());

        if (commonRatedEvents.isEmpty()) {
            return 0.0;
        }

        double covariance = 0;
        double userVariance = 0;
        double anotherUserVariance = 0;
        for (Long eventId : commonRatedEvents) {
            covariance += (user.get(eventId)) * (anotherUser.get(eventId));
            userVariance += Math.pow((user.get(eventId)), 2);
            anotherUserVariance += Math.pow(anotherUser.get(eventId), 2);
        }

        return covariance / Math.sqrt(userVariance) * Math.sqrt(anotherUserVariance);
    }

    private Double calculateScalarProduct(Map<Long, Double> user, Map<Long, Double> anotherUser) {
        Set<Long> commonRatedEvents = user.keySet().stream()
                .filter(anotherUser::containsKey)
                .collect(Collectors.toSet());

        if (commonRatedEvents.isEmpty()) {
            return 0.0;
        }

        return commonRatedEvents.stream()
                .map(eventId -> user.get(eventId) * anotherUser.get(eventId))
                .reduce(0.0, Double::sum);
    }
}