package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.exceptions.UnprocessableFileException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import co.fullstacklabs.battlemonsters.challenge.service.MonsterService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class MonsterServiceImpl implements MonsterService {

    transient private final MonsterRepository monsterRepository;
    transient private final ModelMapper modelMapper;

    public MonsterServiceImpl(MonsterRepository monsterRepository, ModelMapper modelMapper) {
        this.monsterRepository = monsterRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MonsterDTO> getAll() {
        List<Monster> monsters = monsterRepository.findAll();

        return monsters.stream()
                .map(monster -> modelMapper.map(monster, MonsterDTO.class)).collect(Collectors.toList());
    }

    @Override
    public MonsterDTO create(MonsterDTO monsterDTO) {
        if (monsterDTO.getId() == null) {
            int max = 1000;
            int min = 100;
            int range = max - min + 1;
            int randomInt = (int) (Math.random() * range) + min;
            monsterDTO.setId(randomInt);
        }
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        monster = monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);
    }

    private Monster findMonsterById(int id) {
        return monsterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Monster not found"));
    }

    @Override
    public MonsterDTO findById(int id) {
        Monster monster = findMonsterById(id);

        if (monster == null) {
            throw new UnprocessableFileException("Monster not exists");
        }

        return modelMapper.map(monster, MonsterDTO.class);
    }

    @Override
    public MonsterDTO update(MonsterDTO monsterDTO) {
        // Monster monster = findMonsterById(monsterDTO.getId());
        Monster monster = modelMapper.map(monsterDTO, Monster.class);
        monsterRepository.save(monster);
        return modelMapper.map(monster, MonsterDTO.class);

    }

    @Override
    public void delete(Integer id) {
        Monster monster = findMonsterById(id);
        monsterRepository.delete(monster);
    }

    public void importFromInputStream(InputStream inputStream) {
        try (Reader inputReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            BeanListProcessor<MonsterDTO> rowProcessor = new BeanListProcessor<>(MonsterDTO.class);
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setProcessor(rowProcessor);
            CsvParser parser = new CsvParser(settings);
            parser.parse(inputReader);
            List<MonsterDTO> monsters = rowProcessor.getBeans();
            monsters.forEach(this::create);
        } catch (Exception ioEx) {
            throw new UnprocessableFileException(ioEx.getMessage());
        }
    }

}
