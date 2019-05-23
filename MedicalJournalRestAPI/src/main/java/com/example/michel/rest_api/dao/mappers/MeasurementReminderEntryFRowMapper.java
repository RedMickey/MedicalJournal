package com.example.michel.rest_api.dao.mappers;

import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import org.springframework.jdbc.core.RowMapper;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class MeasurementReminderEntryFRowMapper implements RowMapper<MeasurementReminderEntryF> {
    @Override
    public MeasurementReminderEntryF mapRow(ResultSet resultSet, int i) throws SQLException {
        MeasurementReminderEntryF measurementReminderEntryF = new MeasurementReminderEntryF();

        byte[] binStr = resultSet.getBytes("_id_measur_remind_entry");
        ByteBuffer bbr = ByteBuffer.wrap(binStr);
        long high = bbr.getLong();
        long low = bbr.getLong();

        measurementReminderEntryF.setId(new UUID(high, low));
        measurementReminderEntryF.setValue1(resultSet.getDouble("value1"));
        measurementReminderEntryF.setValue2(resultSet.getDouble("value2"));
        measurementReminderEntryF.setIdMeasurementType(resultSet.getInt("_id_measurement_type"));
        measurementReminderEntryF.setMeasurementTypeName(resultSet.getString("type_name"));
        measurementReminderEntryF.setMeasurementValueTypeName(resultSet.getString("type_value_name"));
        measurementReminderEntryF.setDate(new Date(resultSet.getTimestamp("reminder_date").getTime()));
        measurementReminderEntryF.setHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        int havingMealsTimeStr = resultSet.getInt("having_meals_time");
        measurementReminderEntryF.setHavingMealsTime(new Date(measurementReminderEntryF.getDate().getTime()+
                havingMealsTimeStr*60*1000));
        measurementReminderEntryF.setIsDone(resultSet.getInt("is_done"));

        return measurementReminderEntryF;
    }
}
