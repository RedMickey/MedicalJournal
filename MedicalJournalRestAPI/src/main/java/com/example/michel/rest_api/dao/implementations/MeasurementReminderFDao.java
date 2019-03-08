package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IMeasurementReminderFDao;
import com.example.michel.rest_api.mappers.MeasurementReminderFRowMapper;
import com.example.michel.rest_api.mappers.PillReminderFRowMapper;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class MeasurementReminderFDao implements IMeasurementReminderFDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MeasurementReminderF> getAllMeasurementRemindersF() {
        String sql = "select  mr.start_date, mr.is_active, cl.period, mr.times_a_day, mt.type_name, mr._id_having_meals_type, cl.period_DM_type, " +
                "(select COUNT(*) from measurement_reminder_entry mre where mre._id_measurement_reminder=mr._id_measurement_reminder and mre.is_done=0 ) " +
                "as count_left, mr._id_measurement_reminder, mr._id_measurement_type " +
                "from measurement_reminder mr inner join cycle cl on mr._id_cycle=cl._id_cycle inner join measurement_type mt on mr._id_measurement_type=mt._id_measurement_type ORDER BY mr.is_active DESC";

        return jdbcTemplate.query(sql, new MeasurementReminderFRowMapper());
    }
}
