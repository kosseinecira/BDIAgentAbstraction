
public abstract class Agent extends Thread {
    /*
     * The agent has a name
     * it has a collection of beliefs
     * it has a collection of plans
     * it has a collection of Goals (desires)
     * it has a mean to choose the appropriate plan to achieve an intention
     * it has an environment to perceive
     * it has sensor to take input (constructor)
     * it has an effector (to make change in the environment)
     * */
    private String agentName;
    private HashMap<String, Belief> beliefSet = new HashMap();
    private HashMap<String, Desire> desireSet = new HashMap<>();
    //Handler as environment
    private AppCompatActivity environment;
    //plans repository
    private HashMap<String, Plan> plans = new HashMap<>();

    public Agent(String agentName, AppCompatActivity environment) {
        this.agentName = agentName;
        this.environment = environment;
    }

    public Agent(String agentName, AppCompatActivity environment, BlockingQueue<Object> messagesQueue) {
        this.agentName = agentName;
        this.environment = environment;
    }


    public abstract void sensor();

    public abstract void effector();


    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public HashMap<String, Belief> getBeliefSet() {
        return beliefSet;
    }

    public void setBeliefSet(HashMap<String, Belief> beliefSet) {
        this.beliefSet = beliefSet;
    }

    public HashMap<String, Desire> getDesireSet() {
        return desireSet;
    }

    public void setDesireSet(HashMap<String, Desire> desireSet) {
        this.desireSet = desireSet;
    }

    public HashMap<String, Plan> getPlans() {
        return plans;
    }

    public void setPlans(HashMap<String, Plan> plans) {
        this.plans = plans;
    }


    public AppCompatActivity getEnvironment() {
        return environment;
    }

    public void setEnvironment(AppCompatActivity environment) {
        this.environment = environment;
    }

    @Override
    public abstract void run();

    //check any contradiction
    public abstract void beliefRevisionFunction(HashMap<String, Belief> beliefSet, Belief belief);

    // choose the right beliefs and it's plans to execute based on belief set.
    public abstract void optionsGenerationFunction(HashMap<String, Belief> beliefSet, HashMap<String, Desire> desireSet);

    // filter function is used to reduce options by refining it , narrow it down! and choosing a goal to commit
    public abstract Desire filter(HashMap<String, Belief> beliefSet, HashMap<String, Desire> desireSet);

    public abstract void plan(HashMap<String, Belief> beliefSet, Desire committedGoal);

}
