package io.pivotal.pal.tracker;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeEntryRowMapper implements RowMapper<TimeEntry> {
    @Override
    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getString("date"),
                rs.getInt("hours")
        );
    }
}
