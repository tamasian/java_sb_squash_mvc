package pti.sb_squash_mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "places")
public class Place {
	
	
	@Id
	@Column(name = "name")
	private String name;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "rental_fee_HUF_hour")
	private double rental_fee;
	
	
	
	

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

	public double getRental_fee() {
		return rental_fee;
	}

	public void setRental_fee(double rental_fee) {
		this.rental_fee = rental_fee;
	}

	@Override
	public String toString() {
		return "Place [name=" + name + ", address=" + address + ", rental_fee=" + rental_fee + "]";
	}
	
	
	
	
	

}
