package swimclub.models;

import java.time.LocalDate;

public class CompetitionResults {
    private int result;
    private Member member;
    private String event;
    private int placement;
    private double time;
    private LocalDate competitionDate;

    public CompetitionResults(int result, Member member, String event, int placement, double time, LocalDate competitionDate) {

        this.result = result;
        this.member = member;
        this.event = event;
        this.placement = placement;
        this.time = time;
        this.competitionDate = competitionDate;


    }

    public int getResult() {
        return result;
    }

    public Member getMember() {
        return member;
    }

    public String getEvent() {
        return event;
    }

    public int getPlacement() {
        return placement;
    }

    public double getTime() {
        return time;
    }

    public LocalDate getCompetitionDate() {
        return competitionDate;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setCompetitionDate(LocalDate competitionDate) {
        this.competitionDate = competitionDate;
    }

    @Override
    public String toString() {
        return "CompetitionResult{" +
                "resultId=" + result +
                ", member=" + member.getName() + // Display the member's name
                ", event='" + event + '\'' +
                ", placement=" + placement +
                ", time=" + time +
                ", competitionDate=" + competitionDate +
                '}';
    }
}
