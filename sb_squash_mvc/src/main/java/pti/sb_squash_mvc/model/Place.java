package pti.sb_squash_mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "places")
public class Place {
	
	
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "rental_fee_HUF_hour")
	private double rental_fee_HUF;
	
	@Transient
	private double rental_fee_EUR;
		
	

	public Place(String name, String address, double rental_fee_HUF) {
		super();
		this.name = name;
		this.address = address;
		this.rental_fee_HUF = rental_fee_HUF;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getRental_fee_HUF() {
		return rental_fee_HUF;
	}

	public void setRental_fee_HUF(double rental_fee_HUF) {
		this.rental_fee_HUF = rental_fee_HUF;
	}

	public double getRental_fee_EUR() {
		return rental_fee_EUR;
	}

	public void setRental_fee_EUR(double rental_fee_EUR) {
		this.rental_fee_EUR = rental_fee_EUR;
	}

	@Override
	public String toString() {
		return "Place [name=" + name + ", address=" + address + ", rental_fee_HUF=" + rental_fee_HUF
				+ ", rental_fee_EUR=" + rental_fee_EUR + "]";
	}

	
}
