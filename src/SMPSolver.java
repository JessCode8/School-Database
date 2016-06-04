
import java.util.ArrayList;

public class SMPSolver {

	private ArrayList < ? extends Participant > S = new ArrayList <  Participant >() ; // suitors
	private ArrayList < ? extends Participant > R = new ArrayList < Participant >() ; // receivers
	private double avgSuitorRegret ; // average suitor regret
	private double avgReceiverRegret ; // average receiver regret
	private double avgTotalRegret ; // average total regret
	private boolean matchesExist ; // whether or not matches exist
	private boolean stable ; // whether or not matching is stable
	private long compTime ; // computation time
	private boolean suitorFirst ; // whether to print suitor stats first
	
	public SMPSolver (){
		
		this.S = new ArrayList < Participant >();
		this.R = new ArrayList < Participant >();
		this.avgReceiverRegret= 0.0 ;
		this.avgSuitorRegret = 0.0 ;
		this.avgTotalRegret = 0.0 ;
		this.matchesExist=false;
		
		
	}
	
	// getters
	public double getAvgSuitorRegret () { return this . avgSuitorRegret ; }
	public double getAvgReceiverRegret () { return this . avgReceiverRegret ; }
	public double getAvgTotalRegret () { return this . avgTotalRegret ; }
	public boolean matchesExist () { return this.matchesExist; }
	public boolean isStable () { return this.stable; }
	public long getTime (){ return this.compTime; }
	public boolean getSuitorFirst () { return this.suitorFirst; }
	
	public int getNSuitorOpenings () { 
		
		int NSuitorOpenings = 0; 
		for(int i = 0;  i < S.size() ; i ++){
			NSuitorOpenings += S.get(i).getMaxMatches();
		}
		
		return NSuitorOpenings; 
		
	}
	public int getNReceiverOpenings (){
		
		int NReceiverOpenings = 0; 
		for(int i = 0;  i < R.size() ; i ++){
			NReceiverOpenings += R.get(i).getMaxMatches();
		}
		
		return NReceiverOpenings; 
		
	}
	
	// setters
	//public void setMatchesExist ( boolean b){}
	
	public void setSuitorFirst ( boolean b){ this.suitorFirst = b; }//tells me which is the suitor (if true - students, if false - schools)
	
	
	public void setParticipants( ArrayList <? extends Participant > S, ArrayList <?extends Participant > R){
		this.S = S;
		this.R = R; 
		
	}
	
	public boolean match (){
		//Gale - Shapley algorithm to match 
	
		if(!this.matchingCanProceed( this.S.size(), this.R.size() )){
			return false; 
		}
		
		clearMatches();
		
		long start = System.currentTimeMillis();
		//real matching begins here
		
		int n = getNReceiverOpenings(); 
		int nMatches = 0; 
		while(nMatches != n){
			
			for(int i = 0; i < S.size() ; i ++){ //every free suitor proposes
				
				//check for match
				while(this.S.get(i).getMatched() == false){ //while suitor is still not fully satisfied
					
					int toprank = this.S.get(i).gettoprank(); //toprank = just a counter of next best option
					int receiver = this.S.get(i).getRanking(toprank); 
					if(suitorFirst){
						receiver--; //get the actual receiver # (for student suitors, receivers is actually one less (b/c students ranked starting at 1)
					}
					
					
					//check if receiver is matched
					if(R.get(receiver).getMatched()){ //if true=matched, you must make a proposal 
						boolean proposalaccepeted = makeProposal(i, receiver);//sends in the suitor index + their current toprank
					
						if(proposalaccepeted){
							makeEngagement(i, receiver, -1); //old suitor is already dealt with in proposal function, -1 is arbritrary 
						}
					
						
					}
					else{ //receiver unmatched - and must accept	
						makeEngagement(i, receiver, -1); 
						nMatches++;
					}
					
					this.S.get(i).settoprank((toprank+1)); //after all is done, move to next top rank
				
				}
				
			}
		}
		
		 this.compTime = System . currentTimeMillis () - start;
		 System.out.format("\n%d matches made in %dms!\n", nMatches, this.compTime); 
		// print();
			return true; 
		
		
		
		
	}
	
	

	public boolean matchingCanProceed (int suitorsize, int receiversize){
	
		if(suitorsize == 0){
			System.out.println("ERROR: No suitors are loaded!\n");
			return false;
		}
		else if(receiversize == 0){
			System.out.println("ERROR: No receivors are loaded!\n"); 
			return false; 
		}
		else if(this.getNSuitorOpenings() != this.getNReceiverOpenings()){
			System.out.println("ERROR: The number of suitors and receiver openings be equal!\n"); 
			return false;
		}
		else{
			return true; 
		}
		
	}
	
	
	
	
	
