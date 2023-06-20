public class StoragePermission {

    private boolean granted;

    public StoragePermission(boolean granted) {
        this.granted = granted;
    }

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean check) {
        this.granted = check;
    }

}
