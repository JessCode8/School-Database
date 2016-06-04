import java.util.ArrayList;

public class Participant {

	private String name;
	private int [] rankings; 
	private ArrayList <Integer> matches = new ArrayList <Integer>();
	private int regret; 
	private int maxMatches;
	
	//variables i've added
	private int toprank; 
	private boolean matched;
	
	
	public Participant (){
		String name = new String(); 
		int regret = 0; 
		int maxMatches= 0; 
		int [] rankings = new int [2000]; 
		boolean matched = false;
		int toprank = 0;
		
		
	}
	public Participant ( String name , int maxMatches , int nParticipants ){
		this.name = name; 
		this.maxMatches = maxMatches; 
		setNParticipants(nParticipants);		
		this.matched =false; 
		this.toprank = 0; 
		this.regret = 0; 
		this.matches = new ArrayList <Integer>(); 
	}
	
	//print methods
	//public void print ( ArrayList <? extends Participant > P){
	//}
	public void printRankings ( ArrayList <? extends Participant > P, boolean fromstudent, int numstudents){
		
		//print student's rankings of schools (P is schools arraylist)
		if(fromstudent){
		/*for(int i=0; i < this.rankings.length ; i++){
	
			//find index that 1 occurs, 2 , 3, .. .
			int j= 0; 
			while(getRanking(j)!= (i+1)){
				j++;
			}
			//if last line check
			if(i+1 == this.rankings.length){
				System.out.format("%s", P.get(j).getName() );
			}
			else{
				System.out.format("%s, ", P.get(j).getName() ); //P is schools
			}*/
			
			 int i=0; 
				
			 while(i < this.rankings.length){
				
				 
				 int current = this.rankings[i]; 
			//	 System.out.print(current);
				 if(i==((this.rankings.length)-1)){//check if its the last name to print
						System.out.format("%s", P.get(current-1).getName() );
				 }
				 else{
						System.out.format("%s, ", P.get(current-1).getName() );
				 }		  
				  i++;
			 }
			 
			
		}
		
		
		//print schools' rankings of students (P is students arraylist)
		else if(fromstudent == false){
			//System.out.println(this.rankings.length);
			//System.out.format("%s, ", P.get(this.rankings[0]).getName());
			
			for(int k= 0; k < numstudents; k ++){
			//	System.out.print(k);
				if((k+1)==numstudents){//check if its the last name to print
					System.out.format("%s\n", P.get(this.rankings[k]).getName()); //returns student's name at school's rank i 
				}
				else{
					System.out.format("%s, ", P.get(this.rankings[k]).getName());
				}
			}
		
		}
		
	}
	//public String getMatchNames ( ArrayList <? extends Participant > P)
	
	// getters
	public String getName (){ return this.name; }
	public int getRanking ( int i) { return this.rankings[i]; }
	public int getMatch ( int i){ return this.matches.get(i); }
	public int getRegret () { return this.regret; }
	public int getMaxMatches () { return this.maxMatches; } 
	public int getNParticipants () { return this.rankings.length; }// return length of rankings array
	
	public int getMatchesLength () { return this.matches.size(); }
	public boolean getMatched () { return this.matched; } //true or false
	public int gettoprank() {return this.toprank; }
	
	
//	public boolean isFull () { } 
//	public int getNMatches () { } 	
	// setters
	public void setName ( String name ){ this.name = name;}
	public void setRanking ( int i, int r){ this.rankings[i] = r;}
	public void setMatch ( int m) { this.matches.add(m); }
	public void setRegret ( int r){ this.regret += r;}
	public void setNParticipants ( int n){ this.rankings = new int [n]; }
	public void setMaxMatches ( int n){this.maxMatches = n; }
	
	public void removeMatchGivenContent (int m) { this.matches.remove(m); }
	public void removeMatch (int m) { this.matches.remove(m);}
	public void setMatched (boolean matched){this.matched = matched;} //set single or not 
	public void settoprank(int top){ this.toprank = top; }

	
	
}
