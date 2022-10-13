package data;

public enum Key {

    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    AUDIENCE("audience"),
    CLOUD_ID("cloud_id"),
    AUTHORIZATION_URL("authorization_url");

    private String text;

    Key(String key) {
        this.text = key;
    }

    public String getText() {
        return text;
    }
}
