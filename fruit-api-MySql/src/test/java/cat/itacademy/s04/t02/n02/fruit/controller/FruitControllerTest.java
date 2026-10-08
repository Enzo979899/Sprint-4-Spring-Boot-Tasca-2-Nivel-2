package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.exception.FruitNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Fruit;
import cat.itacademy.s04.t02.n02.fruit.service.FruitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FruitController.class)
class FruitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FruitService fruitService;

    @Test
    void shouldCreateFruitAndReturnCreated() throws Exception {
        Fruit savedFruit = new Fruit(1L, "Manzana", 1.5);

        when(fruitService.create(any(Fruit.class))).thenReturn(savedFruit);

        mockMvc.perform(post("/fruits").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "Manzana",
                          "weightInKilos": 1.5
                        }
                        """)).andExpect(status().isCreated()).andExpect(jsonPath("$.id")
                        .value(1)).andExpect(jsonPath("$.name").value("Manzana"))
                .andExpect(jsonPath("$.weightInKilos").value(1.5));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/fruits").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "",
                          "weightInKilos": 1.0
                        }
                        """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status")
                        .value(400)).andExpect(jsonPath("$.error")
                        .value("Bad Request")).andExpect(jsonPath("$.message")
                .value("Validation failed"));
    }

    @Test
    void shouldReturnBadRequestWhenWeightIsNegative() throws Exception {
        mockMvc.perform(post("/fruits").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "Manzana",
                          "weightInKilos": -1.0
                        }
                        """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status")
                        .value(400)).andExpect(jsonPath("$.error")
                        .value("Bad Request")).andExpect(jsonPath("$.message")
                .value("Validation failed"));
    }

    @Test
    void shouldReturnAllFruits() throws Exception {
        Fruit uva = new Fruit(1L, "Uva", 1.4);
        Fruit melon = new Fruit(2L, "Melon", 3.7);

        when(fruitService.findAll()).thenReturn(List.of(uva, melon));

        mockMvc.perform(get("/fruits")).andExpect(status().isOk()).andExpect(jsonPath("$.length()")
                        .value(2)).andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Uva"))
                .andExpect(jsonPath("$[0].weightInKilos").value(1.4))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Melon"))
                .andExpect(jsonPath("$[1].weightInKilos").value(3.7));
    }

    @Test
    void shouldReturnFruitById() throws Exception {
        Fruit mandarinas = new Fruit(1L, "Mandarinas", 1.5);

        when(fruitService.findById(1L)).thenReturn(mandarinas);

        mockMvc.perform(get("/fruits/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id")
                        .value(1)).andExpect(jsonPath("$.name").value("Mandarinas"))
                .andExpect(jsonPath("$.weightInKilos").value(1.5));
    }

    @Test
    void shouldReturnNotFoundWhenFruitDoesNotExist() throws Exception {
        when(fruitService.findById(888L)).thenThrow(new FruitNotFoundException(888L));

        mockMvc.perform(get("/fruits/888")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Fruit with id 888 not found"));
    }

    @Test
    void shouldUpdateFruit() throws Exception {
        Fruit updatedFruit = new Fruit(1L, "Pera", 2.2);

        when(fruitService.update(any(Long.class), any(Fruit.class))).thenReturn(updatedFruit);

        mockMvc.perform(put("/fruits/1").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "Pera",
                          "weightInKilos": 2.2
                        }
                        """)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pera"))
                .andExpect(jsonPath("$.weightInKilos").value(2.2));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {

        mockMvc.perform(put("/fruits/1").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "",
                          "weightInKilos": -2.0
                        }
                        """)).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingFruit() throws Exception {

        when(fruitService.update(any(Long.class), any(Fruit.class))).thenThrow(new FruitNotFoundException(999L));

        mockMvc.perform(put("/fruits/999").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                          "name": "Pera",
                          "weightInKilos": 2.0
                        }
                        """)).andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteFruit() throws Exception {
        mockMvc.perform(delete("/fruits/1")).andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingFruit() throws Exception {

        doThrow(new FruitNotFoundException(999L)).when(fruitService).delete(999L);

        mockMvc.perform(delete("/fruits/999")).andExpect(status().isNotFound());
    }
}
