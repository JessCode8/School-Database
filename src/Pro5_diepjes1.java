import java.io.*;
import java.util.*;
/*import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;*/

public class Pro5_diepjes1 {

	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws Exception {
		
		ArrayList <School> SCHO = new ArrayList <School>(); 
		ArrayList <Student> STU = new ArrayList <Student>(); 
		ArrayList <School> H2 = new ArrayList <School>();
		ArrayList <Student>S2 = new ArrayList <Student> (); 
		SMPSolver GSS = new SMPSolver();
		SMPSolver GSH = new SMPSolver();
		boolean keepgoing = true; 
		boolean assignedrank= false;
		boolean studentsuitormatch=false; 
		boolean schoolsuitormatch =false; 
		boolean schoolmatch =false;
		int numschools = 0; 
		int numstudents = 0; 
		
		while(keepgoing){
			
			String input = MenuInput(); 
			
			if(input.equals("L") || input.equals("l")){
				
				String fileschool = FileExist("Enter school file name (0 to cancel): "); 
				if(fileschool.equals("0")){} //do nothing
				else{
					numschools += loadSchools(SCHO, fileschool);
				}
				
				String filestudent = FileExist("Enter student file name (0 to cancel): ");
				if(filestudent.equals("0")){}
				else{
					numstudents += loadStudents(STU, filestudent, numschools);
					
			//		System.out.println(numstudents);
				
					//students are tacked on, but if remaining student's preferred rankings don't match new uploaded schools - clear them
					boolean keep = true; 
					int countnumstudentsremoved = 0; 
					for(int i= (numstudents-1) ; i >= 0; i--){
						
						keep = STU.get(i).isValid(numschools); 
	
						if(keep == false){
							STU.remove(i);
							countnumstudentsremoved++; 
						}	
					}
					numstudents-=countnumstudentsremoved; 
				}
				
				//after students are loaded, assign school's rankings of them 
				assignedrank = assignRankings(numstudents, numschools, STU, SCHO);
				
				//x.setSchoolArray();
				//x.setStudentArray();
				
			}
			
			else if(input.equals("E")|| input.equals("e")){
				editData(STU, SCHO, numstudents, numschools, assignedrank, schoolmatch);
			}
		
			else if(input.equals("P") || input.equals("p")){
				if(schoolmatch==false){
				printStudents(STU, SCHO, schoolmatch, numstudents, numschools, assignedrank);
				printSchools(STU, SCHO, schoolmatch, numstudents, numschools, assignedrank);
				}
				else if(schoolmatch==true){
					printStudents(S2, H2, schoolmatch, numstudents, numschools, assignedrank);
					printSchools(S2, H2, schoolmatch, numstudents, numschools, assignedrank);
				}
			}
			
			else if(input.equals("M")|| input.equals("m")){	
				//make copies to be sent to SMP 
				H2 = copySchools (SCHO, numstudents); 
				S2 = copyStudents (STU, numschools); 
				
				//GSS = student suitors
				GSS.setParticipants(S2, H2);
				GSS.setSuitorFirst(true);
				schoolmatch = GSS.match(); 
				
				ArrayList <School > H3 = copySchools (SCHO, numstudents); 
				ArrayList <Student> S3 = copyStudents (STU, numschools); 
				
				
				
				//GSH = school suitors
				GSH.setParticipants(H3, S3);
				GSH.setSuitorFirst(false);
				//schoolsuitormatch = GSH.match(numstudents); 
				
			}
			
			else if(input.equals("R")|| input.equals("r")){
				numstudents=0;
				numschools=0; 
				STU.clear();
				SCHO.clear();
				System.out.println("Database cleared!\n");
			}
			
			if(input.equals("Q") || input.equals("q")){
				keepgoing=false; 
			}
			
			
		}
		

	}

	private static ArrayList<Student> copyStudents(ArrayList<Student> O, int numschools) {
		ArrayList <Student> studentcopy = new ArrayList <Student>(); 
		
		for(int i = 0; i < O.size() ; i++){
			String name = O.get(i).getName();
			double GPA = O.get(i).getGPA();
			int ES = O.get(i).getES();
			Student temp = new Student (name, GPA, ES, numschools);
			
			for(int j =0 ; j < numschools; j++){
				temp.setRanking(j, O.get(i).getRanking(j));
			}
			
			studentcopy.add(temp);
		}
		return studentcopy;
	}

