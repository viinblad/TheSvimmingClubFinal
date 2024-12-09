package swimclub.utilities;

import swimclub.models.TrainingResults;
import swimclub.repositories.TrainingResultsRepository;

import java.util.Comparator;

public class ButterflyComparator implements Comparator<TrainingResults> {
    @Override
    public int compare(TrainingResults o1, TrainingResults o2) {
        return Double.compare(o1.getTime(), o2.getTime()) ;

    }
}
