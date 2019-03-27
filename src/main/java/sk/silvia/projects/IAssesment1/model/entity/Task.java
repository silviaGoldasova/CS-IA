package sk.silvia.projects.IAssesment1.model.entity;

import sun.awt.image.ImageWatched;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int duration;
    private String taskCategory;
    private boolean selected;
    private boolean forUpload;

    public Task() {
        this.name = "";
        this.duration = 0;
        this.taskCategory = "Other";
        selected = false;
        forUpload = false;
    }

    public Task(String name, int duration, String taskCategory, boolean select) {
        this.name = name;
        this.duration = duration;
        this.taskCategory = taskCategory;
        this.selected = selected;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isForUpload() {
        return forUpload;
    }

    public void setForUpload(boolean forUpload) {
        this.forUpload = forUpload;
    }

}
