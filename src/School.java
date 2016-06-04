import java.util.ArrayList;

public class School extends Participant {

	private double alpha ; // GPA weight
	
	// constructors
	public School (){
		super(); 
		this.alpha =0.00; 
		
	}
	public School ( String name , double alpha , int maxMatches , int nStudents ){
		super(name, maxMatches, nStudents); 
		this.alpha = alpha; 
		
	}
	
	 // getters and setters
	 public double getAlpha () { return this . alpha ; }
	 public void setAlpha ( double alpha ) { this.alpha = alpha ; }
	 // get new info from the user ; cannot be inherited or overridden from parent
	// public void editSchoolInfo ( ArrayList < Student > S, boolean canEditRankings ){}
//	 public void calcRankings ( ArrayList < Student > S){} // calc rankings from alpha
	
	
	 public void print ( ArrayList <? extends Participant > S, ArrayList <? extends Participant > H,  boolean studentmatch, int j){
		 if(studentmatch){
				if(j<9){
					System.out.format(" %d. %-28s%7d    %4.2f  ", j+1,getName(),getMaxMatches(),getAlpha());
					//print arraylist inside school
					int arraysize = H.get(j).getMatchesLength(); 
						for( int i = 0; i < arraysize; i++){
							int index = H.get(j).getMatch(i);
							System.out.format("%s, ", S.get(index).getName());
						}
						System.out.print("  ");
		 			
				}
				else{
						System.out.format(" %d. %-28s%7d    %4.2f ", j+1,getName(),getMaxMatches(),getAlpha());
						//print arraylist inside school
						int arraysize = H.get(j).getMatchesLength(); 
							for( int i = 0; i < arraysize; i++){
								int index = H.get(j).getMatch(i);
								System.out.format("%s, ", S.get(index).getName());
							}
						System.out.print("  ");
			 			
					}
				}
	
	 
	 	else{
				if(j<9){
					System.out.format(" %d. %-28s%7d    %4.2f  -                          ", j+1,getName(),getMaxMatches(),getAlpha());
				}
				else{
					System.out.format("%d. %-28s%7d     %4.2f  -                          ", j+1,getName(),getMaxMatches(), getAlpha());
				}
				
			}
		 
		 
		 // print school row
	 }
	// public boolean isValid (){} // check if this school has valid info

}
