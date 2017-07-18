package io.pivotal.pal.tracker;

import org.junit.Before;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcTimeEntryRepositoryTest extends TimeEntryRepositoryTest {

    @Before
    public void beforeEach() throws SQLException {
        String datasourceUrl = System.getenv("SPRING_DATASOURCE_URL");
        assertThat(datasourceUrl).isNotNull();

        DataSource dataSource = new MariaDbDataSource(datasourceUrl);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.execute("TRUNCATE time_entries");

        repository = new JdbcTimeEntryRepository(dataSource);
    }
}