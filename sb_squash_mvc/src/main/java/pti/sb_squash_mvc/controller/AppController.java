package pti.sb_squash_mvc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import pti.sb_squash_mvc.db.Database;
import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.Place;
import pti.sb_squash_mvc.model.Player;
import pti.sb_squash_mvc.model.Player_type;

@Controller
public class AppController {
	
	
	private Player loggedPlayer;
	
	
	@GetMapping("/")
	public String index() {
		
		return "index.html";
	}
	
	
	
	@PostMapping("/login")
	public String login(Model model,
				@RequestParam(name = "name") String name,
				@RequestParam(name = "password") String pwd
			)
	{
	
		String targetPage = "";
		
		Database db = new Database();
		
		Player player = db.login(name, pwd);

		if(player != null) {
			
			this.loggedPlayer = player;
			
			if(player.getType() == Player_type.ADMIN) {
				
				targetPage = "admin_index.html";
			}
			else {
				
				if(player.isChangePassword() == true) {
					
					targetPage = "newpwd.html";
					
				
				}
				else {
					
					ArrayList<Game> orderedGamesList = getGameList();
					model.addAttribute("games", orderedGamesList);
					
					ArrayList<Player> playerList = getPlayerList();
					model.addAttribute("players", playerList);
					
					
					ArrayList<Place> placeList = getPlaceList();
					model.addAttribute("places", placeList);
					
					targetPage = "games.html";	
				}		
			}
			
		}
		else {
			
			targetPage = "index.html";
			model.addAttribute("logerror", "Login failed!");
		}
				
		db.close();
		
		return targetPage;
	}
	
	
	@PostMapping("/savepwd")
	public String saveNewPassword(Model model,
				@RequestParam(name = "password1") String pwd1,
				@RequestParam(name = "password2") String pwd2
				
			)
	{
		
		String targetPage = "";
		String success = "";
		
		if (pwd1.equals(pwd2)) {
			
			loggedPlayer.setPassword(pwd1);
			loggedPlayer.setChangePassword(false);
			
			
			Database db = new Database();
			
			db.saveNewPassword(loggedPlayer);
			
			
			db.close();
			
			success = "Password has changed succesfully";
			
			ArrayList<Game> orderedGamesList = getGameList();
			model.addAttribute("games", orderedGamesList);
			
			ArrayList<Player> playerList = getPlayerList();
			model.addAttribute("players", playerList);
			
			
			ArrayList<Place> placeList = getPlaceList();
			model.addAttribute("places", placeList);
			
			targetPage = "games.html"; 
			
		}
		
		else {
			
			success ="The passwords are different";
			targetPage = "newpwd.html"; 
		}
				
		model.addAttribute("pwchangeresult", success);
		return targetPage;
	
	}
	
	
	@GetMapping("/games")
	public String getGames(Model model) {
		
		ArrayList<Game> orderedGamesList = getGameList();
					
		model.addAttribute("games", orderedGamesList);
		
		
		ArrayList<Player> playerList = getPlayerList();
		
		model.addAttribute("players", playerList);
		
		
		
		ArrayList<Place> placeList = getPlaceList();
		
		model.addAttribute("places", placeList);
		
			
		
		return "games.html";
	}
	
	
	@GetMapping("/searchgamebyplayer")
	public String searchGameByPlayer(Model model,
			@RequestParam(name="PlayerName") String name			
			) 
	{
		
		ArrayList<Game> orderedGamesList = getGameList();
		
		if(name.equals("") == true) {
			
			model.addAttribute("games", orderedGamesList);
			
		}
		else {
			
			ArrayList<Game> searchResultList = new ArrayList<Game>();
			
			for(int index = 0; index < orderedGamesList.size(); index++) {
				
				Game cGame = orderedGamesList.get(index);
								
						
				if( (cGame.getPlayer1().getName().equals(name) == true) || (cGame.getPlayer2().getName().equals(name) == true) ) {
			
					searchResultList.add(cGame);
				}
			}
			
			model.addAttribute("games", searchResultList);
			
		}
				
		
		return "games.html";
	}
	
	
	@GetMapping("/searchgamebyplace")
	public String searchGameByPlace(Model model,
			@RequestParam(name="PlaceName") String name			
			) 
	{
		ArrayList<Game> orderedGamesList = getGameList();
			
		
		if(name.equals("") == true) {
			
			model.addAttribute("games", orderedGamesList);
			
		}
		else {
			
			ArrayList<Game> searchResultList = new ArrayList<Game>();
			
			for(int index = 0; index < orderedGamesList.size(); index++) {
				
				Game cGame = orderedGamesList.get(index);
								
						
				if( cGame.getPlace().getName().equals(name) == true ) {
			
					searchResultList.add(cGame);
				}
			}
			
			model.addAttribute("games", searchResultList);
			
		}
				
		
		return "games.html";
		
	}
	
	
	@GetMapping("/admin")
	public String adminFunctions(Model model,
			@RequestParam(name="function") String function
			)
	{
		String targetPage = "";
		
		//GameList
		if (function.equals("gL") == true) {
			
			targetPage = getGames(model);
			
		}
		//new player
		else if (function.equals("nP") == true) {
			
			targetPage = "newplayer.html";
			
		}
		//new place
		else if (function.equals("nL") == true) {
			
			targetPage = "newplace.html";
			
		}
		//new game
		else if (function.equals("nG") == true) {
			
			Database db = new Database();
			ArrayList<Player> players = db.getPlayerList();
			ArrayList<Place> places = db.getPlaceList();
			
			db.close();
			
			model.addAttribute("players", players);
			model.addAttribute("places", places);
					
			targetPage = "newgame.html";		
					
		}
		//set result
		else if (function.equals("nR") == true) {
			
			Database db = new Database();
			ArrayList<Player> players = db.getPlayerList();
			ArrayList<Place> places = db.getPlaceList();
			
			db.close();
			
			model.addAttribute("players", players);
			model.addAttribute("places", places);
			
			targetPage = "searchgametosetresult.html";
			
		}
		else {
			
			model.addAttribute("error", "No such function");
			targetPage = "admin_index.html";
		}
		
		
		return targetPage;
		
	}
	
	
	@GetMapping("/admin/addnewplayer")
	public String addNewPlayer(Model model,
			@RequestParam(name="name") String name
			)
	{
		String targetPage = "";
		String regresult = "";
		
		Database db = new Database();
		boolean playerExists = db.playerExistsWithName(name);
		
		if(playerExists == false) {
			
			Player player = new Player(name);
			
			player.setPassword(generatedPassword());
						
			db.savePlayer(player);
					
			
			targetPage = "admin_index.html";
			regresult = "Player has been added successfully";
			
		}
		else {
			targetPage = "newplayer.html";
			regresult = "Player name already exists!";
		}
		
		
		model.addAttribute("regresult", regresult);
		db.close();
		
		
		
		return targetPage;
		
	}
	
	
	public String generatedPassword() {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	   return generatedString;
	}
	
	
	@GetMapping("/admin/addnewplace")
	public String addNewPlace(Model model,
			@RequestParam(name="name") String name,
			@RequestParam(name="address") String address,
			@RequestParam(name="rentalfee") int rentalfee
			) {
		
		String targetPage = "";
		String regresult = "";
		
		
		Database db = new Database();
		boolean placeExists = db.placeExistsWithName(name);
		
		if(placeExists == false) {
			
			Place place = new Place(name, address, rentalfee);
						
			db.savePlace(place);
					
			
			targetPage = "admin_index.html";
			regresult = "Place has been added successfully";
			
			
		}
		else {
			targetPage = "newplace.html";
			regresult = "Place's name already exists!";
		}
		
		
		model.addAttribute("regresult", regresult);
		db.close();
		
		
		return targetPage;
	}
	
	
	
	
	
