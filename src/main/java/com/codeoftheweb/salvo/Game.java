package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Game() { }

    public Game(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Map<String,Object> makeGameDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getGameplayers()
                .stream().map(gameplayers -> gameplayers.makeGameplayersDTO()).collect(toList()));
        dto.put("scores", this.getGameplayers().stream().map(gamePlayer -> {
            if(gamePlayer.getScore().isPresent()){
                return gamePlayer.getScore().get().makeScoreDTO();}
            else{
                return null;}
        }));
        return dto;
    }

    public Set<Score> getScores() {return scores;}
    public void setScores(Set<Score> scores) {this.scores = scores;}

    public Set<GamePlayer> getGameplayers() {return gameplayers;}
    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }

    public List<Player> getPlayers() {
        return gameplayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }
}

