package pti.sb_squash_mvc.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table (name = "games")
public class Game {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "player1")
	private Player player1;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "player2")
	private Player player2;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "place")
	private Place place;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "player1_score")
	private int player1_score;
	
	@Column(name = "player2_score")
	private int player2_score;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "winner")
	private Player winner;
	
	
	
	
	

	public Game(Player player1, Player player2, Place place, Date date) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		this.place = place;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getPlayer1_score() {
		return player1_score;
	}

	public void setPlayer1_score(int player1_score) {
		this.player1_score = player1_score;
	}

	public int getPlayer2_score() {
		return player2_score;
	}

	public void setPlayer2_score(int player2_score) {
		this.player2_score = player2_score;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", player1=" + player1 + ", player2=" + player2 + ", place=" + place + ", date="
				+ date + ", player1_score=" + player1_score + ", player2_score=" + player2_score + ", winner=" + winner
				+ "]";
	}
	
	
	
	

}
