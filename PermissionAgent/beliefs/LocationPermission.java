public class LocationPermission {
    private boolean granted;

    public LocationPermission(boolean granted) {
        this.granted = granted;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean check) {
        this.granted = check;
    }

}
