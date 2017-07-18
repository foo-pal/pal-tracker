package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntriesController {

    private TimeEntryRepository repository;

    public TimeEntriesController(TimeEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/timeEntries")
    public List<TimeEntry> list() {
        return repository.list();
    }

    @GetMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntry> get(@PathVariable long id) {
        TimeEntry entry = repository.get(id);

        if (entry == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entry);
    }

    @PostMapping("/timeEntries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
        TimeEntry stored = repository.create(entry);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(stored);
    }

    @PutMapping("/timeEntries/{id}")
    public ResponseEntity<TimeEntry> put(@PathVariable long id,
                                    @RequestBody TimeEntry updatedEntry) {

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
        repository.delete(id);

        return ResponseEntity.noContent().build();
    }
}
