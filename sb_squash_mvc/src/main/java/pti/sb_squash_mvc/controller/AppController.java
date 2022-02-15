package pti.sb_squash_mvc.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pti.sb_squash_mvc.db.Database;
import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.Player;
import pti.sb_squash_mvc.model.Player_type;

@Controller
public class AppController {
	
	
	private Player currentPlayer;
	
	
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
			
			this.currentPlayer = player;
			
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
			
			currentPlayer.setPassword(pwd1);
			currentPlayer.setChangePassword(false);
			
			Database db = new Database();
			
			db.saveNewPassword(currentPlayer);
			
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
		
		Database db = new Database();
		
		ArrayList<Game> games = db.getGameList();
		
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
		
		return "games.html";
	}
	

}
