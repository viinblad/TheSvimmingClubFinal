package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.TrainingResultsRepository;
import swimclub.services.TrainingResultsService;
import swimclub.utilities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing training results. It handles the logic for adding,
 * retrieving, and sorting training results for different swimming disciplines.
 */
public class TrainingResultsController {
    private final TrainingResultsService trainingService;
    private final TrainingResultsRepository trainingResultsRepository;

    /**
     * Constructor to initialize the TrainingResultsController with service and repository.
     *
     * @param trainingService           The service responsible for training result operations.
     * @param trainingResultsRepository The repository where training results are stored.
     */
    public TrainingResultsController(TrainingResultsService trainingService, TrainingResultsRepository trainingResultsRepository) {
        this.trainingService = trainingService;
        this.trainingResultsRepository = trainingResultsRepository;
    }

    /**
     * Adds training results for a member in a specific discipline. If a result already exists for the member
     * and discipline, the new result is compared and updated if the new time is better.
     *
     * @param member       The member whose training results are to be added.
     * @param activityType The discipline of the training (e.g., Crawl, Backcrawl).
     * @param time         The time the member achieved in the training.
     * @param date         The date the training was performed.
     * @param level        The membership level (Junior or Senior).
     */
    public void addTrainingResults(Member member, String activityType, double time, String date, MembershipLevel level) {
        ActivityTypeData activity = ActivityTypeData.fromString(activityType);

        TrainingResults existingResults = null;
        // Check if the member already has results for this discipline
        for (TrainingResults trainingResults : trainingResultsRepository.getAllResults()) {
            if (trainingResults.getMember().getMemberId() == member.getMemberId() &&
                    trainingResults.getActivityType().equals(activity.toActivityType())) {
                existingResults = trainingResults;
                break;
            }
        }

        // If no existing results, add new result
        if (existingResults == null) {
            if (member.getAge() < 18) {
                level = MembershipLevel.JUNIOR;
            } else {
                level = MembershipLevel.SENIOR;
            }
            trainingService.addResult(member, activity.toActivityType(), time, date, level);

        } else {
            // If the existing result's time is greater than the new time, update it
            if (time < existingResults.getTime()) {
                existingResults.setTime(time);
                existingResults.setDate(date);

                trainingResultsRepository.updateResults(existingResults);
            }
        }

        // Update member's time and date
        member.setTime(time);
        member.setDate(date);
    }

    /**
     * Retrieves all training results for a specific member.
     *
     * @param member The member whose training results are to be retrieved.
     * @return A list of training results for the specified member.
     */
    public List<TrainingResults> getResultsByMember(Member member) {
        return trainingService.getResultsByMember(member);
    }

    /**
     * Retrieves all training results in the system.
     *
     * @return A list of all training results.
     */
    public List<TrainingResults> getAllResults() {
        return trainingService.getAllResults();
    }

