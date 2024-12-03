package swimclub.models;



public class CompetitionResults {
    private Member member;
    private String event;
    private int placement;
    private double time;


    public CompetitionResults(Member member, String event, int placement, double time) {

        this.member = member;
        this.event = event;
        this.placement = placement;
        this.time = time;


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




    @Override
    public String toString() {
        return "Member: " + member.getName() + ", Event: " + event + ", Placement: " + placement + ", Time: " + time;
    }
}