	private static ArrayList<School> copySchools(ArrayList<School> P, int numstudents) {
		ArrayList <School > newList = new ArrayList < School >();
	
		for (int i = 0; i < P. size (); i++) {
		 String name = P. get(i). getName ();
		 double alpha = P. get(i). getAlpha ();
		 int maxMatches = P.get (i). getMaxMatches ();
		 School temp = new School (name ,alpha , maxMatches , numstudents );
		 for (int j = 0; j < numstudents ; j++) {
		 temp.setRanking (j, P. get(i). getRanking (j));
		}
		
		newList.add(temp);
		 }
		 return newList ;
	}

	public static void editData(ArrayList<Student> STU, ArrayList<School> SCHO, int LoadedStudents, int LoadedSchools,
			boolean RankingsSet, boolean schoolmatch) throws IOException {

		boolean loop = true;
		// Sub menu edit
		do {
			// print menu and collect choice
			String subchoice = printSubMenu(LoadedStudents, LoadedSchools);

			if (subchoice.equals("S") || subchoice.equals("s")) {
				editStudents(STU, SCHO, LoadedStudents, LoadedSchools, RankingsSet, schoolmatch);
			} else if (subchoice.equals("H") || subchoice.equals("h")) {
				editSchools(STU, SCHO, LoadedStudents, LoadedSchools, RankingsSet, schoolmatch);
			} else {// subchoice=Q, leave loop
				loop = false;
			}
		} while (loop);
	}
	
	public static void editSchools(ArrayList<Student> S, ArrayList<School> H, int nStudents, int NSchools,
			boolean rankingsSet, boolean schoolmatch) throws IOException {
		int EditSchoolNum = -1;
		do {
			printSchoolsfromEdit(S, H, schoolmatch, nStudents, NSchools, rankingsSet);
			EditSchoolNum = getInteger("Enter school (0 to quit): ", 0, nStudents);
			System.out.println();
			// if 0 go back to printsubmenu
			if (EditSchoolNum == 0) {// nothing happens, let it loop again}
			} else {
				// access student object
				String Schoolname = getString("Name: ");
				double alpha = getDouble("GPA weight: ", 0.00, 1.00);
				int maxmatches = getIntegerinfinity("Maximum number of matches: ", 1);
				System.out.print("\n");
				H.get(EditSchoolNum - 1).setName(Schoolname);
				H.get(EditSchoolNum - 1).setAlpha(alpha);
				H.get(EditSchoolNum - 1).setMaxMatches(maxmatches);
				rankingsSet = assignRankings(nStudents, NSchools, S, H); 
			}
		} while (EditSchoolNum != 0);
	}


	private static void printSchoolsfromEdit(ArrayList<Student> STU, ArrayList<School> SCHO, boolean schoolmatch,
			int loadedstudents, int loadedschools, boolean assignedrank) {
	
		System.out
				.print(" #  Name                        # spots  Weight  Assigned student           Preferred student order\n"
						+ "--------------------------------------------------------------------------------------------\n");
	
		for (int j = 0; j < loadedschools; j++) {
			SCHO.get(j).print(STU,SCHO, schoolmatch, j);
			if (assignedrank) {
				SCHO.get(j).printRankings(STU, false, loadedstudents);
			} else {
				System.out.format("%-23s\n", "-");
			}

			System.out.println();
		}

		System.out.println(
				"--------------------------------------------------------------------------------------------"); // ends

		

	}

