package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private float score;
    private LocalDateTime finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    public Score() {}

    public Score(float score, LocalDateTime finishDate, Game game, Player player){
        this.score = score;
        this.finishDate = finishDate;
        this.game = game;
        this.player = player;
    }

    public Map<String,Object> makeScoreDTO(){
        Map<String,Object> dto= new LinkedHashMap<>();
        dto.put("player", this.getPlayer().getId());
        dto.put("score", this.getScore());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public float getScore() {return score;}
    public void setScore(float score) {this.score = score;}

    public Game getGame() {return game;}
    public void setGame(Game game) {this.game = game;}

    public Player getPlayer() {return player;}
    public void setPlayer(Player player) {this.player = player;}

    public LocalDateTime getFinishDate() {return finishDate;}
    public void setFinishDate(LocalDateTime finishDate) {this.finishDate = finishDate;}
}
