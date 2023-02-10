package co.fullstacklabs.battlemonsters.challenge.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.springframework.test.web.servlet.MockMvc;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class BattleControllerTest {
    private static final String BATTLE_PATH = "/battle";
    private static final String BATTLE_START_PATH = "/battle/start";

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldFetchAllBattles() throws Exception {
        this.mockMvc
                .perform(get(BATTLE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Is.is(1)));
    }

    @Test
    void shouldFailBattleWithUndefinedMonster() throws Exception {
        this.mockMvc
                .perform(get(BATTLE_START_PATH)
                        .param("idMonsterA", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingServletRequestParameterException));
    }

    @Test
    void shouldFailBattleWithInexistentMonster() throws Exception {
        this.mockMvc
                .perform(get(BATTLE_START_PATH)
                        .param("idMonsterA", "999")
                        .param("idMonsterB", "2"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

    @Test
    void shouldInsertBattleWithMonsterAWinning() throws Exception {
        this.mockMvc
                .perform(get(BATTLE_START_PATH)
                        .param("idMonsterA", "26")
                        .param("idMonsterB", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.id", Is.is(26)));
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() throws Exception {
        this.mockMvc
                .perform(get(BATTLE_START_PATH)
                        .param("idMonsterA", "25")
                        .param("idMonsterB", "24"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winner.id", Is.is(24)));
    }

    @Test
    void shouldDeleteBattleSucessfully() throws Exception {
        BattleService battleServiceMock = mock(BattleService.class);
        doNothing().when(battleServiceMock).deleteById(9);

        BattleController battleController = new BattleController(battleServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(battleController).build();

        this.mockMvc
                .perform(delete(BATTLE_PATH + "/{idBattle}", "9"))
                .andExpect(status().isOk());

        verify(battleServiceMock, times(1)).deleteById(9);
    }

    @Test
    void shouldFailDeletingInexistentBattle() throws Exception {
        this.mockMvc
                .perform(delete(BATTLE_PATH + "/{idBattle}", "999"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }
}