package uoa.lavs.customer;

public class ErrorClass {
    // online 0 = offline, 1 = unsynced, 2 = online
    private int online;
    private String message;

    public ErrorClass() {
        this.online = 0;
        this.message = "";
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnline() {
        return online;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}