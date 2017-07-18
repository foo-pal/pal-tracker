package io.pivotal.pal.tracker;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.metrics.GaugeService;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeEntryHealthIndicatorTest {
    private TimeEntryRepository repository;
    private TimeEntryHealthIndicator indicator;

    @Before
    public void beforeEach() {
        repository = mock(TimeEntryRepository.class);
        indicator = new TimeEntryHealthIndicator(repository);
    }

    @Test
    public void health_lessThanFive_up() {
        when(repository.list()).thenReturn(Arrays.asList(
                new TimeEntry(23L, 5L, 42L, "date", 123),
                new TimeEntry(24L, 5L, 42L, "other date", 123),
                new TimeEntry(25L, 5L, 42L, "many date", 123),
                new TimeEntry(26L, 5L, 42L, "many date", 123)
        ));

        assertThat(indicator.health().getStatus()).isEqualTo(Status.UP);

        verify(repository).list();
    }

    @Test
    public void health_fiveOrMore_down() {
        when(repository.list()).thenReturn(Arrays.asList(
                new TimeEntry(23L, 5L, 42L, "date", 123),
                new TimeEntry(24L, 5L, 42L, "other date", 123),
                new TimeEntry(25L, 5L, 42L, "many date", 123),
                new TimeEntry(26L, 5L, 42L, "many date", 123),
                new TimeEntry(27L, 5L, 42L, "many date", 123)
        ));

        assertThat(indicator.health().getStatus()).isEqualTo(Status.DOWN);

        verify(repository).list();
    }
}