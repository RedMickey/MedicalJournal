package com.example.michel.rest_api.dao.mappers;

import com.example.michel.rest_api.models.auxiliary_models.CycleAndPillComby;
import com.example.michel.rest_api.models.auxiliary_models.CycleDBInsertEntry;
import com.example.michel.rest_api.models.pill.PillReminderCourse;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CycleAndPillCombyRowMapper implements RowMapper<CycleAndPillComby> {

    private ConvertingUtils convertingUtils;

    @Autowired
    public CycleAndPillCombyRowMapper(ConvertingUtils convertingUtils){
        this.convertingUtils =convertingUtils;
    }

    @Override
    public CycleAndPillComby mapRow(ResultSet resultSet, int i) throws SQLException {
        CycleAndPillComby cycleAndPillComby = new CycleAndPillComby();
        PillReminderCourse prc = new PillReminderCourse();

        prc.setPillName(resultSet.getString("pill_name"));
        prc.setPillCount(resultSet.getInt("pill_count"));
        prc.setIdPillCountType(resultSet.getInt("_id_pill_count_type"));
        prc.setIdPillReminder(convertingUtils.convertBytesToUUID(
                resultSet.getBytes("_id_pill_reminder")));
        prc.setStartDate(resultSet.getTimestamp("start_date"));
        UUID idCycle = convertingUtils.convertBytesToUUID(resultSet.getBytes("_id_cycle"));
        prc.setIdCycle(idCycle); // validate!!!
        prc.setIdHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        prc.setHavingMealsTime(resultSet.getInt("having_meals_time"));
        prc.setAnnotation(resultSet.getString("annotation"));
        prc.setIsActive(resultSet.getInt("is_active"));
        //int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));
        cycleAndPillComby.setPillReminderCourse(prc);

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
        cycleAndPillComby.setCycleDBInsertEntry(cdbie);

        return cycleAndPillComby;
    }
}
