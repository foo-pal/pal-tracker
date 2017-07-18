package io.pivotal.pal.tracker;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TimeEntriesControllerTest {
    private MockMvc mockMvc;

    @Before
    public void beforeEach() {
        TimeEntryRepository repository = new InMemoryTimeEntryRepository();
        TimeEntriesController controller = new TimeEntriesController(repository);

        List<TimeEntry> entries = Arrays.asList(
                new TimeEntry(23L, 5L, 42L, "date", 123),
                new TimeEntry(24L, 5L, 42L, "other date", 123),
                new TimeEntry(25L, 5L, 42L, "many date", 123)
        );

        entries.forEach(repository::create);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void list_lists() throws Exception {
        mockMvc.perform(get("/timeEntries"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonList));
    }

    @Test
    public void get_validId_returnsEntry() throws Exception {
        mockMvc.perform(get("/timeEntries/23"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonEntry23));
    }

    @Test
    public void get_invalidId_notFound() throws Exception {
        mockMvc.perform(get("/timeEntries/9000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void create_stores() throws Exception {
        mockMvc.perform(post("/timeEntries")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonEntry26));
    }

    @Test
    public void put_sameIdInBody_updates() throws Exception {
        mockMvc.perform(put("/timeEntries/23")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(putJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/timeEntries/23"))
                .andExpect(status().isOk())
                .andExpect(content().json(putJson));
    }

    @Test
    public void put_differentIdInBody_badRequest() throws Exception {
        mockMvc.perform(put("/timeEntries/24")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(putJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void put_idNotFound_notFound() throws Exception {
        mockMvc.perform(put("/timeEntries/9000")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(putJson9000))
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_deletes() throws Exception {
        mockMvc.perform(delete("/timeEntries/23"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/timeEntries/23"))
                .andExpect(status().isNotFound());
    }


    private static final String jsonList = "[\n" +
            "  {\n" +
            "    \"id\": 23,\n" +
            "    \"projectId\": 5,\n" +
            "    \"userId\": 42,\n" +
            "    \"date\": \"date\",\n" +
            "    \"hours\": 123\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 24,\n" +
            "    \"projectId\": 5,\n" +
            "    \"userId\": 42,\n" +
            "    \"date\": \"other date\",\n" +
            "    \"hours\": 123\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 25,\n" +
            "    \"projectId\": 5,\n" +
            "    \"userId\": 42,\n" +
            "    \"date\": \"many date\",\n" +
            "    \"hours\": 123\n" +
            "  }\n" +
            "]";

    private static final String jsonEntry23 = "{\n" +
            "  \"id\": 23,\n" +
            "  \"projectId\": 5,\n" +
            "  \"userId\": 42,\n" +
            "  \"date\": \"date\",\n" +
            "  \"hours\": 123\n" +
            "}";

    private static final String jsonEntry26 = "{\n" +
            "  \"id\": 26,\n" +
            "  \"projectId\": 5,\n" +
            "  \"userId\": 42,\n" +
            "  \"date\": \"date\",\n" +
            "  \"hours\": 123\n" +
            "}";

    private static final String createJson = "{\n" +
            "  \"projectId\": 5,\n" +
            "  \"userId\": 42,\n" +
            "  \"date\": \"date\",\n" +
            "  \"hours\": 123\n" +
            "}";

    private static final String putJson = "{\n" +
            "  \"id\": 23,\n" +
            "  \"projectId\": 5,\n" +
            "  \"userId\": 42,\n" +
            "  \"date\": \"updated date\",\n" +
            "  \"hours\": 123\n" +
            "}";

    private static final String putJson9000 = "{\n" +
            "  \"id\": 9000,\n" +
            "  \"projectId\": 5,\n" +
            "  \"userId\": 42,\n" +
            "  \"date\": \"updated date\",\n" +
            "  \"hours\": 123\n" +
            "}";
}