	@GetMapping("/admin/addnewgame")
	public String addNewGame(Model model,
			@RequestParam(name="player1") String player1,
			@RequestParam(name="player2") String player2,
			@RequestParam(name="place") String address,
			@RequestParam(name="date") String dateString
			) throws ParseException {
		
		
		Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString+":00");
		
		String targetPage = "";
		String regresult = "";
		
		
		Database db = new Database();
		
		Player pl1 = db.getPlayerByName(player1);
		Player pl2 = db.getPlayerByName(player2);
		Place place = db.getPlaceByName(address);
		
				
		Game game = new Game(pl1, pl2, place, d );
		
		db.saveGame(game);
		db.close();
			
		targetPage = "admin_index.html";
		regresult = "Game has been added successfully";
			
				
		model.addAttribute("regresult", regresult);
		
		
		
		return targetPage;
	}
	

	
	public ArrayList<Game> getGameList(){
		
		Database dbg = new Database();
		
		ArrayList<Game> games = dbg.getGameList();
		
		ArrayList<Game> orderedGamesList = new ArrayList<Game>();
		
		
		int origSize = games.size();
		Game game = null;
		
		for(int l = 0; l < origSize; l++) {
			
			game = games.get(0);
			
			
			for(int m = 1; m < games.size(); m++) {
				
				Game cG = games.get(m);
								
					if( cG.getDate().after(game.getDate()) ) {
					
						game = cG;
					
					}
					
				}
			
			//getting EUR rate
			
			Date date = game.getDate();
			DateFormat dateString = new SimpleDateFormat("yyyyMMdd");  
        			
			Place place = game.getPlace();
			
			
			
			Instant now = ZonedDateTime.now().toInstant();
			Instant gameTime = date.toInstant();
			
			if(gameTime.isBefore(now) == true) {
				
				RestTemplate rt = new RestTemplate();
				double exr = rt.getForObject("http://localhost:8081/sq?date=" + dateString.format(date), Double.class);
				
				double rfEUR = place.getRental_fee_HUF() / exr ;
				place.setRental_fee_EUR( rfEUR );
				
				System.out.println("exr: " + exr);
				System.out.println("rfEUR: " + rfEUR);
				
			}
			else {
				
				place.setRental_fee_EUR( 0 );
			}
			
			
			
			orderedGamesList.add(game);
			games.remove(game);			
			}
			
		return orderedGamesList;
		
	}
	
	public ArrayList<Player> getPlayerList(){
		
		Database db = new Database();
		
		ArrayList<Player> players = db.getPlayerList();
		
		db.close();
		
		return players;
		
	}
	
	
	public ArrayList<Place> getPlaceList(){
		
		Database db = new Database();
		
		ArrayList<Place> places = db.getPlaceList();
		
		db.close();
		
		return places;
		
	}
	
	
	@GetMapping("/admin/searchgamebyplayertosetresult")
	public String searchGameByPlayerToSetResult(Model model,
				@RequestParam(name="PlayerName") String name			
				) 
	{
			
		ArrayList<Game> orderedGamesList = getGameList();
			
		if(name.equals("") == true) {
				
			model.addAttribute("games", orderedGamesList);
				
		}
		else {
				
			ArrayList<Game> searchResultList = new ArrayList<Game>();
				
			for(int index = 0; index < orderedGamesList.size(); index++) {
					
				Game cGame = orderedGamesList.get(index);
									
							
				if( (cGame.getPlayer1().getName().equals(name) == true) || (cGame.getPlayer2().getName().equals(name) == true) ) {
				
					searchResultList.add(cGame);
				}
			}
				
			model.addAttribute("games", searchResultList);
				
		}
					
			
		return "selectgame.html";
	}
		
	@GetMapping("/admin/searchgamebyplacetosetresult")
	public String searchGameByPlaceToSetResult(Model model,
			@RequestParam(name="PlaceName") String name			
			) 
	{
		ArrayList<Game> orderedGamesList = getGameList();
			
		
		if(name.equals("") == true) {
			
			model.addAttribute("games", orderedGamesList);
			
		}
		else {
			
			ArrayList<Game> searchResultList = new ArrayList<Game>();
			
			for(int index = 0; index < orderedGamesList.size(); index++) {
				
				Game cGame = orderedGamesList.get(index);
								
						
				if( cGame.getPlace().getName().equals(name) == true ) {
			
					searchResultList.add(cGame);
				}
			}
			
			model.addAttribute("games", searchResultList);
			
		}
				
		
		return "selectgame.html";
		
	}
	
	
	@GetMapping("/admin/selectgame/{gameid}")
	public String selectGameToSaveResult(Model model,
			@PathVariable(name="gameid") String gameIdString,
			HttpServletResponse response,
			HttpServletRequest request) {
			
		int gameidInt = Integer.parseInt(gameIdString);
		
		String targetPage = "";
		String warning = "";
		
		ArrayList<Game> gameList = getGameList();
		Game cG = null;
		
		for(int index = 0; index < gameList.size(); index++) {
			
			cG = gameList.get(index);
			
			if (cG.getId() == gameidInt) {
							
				break;
			}			
		}
		
		
		if((cG != null) && (cG.getWinner() == null)) {
			
			model.addAttribute("game", cG);
			targetPage = "addresult.html";
			
			
			Cookie[] cookies = request.getCookies();
			
			if(cookies != null) {
				
				for (int index = 0; index < cookies.length; index++) {
					
					 Cookie cookie = cookies[index];
					 
					 if(cookie.getName().equals("gameid")) {
										 
					 cookie.setMaxAge(-1);
					 
					 response.addCookie(cookie);
					 }		
				}
				
			}
			else {
				
				Cookie cookie = new Cookie("gameid", gameIdString);	
				response.addCookie(cookie);
			}	
			
			
		}
		else {
			
			
			if(cG == null) {
				
				warning = "Error, no such game!";
				
			}
			
			if(cG.getWinner() != null) {
				
				warning = "This game's result already exists!";
				
				model.addAttribute("warning", warning);
				
			}
			
			targetPage = "admin_index.html";
		}
					
		
		return targetPage;
	}
	
	
	@GetMapping("/admin/addresult")
	private String addResult(Model model,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		String targetPage = "";
		int gameid = -1;
		
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		
		if(cookies != null) {
			
			for (int index = 0; index < cookies.length; index++) {
				
				 cookie = cookies[index];
				 
				 if(cookie.getName().equals("gameid")) {
									 
				 cookie.setMaxAge(-1);
				 cookie.setPath("/admin");
				 
				 gameid = Integer.parseInt(cookie.getValue());
				 
				 response.addCookie(cookie);
				 break;
				 }		
			}
		}
		
		
						
		if(gameid > 0) {
			
			ArrayList<Game> games = getGameList();
			Game cG = null;
			
			for (int i = 0; i < games.size(); i++) {
				
				cG = games.get(i);
				
				if(cG.getId() == gameid) {
									
					break;
				}				
			} 

			
		}
		else {
			
			model.addAttribute("error", "no such game");
			targetPage = "admin_index.html";
		}
		
		
		
		return targetPage;
		
	}
	
	
	@GetMapping("/admin/saveresult")
	private String saveResult(Model model,
			@RequestParam(name = "player1_score") int player1_score,
			@RequestParam(name = "player2_score") int player2_score,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		String result = "";
		
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		
		if(cookies != null) {
			
			for (int index = 0; index < cookies.length; index++) {
				
				 cookie = cookies[index];
				 
				 if(cookie.getName().equals("gameid")) {
				
				 break;	
				 }
			}
		
		}	
		
		ArrayList<Game> games = getGameList();
		Game game = null;
		
		for (int i = 0; i < games.size(); i++) {
			
			game = games.get(i);
			
			if( game.getId() == Integer.parseInt(cookie.getValue()) ) {
								
				break;
			}
		}
		
		
		if( (game != null) && (game.getWinner() == null) ) {
			
			game.setPlayer1_score(player1_score);
			game.setPlayer2_score(player2_score);
			
			if(player1_score >= player2_score) {
				
				game.setWinner(game.getPlayer1());
				
			}
			else {
				
				game.setWinner(game.getPlayer2());
			}
			
			
			Database db = new Database();
			
			db.saveResults(game);	
			
			db.close();
			
			result = "Result has been saved successfully";
			
		}
		else {
			
			if(game == null) {
				
				result = "Error, no such game!";
				
			}
			
			if(game.getWinner() != null) {
				
				result = "This game's result already exist!";
				
			}
			
		}
		
		cookie.setMaxAge(0);		
		
		response.addCookie(cookie);
		
		model.addAttribute("resultsettingsesult", result);
		return "admin_index.html"; 
	}
	
		
}
