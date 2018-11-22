package com.example.michel.mycalendar2.models;

public class ItemReminderModel extends ReminderModel {
    private int isActive;
    private int numberOfDoingAction;
    private String startDate;
    private String endDate;
    private int numberOfDoingActionLeft;

    public ItemReminderModel(int id, int havingMealsType, int isActive,
                             int numberOfDoingAction, String startDate,
                             String endDate, int numberOfDoingActionLeft)
    {
        super(id, havingMealsType);
        this.isActive = isActive;
        this.numberOfDoingAction = numberOfDoingAction;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDoingActionLeft = numberOfDoingActionLeft;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getNumberOfDoingAction() {
        return numberOfDoingAction;
    }

    public void setNumberOfDoingAction(int numberOfDoingAction) {
        this.numberOfDoingAction = numberOfDoingAction;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfDoingActionLeft() {
        return numberOfDoingActionLeft;
    }

    public void setNumberOfDoingActionLeft(int numberOfDoingActionLeft) {
        this.numberOfDoingActionLeft = numberOfDoingActionLeft;
    }
}
