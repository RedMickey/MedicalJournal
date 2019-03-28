package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IPillReminderEntryFDao;
import com.example.michel.rest_api.mappers.PillReminderEntryFRowMapper;
import com.example.michel.rest_api.models.auxiliary_models.UpdatePillReminderBody;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public class PillReminderEntryFDao implements IPillReminderEntryFDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date, int userId) {
        String sql = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                " from pill_reminder_entry pre inner join pill_reminder pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pill pi on pi._id_pill=pr._id_pill inner join" +
                " pill_count_type pct on pr._id_pill_count_type=pct._id_pill_count_type where DATE(pre.reminder_date)=DATE(?) and (pr.is_active=1 or pre.is_done=1)" +
                " and pre.change_type<3 and pr.user_id=? ORDER BY pre.is_done";

        return jdbcTemplate.query(sql, new PillReminderEntryFRowMapper(), new Object[]{date, userId});
    }

    @Override
    public int updateIsDonePillReminderEntry(UpdatePillReminderBody updatePillReminderBody) {
        String sql = "UPDATE pill_reminder_entry SET reminder_date = ?, is_done = ?, change_type = ?, synch_time = ? " +
                "WHERE _id_pill_reminder_entry = ?";
        byte[] uuid = ByteBuffer.allocate(16).putLong(updatePillReminderBody.getId().getMostSignificantBits())
                .putLong(updatePillReminderBody.getId().getLeastSignificantBits()).array();

        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setTimestamp(1, new Timestamp(updatePillReminderBody.getDate().getTime()));
                preparedStatement.setInt(2, updatePillReminderBody.getIsDone());
                preparedStatement.setInt(3, 2);
                preparedStatement.setTimestamp(4, new Timestamp(new Date().getTime()));
                preparedStatement.setBytes(5, uuid);

            }
        });
    }
}
