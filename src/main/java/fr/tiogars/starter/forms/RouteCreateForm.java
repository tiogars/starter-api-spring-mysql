package fr.tiogars.starter.forms;

public class RouteCreateForm {

    private String name;

    private String path;

    public RouteCreateForm() {
    }

    public RouteCreateForm(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
