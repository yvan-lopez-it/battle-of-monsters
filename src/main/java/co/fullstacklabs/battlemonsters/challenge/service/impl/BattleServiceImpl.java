package co.fullstacklabs.battlemonsters.challenge.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import co.fullstacklabs.battlemonsters.challenge.exceptions.ResourceNotFoundException;
import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import co.fullstacklabs.battlemonsters.challenge.repository.MonsterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.model.Battle;
import co.fullstacklabs.battlemonsters.challenge.repository.BattleRepository;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Service
public class BattleServiceImpl implements BattleService {

    transient private final BattleRepository battleRepository;
    transient private final MonsterRepository monsterRepository;
    transient private final ModelMapper modelMapper;

    @Autowired
    public BattleServiceImpl(BattleRepository battleRepository, MonsterRepository monsterRepository, ModelMapper modelMapper) {
        this.battleRepository = battleRepository;
        this.monsterRepository = monsterRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * List all existence battles
     */
    @Override
    public List<BattleDTO> getAll() {
        List<Battle> battles = battleRepository.findAll();
        return battles.stream().map(battle -> modelMapper.map(battle, BattleDTO.class)).collect(Collectors.toList());
    }

    @Override
    public BattleDTO startBattle(Integer idMonsterA, Integer idMonsterB) {

        if (idMonsterA == null || idMonsterB == null) {
            throw new ResourceNotFoundException("Need two monsters to battle.");
        }

        Monster monster1 = monsterRepository.findById(idMonsterA).orElse(null);
        Monster monster2 = monsterRepository.findById(idMonsterB).orElse(null);

        if (monster1 == null || monster2 == null) {
            throw new ResourceNotFoundException("Monster 1 or 2 not exists - monster1=" + monster1 + "; monster2=" + monster2);
        }

        List<Monster> monsters = Arrays.asList(monster1, monster2);

        // sort monsters by speed and attack
        monsters.sort((m1, m2) -> {
            if (m1.getSpeed().equals(m2.getSpeed())) {
                return Integer.compare(m2.getAttack(), m1.getAttack());
            }
            return Integer.compare(m2.getSpeed(), m1.getSpeed());
        });

        // battle algorithm
        while (monster1.getHp() > 0 && monster2.getHp() > 0) {
            int damage = Math.max(monster1.getAttack() - monster2.getDefense(), 1);
            monster2.setHp(monster2.getHp() - damage);
            damage = Math.max(monster2.getAttack() - monster1.getDefense(), 1);
            monster1.setHp(monster1.getHp() - damage);
        }

        // set winner
        Monster winner = monster1.getHp() > 0 ? monster1 : monster2;

        //builder lombok
        BattleDTO battleDTO = BattleDTO.builder()
                .monsterA(modelMapper.map(monster1, MonsterDTO.class))
                .monsterB(modelMapper.map(monster2, MonsterDTO.class))
                .winner(modelMapper.map(winner, MonsterDTO.class))
                .build();

        // Save the battle
        Battle battle = modelMapper.map(battleDTO, Battle.class);
        battleRepository.save(battle);

        return battleDTO;
    }

    @Override
    public void deleteById(Integer idBattle) {
        try {
            battleRepository.deleteById(idBattle);
        } catch (IllegalArgumentException iae) {
            throw new ResourceNotFoundException("Battle with id=" + idBattle + "; not found");
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

}
