package com.example.michel.rest_api.mappers;

import com.example.michel.rest_api.models.auxiliary_models.CycleAndMeasurementComby;
import com.example.michel.rest_api.models.auxiliary_models.CycleDBInsertEntry;
import com.example.michel.rest_api.models.measurement.MeasurementReminderCourse;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CycleAndMeasurementCombyRowMapper implements RowMapper<CycleAndMeasurementComby> {

    private ConvertingUtils convertingUtils;

    @Autowired
    public CycleAndMeasurementCombyRowMapper(ConvertingUtils convertingUtils){
        this.convertingUtils =convertingUtils;
    }

    @Override
    public CycleAndMeasurementComby mapRow(ResultSet resultSet, int i) throws SQLException {
        CycleAndMeasurementComby cycleAndMeasurementComby = new CycleAndMeasurementComby();
        MeasurementReminderCourse mrc = new MeasurementReminderCourse();

        mrc.setIdMeasurementReminder(convertingUtils.convertBytesToUUID(
                resultSet.getBytes("_id_measurement_reminder")));
        mrc.setIdMeasurementType(resultSet.getInt("_id_measurement_type"));
        mrc.setStartDate(resultSet.getTimestamp("start_date"));
        UUID idCycle = convertingUtils.convertBytesToUUID(resultSet.getBytes("_id_cycle"));
        mrc.setIdCycle(idCycle); // validate!!!
        mrc.setIdHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        mrc.setHavingMealsTime(resultSet.getInt("having_meals_time"));
        mrc.setAnnotation(resultSet.getString("annotation"));
        mrc.setIsActive(resultSet.getInt("is_active"));
        //int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));
        cycleAndMeasurementComby.setMeasurementReminderCourse(mrc);

        CycleDBInsertEntry cdbie = new CycleDBInsertEntry();

        cdbie.setPeriod(resultSet.getInt("period"));
        cdbie.setPeriodDMType(resultSet.getInt("period_DM_type"));
        cdbie.setOnceAPeriod(resultSet.getInt("once_a_period"));
        cdbie.setOnceAPeriodDMType(resultSet.getInt("once_a_period_DM_type"));
        cdbie.setIdCyclingType(resultSet.getInt("_id_cycling_type"));

        byte[] blob = resultSet.getBytes("_id_week_schedule");
        if (blob!=null)
        {
            cdbie.setIdWeekSchedule(convertingUtils.convertBytesToUUID(blob));
        }
        cdbie.setIdCycle(idCycle);
        cycleAndMeasurementComby.setCycleDBInsertEntry(cdbie);

        return cycleAndMeasurementComby;
    }
}
