package io.pivotal.pal.tracker;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest extends TimeEntryRepositoryTest {

    @Before
    public void beforeEach() {
        repository = new InMemoryTimeEntryRepository();
    }

    @Test
    public void create_noIdSet_usesMaxIdPlus1() {
        List<TimeEntry> entries = Arrays.asList(
                new TimeEntry(23L, 5L, 42L, "date", 123),
                new TimeEntry(24L, 5L, 42L, "other date", 123),
                new TimeEntry(25L, 5L, 42L, "many date", 123)
        );

        entries.forEach(entry -> repository.create(entry));

        TimeEntry noId = new TimeEntry(5L, 42L, "such date", 123);
        TimeEntry expected = new TimeEntry(26L, 5L, 42L, "such date", 123);

        assertThat(repository.create(noId))
                .isEqualToComparingFieldByField(expected);
    }
}