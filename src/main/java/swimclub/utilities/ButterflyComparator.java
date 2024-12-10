package swimclub.utilities;

import swimclub.models.TrainingResults;
import swimclub.repositories.TrainingResultsRepository;

import java.util.Comparator;

/**
 * Comparator class for comparing two {@link TrainingResults} based on the time spent in the butterfly stroke activity.
 * This comparator is used to sort training results in ascending order of time.
 */
public class ButterflyComparator implements Comparator<TrainingResults> {

    /**
     * Compares two {@link TrainingResults} objects based on the time.
     * The comparison is done in ascending order, meaning a result with a lower time is considered "less than" one with a higher time.
     *
     * @param o1 The first {@link TrainingResults} to compare.
     * @param o2 The second {@link TrainingResults} to compare.
     * @return A negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(TrainingResults o1, TrainingResults o2) {
        return Double.compare(o1.getTime(), o2.getTime());
    }
}