	public static void editStudents(ArrayList<Student> S, ArrayList<School> H, int nStudents, int NSchools,
			boolean rankingsSet, boolean schoolmatch) throws IOException {

		int EditStudentNum = -1;
		do {
			printStudentsfromedit(S, H, schoolmatch, nStudents, NSchools, rankingsSet);
			EditStudentNum = getInteger("Enter student (0 to quit): ", 0, nStudents);
			System.out.println();
			
			// if 0 go back to printsubmenu
			if (EditStudentNum == 0) {// nothing happens, let it loop again}
			} else {
				// access student object
				String Studentname = getString("Name: ");
				double GPA = getDouble("GPA: ", 0.00, 4.00);
				int ES = getInteger("Extracurricular score: ", 0, 5);
				int maxmatch = getIntegerinfinity("Maximum number of matches: ", 1);
				S.get(EditStudentNum - 1).setGPA(GPA);
				S.get(EditStudentNum - 1).setES(ES);
				S.get(EditStudentNum - 1).setName(Studentname);
				S.get(EditStudentNum - 1).setMaxMatches(maxmatch); 

				// After editing, you must recalculate comp score, so call
				// school object
				if (rankingsSet == true) {
					rankingsSet = assignRankings(nStudents, NSchools, S, H);
				}

				// allow user to edit rankings of that student
				if (rankingsSet == true) {
					String EditRanking = EditRankSubMenu("Edit rankings (y/n): ");
					if (EditRanking.equals("N") || EditRanking.equals("n")) {
					} else {
						System.out.println(); 
						rankingsSet = StudentRanker(S, H, nStudents, NSchools, EditRanking, EditStudentNum); 
						System.out.print("\n\n");
						
					}
				}
			}

		} while (EditStudentNum != 0);
	}
	
	private static boolean StudentRanker(ArrayList<Student> S, ArrayList<School> H, int nStudents, int nSchools, //checks for repetition
			String editRanking, int editStudentNum) {

		String currentname = S.get(editStudentNum - 1).getName();
		System.out.format("Student %s's rankings:\n", currentname);
		// temprank keeps track of rankings assigned, so user can't repeat
		int temprank[] = new int[nSchools];

		// Loop through each school
		for (int j = 0; j < nSchools; j++) {
			int CurrentStudentRank = 0;
			CurrentStudentRank = getCurrentStudentRank(H, temprank, j, nSchools);
			temprank[j] = CurrentStudentRank;
			S.get(editStudentNum - 1).setRanking(j, CurrentStudentRank);

		}

		return true;
	}
	
	public static int getCurrentStudentRank(ArrayList<School> H, int[] temprank, int schoollocation, int nSchools) {
		boolean valid = false;
		int CurrentStudentRank = 0;
		do {
			valid = false;
			try {
				System.out.format("School %s: ", H.get(schoollocation).getName());
				CurrentStudentRank = Integer.parseInt(cin.readLine());
			} catch (NumberFormatException | IOException e) {
				System.out.format("ERROR: Input must be an integer in [%d, %d]!\n", 1, nSchools);
				valid = true;
			}
			if (CurrentStudentRank < 1 || CurrentStudentRank > nSchools) {
				System.out.format("ERROR: Input must be an integer in [%d, %d]!\n", 1, nSchools);
				valid = true;
			} else {
				for (int i = 0; i < nSchools; i++) {
					if (CurrentStudentRank == temprank[i]) {
						System.out.format("ERROR: Rank %d already used!\n\n", CurrentStudentRank);
						valid = true;
					}
				}

			}
		} while (valid);

		return CurrentStudentRank;
	}

	
	public static String EditRankSubMenu(String prompt) throws IOException {
		System.out.format("%s", prompt);
		boolean invalidinput = true;
		String EditRanking = "q";
		do {
			EditRanking = cin.readLine();
			if (EditRanking.equals("Y") || EditRanking.equals("y") || EditRanking.equals("N")
					|| EditRanking.equals("n")) {
				invalidinput = false;
			} else {
				System.out.print("Choice must be 'y' or 'n'!");
			}
		} while (invalidinput);

		return EditRanking;
	}


	
	private static int getIntegerinfinity(String prompt, int LB) {
		
			boolean valid = false;
			int val = 0;
			do {
				valid = false;
				try {
					System.out.print(prompt);
					val = Integer.parseInt(cin.readLine());
				} catch (NumberFormatException | IOException e) {
					System.out.format("\nERROR: Input must be an integer in [%d, %s]!\n\n", LB, "infinity");
					valid = true;
				}
				if (val < 0 ) {
					System.out.format("\nERROR: Input must be an integer in [%d, %s]!\n\n", LB, "infinity");
					valid = true;
				}
			} while (valid);

			return val;
		
	}

