import android.content.Context;

public abstract class Plan {
    private String name;
    private Desire goal;
    private Agent agent;
    private Context environment;

    public Plan(String name, Desire goal, Agent agent, Context environment) {
        this.name = name;
        this.goal = goal;
        this.agent = agent;
        this.environment = environment;
    }

    public abstract void execute(Object... args);

    public abstract boolean preConditions(Object... args);

    public abstract void postConditions(Object... args);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Desire getGoal() {
        return goal;
    }

    public void setGoal(Desire goal) {
        this.goal = goal;
    }

    public Agent getAgent() {
        return this.agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Context getEnvironment() {
        return environment;
    }

    public void setEnvironment(Context environment) {
        this.environment = environment;
    }
}
