package pti.sb_squash_mvc.controller;

import java.util.ArrayList;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	
	@PostMapping("/newpwd")
	public String saveNewPassword(Model model,
				@RequestParam(name = "password1") String pwd1,
				@RequestParam(name = "password2") String pwd2
				
			)
	{
		
		String targetPage = "";
		
		if (pwd1.equals(pwd2)) {
			
			loggedPlayer.setPassword(pwd1);
			loggedPlayer.setChangePassword(false);
			
			Database db = new Database();
			
			db.saveNewPassword(loggedPlayer);
			
			db.close();
			
			
			model.addAttribute("success", "Password has changed succesfully");
			
			targetPage = "games.html"; 
			
		}
		
		else {
			
			model.addAttribute("error", "The passwords are different");
			targetPage = "newpwd.html"; 
		}
				
		
		return targetPage;
	
	}
	
	
	@GetMapping("/games")
	public String getGames(Model model) {
		
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
				
			orderedGamesList.add(game);
			games.remove(game);			
			}
			
			
			
		model.addAttribute("gameList", orderedGamesList);
			
		dbg.close();
		
		
		
		Database dbp = new Database();
		
		ArrayList<Player> players = dbp.getPlayerList();
		
		dbp.close();

		model.addAttribute("players", players);
		
		
		
		Database dbl = new Database();
		
		ArrayList<Place> places = dbl.getPlaceList();
		
		dbp.close();
		
		model.addAttribute("places", places);
		
			
		
		return "games.html";
	}
	
	
	@GetMapping("/searchbyplayer")
	public String searchByPlayer(Model model,
			@RequestParam(name="PlayerName") String name			
			) 
	{
		
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
				
			orderedGamesList.add(game);
			games.remove(game);			
			}
			
			
		dbg.close();
		
		
		if(name.equals("") == true) {
			
			model.addAttribute("gameList", orderedGamesList);
			
		}
		else {
			
			ArrayList<Game> searchResultList = new ArrayList<Game>();
			
			for(int index = 0; index < orderedGamesList.size(); index++) {
				
				Game cGame = orderedGamesList.get(index);
								
						
				if( (cGame.getPlayer1().getName().equals(name) == true) || (cGame.getPlayer2().getName().equals(name) == true) ) {
			
					searchResultList.add(cGame);
				}
			}
			
			model.addAttribute("gameList", searchResultList);
			
		}
				
		
		return "games.html";
	}
	
	
	@GetMapping("/searchbyplace")
	public String searchByPlace(Model model,
			@RequestParam(name="PlaceName") String name			
			) 
	{
		
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
				
			orderedGamesList.add(game);
			games.remove(game);			
			}
			
			
		dbg.close();
		
		
		if(name.equals("") == true) {
			
			model.addAttribute("gameList", orderedGamesList);
			
		}
		else {
			
			ArrayList<Game> searchResultList = new ArrayList<Game>();
			
			for(int index = 0; index < orderedGamesList.size(); index++) {
				
				Game cGame = orderedGamesList.get(index);
								
						
				if( cGame.getPlace().getName().equals(name) == true ) {
			
					searchResultList.add(cGame);
				}
			}
			
			model.addAttribute("gameList", searchResultList);
			
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
					
			targetPage = "newgame.html";		
					
		}
		//result
		else if (function.equals("nG") == true) {
			
			targetPage = "addresult.html";
			
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
						
			db.savePlayer(player);
					
			
			targetPage = "admin_index.html";
			regresult = "Successful registration";
			
		}
		else {
			targetPage = "newplayer.html";
			regresult = "Player name already exists!";
		}
		
		
		model.addAttribute("regresult", regresult);
		db.close();
		
		
		
		return targetPage;
		
	}
	
	
	@GetMapping("/addnewplace")
	public String addNewPlace(Model model,
			@RequestParam(name="name") String name,
			@RequestParam(name="address") String address,
			@RequestParam(name="rentalfee") double rentalfee
			) {
		
		String targetPage = "";
		String regresult = "";
		
		
		Database db = new Database();
		boolean placeExists = db.placeExistsWithName(name);
		
		if(placeExists == false) {
			
			Place place = new Place(name, address, rentalfee);
						
			db.savePlace(place);
					
			
			targetPage = "admin_index.html";
			regresult = "Successful registration";
			
			
		}
		else {
			targetPage = "newplace.html";
			regresult = "Place's name already exists!";
		}
		
		
		model.addAttribute("regresult", regresult);
		db.close();
		
		
		
		
		
		
		
		return targetPage;
	}
	
	
}
