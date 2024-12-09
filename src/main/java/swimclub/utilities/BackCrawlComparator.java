package swimclub.utilities;

import swimclub.models.*;

import java.util.Comparator;

public class BackCrawlComparator implements Comparator<TrainingResults> {


    @Override
    public int compare(TrainingResults o1, TrainingResults o2) {
        return Double.compare(o1.getTime(), o2.getTime()) ;

    }
}
