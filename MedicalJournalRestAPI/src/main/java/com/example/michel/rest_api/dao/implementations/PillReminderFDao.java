package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IPillReminderFDao;
import com.example.michel.rest_api.dao.mappers.CycleAndPillCombyRowMapper;
import com.example.michel.rest_api.dao.mappers.PillReminderFRowMapper;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndPillComby;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class PillReminderFDao implements IPillReminderFDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConvertingUtils convertingUtils;

    @Override
    public List<PillReminderF> getAllPillRemindersF(int userId) {
        String sql = "select pr._id_pill_reminder, pr._id_having_meals_type, pr.pill_count, pct.type_name, pi.pill_name, pr.start_date, pr.is_active, cl.period, pr.times_a_day, "+
                "cl.period_DM_type,(select COUNT(*) from pill_reminder_entry pre where pre.change_type<3 and pre._id_pill_reminder=pr._id_pill_reminder and pre.is_done=0 and" +
                " pr.user_id=? ) as count_left from pill_reminder pr inner join pill pi on pi._id_pill=pr._id_pill inner join pill_count_type pct on pr._id_pill_count_type=pct._id_pill_count_type "+
                "inner join cycle cl on pr._id_cycle=cl._id_cycle where pr.change_type<3 and pr.user_id=? ORDER BY pr.is_active DESC, pi.pill_name";

        return jdbcTemplate.query(sql, new PillReminderFRowMapper(), new Object[]{userId, userId});
    }

    @Override
    public CycleAndPillComby getCycleAndPillCombyByID(UUID prID, int userId) {
        String sql = "select pr._id_pill_reminder, pi.pill_name, pr.pill_count, pr._id_pill_count_type, pr.start_date, pr._id_having_meals_type, pr.having_meals_time, pr.annotation," +
                " pr.is_active, pr.times_a_day, pr._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_cycling_type, cl._id_week_schedule" +
                " from pill_reminder pr inner join pill pi on pi._id_pill=pr._id_pill inner join cycle cl on pr._id_cycle=cl._id_cycle" +
                " where pr.change_type<3 and pr.user_id=? and pr._id_pill_reminder = ?";
        return jdbcTemplate.queryForObject(sql, new CycleAndPillCombyRowMapper(convertingUtils), new Object[]{userId, convertingUtils.convertUUIDToBytes(prID)});
    }
}
