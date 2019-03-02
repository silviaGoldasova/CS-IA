package sk.silvia.projects.IAssesment1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int duration;
    private String taskCategory;

    public String getTaskCategory() {
        return taskCategory;
    }
    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Task() {
        this.name = "";
        this.duration = 0;
        this.taskCategory = "Other";
    }
}
