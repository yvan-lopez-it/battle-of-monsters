package co.fullstacklabs.battlemonsters.challenge.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.impl.MonsterServiceImpl;
import co.fullstacklabs.battlemonsters.challenge.testbuilders.MonsterTestBuilder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@ExtendWith(MockitoExtension.class)
public class MonsterServiceTest {
    @InjectMocks
    public MonsterServiceImpl monsterService;

    @Mock
    public MonsterRepository monsterRepository;

    @Mock
    private ModelMapper mapper;

    @Test
    public void testGetAll() {
        String monsterName1 = "Monster 1";
        String monsterName2 = "Monster 2";
        Monster monster1 = MonsterTestBuilder.builder().id(1)
                .name(monsterName1).attack(30).defense(20).hp(21).speed(15)
                .imageURL("imageUrl1").build();

        Monster monster2 = MonsterTestBuilder.builder().id(2)
                .name(monsterName2).attack(30).defense(20).hp(21).speed(15)
                .imageURL("imageUrl1").build();

        List<Monster> monsterList = Arrays.asList(new Monster[]{monster1, monster2});
        Mockito.when(monsterRepository.findAll()).thenReturn(monsterList);

        monsterService.getAll();

        verify(monsterRepository).findAll();
        verify(mapper).map(monsterList.get(0), MonsterDTO.class);
        verify(mapper).map(monsterList.get(1), MonsterDTO.class);
    }

    @Test
    public void testGetMonsterByIdSuccessfully() throws Exception {
        int id = 1;
        Monster monster1 = MonsterTestBuilder.builder().build();
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        monsterService.findById(id);
        verify(monsterRepository).findById(id);
        verify(mapper).map(monster1, MonsterDTO.class);
    }

    @Test
    public void testGetMonsterByIdNotExists() throws Exception {
        int id = 1;
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> monsterService.findById(id));
    }

    @Test
    public void testDeleteMonsterSuccessfully() throws Exception {
        int id = 1;
        Monster monster1 = MonsterTestBuilder.builder().build();
        Mockito.when(monsterRepository.findById(id)).thenReturn(Optional.of(monster1));
        Mockito.doNothing().when(monsterRepository).delete(monster1);

        monsterService.delete(id);

        verify(monsterRepository).findById(id);
        verify(monsterRepository).delete(monster1);
    }

    @Test
    void testImportCsvSucessfully() throws Exception {
        //TOOD: Implement take as a sample data/monstere-correct.csv
        InputStream inputStream = Files.newInputStream(Paths.get("data/monsters-correct.csv"));
        MonsterService monsterServiceMock = mock(MonsterService.class);
        monsterServiceMock.importFromInputStream(inputStream);

        verify(monsterServiceMock, atLeastOnce()).importFromInputStream(inputStream);
    }

    @Test
    void testImportCsvInexistenctColumns() throws Exception {
        //TOOD: Implement take as a sample data/monsters-wrong-column.csv

        try {
            InputStream inputStream = Files.newInputStream(Paths.get("data/monsters-wrong-column.csv"));

            MonsterService monsterServiceMock = mock(MonsterService.class);
            doThrow(new ResourceNotFoundException("Nonexistent column"))
                    .when(monsterServiceMock)
                    .importFromInputStream(inputStream);
            monsterServiceMock.importFromInputStream(inputStream);
        } catch (ResourceNotFoundException rnfEx) {
            assertThat(rnfEx.getMessage(), Is.is("Nonexistent column"));
        }

    }

    @Test
    void testImportCsvInexistenctMonster() throws Exception {
        //TOOD: Implement take as a sample data/monsters-empty-monster.csv
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("data/monsters-empty-monster.csv"));

            MonsterService monsterServiceMock = mock(MonsterService.class);
            doThrow(new ResourceNotFoundException("empty monster"))
                    .when(monsterServiceMock)
                    .importFromInputStream(inputStream);
            monsterServiceMock.importFromInputStream(inputStream);
        } catch (ResourceNotFoundException rnfEx) {
            assertThat(rnfEx.getMessage(), Is.is("empty monster"));
        }
    }

}
