package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GameRepository gameRepository, GamePlayerRepository gameplayerRepository, ShipRepository shipRepository ){return (args) -> {
			Player player1 = new Player("juanivillalba@outlook.com");
			Player player2 = new Player("lionelmessi@gmail.com");
			Player player3 = new Player("villabajuani@outlook.com");
		    Player player4 = new Player("villabajuani@outlook.com");
            playerRepository.save(player1);
            playerRepository.save(player2);
    		playerRepository.save(player3);
    		playerRepository.save(player4);

		    Game game1 = new Game(LocalDateTime.now());
		    Game game2 = new Game(LocalDateTime.now().plusHours(1));
		    Game game3 = new Game(LocalDateTime.now().plusHours(2));
		    Game game4 = new Game(LocalDateTime.now().plusHours(3));
		    Game game5 = new Game(LocalDateTime.now().plusHours(4));
		    Game game6 = new Game(LocalDateTime.now().plusHours(5));
            gameRepository.save(game1);
		    gameRepository.save(game2);
		    gameRepository.save(game3);
		    gameRepository.save(game4);
		    gameRepository.save(game5);
		    gameRepository.save(game6);

		    GamePlayer gameplayer1 = new GamePlayer(player1, game1, LocalDateTime.now());
		    GamePlayer gameplayer2 = new GamePlayer(player2, game1, LocalDateTime.now());
		    GamePlayer gameplayer3 = new GamePlayer(player1, game2, LocalDateTime.now());
		    GamePlayer gameplayer4 = new GamePlayer(player2, game2, LocalDateTime.now());
		    GamePlayer gameplayer5 = new GamePlayer(player2, game3, LocalDateTime.now());
	        GamePlayer gameplayer6 = new GamePlayer(player3, game3, LocalDateTime.now());
		    GamePlayer gameplayer7 = new GamePlayer(player1, game4, LocalDateTime.now());
		    GamePlayer gameplayer8 = new GamePlayer(player3, game4, LocalDateTime.now());
		    GamePlayer gameplayer9 = new GamePlayer(player3, game5, LocalDateTime.now());
		    GamePlayer gameplayer10 = new GamePlayer(player1, game5, LocalDateTime.now());
		    GamePlayer gameplayer11 = new GamePlayer(player4, game6, LocalDateTime.now());

		    gameplayerRepository.save(gameplayer1);
		    gameplayerRepository.save(gameplayer2);
		    gameplayerRepository.save(gameplayer3);
		    gameplayerRepository.save(gameplayer4);
		    gameplayerRepository.save(gameplayer5);
		    gameplayerRepository.save(gameplayer6);
		    gameplayerRepository.save(gameplayer7);
		    gameplayerRepository.save(gameplayer8);
		    gameplayerRepository.save(gameplayer9);
		    gameplayerRepository.save(gameplayer10);
		    gameplayerRepository.save(gameplayer11);

		    Ship ship1 = new Ship(gameplayer1,"Carrier", Arrays.asList("H1","H2","H3","H4","H5"));
			Ship ship2 = new Ship(gameplayer1,"Battleship", Arrays.asList("H1","H2","H3","H4"));
			Ship ship3 = new Ship(gameplayer1,"Cruiser", Arrays.asList("H1","H2","H3"));
			Ship ship4 = new Ship(gameplayer1,"Submarine", Arrays.asList("H1","H2","H3"));
			Ship ship5 = new Ship(gameplayer1,"Destroyer", Arrays.asList("H1","H2"));
		    shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
 		};
	}
}
