
package cat.itacademy.s04.t02.n02.fruit.controller;

import cat.itacademy.s04.t02.n02.fruit.exception.ProviderAlreadyExistsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderHasFruitsException;
import cat.itacademy.s04.t02.n02.fruit.exception.ProviderNotFoundException;
import cat.itacademy.s04.t02.n02.fruit.model.Provider;
import cat.itacademy.s04.t02.n02.fruit.service.ProviderService;
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

@WebMvcTest(ProviderController.class)
class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    @Test
    void shouldCreateProviderAndReturnCreated() throws Exception {

        Provider provider = new Provider("Frutas del Sur", "España");

        when(providerService.create(any(Provider.class))).thenReturn(provider);

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON).content("""
                                {
                                  "name": "Frutas del Sur",
                                  "country": "España"
                                }
                                """)).andExpect(status().isCreated()).andExpect(jsonPath("$.name")
                        .value("Frutas del Sur")).andExpect(jsonPath("$.country")
                .value("España"));
    }

    @Test
    void shouldReturnBadRequestWhenProviderNameIsBlank() throws Exception {

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "",
                              "country": "España"
                            }
                            """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status")
                        .value(400)).andExpect(jsonPath("$.error")
                        .value("Bad Request")).andExpect(jsonPath("$.message")
                .value("Validation failed"));

        verify(providerService, never()).create(any(Provider.class));
    }

    @Test
    void shouldReturnBadRequestWhenProviderCountryIsBlank() throws Exception {

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "Frutas del Sur",
                              "country": ""
                            }
                            """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status")
                        .value(400)).andExpect(jsonPath("$.error")
                        .value("Bad Request")).andExpect(jsonPath("$.message")
                .value("Validation failed"));

        verify(providerService, never()).create(any(Provider.class));
    }

    @Test
    void shouldReturnConflictWhenProviderNameAlreadyExists() throws Exception {

        when(providerService.create(any(Provider.class)))
                .thenThrow(new ProviderAlreadyExistsException("Frutas del Sur"));

        mockMvc.perform(post("/providers").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "Frutas del Sur",
                              "country": "España"
                            }
                            """)).andExpect(status().isConflict()).andExpect(jsonPath("$.status")
                        .value(409)).andExpect(jsonPath("$.error")
                        .value("Conflict")).andExpect(jsonPath("$.message")
                .value("Provider already exists: Frutas del Sur"));

        verify(providerService).create(any(Provider.class));
    }

    @Test
    void shouldReturnAllProviders() throws Exception {

        Provider provider1 = new Provider("Frutas del Sur", "España");
        Provider provider2 = new Provider("Tropical Fruits", "Brasil");

        when(providerService.findAll()).thenReturn(List.of(provider1, provider2));

        mockMvc.perform(get("/providers")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Frutas del Sur"))
                .andExpect(jsonPath("$[0].country").value("España"))
                .andExpect(jsonPath("$[1].name").value("Tropical Fruits"))
                .andExpect(jsonPath("$[1].country").value("Brasil"));
    }

    @Test
    void shouldUpdateProviderAndReturnOk() throws Exception {

        Provider updatedProvider = new Provider("Frutas del Norte", "España");

        when(providerService.update(eq(1L), any(Provider.class))).thenReturn(updatedProvider);

        mockMvc.perform(put("/providers/1").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "Frutas del Norte",
                              "country": "España"
                            }
                            """)).andExpect(status().isOk()).andExpect(jsonPath("$.name")
                        .value("Frutas del Norte")).andExpect(jsonPath("$.country")
                .value("España"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingProvider() throws Exception {

        when(providerService.update(eq(999L), any(Provider.class))).thenThrow(new ProviderNotFoundException(999L));

        mockMvc.perform(put("/providers/999").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "Frutas del Norte",
                              "country": "España"
                            }
                            """)).andExpect(status().isNotFound()).andExpect(jsonPath("$.status")
                        .value(404)).andExpect(jsonPath("$.error")
                        .value("Not Found")).andExpect(jsonPath("$.message")
                .value("Provider with id 999 not found"));
    }

    @Test
    void shouldReturnConflictWhenUpdatingProviderWithDuplicateName() throws Exception {

        when(providerService.update(eq(2L), any(Provider.class))).thenThrow(
                new ProviderAlreadyExistsException("Frutas del Sur"));

        mockMvc.perform(put("/providers/2").contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "name": "Frutas del Sur",
                              "country": "España"
                            }
                            """)).andExpect(status().isConflict()).andExpect(jsonPath("$.status")
                        .value(409)).andExpect(jsonPath("$.error")
                .value("Conflict")).andExpect(jsonPath("$.message")
                .value("Provider already exists: Frutas del Sur"));
    }

    @Test
    void shouldDeleteProviderWithoutFruits() throws Exception {

        mockMvc.perform(delete("/providers/1")).andExpect(status().isNoContent());

        verify(providerService).delete(1L);
    }

    @Test
    void shouldReturnBadRequestWhenDeletingProviderWithFruits() throws Exception {

        doThrow(new ProviderHasFruitsException(1L)).when(providerService).delete(1L);

        mockMvc.perform(delete("/providers/1")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Cannot delete provider with id 1 because it has associated fruits"));


    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingProvider() throws Exception {

        doThrow(new ProviderNotFoundException(999L)).when(providerService).delete(999L);

        mockMvc.perform(delete("/providers/999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Provider with id 999 not found"));
    }
}

