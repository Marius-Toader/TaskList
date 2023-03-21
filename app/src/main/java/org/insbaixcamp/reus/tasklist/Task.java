package org.insbaixcamp.reus.tasklist;

public class Task {

    private String name;

    private String description;

    private String urgency;

    private String responsable;

    public Task(String name, String description, String urgency, String responsable) {
        this.name = name;
        this.description = description;
        this.urgency = urgency;
        this.responsable = responsable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
}
