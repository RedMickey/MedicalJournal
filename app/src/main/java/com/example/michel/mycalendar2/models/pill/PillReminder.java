package com.example.michel.mycalendar2.models.pill;

import com.example.michel.mycalendar2.models.PillModel;

public class PillReminder extends PillModel {
    private int isActive;
    private int countOfTakingMedicine;
    private String startDate;
    private String endDate;
    private int countOfTakingMedicineLeft;

    public PillReminder(int id, String pillName, int pillCount, String pillCountType,
                        int havingMealsType, int isActive, int countOfTakingMedicine,
                        String startDate, String endDate, int countOfTakingMedicineLeft){
        super(id, pillName, pillCount, pillCountType, havingMealsType);
        this.isActive = isActive;
        this.countOfTakingMedicine = countOfTakingMedicine;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countOfTakingMedicineLeft = countOfTakingMedicineLeft;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getCountOfTakingMedicine() {
        return countOfTakingMedicine;
    }

    public void setCountOfTakingMedicine(int countOfTakingMedicine) {
        this.countOfTakingMedicine = countOfTakingMedicine;
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

    public int getCountOfTakingMedicineLeft() {
        return countOfTakingMedicineLeft;
    }

    public void setCountOfTakingMedicineLeft(int countOfTakingMedicineLeft) {
        this.countOfTakingMedicineLeft = countOfTakingMedicineLeft;
    }
}
