package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
        dto.put("email", this.getPlayerid().makePlayerDTO());
        return dto;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}
}
