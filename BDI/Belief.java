
public class Belief<T> {
    private String beliefName;
    //  private beliefValueType belief;
    // the value of the belief is of type belief means it is a class extend this class (subclass)
    private T belief;

    public Belief(String beliefName, T beliefValue) {
        this.beliefName = beliefName;
        this.belief = beliefValue;
    }

    //default constructor for beliefs

    public T getBeliefValue() {
        return belief;
    }

    public String getBeliefName() {
        return
                this.beliefName;
    }

    public void setBeliefName(String beliefName) {
        this.beliefName = beliefName;
    }

    public void setBelief(T belief) {
        this.belief = belief;
    }

    @Override
    public String toString() {
        return "Belief{" +
                "beliefName='" + beliefName + '\'' +
                ", belief=" + belief +
                '}';
    }
}


