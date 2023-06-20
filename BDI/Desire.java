public class Desire {
    private int priority;
    private String name;
    private DesireState currentDesireState;

    public Desire(String name, int priority) {
        this.name = name;
        this.priority = priority;
        this.currentDesireState = DesireState.UN_ACHIEVED;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DesireState getCurrentState() {
        return this.currentDesireState;
    }

    public void setCurrentState(DesireState currentDesireState) {
        this.currentDesireState = currentDesireState;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Desire{" +
                "priority=" + priority +
                ", name='" + name + '\'' +
                ", currentDesireState=" + currentDesireState +
                '}';
    }
}
