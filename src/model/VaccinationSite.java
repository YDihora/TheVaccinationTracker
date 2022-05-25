package model;

public class VaccinationSite {
	 private String name;
	 private VaccineDistribution[] distributions;
	 private int nod; // number of distribution
	 private final int MAX_NUM_OF_DIST = 4;
	 private int maxSupply;
	 private HealthRecord[] appointments;
	 private int noa;
	 private final int MAX_NUM_OF_APPOINTMENTS=200;
	 
	 
	public VaccinationSite(String name,int maxSupply) {
		this.maxSupply = maxSupply;
		this.name = name;
		this.distributions = new VaccineDistribution[MAX_NUM_OF_DIST];
		this.nod = 0;
		this.appointments = new HealthRecord[MAX_NUM_OF_APPOINTMENTS];
		this.noa=0;
		
	}
	
	public void addDistribution(Vaccine v,int numberOfDoses) throws 
	UnrecognizedVaccineCodeNameException,TooMuchDistributionException
	{
		
		
		// equivalent to: v.isRecognized()==false
		if(!v.isRecognized()) {
			throw new UnrecognizedVaccineCodeNameException("Error:....");
			
		}
		else if(numberOfDoses+this.getNumberOfAvailableDoses()>this.maxSupply) {
			throw new TooMuchDistributionException("Error:.......");
		}
		else {
			int index =-1;
			boolean found = false;
			for(int i=0;!found&&i<this.nod;i++) {
				if(this.distributions[i].getVaccine().getcodename().equals(v.getcodename())) {
					found = true;
					index = i;
				}
			}
			if(index<0) {
				// case 1: The first distribution of 'v' has not been added yet/
				this.distributions[this.nod] =new VaccineDistribution(v,numberOfDoses);
				this.nod++;
			}
			else {// index>=0
				// case 2: The first distribution of 'v' has been added.
				VaccineDistribution existing = this.distributions[index];
			
				existing.setNumberOfDoses(existing.getNumberOfDoses()+numberOfDoses);
			}
			
				}
		
		
	}
	
	public void bookAppointment(HealthRecord patient)
	throws InsufficientVaccineDosesException{
		
		if(this.getNumberOfAvailableDoses()-this.noa==0) {
			patient.setAppointmentStatus(String.format("Last vaccination appointment for %s with %s failed", 
					patient.getName(),this.name));
			throw new InsufficientVaccineDosesException("Error....");
			
		}
		else {
			this.appointments[this.noa]= patient;
			this.noa++;
			patient.setAppointmentStatus(String.format("Last vaccination appointment for %s with %s succeeded", 
					patient.getName(),this.name));
			
		}
	}
	public int getNumberOfAvailableDoses() {
		int total=0;
		for(int i=0;i<this.nod;i++) {
			total+= this.distributions[i].getNumberOfDoses();
			
		}
		
		return total;
	}
	public void administer(String date) {
		// administer the appointment one by one
		for(int i=0;i<this.noa;i++) {
			HealthRecord patient = this.appointments[i];
			
			// find out the first distribution that has >0 doses available
			boolean found=false;
			int index =-1;
			for(int j =0; !found&&j<nod;j++) {
				if(this.distributions[j].getNumberOfDoses()>0) {
					found =true;
					index=j;
				}
			}
			// index is guaranteed to be re-assigned to some valid index value(>=0)
			//otherwise, there would have been an InsufficientVaccineDosesException from bookAppointment
			
			// Updates on the site
			VaccineDistribution d = this.distributions[index];
			d.setNumberOfDoses(d.getNumberOfDoses()-1);
			
			// Updates on the patients
			patient.addRecord(this.distributions[index].getVaccine(), this.name,date);
		}
		// Updates on the site
		this.appointments = new HealthRecord[MAX_NUM_OF_APPOINTMENTS];
		this.noa = 0;
	}
	public int getNumberOfAvailableDoses(String codename) {
		int index =-1;
		boolean found = false;
		for(int i=0;!found&&i<this.nod;i++) {
			
			if(this.distributions[i].getVaccine().getcodename().equals(codename)) {
				found = true;
				index = i;
			}
		}
		int result = 0;
		if(index>=0) {
			result = this.distributions[index].getNumberOfDoses();
		}
		return result;
		
	}
	public String toString() {
		String result;
		String list="<";
		// loop
		//North York General Hospital has 3 available doses: <3 doses of Moderna>
		for(int i=0;i<this.nod;i++) {
			VaccineDistribution d = this.distributions[i];
			list+= String.format("%d doses of %s",
					d.getNumberOfDoses(),
					d.getVaccine().getmanufacturer());
		if(i<this.nod-1) {
			list+=", ";
		}
		}
		list+=">";
		result= String.format("%s has %d available doses: %s",
				this.name,
				this.getNumberOfAvailableDoses(),
				list);
		
		return result;
	}
}
