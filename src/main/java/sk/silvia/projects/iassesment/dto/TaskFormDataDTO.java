package sk.silvia.projects.iassesment.dto;

public class TaskFormDataDTO {
    private String name;
    private String duration;
    private String taskCategory;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTaskCategory() {
        return taskCategory;
    }
    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }
}
