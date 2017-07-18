package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntriesController {

    private TimeEntryRepository repository;
    private CounterService counterService;
    private GaugeService gaugeService;

    public TimeEntriesController(TimeEntryRepository repository,
                                 CounterService counterService,
                                 GaugeService gaugeService) {

        this.repository = repository;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
    }

    @GetMapping("/timeEntries")
    public List<TimeEntry> list() {
        counterService.increment("list");

        List<TimeEntry> entries = repository.list();
        gaugeService.submit("timeEntries", entries.size());

        return entries;
    }

    @GetMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntry> get(@PathVariable long id) {
        counterService.increment("get");

        TimeEntry entry = repository.get(id);

        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entry);
    }

    @PostMapping("/timeEntries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
        counterService.increment("create");

        TimeEntry stored = repository.create(entry);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(stored);
    }

    @PutMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntry> put(@PathVariable long id,
                                    @RequestBody TimeEntry updatedEntry) {

        counterService.increment("update");

        if (updatedEntry.getId() != id) {
            return ResponseEntity.badRequest().build();
        }

        TimeEntry entry = repository.get(id);

        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        repository.update(id, updatedEntry);

        return ResponseEntity.ok(updatedEntry);
    }

    @DeleteMapping("/timeEntries/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        counterService.increment("delete");

        repository.delete(id);

        return ResponseEntity.noContent().build();
    }
}
