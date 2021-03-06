package pti.sb_squash_mvc.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import pti.sb_squash_mvc.model.Game;
import pti.sb_squash_mvc.model.Place;
import pti.sb_squash_mvc.model.Player;

public class Database {
	
private SessionFactory sessionFactory;
	
	
	public Database() {
		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() 
				.build();
		
		
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	
	public Player login(String name, String password) {
		
		Player player = null;		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Player p WHERE p.name = :name AND p.password = :password", Player.class);
		query.setParameter("name", name);
		query.setParameter("password", password);
		List<Player> players = query.getResultList();
		
		if(players.isEmpty() == false) {
			
			player = players.get(0);
		}
		
		
		transaction.commit();
		session.close();
		
		return player;
	}
	
	
	
	public boolean saveNewPassword(Player player) {
		
				
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("UPDATE Player p SET p.password = :password, p.changePassword = :changePassword  WHERE p.name = :name");
		query.setParameter("password", player.getPassword());
		query.setParameter("name", player.getName());
		query.setParameter("changePassword", player.isChangePassword());
		query.executeUpdate();
		boolean success = true;
		
		
		transaction.commit();
		session.close();
		
		return success;
	}
	
	
	public ArrayList<Game> getGameList() {
		
		ArrayList<Game> games = new ArrayList<Game>();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT g FROM Game g ", Game.class);
		List<Game> gameList = query.getResultList();
		
		if(gameList.isEmpty() == false) {
			
			for(int index = 0; index < gameList.size(); index++) {
				
				Game game = gameList.get(index);
				games.add(game);
			}
			
		}
		
		transaction.commit();
		session.close();
		
		return games;
	}
	
	
	public ArrayList<Player> getPlayerList(){
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Player p ", Player.class);
		List<Player> playerList = query.getResultList();
		
		if(playerList.isEmpty() == false) {
			
			for(int index = 0; index < playerList.size(); index++) {
				
				Player player = playerList.get(index);
				players.add(player);
			}
			
		}
				
		transaction.commit();
		session.close();
		
		return players;
	}
	
	
	public ArrayList<Place> getPlaceList(){
		
		ArrayList<Place> places = new ArrayList<Place>();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Place p ", Place.class);
		List<Place> placeList = query.getResultList();
		
		if(placeList.isEmpty() == false) {
			
			for(int index = 0; index < placeList.size(); index++) {
				
				Place place = placeList.get(index);
				places.add(place);
			}
			
		}
				
		transaction.commit();
		session.close();
		
		return places;
	}
	
	public Player getPlayerByName(String name) {
		
		Player player = null;		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class);
		query.setParameter("name", name);
		List<Player> players = query.getResultList();
		
		if(players.isEmpty() == false) {
			
			player = players.get(0);
		}
		
		
		transaction.commit();
		session.close();
		
		return player;
	}
	
	public Place getPlaceByName(String name) {
		
		Place place = null;		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Place p WHERE p.name = :name", Place.class);
		query.setParameter("name", name);
		List<Place> places = query.getResultList();
		
		if(places.isEmpty() == false) {
			
			place = places.get(0);
		}
		
		
		transaction.commit();
		session.close();
		
		return place;
	}
	
	
	
	public boolean playerExistsWithName(String name) {
		
		boolean playerExist = false;
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class);
		query.setParameter("name", name);
		List<Player> playerList = query.getResultList();
		
		if(playerList.isEmpty() == false) {
			playerExist = true;
		}
		
		transaction.commit();
		session.close();
		
		return playerExist;
	}
	
	public void savePlayer(Player player) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(player);
		
		
		transaction.commit();
		session.close();	
	}
	
	
	public boolean placeExistsWithName(String name ) {
		
		boolean placeExist = false;
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT p FROM Place p WHERE p.name = :name", Place.class);
		query.setParameter("name", name);
		List<Place> placeList = query.getResultList();
		
		if(placeList.isEmpty() == false) {
			placeExist = true;
		}
		
		transaction.commit();
		session.close();
		
		return placeExist;
	}
	
	public void savePlace(Place place) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(place);
		
		
		transaction.commit();
		session.close();	
	}
	
	
	public void saveGame(Game game) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		session.save(game);
		
		
		transaction.commit();
		session.close();	
	}
	
	public void saveResults(Game game) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("UPDATE Game g SET "
				+ "g.player1_score = :player1_score,  "
				+ "g.player2_score = :player2_score,  "
				+ "g.winner = :winner  "
				+ "WHERE g.id = :id");
		query.setParameter("player1_score", game.getPlayer1_score());
		query.setParameter("player2_score", game.getPlayer2_score());
		query.setParameter("player2_score", game.getPlayer2_score());
		query.setParameter("winner", game.getWinner());
		query.setParameter("id", game.getId());
		query.executeUpdate();		
		
		transaction.commit();
		session.close();
	}
	
		
	public void close() {
		sessionFactory.close();
	}
		
}
