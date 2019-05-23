package com.example.michel.rest_api.dao.mappers;

import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import org.springframework.jdbc.core.RowMapper;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class PillReminderEntryFRowMapper implements RowMapper<PillReminderEntryF> {
    @Override
    public PillReminderEntryF mapRow(ResultSet resultSet, int i) throws SQLException {
        PillReminderEntryF pillReminderEntryF = new PillReminderEntryF();

        byte[] binStr = resultSet.getBytes("_id_pill_reminder_entry");
        ByteBuffer bbr = ByteBuffer.wrap(binStr);
        long high = bbr.getLong();
        long low = bbr.getLong();

        pillReminderEntryF.setId(new UUID(high, low));
        pillReminderEntryF.setPillName(resultSet.getString("pill_name"));
        pillReminderEntryF.setPillCount(resultSet.getInt("pill_count"));
        pillReminderEntryF.setPillCountType(resultSet.getString("type_name"));
        pillReminderEntryF.setDate(new Date(resultSet.getTimestamp("reminder_date").getTime()));
        pillReminderEntryF.setHavingMealsType(resultSet.getInt("_id_having_meals_type"));
        int havingMealsTimeStr = resultSet.getInt("having_meals_time");
        pillReminderEntryF.setHavingMealsTime(new Date(pillReminderEntryF.getDate().getTime()+
            havingMealsTimeStr*60*1000));
        pillReminderEntryF.setIsDone(resultSet.getInt("is_done"));

        return pillReminderEntryF;
    }
}
