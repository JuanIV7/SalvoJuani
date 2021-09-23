package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvoes;

    @ElementCollection
    @Column(name="hits_self")
    private List<String> self = new ArrayList<>();

    @ElementCollection
    @Column(name="hits_opponent")
    private List<String> opponent = new ArrayList<>();

    public Player getPlayer() {return player;}
    public void setPlayer(Player player) {this.player = player;}

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public GamePlayer() { }

    public Game getGame() {return game;}

    public void setGame(Game game) {this.game = game;}
    public GamePlayer(Player player, Game game, LocalDateTime joinDate) {
        this.game = game;
        this.joinDate = joinDate;
        this.player = player;
    }

    public LocalDateTime getJoinDate() {return joinDate;}

    public void setJoinDate(LocalDateTime joinDate) {this.joinDate = joinDate;}
    public Map<String,Object> makeGameplayersDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }

    public Optional<GamePlayer> Opponent(GamePlayer gamePlayer){
        return gamePlayer.getGame().getGameplayers().stream().filter(gp -> gp!=gamePlayer).findFirst();
    }

    public Map<String,Object> makeGameViewDTO(GamePlayer gamePlayer){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getGame().getId());
        dto.put("created", this.getJoinDate());
        dto.put("gameState", getGameState(gamePlayer));
        dto.put("gamePlayers", this.getGame().getGameplayers().stream().map(x -> x.makeGameplayersDTO()).collect(Collectors.toList()));
        dto.put("ships", this.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", this.getGame().getGameplayers().stream()
                .flatMap(gamePlaye -> gamePlaye.getSalvoes().stream().map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));
        dto.put("hits", this.makeHitsDTO(gamePlayer));
        return dto ;
    }

    public Map<String, Object> makeHitsDTO(GamePlayer gamePlayer){
        Map <String, Object> dto = new LinkedHashMap<>();
        if(Opponent(gamePlayer).isPresent()){
            dto.put("self", Opponent(gamePlayer).get().getSalvoes().stream().map(z -> HitsSELFOPDTO(Opponent(gamePlayer).get(), z)));
            dto.put("opponent", gamePlayer.getSalvoes().stream().map(z -> HitsSELFOPDTO(gamePlayer, z)));
        }else{
            dto.put("self", new ArrayList<>());
            dto.put("opponent", new ArrayList<>());
        }
        return dto;
    }

    public Map<String, Object> HitsSELFOPDTO(GamePlayer gamePlayer, Salvo salvoes){
        Map <String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvoes.getTurn());
        dto.put("hitLocations", this.getHitsLocations(gamePlayer, salvoes));
        dto.put("damages", getDamages(gamePlayer, salvoes));
        dto.put("missed", salvoes.getSalvoLocations().size() - this.getHitsLocations(gamePlayer, salvoes).size());
        return dto;
    }

    public List<String> getHitsLocations(GamePlayer gamePlayer, Salvo salvoes){
        List<String> hits = new ArrayList<>();
        List<String> oplocations = new ArrayList<>();
        oplocations = Opponent(gamePlayer).get().getShips().stream().flatMap(x -> x.getShipLocations().stream()).collect(Collectors.toList());
        hits = oplocations.stream().filter(x -> salvoes.getSalvoLocations().contains(x)).collect(Collectors.toList());
        return hits;
    }

    public Map<String, Object> getDamages(GamePlayer gamePlayer, Salvo salvoes){

        Map<String, Object> dto= new LinkedHashMap<>();
        int CarrierHitsTurn=0;
        int BattleshipHitsTurn=0;
        int SubmarineHitsTurn=0;
        int DestroyerHitsTurn=0;
        int PatrolboatHitsTurn=0;
        int CarrierHitsSuma=0;
        int BattleshipHitsSuma=0;
        int SubmarineHitsSuma=0;
        int DestroyerHitsSuma=0;
        int PatrolboatHitsSuma=0;

        List<String> CarrierLoc = new ArrayList<>();
        List<String> BattleshipLoc = new ArrayList<>();
        List<String> SubmarineLoc = new ArrayList<>();
        List<String> DestroyerLoc = new ArrayList<>();
        List<String> PatrolboatLoc = new ArrayList<>();

        Ship tipoCarrier = Opponent(gamePlayer).get().getShips().stream().filter(x -> x.getType().equals("carrier")).findFirst().get();
        CarrierLoc= tipoCarrier.getShipLocations();
        Ship tipoBattleship = Opponent(gamePlayer).get().getShips().stream().filter(x -> x.getType().equals("battleship")).findFirst().get();
        BattleshipLoc= tipoBattleship.getShipLocations();
        Ship tipoSubmarine = Opponent(gamePlayer).get().getShips().stream().filter(x -> x.getType().equals("submarine")).findFirst().get();
        SubmarineLoc= tipoSubmarine.getShipLocations();
        Ship tipoDestroyer = Opponent(gamePlayer).get().getShips().stream().filter(x -> x.getType().equals("destroyer")).findFirst().get();
        DestroyerLoc= tipoDestroyer.getShipLocations();
        Ship tipoPatrolboat = Opponent(gamePlayer).get().getShips().stream().filter(x -> x.getType().equals("patrolboat")).findFirst().get();
        PatrolboatLoc= tipoPatrolboat.getShipLocations();

        List<List<Salvo>> hitssuma = new ArrayList<>();
        hitssuma.add(gamePlayer.getSalvoes().stream().filter(sv -> sv.getTurn() <= salvoes.getTurn()).collect(Collectors.toList()));
        for (List<Salvo> x3: hitssuma){
            for (Salvo x1 : x3) {
                for (String x2 : getHitsLocations(gamePlayer, x1)) {
                    if (CarrierLoc.contains(x2)) {
                        CarrierHitsSuma++;
                    }
                    if (BattleshipLoc.contains(x2)) {
                        BattleshipHitsSuma++;
                    }
                    if (SubmarineLoc.contains(x2)) {
                        SubmarineHitsSuma++;
                    }
                    if (DestroyerLoc.contains(x2)) {
                        DestroyerHitsSuma++;
                    }
                    if (PatrolboatLoc.contains(x2)) {
                        PatrolboatHitsSuma++;
                    }
                }
            }
        }
        for(String locationStr : getHitsLocations(gamePlayer, salvoes)){
        if(CarrierLoc.contains(locationStr)){
            CarrierHitsTurn++;
        }
        if(BattleshipLoc.contains(locationStr)){
            BattleshipHitsTurn++;
        }
        if(SubmarineLoc.contains(locationStr)){
            SubmarineHitsTurn++;
        }
        if(DestroyerLoc.contains(locationStr)){
            DestroyerHitsTurn++;
        }
        if(PatrolboatLoc.contains(locationStr)){
            PatrolboatHitsTurn++;
        }
        }
        dto.put("carrierHits", CarrierHitsTurn);
        dto.put("battleshipHits",BattleshipHitsTurn);
        dto.put("submarineHits",SubmarineHitsTurn);
        dto.put("destroyerHits",DestroyerHitsTurn);
        dto.put("patrolboatHits",PatrolboatHitsTurn);
        dto.put("carrier",CarrierHitsSuma);
        dto.put("battleship",BattleshipHitsSuma);
        dto.put("submarine",SubmarineHitsSuma);
        dto.put("destroyer",DestroyerHitsSuma);
        dto.put("patrolboat",PatrolboatHitsSuma);

        return dto;
    }

    public String getGameState(GamePlayer gamePlayer){
        String gameState="";
        if (Opponent(gamePlayer).isEmpty()){
            gameState = "WAITINGFOROPP";
            return gameState;
        }
        int turnosgp1=gamePlayer.getSalvoes().size();
        int turnosgp2=Opponent(gamePlayer).get().getSalvoes().size();
        if (gamePlayer.getShips().size() != 5){
            gameState = "PLACESHIPS";
            return gameState;
        }
        if(Opponent(gamePlayer).get().getShips().size() != 5){
            gameState = "WAIT";
            return gameState;
        }
        if(gamePlayer.getShips().size() == 5 && Opponent(gamePlayer).get().getShips().size() == 5) {
            if(gamePlayer.getId() < Opponent(gamePlayer).get().getId()){
                if(gamePlayer.sunkenShips(Opponent(gamePlayer).get(), gamePlayer)){
                    if (turnosgp1>turnosgp2){
                        gameState = "WAIT";
                        return gameState;
                    }else if(gamePlayer.sunkenShips(gamePlayer, Opponent(gamePlayer).get())){
                        gameState = "TIE";
                        return gameState;
                    }else{
                        gameState = "WON";
                        return gameState;
                    }
                }if(gamePlayer.sunkenShips(gamePlayer , Opponent(gamePlayer).get())){
                    gameState = "LOST";
                    return gameState;
                }
            }else{
                if (gamePlayer.sunkenShips(Opponent(gamePlayer).get(), gamePlayer)) {
                    if (gamePlayer.sunkenShips(gamePlayer, Opponent(gamePlayer).get())) {
                        gameState = "TIE";
                        return gameState;
                    } else {
                        gameState = "WON";
                        return gameState;
                    }
                }
                if (gamePlayer.sunkenShips(gamePlayer, Opponent(gamePlayer).get()) && turnosgp1==turnosgp2){
                    gameState = "LOST";
                    return gameState;
                }
            }
            if (gamePlayer.getId() < Opponent(gamePlayer).get().getId()) {
                if (turnosgp1 == turnosgp2) {
                    gameState = "PLAY";
                    return gameState;
                } else if (turnosgp1 > turnosgp2) {
                    gameState = "WAIT";
                    return gameState;
                }
            }
            if (gamePlayer.getId() > Opponent(gamePlayer).get().getId()) {
                if (turnosgp1 == turnosgp2) {
                    gameState = "WAIT";
                    return gameState;
                } else if (turnosgp2 > turnosgp1) {
                    gameState = "PLAY";
                    return gameState;
                }
            }
        }
        return gameState;
    }

    private boolean sunkenShips(GamePlayer gpShips, GamePlayer gpSalvoes){
        if(!gpShips.getShips().isEmpty() && !gpSalvoes.getSalvoes().isEmpty()){
            return gpSalvoes.getSalvoes().stream().flatMap(x -> x.getSalvoLocations().stream()).collect(Collectors.toList())
                    .containsAll(gpShips.getShips().stream().flatMap(z -> z.getShipLocations().stream()).collect(Collectors.toList()));
        }
        return false;
    }

    public Optional<Score> getScore(){return this.getPlayer().getScore(this.getGame());}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Set<Salvo> getSalvoes() {return salvoes;}
    public void setSalvoes(Set<Salvo> salvoes) {this.salvoes = salvoes;}

    public Set<Ship> getShips() {
        return ships;
    }
    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

}