	// Sub-area menu to edit students and schools.
		public static String printSubMenu(int numStudents, int numSchools) throws IOException {
			// careful not to change global variable values
			String subchoice = "";
			boolean valid = false;
			do {
				System.out.println("Edit data\n" + "---------");
				System.out.println("S - Edit students\n" + "H - Edit high schools\n" + "Q - Quit\n");
				subchoice = getString("Enter choice: ");
				System.out.println();
				if (subchoice.equals("S") || subchoice.equals("s")) {
					if (numStudents == 0) {
						System.out.print("ERROR: No students are loaded!\n\n");
						valid = true;
					} else {
						valid = false;
					}
				} else if (subchoice.equals("H") || subchoice.equals("h")) {
					if (numSchools == 0) {
						System.out.print("ERROR: No schools are loaded!\n\n");
						valid = true;
					} else {
						valid = false;
					}
				} else if (subchoice.equals("Q") || subchoice.equals("q")) {
					valid = false;
				} else {
					System.out.print("ERROR: Invalid menu choice!\n\n");
					valid = true; // keep looping till its one of the three options
				}

			} while (valid);
			return subchoice;

		}

		private static void printStudentsfromedit(ArrayList<Student> STU, ArrayList<School> SCHO, boolean schoolmatch,
				int loadedstudents, int loadedschools, boolean assignedrank) {

			System.out
					.print(" #  Name                            GPA  ES  Assigned school            Preferred school order\n"
							+ "----------------------------------------------------------------------------------------------\n");
			for (int i = 0; i < loadedstudents; i++) {
				STU.get(i).print(STU, SCHO, schoolmatch, i);
				if (assignedrank) {
					STU.get(i).printRankings(SCHO, true, 0);
				} else {
					System.out.format("%-22s\n", "-");
				}
				System.out.println();
			}
			System.out.println(
					"----------------------------------------------------------------------------------------------"); // end
		
			
			
			
		}

