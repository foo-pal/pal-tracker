package io.pivotal.pal.tracker;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class TimeEntryRepositoryTest {
    protected TimeEntryRepository repository;

    @Test
    public void create_createsNewEntry() {
        TimeEntry entry = new TimeEntry(23L, 5L, 42L, "date", 123);
        repository.create(entry);

        assertThat(repository.get(23L)).isEqualTo(entry);
    }

    @Test
    public void list_returnsAllEntries() {
        List<TimeEntry> entries = Arrays.asList(
                new TimeEntry(23L, 5L, 42L, "date", 123),
                new TimeEntry(24L, 5L, 42L, "other date", 123),
                new TimeEntry(25L, 5L, 42L, "many date", 123)
        );

        entries.forEach(entry -> repository.create(entry));

        assertThat(repository.list()).containsOnlyElementsOf(entries);
    }

    @Test
    public void update_updates() {
        TimeEntry entry = new TimeEntry(23L, 5L, 42L, "date", 123);
        repository.create(entry);

        TimeEntry updated = new TimeEntry(23L, 5L, 42L, "new date", 124);
        repository.update(23L, updated);

        assertThat(repository.get(23L)).isEqualTo(updated);
    }

    @Test
    public void delete_deletes() {
        TimeEntry entry = new TimeEntry(23L, 5L, 42L, "date", 123);
        repository.create(entry);

        repository.delete(23L);

        assertThat(repository.get(23L)).isNull();
    }
}
