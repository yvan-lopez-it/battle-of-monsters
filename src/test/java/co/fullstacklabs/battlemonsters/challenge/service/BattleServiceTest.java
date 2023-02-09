package co.fullstacklabs.battlemonsters.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.BattleServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.BattleTestBuilder;

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
        Mockito.when(battleRepository.findAll()).thenReturn(battleList);
        
        battleService.getAll();

        Mockito.verify(battleRepository).findAll();
        Mockito.verify(mapper).map(battleList.get(0), BattleDTO.class);
        Mockito.verify(mapper).map(battleList.get(1), BattleDTO.class);        
    }
    
     @Test
     void shouldFailBattleWithUndefinedMonster() {
        //TODO: Implement        
        assertEquals(1, 1);
    }

    @Test
    void shouldFailBattleWithInexistentMonster() {
        //TODO: Implement
        assertEquals(1, 1);
    }

    @Test
    void shouldInsertBattleWithMonsterAWinning() {
        //TODO: Implement
      assertEquals(1, 1);    
    }

    @Test
    void shouldInsertBattleWithMonsterBWinning() {
        //TODO: Implement
          assertEquals(1, 1);    
    }

    @Test
    void shouldDeleteBattleSucessfully() {
        //TODO: Implement        
         assertEquals(1, 1);    
    }

    @Test
    void shouldFailDeletingInexistentBattle() {
        //TODO: Implement
         assertEquals(1, 1);    
    }



}
