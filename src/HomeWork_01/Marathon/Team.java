package HomeWork_01.Marathon;

public class Team {
    private final String name;
    private Competitor[] competitors;

    public Team(String name, Competitor... competitors) {
        this.name = name;
        this.competitors = competitors;
    }

    public void showResults(){
        for(Competitor comp : competitors) {
            comp.printInfo();
        }
    }

    public Competitor[] getCompetitors() {
        Competitor[] copyComp = competitors;
        return copyComp;
    }
}
