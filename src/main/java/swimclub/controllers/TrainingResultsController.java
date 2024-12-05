package swimclub.controllers;

import swimclub.models.*;
import swimclub.services.TrainingResultsService;

import java.util.List;

public class TrainingResultsController {
    private final TrainingResultsService trainingService;

    public TrainingResultsController(TrainingResultsService trainingService){
        this.trainingService = trainingService;
    }

    public void addTrainingResults(Member member, String activityType,int length, double time, String date,MembershipLevel level){
        ActivityTypeData activity = ActivityTypeData.fromString(activityType);
        if (member.getAge() < 18){
            level = MembershipLevel.JUNIOR;
            trainingService.addResult(member,activity.toActivityType(),length,time,date,level);
        } else if (member.getAge() > 18){
            level = MembershipLevel.SENIOR;
            trainingService.addResult(member,activity.toActivityType(),length,time,date,level);
        }

    }

    public List<TrainingResults> getResultsByMember(Member member){
        return trainingService.getResultsByMember(member);
    }
    public List<TrainingResults> getAllResults(){
        return trainingService.getAllResults();
    }
}
