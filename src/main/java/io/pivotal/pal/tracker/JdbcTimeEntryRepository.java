package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() != -1) {
            return createWithId(timeEntry);
        }

        return createWithoutId(timeEntry);
    }

    private TimeEntry createWithId(TimeEntry timeEntry) {
        jdbcTemplate.update(
                "INSERT INTO \n" +
                        "    time_entries (id, project_id, user_id, date, hours)\n" +
                        "VALUES\n" +
                        "    (?, ?, ?, ?, ?)",
                timeEntry.getId(),
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours());

        return timeEntry;
    }

    private TimeEntry createWithoutId(TimeEntry timeEntry) {
        jdbcTemplate.update(
                "INSERT INTO \n" +
                        "    time_entries (project_id, user_id, date, hours)\n" +
                        "VALUES\n" +
                        "    (?, ?, ?, ?)",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours());

        return new TimeEntry(
                lastInsertId(),
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );
    }

    private Long lastInsertId() {
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public TimeEntry get(Long id) {
        List<TimeEntry> matches = jdbcTemplate.query(
                "SELECT\n" +
                        "    id, project_id, user_id, date, hours\n" +
                        "FROM\n" +
                        "    time_entries\n" +
                        "WHERE\n" +
                        "    id=?",
                new TimeEntryRowMapper(),
                id
        );

        if (matches.size() == 0) {
            return null;
        }

        return matches.get(0);
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query(
                "SELECT\n" +
                        "    id, project_id, user_id, date, hours\n" +
                        "FROM\n" +
                        "    time_entries",
                new TimeEntryRowMapper()
        );
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        jdbcTemplate.update(
                "UPDATE\n" +
                        "    time_entries\n" +
                        "SET \n" +
                        "    project_id=?, user_id=?, date=?, hours=?\n" +
                        "WHERE\n" +
                        "    id=?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours(),
                id);

        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE IGNORE FROM time_entries WHERE id=?",
                id
        );
    }
}
