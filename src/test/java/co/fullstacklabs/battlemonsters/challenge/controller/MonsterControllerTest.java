package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.mockito.Mockito.mock;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class MonsterControllerTest {
    private static final String MONSTER_PATH = "/monster";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //@Test    
    void shouldFetchAllMonsters() throws Exception {
        this.mockMvc.perform(get(MONSTER_PATH)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].name", Is.is("Monster 1")))
                .andExpect(jsonPath("$[0].attack", Is.is(50)))
                .andExpect(jsonPath("$[0].defense", Is.is(40)))
                .andExpect(jsonPath("$[0].hp", Is.is(30)))
                .andExpect(jsonPath("$[0].speed", Is.is(25)));

    }

    @Test
    void shouldGetMosterSuccessfully() throws Exception {
        long id = 1l;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Is.is("Monster 1")));
    }

    @Test
    void shoulGetMonsterNotExists() throws Exception {
        //long id = 3l;
        Integer id = 3;
        this.mockMvc.perform(get(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMonsterSuccessfully() throws Exception {
        int id = 4;

        MonsterDTO newMonster = MonsterDTO.builder().id(id).name("Monster 4")
                .attack(50).defense(30).hp(30).speed(22)
                .imageUrl("ImageURL1").build();

        this.mockMvc.perform(post(MONSTER_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMonster)));


        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteMonsterNotFound() throws Exception {
        int id = 5;

        this.mockMvc.perform(delete(MONSTER_PATH + "/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testImportCsvSucessfully() throws Exception {
        //TOOD: Implement take as a sample data/monstere-correct.csv
        Path path = Paths.get("data/monsters-correct.csv");
        byte[] fileContent = Files.readAllBytes(path);

        // Create a mock file with the contents of the file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "monsters-correct.csv",
                "text/csv",
                fileContent);

        // Perform a file upload request to your endpoint
        mockMvc.perform(multipart(MONSTER_PATH + "/import").file(file))
                .andExpect(status().isOk());
    }

    @Test
    void testImportCsvInexistenctColumns() throws Exception {
        //TOOD: Implement take as a sample data/monsters-wrong-column.csv
        Path path = Paths.get("data/monsters-wrong-column.csv");
        byte[] fileContent = Files.readAllBytes(path);

        // Create a mock file with the contents of the file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "monsters-wrong-column.csv",
                "text/csv",
                fileContent);

        // Perform a file upload request to your endpoint
        mockMvc.perform(multipart(MONSTER_PATH + "/import").file(file))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testImportCsvInexistenctMonster() throws Exception {
        //TOOD: Implement take as a sample data/monsters-empty-monster.csv
        Path path = Paths.get("data/monsters-empty-monster.csv");
        byte[] fileContent = Files.readAllBytes(path);

        // Create a mock file with the contents of the file
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "monsters-empty-monster.csv",
                "text/csv",
                fileContent);

        // Perform a file upload request to your endpoint
        mockMvc.perform(multipart(MONSTER_PATH + "/import").file(file))
                .andExpect(status().isInternalServerError());
    }
}
