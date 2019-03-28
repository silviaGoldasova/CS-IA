package sk.silvia.projects.iassesment.dto;

public class TaskFormDataDTO {
    private String name;
    private int duration;
    private String taskCategory;

    public TaskFormDataDTO name(String name) {
        this.name = name;
        return this;
    }

    public TaskFormDataDTO duration(int duration) {
        this.duration = duration;
        return this;
    }

    public TaskFormDataDTO taskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
        return this;
    }


    public static void test() {
        TaskFormDataDTO t1 = new TaskFormDataDTO();
        t1.setName("name1");
        t1.setDuration(1);
        t1.setTaskCategory("category1");


        TaskFormDataDTO t2 = new TaskFormDataDTO()
                                        .name("name2")
                                        .duration(2)
                                        .taskCategory("category2");
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
}
