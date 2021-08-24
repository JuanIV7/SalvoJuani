package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    public Player getPlayer() {return player;}
    public void setPlayer(Player player) {this.player = player;}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch=FetchType.EAGER)
    Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvoes;

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


    public Map<String,Object> makeGameViewDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getGame().getId());
        dto.put("created", this.getJoinDate());
        dto.put("gamePlayers", this.getGame().getGameplayers().stream().map(x -> x.makeGameplayersDTO()).collect(Collectors.toList()));
        dto.put("ships", this.getShips().stream().map(ship -> ship.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", this.getGame().getGameplayers().stream()
                .flatMap(gamePlayer -> gamePlayer.getSalvoes().stream().map(salvo -> salvo.makeSalvoDTO()))
                .collect(Collectors.toList()));
        return dto;
    }

    public Optional<Score> getScore(){
        return this.getPlayer().getScore(this.getGame());
    }

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
