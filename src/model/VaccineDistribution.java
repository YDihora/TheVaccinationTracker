package model;

public class VaccineDistribution {

	private String status;
	private int numberOfDoses;
	// declaring these two attribute is not neccessary instead you just need to initialiszed a vaccine attribute
	//	private String codename;
	// 	private String manufacturer;
	private Vaccine vaccine;




	public VaccineDistribution(Vaccine vaccine,int numberOfDoses) {
		this.numberOfDoses = numberOfDoses;
		this.vaccine = vaccine;
		this.status = String.format("%d doses of %s by %s",
				this.numberOfDoses,
				this.vaccine.getcodename(),
				this.vaccine.getmanufacturer());
	}
	public String toString() {
		return this.status;

	}

	public Vaccine getVaccine() {
		return this.vaccine;
	}
	public int getNumberOfDoses() {
		return this.numberOfDoses;
	}
	public void setNumberOfDoses(int newNumberOfDoses) {
		this.numberOfDoses = newNumberOfDoses;
	}



}
