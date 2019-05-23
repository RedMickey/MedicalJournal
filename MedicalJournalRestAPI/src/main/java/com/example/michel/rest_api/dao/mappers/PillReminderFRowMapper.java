package com.example.michel.rest_api.dao.mappers;

import com.example.michel.rest_api.models.pill.PillReminderF;
import org.springframework.jdbc.core.RowMapper;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PillReminderFRowMapper implements RowMapper<PillReminderF> {
    @Override
    public PillReminderF mapRow(ResultSet resultSet, int i) throws SQLException {
        PillReminderF pillReminderF = new PillReminderF();

        Calendar calendar = Calendar.getInstance();

        byte[] binStr = resultSet.getBytes("_id_pill_reminder");
        ByteBuffer bbr = ByteBuffer.wrap(binStr);
        long high = bbr.getLong();
        long low = bbr.getLong();

        Date startDate = resultSet.getDate("start_date");
        int period = resultSet.getInt("period");
        int periodDMType = resultSet.getInt("period_DM_type");

        pillReminderF.setId(new UUID(high, low));
        pillReminderF.setPillName(resultSet.getString("pill_name"));
        pillReminderF.setPillCount(resultSet.getInt("pill_count"));
        pillReminderF.setPillCountType(resultSet.getString("type_name"));
        pillReminderF.setHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        pillReminderF.setIsActive(resultSet.getInt("is_active"));
        pillReminderF.setNumberOfDoingActionLeft(resultSet.getInt("count_left"));
        pillReminderF.setNumberOfDoingAction(resultSet.getInt("times_a_day"));

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
        pillReminderF.setStartDate(startDateStr);
        pillReminderF.setEndDate(endDateStr);

        return pillReminderF;
    }
}
