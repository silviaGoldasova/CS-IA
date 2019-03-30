package sk.silvia.projects.iassesment.dto;

public class ScheduleFormDTO {
    private String sessionLength;
    private String breakLength;
    private String breakFrequency;
    private String categorySelected;

    public ScheduleFormDTO() {
        categorySelected = "Other";
    }

    public String getSessionLength() {
        return sessionLength;
    }
    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getBreakLength() {
        return breakLength;
    }
    public void setBreakLength(String breakLength) {
        this.breakLength = breakLength;
    }

    public String getBreakFrequency() {
        return breakFrequency;
    }
    public void setBreakFrequency(String breakFrequency) {
        this.breakFrequency = breakFrequency;
    }

    public String getCategorySelected() {
        return categorySelected;
    }
    public void setCategorySelected(String categorySelected) {
        this.categorySelected = categorySelected;
    }

}
