package project.bumar.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.bumar.web.models.bindingModels.news.NewsCreateBindingModel;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    public void createNews_whenGiveValidData_shouldReturnCreated() throws Exception {
        NewsCreateBindingModel newsCreateBindingModel = new NewsCreateBindingModel();
        newsCreateBindingModel.setText("SomeTitle");
        newsCreateBindingModel.setText("SomeTextSomeText");

        mockMvc.perform(post("/news/create")
                        .content(new ObjectMapper().writeValueAsString(newsCreateBindingModel))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("SomeText"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}
