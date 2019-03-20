package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IMeasurementReminderEntryFDao;
import com.example.michel.rest_api.mappers.MeasurementReminderEntryFRowMapper;
import com.example.michel.rest_api.models.auxiliary_models.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.auxiliary_models.UpdatePillReminderBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
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
public class MeasurementReminderEntryFDao implements IMeasurementReminderEntryFDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MeasurementReminderEntryF> getMeasurementReminderEntriesByDate(Date date) {
        String sql = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date " +
                " from measurement_reminder_entry mre inner join measurement_reminder mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_type mt on mr._id_measurement_type=mt._id_measurement_type " +
                " inner join measurement_value_type mvt on mt._id_measur_value_type=mvt._id_measur_value_type where DATE(mre.reminder_date)=DATE(?) and (mr.is_active=1 or mre.is_done=1) ORDER BY mre.is_done";

        return jdbcTemplate.query(sql, new MeasurementReminderEntryFRowMapper(), date);
    }

    @Override
    public int updateIsDoneMeasurementReminderEntry(UpdateMeasurementReminderBody updateMeasurementReminderBody) {
        String sql = "UPDATE measurement_reminder_entry SET reminder_date = ?, is_done = ?, value1 = ?, value2 = ?, change_type = ?, synch_time = ? " +
                "WHERE _id_measur_remind_entry = ?";
        byte[] uuid = ByteBuffer.allocate(16).putLong(updateMeasurementReminderBody.getId().getMostSignificantBits())
                .putLong(updateMeasurementReminderBody.getId().getLeastSignificantBits()).array();

        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setTimestamp(1, new Timestamp(updateMeasurementReminderBody.getDate().getTime()));
                preparedStatement.setInt(2, updateMeasurementReminderBody.getIsDone());
                preparedStatement.setDouble(3, updateMeasurementReminderBody.getValue1());
                preparedStatement.setDouble(4, updateMeasurementReminderBody.getValue2());
                preparedStatement.setInt(5, 2);
                preparedStatement.setTimestamp(6, new Timestamp(new Date().getTime()));
                preparedStatement.setBytes(7, uuid);
            }
        });
    }
}
