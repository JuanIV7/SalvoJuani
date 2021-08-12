package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String type;

    @ElementCollection
    @Column(name="")
    private List<String> shipLocation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship() {}

    public Ship(GamePlayer gamePlayer, String type, List<String> shipLocation) {
        this.gamePlayer = gamePlayer;
        this.type = type;
        this.shipLocation = shipLocation;
    }
    public Map<String, Object> makeShipDTO(){
        Map<String,Object> dto=new LinkedHashMap<>();
        dto.put("type", this.getType());
        dto.put("locations", this.getShipLocation());
        return dto;
    }


    public List<String> getShipLocation() {return shipLocation;}
    public void setShipLocation(List<String> shipLocation) {this.shipLocation = shipLocation;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public GamePlayer getGamePlayer() {return gamePlayer;}
    public void setGamePlayer(GamePlayer gamePlayer) {this.gamePlayer = gamePlayer;}

    //@JsonIgnore
    //public List<GamePlayer> getGamePlayers() {
    //    return gamePlayers.stream().map(ship -> ship.getGamePlayer()).collect(toList());
    //}
}
