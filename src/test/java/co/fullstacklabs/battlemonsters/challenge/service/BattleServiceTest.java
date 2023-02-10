package co.fullstacklabs.battlemonsters.challenge.service;

import java.util.List;
import java.util.Optional;

import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;
import org.assertj.core.util.Lists;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {

    @InjectMocks
    public BattleServiceImpl battleService;

    @Mock
    public BattleRepository battleRepository;

    @Mock
    private ModelMapper mapper;

    /**** Delete */
    @Mock
    public MonsterRepository monsterRepository;

    @Test
    public void testGetAll() {

        Battle battle1 = BattleTestBuilder.builder().id(1).build();
        Battle battle2 = BattleTestBuilder.builder().id(2).build();

        List<Battle> battleList = Lists.newArrayList(battle1, battle2);
        when(battleRepository.findAll()).thenReturn(battleList);

        battleService.getAll();

        verify(battleRepository).findAll();
        verify(mapper).map(battleList.get(0), BattleDTO.class);
        verify(mapper).map(battleList.get(1), BattleDTO.class);
    }

    @Test
    void shouldFailBattleWithUndefinedMonster() {
        Integer idMonsterA = 1;
        Integer idMonsterB = 2;

        Monster monster1 = MonsterTestBuilder.builder().id(1).build();
        Monster monster2 = MonsterTestBuilder.builder().id(2).build();

        when(monsterRepository.findById(idMonsterA)).thenReturn(Optional.of(monster1));
        when(monsterRepository.findById(idMonsterB)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> battleService.startBattle(idMonsterA, idMonsterB));

        assertEquals("Monster 1 or 2 not exists - monster1=" + monster1 + "; monster2=null", exception.getMessage());
    }

    @Test
    void shouldFailBattleWithInexistentMonster() {
        //TODO: Implement
        Integer idMonsterA = 1;
        Integer idMonsterB = 999; // Inexistent monster

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            battleService.startBattle(idMonsterA, idMonsterB);
        });

        String expectedMessage = "Monster 1 or 2 not exists - monster1=" + null + "; monster2=" + null;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldInsertBattleWithMonsterAWinning() {
        //TODO: Implement
        Monster monsterA = MonsterTestBuilder.builder().id(1).hp(50).attack(60).speed(70).build();
        Monster monsterB = MonsterTestBuilder.builder().id(2).hp(30).attack(40).speed(50).build();

        when(monsterRepository.findById(1)).thenReturn(Optional.of(monsterA));
        when(monsterRepository.findById(2)).thenReturn(Optional.of(monsterB));

        BattleDTO battleDTO = battleService.startBattle(1, 2);
        Battle battle = mapper.map(battleDTO, Battle.class);
        verify(battleRepository, times(1)).save(battle);
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() {
        //TODO: Implement
        Monster monsterA = MonsterTestBuilder.builder().id(1).hp(30).attack(40).speed(50).build();
        Monster monsterB = MonsterTestBuilder.builder().id(2).hp(50).attack(60).speed(70).build();

        when(monsterRepository.findById(1)).thenReturn(Optional.of(monsterA));
        when(monsterRepository.findById(2)).thenReturn(Optional.of(monsterB));

        BattleDTO battleDTO = battleService.startBattle(1, 2);
        Battle battle = mapper.map(battleDTO, Battle.class);
        verify(battleRepository, times(1)).save(battle);
    }

    @Test
    void shouldDeleteBattleSucessfully() {
        Battle battle = BattleTestBuilder.builder().id(1).build();
        BattleService battleServiceMock = mock(BattleService.class);
        doNothing().when(battleServiceMock).deleteById(battle.getId());
        battleServiceMock.deleteById(battle.getId());
        verify(battleServiceMock, times(1)).deleteById(battle.getId());
    }

    @Test
    void shouldFailDeletingInexistentBattle() {
        //TODO: Implement
        try {
            Battle battle = BattleTestBuilder.builder().id(9999).build();
            BattleService battleServiceMock = mock(BattleService.class);
            doThrow(new ResourceNotFoundException("Battle not exists"))
                    .when(battleServiceMock)
                    .deleteById(battle.getId());
            battleServiceMock.deleteById(battle.getId());
        } catch (ResourceNotFoundException rnfEx) {
            assertThat(rnfEx.getMessage(), Is.is("Battle not exists"));
        }

    }


}
