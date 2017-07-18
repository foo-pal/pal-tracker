package io.pivotal.pal.tracker;

import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
    private Map<Long, TimeEntry> entries = new HashMap<>();

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() == -1) {
            timeEntry.setId(nextId());
        }

        entries.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry get(Long id) {
        return entries.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(entries.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        if (entries.get(id) == null) {
            throw new RuntimeException("not found");
        }

        entries.put(id, timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(Long id) {
        entries.remove(id);
    }

    private long nextId() {
        if (entries.size() == 0) {
            return 1L;
        }

        long maxValue = entries.values().stream()
                .map(TimeEntry::getId)
                .max(Comparator.naturalOrder())
                .get();

        return maxValue + 1;
    }
}
