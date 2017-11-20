package model;

/**
 * Created by Admin on 11/16/2017.
 */

public class Figure {
    private String name;
    private String url;

    public Figure(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
