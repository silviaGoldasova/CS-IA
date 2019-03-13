package sk.silvia.projects.IAssesment1.model;

public class ScheduleFormDTO {
    private int sessionLength;
    private int breakLength;
    private int breakFrequency;
    private String categorySelected;

    public ScheduleFormDTO() {
        sessionLength = 0;
        breakLength = 0;
        breakFrequency = 0;
        categorySelected = "Other";
    }

    public int getSessionLength() {
        return sessionLength;
    }
    public void setSessionLength(int sessionLength) {
        this.sessionLength = sessionLength;
    }

    public int getBreakLength() {
        return breakLength;
    }
    public void setBreakLength(int breakLength) {
        this.breakLength = breakLength;
    }

    public int getBreakFrequency() {
        return breakFrequency;
    }
    public void setBreakFrequency(int breakFrequency) {
        this.breakFrequency = breakFrequency;
    }

    public String getCategorySelected() {
        return categorySelected;
    }
    public void setCategorySelected(String categorySelected) {
        this.categorySelected = categorySelected;
    }

}
