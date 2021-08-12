package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String userName;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    public Player() { }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Player(String userName) {this.userName = userName;}

    public String getUserName() {return userName;}

    public Set<GamePlayer> getGameplayers() {return gameplayers;}

    public void setGameplayers(Set<GamePlayer> gameplayers) {this.gameplayers = gameplayers;}

    public void setUserName(String userName) {this.userName = userName;}

    public Map<String,Object> makePlayerDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    @JsonIgnore
    public List<Game> getGames() {
        return gameplayers.stream().map(sub -> sub.getGame()).collect(toList());
    }
}