		public static int getInteger(String prompt, int LB, int UB) {
			boolean valid = false;
			int val = 0;
			do {
				valid = false;
				try {
					System.out.print(prompt);
					val = Integer.parseInt(cin.readLine());
				} catch (NumberFormatException | IOException e) {
					System.out.format("\nERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					valid = true;
				}
				if (val < 0 || val > UB) {
					System.out.format("\nERROR: Input must be an integer in [%d, %d]!\n\n", LB, UB);
					valid = true;
				}
			} while (valid);

			return val;
		}

		public static double getDouble(String prompt, double LB, double UB) {
			boolean valid = false;
			double val = 0.00;
			do {
				valid = false;
				try {
					System.out.print(prompt);
					val = Double.parseDouble((cin.readLine()));
				} catch (NumberFormatException | IOException e) {
					System.out.format("\nERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
					valid = true;
				}
				if (valid == false && (val < LB || val > UB)) {
					System.out.format("\nERROR: Input must be a real number in [%.2f, %.2f]!\n\n", LB, UB);
					valid = true;
				}
			} while (valid);

			return val;
		}

		private static String getString(String prompt) throws IOException {
			System.out.print(prompt);
			String name = cin.readLine();
			return name;
		}
		
	private static void printSchools(ArrayList<Student> STU, ArrayList<School> SCHO, boolean schoolmatch,
			int loadedstudents, int loadedschools, boolean assignedrank) {
		
		if(loadedschools ==0){
			System.out.println("ERROR: No schools are loaded!\n");
		}
		
		else{
			System.out.println("SCHOOLS:\n");
		System.out
				.print(" #  Name                        # spots  Weight  Assigned student           Preferred student order\n"
						+ "--------------------------------------------------------------------------------------------\n");
	
		for (int j = 0; j < loadedschools; j++) {
			SCHO.get(j).print(STU, SCHO, schoolmatch, j);
			if (assignedrank) {
				SCHO.get(j).printRankings(STU, false, loadedstudents);
			} else {
				System.out.format("%-23s\n", "-");
			}

			System.out.println();
		}

		System.out.println(
				"--------------------------------------------------------------------------------------------"); // ends
																													// this
																													// table
		System.out.println();

		}
	
	}
		
		
	private static void printStudents(ArrayList<Student> STU, ArrayList<School> SCHO, boolean schoolmatch,
			int loadedstudents, int loadedschools, boolean assignedrank) {
		
		if(loadedstudents ==0){
			System.out.println("ERROR: No students are loaded!\n");
		}
		
		else{
			System.out.println("STUDENTS:\n");
		System.out
				.print(" #  Name                            GPA  ES  Assigned school            Preferred school order\n"
						+ "----------------------------------------------------------------------------------------------\n");
		for (int i = 0; i < loadedstudents; i++) {
			STU.get(i).print(STU, SCHO, schoolmatch, i);
			if (assignedrank) {
				STU.get(i).printRankings(SCHO, true, 0);
			} else {
				System.out.format("%-22s\n", "-");
			}
			System.out.println();
		}
		System.out.println(
				"----------------------------------------------------------------------------------------------"); // ends
																													// this
																													// table
		System.out.println();
		}
		

	}
	
	
	private static boolean assignRankings(int nStudents, int nSchools, ArrayList<Student> S, ArrayList<School> H) {
		
		//if numstudents != sum of numopenings of schools, don't assign! - solution program assigns regardless
		/*int i =0; 
		int numOpenings = 0;  
		while(i!= (nSchools)){
			numOpenings += H.get(i).getMaxMatches(); 
			i++;
		}
		
	//	System.out.format("\n Num of openings %d, num of students %d\n\n", numOpenings, nStudents);
		
		if(numOpenings != nStudents){
			return false; 
		}*/
		
		// Create array of composite scores per school for all students
		// Loop through each school 
	//	else{
			for (int k = 0; k < nSchools; k++) {
		
			double CompArray[] = new double[nStudents];

			// Loop through each student, calculate their score, store in comp array
			for (int l = 0; l < nStudents; l++) {
				double StudentScore = (H.get(k).getAlpha() * S.get(l).getGPA())
						+ (1 - H.get(k).getAlpha()) * S.get(l).getES();
				CompArray[l] = StudentScore;
				// System.out.format("\n Student %d's Comp Score for school %d
				// is: %f", l+1, k+1, StudentScore);
			}

			// Create Sorted Comp Array
			double SortedCompArray[] = new double[nStudents];
			SortedCompArray = Arrays.copyOf(CompArray, CompArray.length);
			Arrays.sort(SortedCompArray);

			// Loop through Sortedcomparray, start at last element- work back
			int index = 0;
			int student = 0;
			int temparray[] = new int[nStudents];
			for (int l = nStudents - 1; l >= 0; l--) {

				// Look through comp array for student with that comp score
				for (int m = 0; m < nStudents; m++) {
					if (CompArray[m] == SortedCompArray[l]) {
						// System.out.format("Found student %d, with comp score
						// of %f", m, CompArray[m]);
						student = m;
						// Change that value, so you wont select that student
						// again
						CompArray[m] = -1;
						break;

					}
				}

			//System.out.format("\nSchool %d Wants student: %d\n", k, student);
				temparray[index] = student;
				H.get(k).setRanking(index, student); // student starts at 0 
				
				index++;
				}
			}
			return true;
		//}
	}
	
	private static int loadStudents(ArrayList<Student> STU, String filestudent, int numschools) throws Exception {
		
		BufferedReader fin = new BufferedReader (new FileReader (filestudent));
		
		//Read one line at a time
		String line; 
		int numStudents = 0;
		int numStudentsAdded= 0; 
		
		while((line=fin.readLine())!=null){
			
			numStudents++; 
			String [] splitstring = line.split(","); 
			
			String name = splitstring[0];
			double GPA = Double.parseDouble(splitstring[1]);
			int ES = Integer.parseInt(splitstring[2]); 
			int size = splitstring.length -3; //subtract name, gpa, and es = # of schools 
			int [] rankings = new int [size]; 
			
			
			//move to isvalid() function in student object if time permits
			
			//check is size array = num schools, if not - then that student cannot be stored w/o rankings of their preferred schools
			//make sure rankings of schools aren't repeated
			int [] temp = new int [size]; 
			boolean repeat =false; 
			
			for (int i =0; i < rankings.length ; i++){
				
				int currentrank = Integer.parseInt(splitstring[3+i]); 
				
				
					for(int j =0; j< temp.length ; j++){
						if(currentrank == temp[j]){
							repeat =true; 
							break; 
						}
					}
				
				temp[i]= currentrank; 
			}
			
			
			//check gpa and es for bounds and ranking repetition
			if(GPA> 4.0 || GPA < 0.0 || ES >5 || ES < 0 || repeat==true || size!= numschools){
			}
			else{
				//store this student!
				numStudentsAdded++; 
				Student stud = new Student(name, GPA, ES, size); //remember size= nschools
				
				int count =3; 
				for(int k =0 ; k < size ; k++){
					stud.setRanking(k, Integer.parseInt(splitstring[count]));
					count++;
				}
				STU.add(stud); 
			}
				
			
			
		}
		
		System.out.format("%d of %d students loaded!\n\n", numStudentsAdded, numStudents);
		fin.close();
		return numStudentsAdded;
		
	}




	private static int loadSchools(ArrayList<School> SCHO, String fileschool) throws Exception {
		
		BufferedReader fin = new BufferedReader(new FileReader(fileschool));
		
		//read one line at a time
		String line; 
		int numSchools = 0; 
		int numSchoolsAdded = 0; 
		
		while((line = fin.readLine()) != null){
			
			numSchools++; 
			String [] splitstring = line.split(","); 
			
			//accept all strings, discard alphas, schools must accept >= 1 student(s) 
			String name = splitstring[0]; 
			double alpha = Double.parseDouble(splitstring[1]); 
			int studentsaccepted = Integer.parseInt(splitstring[2]); 
			
			if(alpha < 0.00 || alpha > 1.00){}
			else if(studentsaccepted < 1){}
			else{ //now you can add!
				
				School i = new School(name, alpha, studentsaccepted, 2000); //studentsaccepted= maxmatches, 2000 = rankings array size
				SCHO.add(i);
		//		System.out.format("%s\n", SCHO.get(1).getName());
				numSchoolsAdded++; 
		
			}
			
		}
		
		System.out.format("%d of %d schools loaded!\n\n", numSchoolsAdded, numSchools); 
		fin.close();
		return numSchoolsAdded;
	}

	public static String FileExist(String prompt) throws IOException {

		boolean loop = true;
		String file = "none";
		// Get School file first
		while (loop) {
			System.out.print(prompt);
			file = cin.readLine();
			System.out.println();

			// check if file exists
			File file1 = new File(file);
			boolean exists = file1.exists();

			if (file.equals("0")) { 
				System.out.println("File loading process canceled.\n");
				loop = false;
			} else if (exists == false) {
				System.out.println("ERROR: File not found!\n");
			} else {
				loop = false;
			}
		}
		return file;
	}
	
	public static String MenuInput() throws IOException {
		String MenuIn = "q";
		boolean valid = false;
		do {
			valid = false;
			DisplayMenu();
			MenuIn = cin.readLine();
			System.out.println();
			if (MenuIn.equals("L") || MenuIn.equals("l") || MenuIn.equals("E") || MenuIn.equals("e")
					|| MenuIn.equals("P") || MenuIn.equals("p") || MenuIn.equals("M") || MenuIn.equals("m")
					|| MenuIn.equals("D") || MenuIn.equals("d") ||MenuIn.equals("X") || MenuIn.equals("x") || MenuIn.equals("R") || MenuIn.equals("r")
					|| MenuIn.equals("Q") || MenuIn.equals("q")) {
				// Do nothing, input is valid
			}

			else {
				System.out.println("ERROR: Invalid menu choice!\n");
				valid = true;
			}
		} while (valid);

		return MenuIn;

	}

	public static void DisplayMenu() {
		System.out.print("JAVA STABLE MARRIAGE PROBLEM v2\n\n" + "L - Load students and schools from file\n"
				+ "E - Edit students and schools\n" + "P - Print students and schools\n"
				+ "M - Match students and schools using Gale-Shapley algorithm\n"
				+ "D - Display matches and statistics\n" + "X - Compare student-optimal and school-optimal matches\n" + "R - Reset database\n" + "Q - Quit\n" + "\n"
				+ "Enter choice: ");
	}

}
	



