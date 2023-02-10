package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@RestController
@RequestMapping("/battle")
public class BattleController {

    transient private final BattleService battleService;

    @Autowired
    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping()
    public List<BattleDTO> getAll() {
        return battleService.getAll();
    }

    @GetMapping("/start")
    public BattleDTO startBattle(@RequestParam Integer idMonsterA, @RequestParam Integer idMonsterB) {
        return battleService.startBattle(idMonsterA, idMonsterB);
    }

    @DeleteMapping("/{idBattle}")
    public void deleteBattle(@PathVariable Integer idBattle) {
        battleService.deleteById(idBattle);
    }

}