    /**
     * Displays the top 5 training results for the Crawl discipline (Senior).
     * Sorts the results based on time and prints the top performers.
     */
    /**
     * Displays the top 5 training results for the Crawl discipline (Senior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5Crawl() {
        List<TrainingResults> crawlList = trainingResultsRepository.getAllResults();

        List<TrainingResults> crawlResults = new ArrayList<>();

        // Filter results for Crawl discipline
        for (TrainingResults result : crawlList) {
            if (result.getActivityType().equals(ActivityType.CRAWL)) {
                crawlResults.add(result);
            }
        }

        // If no results for Crawl, print a message
        if (crawlResults.isEmpty()) {
            System.out.println("No Crawl results found.");
        } else {
            // Sort results by the CrawlComparator (best time first)
            crawlResults.sort(new CrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(crawlResults.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(crawlResults.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Backcrawl discipline (Senior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5BackCrawl() {
        List<TrainingResults> backCrawlList = trainingResultsRepository.getAllResults();

        List<TrainingResults> backCrawlResults = new ArrayList<>();

        // Filter results for Backcrawl discipline
        for (TrainingResults result : backCrawlList) {
            if (result.getActivityType().equals(ActivityType.BACKCRAWL)) {
                backCrawlResults.add(result);
            }
        }

        // If no results for Backcrawl, print a message
        if (backCrawlResults.isEmpty()) {
            System.out.println("No Backcrawl results found.");
        } else {
            // Sort results by the BackCrawlComparator (best time first)
            backCrawlResults.sort(new BackCrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(backCrawlResults.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(backCrawlResults.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Breaststroke discipline (Senior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5Breaststroke() {
        List<TrainingResults> breathstrokeList = trainingResultsRepository.getAllResults();

        List<TrainingResults> breaststrokeResults = new ArrayList<>();

        // Filter results for Breaststroke discipline
        for (TrainingResults result : breathstrokeList) {
            if (result.getActivityType().equals(ActivityType.BREASTSTROKE)) {
                breaststrokeResults.add(result);
            }
        }

        // If no results for Breaststroke, print a message
        if (breaststrokeResults.isEmpty()) {
            System.out.println("No Breaststroke results found.");
        } else {
            // Sort results by the BreaststrokeComparator (best time first)
            breaststrokeResults.sort(new BreaststrokeComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(breaststrokeResults.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(breaststrokeResults.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Butterfly discipline (Senior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5Butterfly() {
        List<TrainingResults> butterflyList = trainingResultsRepository.getAllResults();

        List<TrainingResults> butterflyResults = new ArrayList<>();

        // Filter results for Butterfly discipline
        for (TrainingResults result : butterflyList) {
            if (result.getActivityType().equals(ActivityType.BUTTERFLY)) {
                butterflyResults.add(result);
            }
        }

        // If no results for Butterfly, print a message
        if (butterflyResults.isEmpty()) {
            System.out.println("No Butterfly results found.");
        } else {
            // Sort results by the ButterflyComparator (best time first)
            butterflyResults.sort(new ButterflyComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(butterflyResults.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(butterflyResults.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Crawl discipline (Junior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5CrawlJunior() {
        List<TrainingResults> crawlListJunior = trainingResultsRepository.getAllResults();

        List<TrainingResults> crawlResultsJunior = new ArrayList<>();

        // Filter results for Crawl discipline and junior age (under 18)
        for (TrainingResults result : crawlListJunior) {
            if (result.getActivityType().equals(ActivityType.CRAWL) && result.getMember().getAge() < 18) {
                crawlResultsJunior.add(result);
            }
        }

        // If no results for Crawl Junior, print a message
        if (crawlResultsJunior.isEmpty()) {
            System.out.println("No Crawl Junior results found.");
        } else {
            // Sort results by the CrawlComparator (best time first)
            crawlResultsJunior.sort(new CrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(crawlResultsJunior.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(crawlResultsJunior.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Backcrawl discipline (Junior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5BackcrawlJunior() {
        List<TrainingResults> backcrawlListJunior = trainingResultsRepository.getAllResults();

        List<TrainingResults> backcrawlResultsJunior = new ArrayList<>();

        // Filter results for Backcrawl discipline and junior age (under 18)
        for (TrainingResults result : backcrawlListJunior) {
            if (result.getActivityType().equals(ActivityType.BACKCRAWL) && result.getMember().getAge() < 18) {
                backcrawlResultsJunior.add(result);
            }
        }

        // If no results for Backcrawl Junior, print a message
        if (backcrawlResultsJunior.isEmpty()) {
            System.out.println("No Backcrawl Junior results found.");
        } else {
            // Sort results by the CrawlComparator (best time first)
            backcrawlResultsJunior.sort(new CrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(backcrawlResultsJunior.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(backcrawlResultsJunior.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Breaststroke discipline (Junior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5BreaststrokeJunior() {
        List<TrainingResults> breaststrokeListJunior = trainingResultsRepository.getAllResults();

        List<TrainingResults> breaststrokeResultsJunior = new ArrayList<>();

        // Filter results for Breaststroke discipline and junior age (under 18)
        for (TrainingResults result : breaststrokeListJunior) {
            if (result.getActivityType().equals(ActivityType.BREASTSTROKE) && result.getMember().getAge() < 18) {
                breaststrokeResultsJunior.add(result);
            }
        }

        // If no results for Breaststroke Junior, print a message
        if (breaststrokeResultsJunior.isEmpty()) {
            System.out.println("No Breaststroke Junior results found.");
        } else {
            // Sort results by the CrawlComparator (best time first)
            breaststrokeResultsJunior.sort(new CrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(breaststrokeResultsJunior.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(breaststrokeResultsJunior.get(i).toString());
            }
        }
    }

    /**
     * Displays the top 5 training results for the Butterfly discipline (Junior).
     * Sorts the results based on time and prints the top performers.
     */
    public void top5ButterflyJunior() {
        List<TrainingResults> butterflyListJunior = trainingResultsRepository.getAllResults();

        List<TrainingResults> butterflyResultsJunior = new ArrayList<>();

        // Filter results for Butterfly discipline and junior age (under 18)
        for (TrainingResults result : butterflyListJunior) {
            if (result.getActivityType().equals(ActivityType.BUTTERFLY) && result.getMember().getAge() < 18) {
                butterflyResultsJunior.add(result);
            }
        }

        // If no results for Butterfly Junior, print a message
        if (butterflyResultsJunior.isEmpty()) {
            System.out.println("No Butterfly Junior results found.");
        } else {
            // Sort results by the CrawlComparator (best time first)
            butterflyResultsJunior.sort(new CrawlComparator());

            // Display top 5 results (or fewer if there aren't enough)
            int topResultsCount = Math.min(butterflyResultsJunior.size(), 5);
            for (int i = 0; i < topResultsCount; i++) {
                System.out.println(butterflyResultsJunior.get(i).toString());
            }
        }
    }
}

