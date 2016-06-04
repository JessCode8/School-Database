import java.util.ArrayList;

public class Student extends Participant {
	private double GPA; 
	private int ES;
	private String []beer; 
	//constructors 
	public Student (){
		super(); 
		this.GPA =0.0;
		this.ES = 0; 
		
	}
	
	public Student(String name, double GPA, int ES, int nSchools){
		super(name, 1, nSchools); 
		this.GPA =GPA;
		this.ES =ES; 	
	}
	
	public void setBeer(String[] b){
		for(int i = 0; i<b.length; i++){
			
			beer[i]= b[i];
		}
		
	}
	public double getGPA (){return this.GPA; }
	public int getES (){ return this.ES;}
	public void setGPA ( double GPA){ this.GPA = GPA; } 
	public void setES ( int ES){ this.ES = ES; }
	
	//public void editInfo ( ArrayList < School > H, boolean canEditRankings ){}
	public void print ( ArrayList <? extends Participant > S, ArrayList <? extends Participant > H, boolean schoolmatch, int i) {
		 if(schoolmatch){
			 	if(i<9){
				System.out.format(" %d. %-31s%-2.2f%4d  %-27s", i+1, getName(), getGPA(), getES(), H.get(S.get(i).getMatch(0)).getName()); 
			 	}
			 	else{
			 		System.out.format("%d. %-31s%-2.2f%4d  %-27s", i+1, getName(), getGPA(), getES(), H.get(S.get(i).getMatch(0)).getName());
			 	}
		 }
			
		else{
				if(i<9){
				System.out.format(" %d. %-31s%-2.2f%4d  %-27s", i+1, getName(), getGPA(), getES(),"-");
				}
				else{
					System.out.format("%d. %-31s%-2.2f%4d  %-27s", i+1, getName(), getGPA(), getES(),"-");
				
				}
			}
	
	}
	
	public boolean isValid (int numschools){
		
		System.out.println(getNParticipants());
		if(getNParticipants()== numschools ){
			return true;
		}
		else{
			return false;
		}
	}
	
}
