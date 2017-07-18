package io.pivotal.pal.tracker;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeEntryTest {

    @Test
    public void constructor_noId_setsIdToMinus1() {
        TimeEntry entry = new TimeEntry(23L, 5L, "date", 42);
        assertThat(entry.getId()).isEqualTo(-1L);

        TimeEntry defaultEntry = new TimeEntry();
        assertThat(defaultEntry.getId()).isEqualTo(-1L);
    }
}