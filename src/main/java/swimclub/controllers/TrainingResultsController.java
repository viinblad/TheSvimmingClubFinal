package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.TrainingResultsRepository;
import swimclub.services.TrainingResultsService;
import swimclub.utilities.CrawlComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for managing training results.
 */
public class TrainingResultsController {

    private final TrainingResultsService trainingService;

    // === CONSTRUCTOR ===
    /**
     * Constructor to initialize the TrainingResultsController with the necessary service.
     *
     * @param trainingService The service responsible for managing training results.
     */
    public TrainingResultsController(TrainingResultsService trainingService, TrainingResultsRepository trainingResultsRepository) {
        this.trainingService = trainingService;
        this.trainingResultsRepository = trainingResultsRepository;
    }
    // === ADD TRAINING RESULTS ===
    /**
     * Adds the training results for a member.
     *
     * @param member The member for whom the results are being added.
     * @param activityType The type of activity the member completed.
     * @param time The time it took for the activity (in seconds).
     * @param date The date the activity took place.
     * @param level The membership level of the member (Junior or Senior).
     */
    public void addTrainingResults(Member member, String activityType, double time, String date, MembershipLevel level) {
        ActivityTypeData activity = ActivityTypeData.fromString(activityType);

        TrainingResults existingResults = null;
        for (TrainingResults trainingResults : trainingResultsRepository.getAllResults()) {
            if (trainingResults.getMember().getMemberId() == member.getMemberId() && trainingResults.getActivityType().equals(activity.toActivityType())) {
                existingResults = trainingResults;
                break;
            }
        }
        if (existingResults == null) {
            if (member.getAge() < 18) {
                level = MembershipLevel.JUNIOR;
            } else {
                level = MembershipLevel.SENIOR;
            }
            trainingService.addResult(member, activity.toActivityType(), time, date, level);

        } else {
            if (time < existingResults.getTime()) { // Check to see if new time is better
                existingResults.setTime(time);
                existingResults.setDate(date);

                trainingResultsRepository.updateResults(existingResults);
            }
        }

        member.setTime(time);
        member.setDate(date);
    }
    // === GET RESULTS BY MEMBER ===
    /**
     * Retrieves the training results for a specific member.
     *
     * @param member The member whose results are being fetched.
     * @return A list of training results for the given member.
     */
    public List<TrainingResults> getResultsByMember(Member member) {
        return trainingService.getResultsByMember(member);
    }
    // === GET ALL TRAINING RESULTS ===
    /**
     * Retrieves all training results for all members.
     *
     * @return A list of all training results.
     */
    public List<TrainingResults> getAllResults() {
        return trainingService.getAllResults();
    }

    public void top5Crawl() {
        List<TrainingResults> crawlList = trainingResultsRepository.getAllResults();

        List<TrainingResults> crawlResults = new ArrayList<>();

        for (TrainingResults result : crawlList) {
            if (result.getActivityType().equals(ActivityType.CRAWL)) {
                crawlResults.add(result);
            }
        }
        crawlResults.sort(new CrawlComparator());

        int topResultsCount = Math.min(crawlResults.size(),5);

        for (int i = 0; i < topResultsCount; i++){
            System.out.println("" + crawlResults.get(i).toString());
        }

        crawlList.subList(0, topResultsCount);

    }
    public void top5BackCrawl() {
        List<TrainingResults> backCrawlList = trainingResultsRepository.getAllResults();

        List<TrainingResults> backCrawlResults = new ArrayList<>();

        for (TrainingResults result : backCrawlList) {
            if (result.getActivityType().equals(ActivityType.CRAWL)) {
                backCrawlResults.add(result);
            }
        }
        backCrawlResults.sort(new CrawlComparator());

        int topResultsCount = Math.min(backCrawlResults.size(),5);

        for (int i = 0; i < topResultsCount; i++){
            System.out.println(backCrawlResults.get(i).toString());
        }

        backCrawlResults.subList(0, topResultsCount);

    }




}