	// methods for matching
	public void clearMatches (){
		for(int i=0 ; i < this.S.size() ; i++ ){
			this.S.get(i).setMatched(false);
			
		}
			
		for(int j =0 ; j< this.R.size(); j++){
			this.R.get(j).setMatched(false); 
		}
		
	}//clear out existing matches
	
	
	
	private boolean makeProposal (int suitor , int receiver ){
		
		int IndMaxRegret = (this.R.get(receiver).getMaxMatches())-1; // max regret will be with the suitor in the last element of arraylist matches
																	//& since makeproposal is only called when fully matched, last element = nmaxmatches -1 				
		int oldsuitor = this.R.get(receiver).getMatch(IndMaxRegret);//access the arraylist of matches of receivers at indmaxregret 
		
		int CurrentMaxRegret = ReceiverRegretCalculator(oldsuitor, receiver);  //currentmaxregret of receiver
		int PotentialRegret = ReceiverRegretCalculator(suitor, receiver); 	
		
		if(PotentialRegret > CurrentMaxRegret){ //proposal rejected
			this.S.get(suitor).setMatched(false);
			return false;  
		}
		else{
			//old suitor is no longer matched with this receiver- clear previous match
			this.S.get(oldsuitor).setMatched(false);
			
			//deduct old regret (new regret will be added in makeEngagement function)
			this.R.get(receiver).setRegret((-1*CurrentMaxRegret));
			this.S.get(oldsuitor).setRegret((-1*SuitorRegretCalculator(oldsuitor, receiver)));
			
			//remove old matches from arraylist
			this.S.get(oldsuitor).removeMatchGivenContent(receiver);
			this.R.get(receiver).removeMatch(IndMaxRegret);
			
			
			return true; 
		}
	}
	
	private int SuitorRegretCalculator(int oldsuitor, int receiver){
		
		//if old suitor was a student, you must look for +1 school 
		if(this.suitorFirst){ 
			for(int i = 0; i < this.S.get(oldsuitor).getNParticipants() ; i ++){
				
				if(this.S.get(oldsuitor).getRanking(i)== receiver+1){
					return i; 
				}
				
			}
		}
		
		//if oldsuitor was the school
		else{
			return (this.S.get(oldsuitor).gettoprank() - (this.S.get(oldsuitor).getMaxMatches() - 1) );
		}
		
		return -500;
		
	}
	
	private int ReceiverRegretCalculator (int suitor, int receiver){
		
		System.out.println(this.R.get(receiver).getNParticipants());
		//receiver's regret = how long it takes to get to the suitor's index in their rankings array
		
		//schools set their students starting at 0 
		if(this.suitorFirst) //true means student suitors, so we're finding the school's regret with this student
			for(int i = 0; i < this.R.get(receiver).getNParticipants() ; i++){ 
			
				if(this.R.get(receiver).getRanking(i) == suitor){
					return i; 
				}		
		}
		
		//students set their schools starting at 1
		else{ //false means school suitors, so we're finding the student's regret with this school 
			for ( int j = 0; j < this.R.get(receiver).getNParticipants() ; j++){
				if(this.R.get(receiver).getRanking(j)== (suitor+1)){
					return j;
				}
			}
		}
		
		return -1; 
	}
	
	
	private void makeEngagement (int suitor , int receiver , int oldSuitor ){
		
		this.S.get(suitor).setMatch(receiver); //add to arraylist (starts 0,1,2, ... )
		if(this.S.get(suitor).getMatchesLength() == this.S.get(suitor).getMaxMatches()){
			this.S.get(suitor).setMatched(true);
		}
		
		//set suitor regret
		
		
		
		this.R.get(receiver).setMatch(suitor); //add to arraylist (starts 0,1,2, ... )
		if(this.R.get(receiver).getMatchesLength() == this.R.get(receiver).getMaxMatches()){
			this.R.get(receiver).setMatched(true);
		}
	
					
			//update receiver regrets
		int PotentialRegret = ReceiverRegretCalculator(suitor, receiver); 
		this.R.get(receiver).setRegret(PotentialRegret);
		this.S.get(suitor).setRegret(SuitorRegretCalculator(suitor, receiver));	
		
					
		
		// make suitor - receiver engagement , break receiver - oldSuitor engagement
	}
	
	
	/*public void calcRegrets () // calculate regrets
	public boolean determineStability () // calculate if a matching is stable

	// print methods
	public void print () // print the matching results and statistics
	public void printMatches () // print matches
	public void printStats () // print matching statistics
	public void printStatsRow ( String rowHeading ) // print stats as row
	
	*/
	
	
}
