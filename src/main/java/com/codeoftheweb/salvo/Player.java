package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private String userName;
    private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Player(String userName) {this.userName = userName;}
    public String getUserName() {return userName;}

    public Set<GamePlayer> getGameplayers() {return gameplayers;}
    public void setGameplayers(Set<GamePlayer> gameplayers) {this.gameplayers = gameplayers;}

    public void setUserName(String userName) {this.userName = userName;}

    public Set<Score> getScores() {return scores;}
    public void setScores(Set<Score> scores) {this.scores = scores;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Map<String,Object> makePlayerDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    public Optional<Score> getScore(Game game){
        return this.getScores().stream().filter(sc -> sc.getGame().getId().equals(game.getId())).findFirst();
    }


}
