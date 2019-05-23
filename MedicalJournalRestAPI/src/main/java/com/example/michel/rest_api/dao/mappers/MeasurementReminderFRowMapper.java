package com.example.michel.rest_api.dao.mappers;

import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import org.springframework.jdbc.core.RowMapper;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MeasurementReminderFRowMapper implements RowMapper<MeasurementReminderF> {
    @Override
    public MeasurementReminderF mapRow(ResultSet resultSet, int i) throws SQLException {
        MeasurementReminderF measurementReminderF = new MeasurementReminderF();

        Calendar calendar = Calendar.getInstance();

        byte[] binStr = resultSet.getBytes("_id_measurement_reminder");
        ByteBuffer bbr = ByteBuffer.wrap(binStr);
        long high = bbr.getLong();
        long low = bbr.getLong();

        Date startDate = resultSet.getDate("start_date");
        int period = resultSet.getInt("period");
        int periodDMType = resultSet.getInt("period_DM_type");

        measurementReminderF.setId(new UUID(high, low));
        measurementReminderF.setHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        measurementReminderF.setIsActive(resultSet.getInt("is_active"));
        measurementReminderF.setNumberOfDoingActionLeft(resultSet.getInt("count_left"));
        measurementReminderF.setNumberOfDoingAction(resultSet.getInt("times_a_day"));
        measurementReminderF.setMeasurementTypeName(resultSet.getString("type_name"));
        measurementReminderF.setIdMeasurementType(resultSet.getInt("_id_measurement_type"));

        SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd.MM.yyyy");
        String startDateStr = dateFormatNew.format(startDate);

        String endDateStr = "00.00.0000";
        calendar.setTime(startDate);
        switch (periodDMType){
            case 1:
                calendar.add(Calendar.DATE, period);
                break;
            case 2:
                calendar.add(Calendar.DATE, period*7);
                break;
            case 3:
                calendar.add(Calendar.DATE, period*30);
                break;
        }
        endDateStr = dateFormatNew.format(calendar.getTime());
        measurementReminderF.setStartDate(startDateStr);
        measurementReminderF.setEndDate(endDateStr);

        return measurementReminderF;
    }
}
