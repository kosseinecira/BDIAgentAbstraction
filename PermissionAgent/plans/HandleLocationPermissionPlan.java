public class HandleLocationPermissionPlan extends Plan {
    private PermissionActivity localEnvironment;

    public HandleLocationPermissionPlan(String name, Desire goal, Agent agent, AppCompatActivity environment) {
        super(name, goal, agent, environment);
        this.localEnvironment = (PermissionActivity) getEnvironment();
    }

    // to get ride of casting , we could use generics!
    @Override
    public void execute(Object... args) {
        //getLocalEnvironment().getLastLocation();
        getLocalEnvironment().requestLocationPermission();
        try {
            synchronized (getLocalEnvironment()) {
                //waiting for it to finish
                getLocalEnvironment().wait();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean preConditions(Object... args) {
        return false;
    }

    @Override
    public void postConditions(Object... args) {
    }

    public PermissionActivity getLocalEnvironment() {
        return localEnvironment;
    }

    public void setLocalEnvironment(PermissionActivity localEnvironment) {
        this.localEnvironment = localEnvironment;
    }


}
