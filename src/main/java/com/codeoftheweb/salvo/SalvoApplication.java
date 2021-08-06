package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository basededatosjugador , GameRepository basededatosjuego, GamePlayerRepository basededatosgp ){return (args) -> {
			Player player1 = new Player("juanivillalba@outlook.com");
			Player player2 = new Player("lionelmessi@gmail.com");
			Player player3 = new Player("villabajuani@outlook.com");
		    Player player4 = new Player("villabajuani@outlook.com");
            basededatosjugador.save(player1);
            basededatosjugador.save(player2);
    		basededatosjugador.save(player3);
    		basededatosjugador.save(player4);

		    Game game1 = new Game(LocalDateTime.now());
		    Game game2 = new Game(LocalDateTime.now().plusHours(1));
		    Game game3 = new Game(LocalDateTime.now().plusHours(2));
		    Game game4 = new Game(LocalDateTime.now().plusHours(3));
		    Game game5 = new Game(LocalDateTime.now().plusHours(4));
		    Game game6 = new Game(LocalDateTime.now().plusHours(5));
            basededatosjuego.save(game1);
		    basededatosjuego.save(game2);
		    basededatosjuego.save(game3);
		    basededatosjuego.save(game4);
		    basededatosjuego.save(game5);
		    basededatosjuego.save(game6);

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

		    basededatosgp.save(gameplayer1);
		    basededatosgp.save(gameplayer2);
		    basededatosgp.save(gameplayer3);
		    basededatosgp.save(gameplayer4);
		    basededatosgp.save(gameplayer5);
		    basededatosgp.save(gameplayer6);
		    basededatosgp.save(gameplayer7);
		    basededatosgp.save(gameplayer8);
		    basededatosgp.save(gameplayer9);
		    basededatosgp.save(gameplayer10);
		    basededatosgp.save(gameplayer11);
 		};
	}
}
