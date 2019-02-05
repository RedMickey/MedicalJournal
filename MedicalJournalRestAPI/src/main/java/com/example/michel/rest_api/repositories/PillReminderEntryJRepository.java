package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminder;
import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.models.pill.PillReminderEntryJ;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface PillReminderEntryJRepository extends CrudRepository<PillReminderEntry, String> {

    /*@Query(value = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
            " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
            " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date=?1 and (pr.IsActive=1 or pre.is_done=1) ORDER BY pre.is_done", nativeQuery = true)
    List<PillReminderEntryJ> getPillReminderEntriesByDate(Date date);*/
}
