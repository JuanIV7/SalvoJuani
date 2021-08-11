package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    //private ShipRepository shipRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public List<Map<String,Object>> getGames() {
        return gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList());
    }
    @RequestMapping("/game_view/{gamePlayerID}")
    public Map<String,Object> findGamePlayer(@PathVariable Long gamePlayerID){
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).get();
    return gamePlayer.getGameid().makeGameDTO();
    }

}
