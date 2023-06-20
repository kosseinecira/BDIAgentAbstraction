public class HandleStoragePermissionPlan extends Plan {
    private PermissionActivity localEnvironment;

    public HandleStoragePermissionPlan(String name, Desire goal, Agent agent, AppCompatActivity environment) {
        super(name, goal, agent, environment);
        this.localEnvironment = (PermissionActivity) getEnvironment();
    }

    @Override
    public void execute(Object... args) {
        try {
            Thread.sleep(1000);
            System.out.println("in storagePermissionPlan");
        } catch (Exception e) {

        }
        getLocalEnvironment().requestStoragePermission();

        try {
            synchronized (getLocalEnvironment()) {
                //waiting for it to finish
                getLocalEnvironment().wait();
            }
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
                System.out.println("in storagepermissionplan error occured!!");
            } catch (Exception ex) {

            }
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
