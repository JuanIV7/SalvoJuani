package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private int turn;

    @ElementCollection
    @Column(name="")
    private List<String> salvoLocation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo() {}

    public Salvo(GamePlayer gamePlayer, int turn, List<String> salvoLocation) {
        this.gamePlayer = gamePlayer;
        this.turn = turn;
        this.salvoLocation = salvoLocation;
    }

    public Map<String, Object> makeSalvoDTO(){
        Map<String,Object> dto=new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocation());
        return dto;
    }

    public List<String> getSalvoLocation() {return salvoLocation;}
    public void setSalvoLocation(List<String> salvoLocation) {this.salvoLocation = salvoLocation;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public int getTurn() {return turn;}
    public void setTurn(int turn) {this.turn = turn;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

}