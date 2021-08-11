package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private Player playerid;

    public Player getPlayerid() {return playerid;}
    public void setPlayerid(Player playerid) {this.playerid = playerid;}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game gameid;

    @OneToMany(mappedBy = "gamePlayerID", fetch=FetchType.EAGER)
    Set<Ship> ships;

    public GamePlayer() { }

    public Game getGameid() {return gameid;}
    public void setGameid(Game gameid) {this.gameid = gameid;}

    public GamePlayer(Player playerid, Game gameid, LocalDateTime joinDate) {
        this.gameid = gameid;
        this.joinDate = joinDate;
        this.playerid = playerid;
    }

    public LocalDateTime getJoinDate() {return joinDate;}
    public void setJoinDate(LocalDateTime joinDate) {this.joinDate = joinDate;}

    public Map<String,Object> makeGameplayersDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayerid().makePlayerDTO());
        dto.put("ship", this.getShips().stream().map(xx -> xx.makeShipDTO()).collect(Collectors.toList()));
        return dto;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    //@JsonIgnore
    //public List<Ship> getShips() {
    //    return ships.stream().map(gamePlayer -> gamePlayer.getShips()).collect(toList());
    //}
}
