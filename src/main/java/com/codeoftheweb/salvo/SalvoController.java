package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @RequestMapping("/games")
    public Map<String,Object> getControllerDTO(Authentication authentication) {
        Map<String,Object> dto= new LinkedHashMap<>();
        if(isGuest(authentication)){
            dto.put("player", "Guest");
        }
        else{
            dto.put("player", playerRepository.findByUserName(authentication.getName()).makePlayerDTO());
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.makeGameDTO()).collect(Collectors.toList()));
        return dto;
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }else{
        Game newGame = new Game(LocalDateTime.now());
        gameRepository.save(newGame);
        Player currentPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer newGamePlayer = new GamePlayer( currentPlayer, newGame, LocalDateTime.now());
        gamePlayerRepository.save(newGamePlayer);
        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @PostMapping("/game/{nn}/players")
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable Long nn, Authentication authentication){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "Usuario no logeado"), HttpStatus.UNAUTHORIZED);
        }else {
            Player currentPlayer = playerRepository.findByUserName(authentication.getName());
            if(gameRepository.findById(nn).isPresent()){
                if (gameRepository.findById(nn).get().getGameplayers().size() == 2){
                    return new ResponseEntity<>(makeMap("error","El juego esta lleno"), HttpStatus.FORBIDDEN);
                }else if(gameRepository.findById(nn).get().getGameplayers().stream().findFirst().get().getPlayer().getId() == currentPlayer.getId()){
                        return new ResponseEntity<>(makeMap("error","El usuario ya se encuentra en esta partida"), HttpStatus.FORBIDDEN);
                    }else{
                        Game joinedGame = gameRepository.findById(nn).get();
                        GamePlayer newGamePlayer = new GamePlayer( currentPlayer, joinedGame , LocalDateTime.now());
                        gamePlayerRepository.save(newGamePlayer);
                        return new ResponseEntity<>(makeMap("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
                    }
            }else{
                return new ResponseEntity<>(makeMap("error","Ese juego no existe"), HttpStatus.FORBIDDEN);
            }
        }
    }

    @PostMapping ("/games/players/{nn}/ships")
    public ResponseEntity<Map<String,Object>> placeShips(@PathVariable Long nn, @RequestBody List<Ship> ships, Authentication authentication){
    Optional<GamePlayer> gamePlayer= gamePlayerRepository.findById(nn);
    if (isGuest(authentication)){
        return new ResponseEntity<>(makeMap("error", "Usuario no logeado"), HttpStatus.UNAUTHORIZED);
    }
    if(!gamePlayer.isPresent()){
        return new ResponseEntity<>(makeMap("error","Ese gamePlayer no existe"), HttpStatus.UNAUTHORIZED);
    }
    if(playerRepository.findByUserName(authentication.getName()).getGameplayers().stream().noneMatch(gp-> gp.equals(gamePlayer.get()))){
        return new ResponseEntity<>(makeMap("error","Ese gamePlayer no le corresponde"), HttpStatus.UNAUTHORIZED);
    }
    if(ships.size() != 5) {
        return new ResponseEntity<>(makeMap("error", "5 barcos"), HttpStatus.FORBIDDEN);
    }
    if (gamePlayer.get().getShips().size() !=0){
        return new ResponseEntity<>(makeMap("error", "Los barcos ya estan ubicados"), HttpStatus.FORBIDDEN);
    }
    for (Ship newShip: ships) {
        if ((newShip.getType().equals("destroyer")) && (newShip.getShipLocations().size() != 3)){
            return new ResponseEntity<>(makeMap("error","El tamaño del barco 'Destroyer' deberia ser de 3 casillas"), HttpStatus.FORBIDDEN);
        }
        if ((newShip.getType().equals("patrolboat")) && (newShip.getShipLocations().size() != 2)){
            return new ResponseEntity<>(makeMap("error","El tamaño del barco 'Patrol boat' deberia ser de 2 casillas"), HttpStatus.FORBIDDEN);
        }
        if ((newShip.getType().equals("submarine")) && (newShip.getShipLocations().size() != 3)){
            return new ResponseEntity<>(makeMap("error","El tamaño del barco 'Submarine' deberia ser de 3 casillas"), HttpStatus.FORBIDDEN);
        }
        if ((newShip.getType().equals("battleship")) && (newShip.getShipLocations().size() != 4)){
            return new ResponseEntity<>(makeMap("error","El tamaño del barco 'Battleship' deberia ser de 4 casillas"), HttpStatus.FORBIDDEN);
        }
        if ((newShip.getType().equals("carrier")) && (newShip.getShipLocations().size() != 5)){
            return new ResponseEntity<>(makeMap("error","El tamaño del barco 'Carrier' deberia ser de 5 casillas"), HttpStatus.FORBIDDEN);
        }
        newShip.setGamePlayer(gamePlayer.get());
        shipRepository.save(newShip);
    }
    return new ResponseEntity<>(makeMap("OK", "todo regio"), HttpStatus.ACCEPTED);
    }

    @RequestMapping ("/games/players/{nn}/ships")
    public ResponseEntity<Map<String,Object>> placeShips(@PathVariable Long nn, Authentication authentication){
        Optional<GamePlayer> gamePlayer= gamePlayerRepository.findById(nn);
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "Usuario no logeado"), HttpStatus.UNAUTHORIZED);
        }
        if(!gamePlayer.isPresent()){
            return new ResponseEntity<>(makeMap("error","Ese gamePlayer no existe"), HttpStatus.UNAUTHORIZED);
        }
        if(playerRepository.findByUserName(authentication.getName()).getGameplayers().stream().noneMatch(gp-> gp.equals(gamePlayer.get()))){
            return new ResponseEntity<>(makeMap("error","Ese gamePlayer no le corresponde"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.get().getShips().size() == 0){
            return new ResponseEntity<>(makeMap("error", "No hay barcos"), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(makeMap("ships", gamePlayer.get().getShips().stream().map(Ship::makeShipDTO).collect(Collectors.toList())), HttpStatus.ACCEPTED);
    }

    @PostMapping("/games/players/{nn}/salvoes")
    public ResponseEntity<Map<String,Object>> fireSalvos(@PathVariable Long nn, @RequestBody Salvo salvoes, Authentication authentication){
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(nn);
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Usuario no logeado"), HttpStatus.UNAUTHORIZED);
        }
        if(gamePlayer.isEmpty()){
            return new ResponseEntity<>(makeMap("error","El gamePlayer no existe"), HttpStatus.FORBIDDEN);
        }
        Optional<GamePlayer> gamePlayer2 = gamePlayer.get().getGame().getGameplayers().stream().filter(gp -> gp!=gamePlayer.get()).findFirst();
        if(gamePlayer2.isEmpty()){
            return new ResponseEntity<>(makeMap("error","El gamePlayer rival no existe"), HttpStatus.FORBIDDEN);
        }
        if(playerRepository.findByUserName(authentication.getName()).getGameplayers().stream().noneMatch(gp-> gp.equals(gamePlayer.get()))){
            return new ResponseEntity<>(makeMap("error","Ese gamePlayer no le corresponde"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.get().getShips().size() !=5){
            return new ResponseEntity<>(makeMap("error", "Tus barcos no estan ubicados"), HttpStatus.FORBIDDEN);
        }
        if (gamePlayer2.get().getShips().size() !=5){
            return new ResponseEntity<>(makeMap("error", "Los barcos rivales no estan ubicados"), HttpStatus.FORBIDDEN);
        }
        if(salvoes.getSalvoLocations().size() < 1 || salvoes.getSalvoLocations().size() > 5){
            return new ResponseEntity<>(makeMap("error","Dispare entre 1 y 5 veces"), HttpStatus.FORBIDDEN);
        }
        if(gamePlayer.get().getId() < gamePlayer2.get().getId()){
            if(gamePlayer.get().getSalvoes().size() == gamePlayer2.get().getSalvoes().size()){
                salvoes.setGamePlayer(gamePlayer.get());
                salvoes.setTurn(gamePlayer.get().getSalvoes().size() + 1);
                salvoRepository.save(salvoes);
                return new ResponseEntity<>(makeMap("OK","todo regio"), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(makeMap("error","Por favor espere su turno"), HttpStatus.FORBIDDEN);
            }
        }else{
            if(gamePlayer.get().getSalvoes().size() < gamePlayer2.get().getSalvoes().size()){
                salvoes.setGamePlayer(gamePlayer.get());
                salvoes.setTurn(gamePlayer.get().getSalvoes().size() + 1);
                salvoRepository.save(salvoes);
                return new ResponseEntity<>(makeMap("OK","todo regio"), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(makeMap("error","Por favor espere su turno"), HttpStatus.FORBIDDEN);
            }
        }
    }

    @PostMapping("/players")
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String email, @RequestParam String password) {
        if (email.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
        }
        Player player = playerRepository.findByUserName(email);
        if (player != null) {
            return new ResponseEntity<>(makeMap("error", "Username already exists"), HttpStatus.FORBIDDEN);
        }
        Player newPlayer = playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping("/game_view/{nn}")
    public ResponseEntity<Map<String,Object>> findGamePlayer(@PathVariable Long nn, Authentication authentication) {
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(nn);
        if (gamePlayer.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "No existe el gamePlayer"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.get().getPlayer().getId() != playerRepository.findByUserName(authentication.getName()).getId()) {
            return new ResponseEntity<>(makeMap("error", "No te lo voy a permitir"), HttpStatus.UNAUTHORIZED);
        } else {
            if (gamePlayer.get().getGameState(gamePlayer.get()).equals("WON")) {
                Score puntaje = new Score(1f, LocalDateTime.now(), gamePlayer.get().getGame(), gamePlayer.get().getPlayer());
                scoreRepository.save(puntaje);
            }
            if (gamePlayer.get().getGameState(gamePlayer.get()).equals("TIE")) {
                Score puntaje = new Score(0.5f, LocalDateTime.now(), gamePlayer.get().getGame(), gamePlayer.get().getPlayer());
                scoreRepository.save(puntaje);
            }
            if (gamePlayer.get().getGameState(gamePlayer.get()).equals("LOST")) {
                Score puntaje = new Score(0f, LocalDateTime.now(), gamePlayer.get().getGame(), gamePlayer.get().getPlayer());
                scoreRepository.save(puntaje);
            }
            return new ResponseEntity<>(gamePlayer.get().makeGameViewDTO(gamePlayer.get()), HttpStatus.ACCEPTED);
        }
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}

