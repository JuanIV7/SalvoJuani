package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy="native")
    private Long id;
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="gameid", fetch=FetchType.EAGER)
    Set<GamePlayer> gameplayers;

    public Game() { }
    public Game(LocalDateTime creationDate) {this.creationDate = creationDate;}

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGameplayers() {
        return gameplayers;
    }

    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }

    public List<Player> getPlayers() {
        return gameplayers.stream().map(sub -> sub.getPlayerid()).collect(toList());
    }
}

