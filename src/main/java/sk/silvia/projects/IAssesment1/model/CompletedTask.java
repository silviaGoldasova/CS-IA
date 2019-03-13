package sk.silvia.projects.IAssesment1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class CompletedTask{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private int duration;
    private String taskCategory;
    private LocalDate localDate = LocalDate.now();
    private String localDateString;


    public CompletedTask() {
        this.name = "";
        this.duration = 0;
        this.taskCategory = "Other";
        this.localDateString = "-";
    }

    public CompletedTask(Task task) {
        this.name = task.getName();
        this.duration = task.getDuration();
        this.taskCategory = task.getTaskCategory();
        this.localDateString = "-";
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

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getLocalDateString() {
        return localDateString;
    }

    public void setLocalDateString(String localDateString) {
        this.localDateString = localDateString;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setTodayAsDate() {
        localDate = LocalDate.now();
        localDateString = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }



}