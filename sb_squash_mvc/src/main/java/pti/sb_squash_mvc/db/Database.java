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
		
		
		Query query = session.createQuery("SELECT u FROM Player u WHERE u.name = :name AND u.password = :password", Player.class);
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
		
		boolean success = false;
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		Query query = session.createQuery("UPDATE Player p SET p.password = :password WHERE p.name = :name", Player.class);
		query.setParameter("password", player.getPassword());
		query.setParameter("name", player.getName());
		query.executeUpdate();
		success = true;
		
		
		transaction.commit();
		session.close();
		
		return success;
	}
	
	
	public ArrayList<Game> getGameList() {
		
		ArrayList<Game> games = new ArrayList<Game>();
		
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		
		
		Query query = session.createQuery("SELECT g FROM Game u ", Game.class);
		List<Game> gameList = query.getResultList();
		
		if(games.isEmpty() == false) {
			
			for(int index = 0; index < gameList.size(); index++) {
				
				Game game = gameList.get(index);
				games.add(game);
			}
			
		}
		
		transaction.commit();
		session.close();
		
		return games;
	}
	
	
	
	
	
	
		
	public void close() {
		sessionFactory.close();
	}
	
	
	
	
	
	
	
	
	
	
	
}
