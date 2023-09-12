package th.mfu;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConcertController.class)
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddAndListConcerts() throws Exception {
        List<Concert> Concerts = new ArrayList<>();
        Concerts.add(new Concert("xxxxx", "description of xxxx"));
        Concerts.add(new Concert("yyyyyy", "description of yyyyy"));

        for (Concert Concert : Concerts) {
            mockMvc.perform(post("/concerts")
                    .param("title", Concert.getTitle())
                    .param("description", Concert.getDescription()))
                    .andExpect(redirectedUrl("/concerts"));
        }

        mockMvc.perform(get("/concerts"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-concert"))
                .andExpect(model().attribute("concerts", hasSize(2)));
    }

    @AfterEach
    public void resetDb() throws Exception {
        mockMvc.perform(get("/delete-concert"));
    }

    @Test
    public void testAddAndDeleteConcerts() throws Exception {

        List<Concert> Concerts = new ArrayList<>();
        Concerts.add(new Concert("xxxxx", "description of xxxx"));
        Concerts.add(new Concert("yyyyyy", "description of yyyyy"));

        for (Concert Concert : Concerts) {
            mockMvc.perform(post("/concerts")
                    .param("title", Concert.getTitle())
                    .param("description", Concert.getDescription()))
                    .andExpect(redirectedUrl("/concerts"));
        }

        mockMvc.perform(get("/delete-concert/1"))
                .andExpect(view().name("redirect:/concerts"));

        mockMvc.perform(get("/concerts"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-concert"))
                .andExpect(model().attribute("concerts", hasSize(1)));

    }

}
