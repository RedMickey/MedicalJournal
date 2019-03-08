package com.example.michel.rest_api.dao.implementations;

import com.example.michel.rest_api.dao.interfaces.IPillReminderFDao;
import com.example.michel.rest_api.mappers.PillReminderFRowMapper;
import com.example.michel.rest_api.models.pill.PillReminderF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class PillReminderFDao implements IPillReminderFDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<PillReminderF> getAllPillRemindersF() {
        String sql = "select pr._id_pill_reminder, pr._id_having_meals_type, pr.pill_count, pct.type_name, pi.pill_name, pr.start_date, pr.is_active, cl.period, pr.times_a_day, "+
                "cl.period_DM_type,(select COUNT(*) from pill_reminder_entry pre where pre._id_pill_reminder=pr._id_pill_reminder and pre.is_done=0 ) as count_left "+
                "from pill_reminder pr inner join pill pi on pi._id_pill=pr._id_pill inner join pill_count_type pct on pr._id_pill_count_type=pct._id_pill_count_type "+
                "inner join cycle cl on pr._id_cycle=cl._id_cycle ORDER BY pr.is_active DESC, pi.pill_name";

        return jdbcTemplate.query(sql, new PillReminderFRowMapper());
    }
}
