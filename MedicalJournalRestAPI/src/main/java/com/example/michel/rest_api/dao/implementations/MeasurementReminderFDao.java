package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IMeasurementReminderFDao;
import com.example.michel.rest_api.mappers.CycleAndMeasurementCombyRowMapper;
import com.example.michel.rest_api.mappers.MeasurementReminderFRowMapper;
import com.example.michel.rest_api.mappers.PillReminderFRowMapper;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndMeasurementComby;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class MeasurementReminderFDao implements IMeasurementReminderFDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConvertingUtils convertingUtils;

    @Override
    public List<MeasurementReminderF> getAllMeasurementRemindersF(int userId) {
        String sql = "select  mr.start_date, mr.is_active, cl.period, mr.times_a_day, mt.type_name, mr._id_having_meals_type, cl.period_DM_type, " +
                "(select COUNT(*) from measurement_reminder_entry mre where mre._id_measurement_reminder=mr._id_measurement_reminder and mre.is_done=0 and mre.change_type<3 " +
                "and mr.user_id=?) as count_left, mr._id_measurement_reminder, mr._id_measurement_type " +
                "from measurement_reminder mr inner join cycle cl on mr._id_cycle=cl._id_cycle inner join measurement_type mt on mr._id_measurement_type=mt._id_measurement_type " +
                "where mr.change_type<3 and mr.user_id=? ORDER BY mr.is_active DESC";

        return jdbcTemplate.query(sql, new MeasurementReminderFRowMapper(), new Object[]{userId, userId});
    }

    @Override
    public CycleAndMeasurementComby getCycleAndMeasurementCombyById(UUID mrID, int userId) {
        String sql = "select mr._id_measurement_reminder, mr._id_measurement_type, mt._id_measur_value_type, mr.start_date, mr._id_having_meals_type, mr.having_meals_time, mr.annotation," +
                " mr.is_active, mr.times_a_day, mr._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_cycling_type, cl._id_week_schedule " +
                " from measurement_reminder mr inner join cycle cl on mr._id_cycle=cl._id_cycle inner join measurement_type mt on mr._id_measurement_type=mt._id_measurement_type " +
                " where mr.change_type<3 and mr.user_id=? and mr._id_measurement_reminder = ?";
        return jdbcTemplate.queryForObject(sql, new CycleAndMeasurementCombyRowMapper(convertingUtils), new Object[]{userId, convertingUtils.convertUUIDToBytes(mrID)});
    }
}
