package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GameRepository gameRepository, GamePlayerRepository gameplayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository){return (args) -> {

			Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
		    Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));
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
			Ship ship2 = new Ship(gameplayer1,"Battleship", Arrays.asList("G1","G2","G3","G4"));
			Ship ship3 = new Ship(gameplayer1,"Cruiser", Arrays.asList("A1","A2","A3"));
			Ship ship4 = new Ship(gameplayer1,"Submarine", Arrays.asList("B1","B2","B3"));
			Ship ship5 = new Ship(gameplayer1,"Destroyer", Arrays.asList("D1","C1"));
			Ship ship6 = new Ship(gameplayer2,"Destroyer", Arrays.asList("D3","C3"));
		    shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);
			shipRepository.save(ship6);

			Salvo salvo1 = new Salvo(gameplayer1, 1, Arrays.asList("E1","E2"));
			Salvo salvo2 = new Salvo(gameplayer2, 1, Arrays.asList("G1","G2","G3","G4","G5"));
			Salvo salvo3 = new Salvo(gameplayer1, 2, Arrays.asList("A5"));
			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);

			Score score1 = new Score(1f, LocalDateTime.now(), game1, player1);
			Score score2 = new Score(0f, LocalDateTime.now(), game1, player2);
			Score score3 = new Score(0.5f, LocalDateTime.now(), game2, player1);
			Score score4 = new Score(0.5f, LocalDateTime.now(), game2, player2);
			Score score5 = new Score(0.5f, LocalDateTime.now(), game5, player1);
			Score score6 = new Score(0.5f, LocalDateTime.now(), game5, player3);
			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);
	};
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("PLAYER")) {
				};
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}



@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/game_view/**").hasAuthority("PLAYER")
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/api/games").permitAll();

		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}
	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}