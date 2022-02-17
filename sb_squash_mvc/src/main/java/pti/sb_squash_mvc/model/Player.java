package pti.sb_squash_mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "players")
public class Player {
	
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "password")
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Player_type type;
	
	@Column(name = "change_password")
	private boolean changePassword;
	
	
	
	
	public Player() {
		super();
	}

	public Player(String name) {
		super();
		this.name = name;
		this.password = "generált";
		this.type = Player_type.USER;
		
				
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	

	public Player_type getType() {
		return type;
	}

	public void setType(Player_type type) {
		this.type = type;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	

	
	
	
		

}
