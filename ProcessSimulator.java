
// Mohamed Irsath Abdul Azeez
// CMP 426 tue/thu 07:50 pm

import java.io.FileInputStream;		// Need to read file
import java.io.FileNotFoundException;	// throw exception of file reading
import java.io.IOException;
import java.util.*;	// array list and other use

public class ProcessSimulator {

	public static void main(String[] args) {
		
		
		if(args.length >= 2 && args[1].compareTo("fcfs") == 0 ){ // if args array size greater than 2 (file, fcfs) and varify if first element is fcfs than run this

			ProcessSimulator simulator = new ProcessSimulator();	// creating an object of main class
			ProcessSimulator.fcfs FCFS = simulator.new fcfs(args[0]);	// creating an object of subclass that take file and produce results

		}
		else if(args.length >= 3 && args[1].compareTo("rr") == 0 && Integer.parseInt(args[2]) > 0){ // if args array size greater than 3 (file, 'rr', time-quantum) and varify if first element is rr, than run this
			ProcessSimulator simulator = new ProcessSimulator();// creating an object of main class
			int tq = Integer.parseInt(args[2]); // converting the arsg second element to integer
			
			ProcessSimulator.RoundRobin RR = simulator.new RoundRobin(args[0],tq);	// creating an object of subclass that take file an file and time quantum value
		}
		else {
			System.out.println("Please check the input! in the terminal command line");	// display message if user input in command line is not valid
			}
		
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println("		Project Done By: Mohamed Irsath - Abdul Azeez");
		System.out.println("-----------------------------------------------------------------------------");
		System.out.println();
		
		}
	
	//-----------------------------------------------------------------------------------------------------------------------------
	
	public class fcfs{				// class for fcfs scheduling
		
		// array lists that get the list from text file
		private ArrayList<String>process;	
		private ArrayList<Integer>arrival;		
		private ArrayList<Integer>burst;		
		private int waitTimes[];			// array get wait times calculated for processes
		private int TurnaroundTimes[];
		private int ResponseTimes[];
		
		public fcfs(String fileName) {			// constructor
			this.process = new ArrayList<String>();
			this.arrival = new ArrayList<Integer>();
			this.burst = new ArrayList<Integer>();							
			readFile(fileName);			// read the file and get data from text file
			this.waitTimes = new int[this.process.size()];
			this.TurnaroundTimes = new int[this.process.size()];
			this.ResponseTimes = new int[this.process.size()];
			displayProcesses();			// display the process on console
			sortProcesses();			// sorting the process received from text file
			displayGanttChart();		// produce gantt chart and display on console
			displayAverageTimes();		// calculate times and average times
			
		}
		public void readFile(String fileName) {
			
			try {
				FileInputStream file = new FileInputStream(fileName);	// open the file
				Scanner read = new Scanner(file);	// scan the file
				
				while(read.hasNextLine()) {		// check if there is any line
					String Line = read.nextLine();	// take the line if exist
					if(Line.length()>0) {			// avoiding empty line and take the line has values
						String arr[] = Line.split(",");		// split the string line with comma delimiter
						this.process.add(arr[0].trim());		// avoid extra spaces and add the process			
						this.arrival.add(Integer.parseInt(arr[1].trim()));		// avoid extra spaces and add the process's arrival time				
						this.burst.add(Integer.parseInt(arr[2].trim())); // avoid extra spaces and add the process's burst time
						
						
						
					}
					
				}// end while
				
				
				
				read.close();	// closing the scanner object
				
				try {
					file.close();	 // close the file
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Unable to close the file"); // throw exception if unable to close the file
					e.printStackTrace();
				}  
				
			} catch (FileNotFoundException e) {
				System.out.println("Error opening file!");
				e.printStackTrace();
			}
			
		}
		
		public void displayProcesses() {
			
			System.out.println("\n----------------------------------------------------");
			System.out.println("             CPU Scheduling Simulation              ");
			System.out.println("----------------------------------------------------\n");
					
					
			System.out.println("-------------------------------");
			System.out.printf("| %7s | %7s  | %7s|\n", "Process", "Arrival","Burst ");
			System.out.println("-------------------------------");	
			
			
				
			        for(int k = 0; k < this.process.size() ; k++){		// showing all the process in a table obtained from text file
			        	System.out.printf("| %2s \t  |   %2d     |    %2d  |\n", this.process.get(k),this.arrival.get(k),this.burst.get(k));
			        	
			        }
			        System.out.println("-------------------------------\n");
			        
			System.out.println("\n----------------------------------------------------");
			System.out.println("             First Come First Served Scheduling              ");
			System.out.println("----------------------------------------------------\n");  
			
			
			        
			}
		
		public void displayGanttChart() {
			
			System.out.println("Gant Chart:	\n");
			
			Stack<String>CompleteProcess = new Stack<String>();		// creating a stack object
			
			for(int i = 0; i < this.process.size(); i++){ 				// Coping all sorted array to vector
				CompleteProcess.push(this.process.get(i));	// pass all the process into stack object that check completion of process
		    }
			
			int i=0;
		    int count_track = 0;	// tracking the process time
		    
//		    System.out.println(CompleteProcess.size());

		    while(!CompleteProcess.empty()){		// loop until all the processes completed
		    	
		        if(i == 0){		// first process
		            if(this.arrival.get(0)>0){	// check if the first process arrival time is greater than 0
		                System.out.printf("[0 - %2d]\tCPU Idle\n", this.arrival.get(0));	// if greater than 0 than check the Idle time         
		                count_track += this.arrival.get(0);		// adding the idle time to count track

		            	}
		            
		            // this will run if the first process arrive at time 0 so no idle set
		            System.out.printf("[%d - %2d]\tprocess %s running\n", count_track, (count_track + this.burst.get(i)), this.process.get(i));	//00000
		            this.waitTimes[i] = count_track - this.arrival.get(i);		// calculating the wait time of the first process
		            this.TurnaroundTimes[i] = this.waitTimes[i] + this.burst.get(i);	// calculating the turn  around times
		            this.ResponseTimes[i] = count_track - this.arrival.get(i);	// calculating the response time	   
		            count_track += this.burst.get(i);	// add the burst time to count track
		            i++;
		            CompleteProcess.pop();	// delete a process from process list so we ensure that one process is completed

		        }
		        else{	// other process than first process
		            if(i != (this.process.size()-1)){		// in between process than first and last
		                if((this.arrival.get(i) - count_track > 0)){	// if the next arrive after the count track time than idle
		                	
		                	System.out.printf("[%d - %2d]\tCPU Idle\n", count_track, (count_track + (this.arrival.get(i)-count_track)));		                			                    
		                    count_track += (this.arrival.get(i)-count_track);

		                }
		                else{	// in no idle, bring the next ready process

		                	System.out.printf("[%d - %2d]\tProcess %s running\n", count_track, (count_track + this.burst.get(i)), this.process.get(i) );
		                    
		                	this.waitTimes[i] = count_track - this.arrival.get(i);
		                    this.TurnaroundTimes[i] = this.waitTimes[i] + this.burst.get(i);
		                    this.ResponseTimes[i] = count_track - this.arrival.get(i);	                    
		                    count_track += this.burst.get(i);
		                    i++;
		                    CompleteProcess.pop();	// delete a process in the list when completed
		                }
		            }
		            else{   // last process
		            	
		            	System.out.printf("[%d - %2d]\tProcess %s running\n", count_track, (count_track + this.burst.get(i)), this.process.get(i));
		                this.waitTimes[i] =  count_track - this.arrival.get(i);
		                this.TurnaroundTimes[i] = this.waitTimes[i] + this.burst.get(i);
		                this.ResponseTimes[i] = count_track - this.arrival.get(i);		                
		                i++;
		                CompleteProcess.pop();	// delete a process in the list of completed
		            }
		        }

		    }
		   System.out.println();
			
			
		}
		
		public void sortProcesses() {
			int min;
		    for(int i = 0; i < this.process.size(); i++){
		        min = this.arrival.get(i);
		        
		        for(int k = 0; k < this.process.size(); k++){				// sorting all parallel arrays based on arrival times in ascending order
		            if(min < this.arrival.get(k) ){
		                int temp1,temp2;
		                String temp3;

		                temp1 = this.arrival.get(i);
		                this.arrival.set(i, this.arrival.get(k));
		                this.arrival.set(k, temp1);
		               

		                temp2 = this.burst.get(i);
		                this.burst.set(i, this.burst.get(k));
		                this.burst.set(k, temp2);
		                

		                temp3 = this.process.get(i);
		                this.process.set(i, this.process.get(k));
		                this.process.set(k, temp3);	              


		            }

		        }
		    } 
			
			
			
		}
		
		public void displayAverageTimes() {
			
			 double Avg_Wait, Avg_Turn, Avg_Resp;		// variable that helps to calculate the times of process and average times
			 double tot_w=0, tot_t=0, tot_r=0;
			 
			 System.out.println();
			 System.out.println("Turnaround times:");
			 
			    
			    int j = 0;
			    for(int value: this.TurnaroundTimes){		// display all process's turn around time collected in an array
			        tot_t += value;
			        System.out.printf("%s = %d\n", this.process.get(j), value);			        
			        j++;
			    }
			    
			    System.out.println();			    
			    System.out.println("Wait times:");
			    
			    
			    j = 0;
			    for(int value: this.waitTimes){		// display all wait time collected in an array
			        tot_w += value;
			        System.out.printf("%s = %d\n", this.process.get(j), value);			        
			        j++;
			    }
			    
			    System.out.println();			    
			    System.out.println("Response times:");			   
			    
			    j=0;
			    for(int value: this.ResponseTimes){		// display all process's response time collected in an array
			        tot_r += value;
			        System.out.printf("%s = %d\n", this.process.get(j), value);	
			        j++;
			    }
			    System.out.println();
			    
			    // calculating average times of following
			    Avg_Turn = tot_t/this.process.size();
			    Avg_Resp = tot_r/this.process.size();
			    Avg_Wait = tot_w/this.process.size();
			    
			    System.out.printf("Average Turnaround time: %.2f\n", Avg_Turn);
			    System.out.printf("Average Wait time: %.2f\n", Avg_Wait);
			    System.out.printf("Average Response time: %.2f\n", Avg_Resp);
			 
			 
		}
			
		
	};
	
	
	//------------------------------------------------------------------------------------------------------------
	
	public class RoundRobin{			// creating class for round robin schedule

		// array list get the process details from text file
		private ArrayList<String> process;
		private ArrayList<Integer> arrival;
		private ArrayList<Integer> burst;
		private int timeQuantum;
		private Vector<Integer>timeInterval = new Vector<Integer>();	// Vector array to get the time interval of gantt chart
		private Vector<String>que = new Vector<String>();		// vector array of que of process execution
		private Vector<String>List = new Vector<String>();		// vector array 
		
		
		
		public RoundRobin(String fileName, int tq) {
		
			this.process = new ArrayList<String>();
			this.arrival = new ArrayList<Integer>();
			this.burst = new ArrayList<Integer>();
			this.timeQuantum = tq;
			
			readFile(fileName);			
			displayProcesses();
			sortProcesses();
			displayGanttChart();
			displayAverageTimes();
			
		}
		public void sortProcesses() {
			int min;
		    for(int i=0; i < this.process.size();i++){
		        min = this.arrival.get(i);
		        for(int k=0; k<4; k++){
		            if(min < this.arrival.get(k) ){
		                int temp1,temp2;
		                String temp3;

		                temp1 = this.arrival.get(i);
		                this.arrival.set(i, this.arrival.get(k));
		                this.arrival.set(k, temp1);
		                

		                temp2 = this.burst.get(i);
		                this.burst.set(i, this.burst.get(k));
		                this.burst.set(k, temp2);
		               

		                temp3 = this.process.get(i);
		                this.process.set(i, this.process.get(k));
		                this.process.set(k, temp3);
		               


		            }

		        }
		    } 
			
			
			
		}
		
		public void readFile(String fileName) {
			
			try {
				FileInputStream file = new FileInputStream(fileName);
				Scanner read = new Scanner(file);
				
				while(read.hasNextLine()) {
					String Line = read.nextLine();
					if(Line.length()>0) {			// avoiding empty line and take the line has values
						String arr[] = Line.split(",");
						this.process.add(arr[0].trim());
						this.arrival.add(Integer.parseInt(arr[1].trim()));
						this.burst.add(Integer.parseInt(arr[2].trim()));	
						
					}
					
				}// end while
				read.close();
				try {
					file.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Error in closing file\n");   // if any error occurred in closing file
					e.printStackTrace();
				}
				
				
			} catch (FileNotFoundException e) {
				System.out.println("Error opening file!");		// catch file reading exception
				e.printStackTrace();
			}
			
			
		}// read file end
		
		public void displayProcesses() { // method display all the process
			
			System.out.println("\n----------------------------------------------------");
			System.out.println("             CPU Scheduling Simulation              ");
			System.out.println("----------------------------------------------------\n");
					
					
			System.out.println("-------------------------------");
			System.out.printf("| %7s | %7s  | %7s|\n", "Process", "Arrival","Burst ");
			System.out.println("-------------------------------");	
			
			
				
			        for(int k=0; k < this.process.size();k++){		// displaying the process on screen obtained from file
			        	System.out.printf("| %2s \t  |   %2d     |    %2d  |\n", this.process.get(k),this.arrival.get(k),this.burst.get(k));		        
			        	
			        }
			        System.out.println("-------------------------------\n");
			        
			System.out.println("\n----------------------------------------------------");
			System.out.println("              Round Robin Scheduling               ");
			System.out.println("----------------------------------------------------\n");  
			
			
			        
			}
		
		public void displayGanttChart() {
			
			System.out.println("Gant Chart:	\n");
			
			Stack<String>CompleteProcess = new Stack<String>(); // collect all process in a stack and check for completion
			Vector<String>takenProcess = new Vector<String>();		// vector array that check if the particular process is taken		
			Vector<Integer>duplicateBurst = new Vector<Integer>();	// duplicate the burst time and take into this vector 
			
			int count = 0;
			int tq = this.timeQuantum;	// take the time quantum value is taken from user
			
			
			for(int i=0; i < this.process.size(); i++){ 				// Coping all sorted process and burst time array to vectors
				CompleteProcess.push(this.process.get(i));
				takenProcess.add(this.process.get(i));
				duplicateBurst.add(this.burst.get(i));

		    }
			
			while(!CompleteProcess.empty()){ // this loop until all processes are executed

		        if(this.arrival.get(0) > 0){		// check the first process arrival time is greater than 0
		        	
		        	System.out.printf("[%d - %2d]\tCPU IDLE\n", count, (count + this.arrival.get(0))); // then there is an IDLE	            
		            count += this.arrival.get(0);	// add that idle time to count
		            this.timeInterval.add(this.arrival.get(0));	// take the time interval to vector
		            this.List.add("I");		// add the idle to list so we can have same parallel array of time interval and execution of cpu
		            

		        }
		        this.que.add(this.process.get(0));		// add the first que to que vector
		        
		        if(!takenProcess.isEmpty()){	// we remove the process that has been taken into que
		        	takenProcess.remove(0);
		        }

		       
		       
		        // check the other processes that arrived in the particular time count
		        for(int k=0; k < this.process.size(); k++){		// check all the processes in the taken process vector
		            if(!takenProcess.isEmpty()){ // Verify if there is any process available in the taken process vector
		                if(this.arrival.get(k) <= count && check(takenProcess, this.process.get(k))){	// if the particular position process arrival time is less than equal than take those process
		                    this.que.add( this.process.get(k));		// add those process into que vector
		                    for(int i = 0; i < takenProcess.size();i++){	// check in the taken process and remove process that taken
		                        if(takenProcess.elementAt(i).compareTo( this.process.get(k)) == 0){
		                        	takenProcess.remove(i);		                            
		                            }
		                        }
		                    }
		                
		            }

		        }
		        
	        
		        for(int i=0; i <this.que.size();i++){		// now loop through all the que vector that has process
		            
		           
		            if(i == 0){		// check the first process
		                
		                if(duplicateBurst.elementAt(0) <= tq){	// check the first process's burst time in the que is less than time quantum
		                	this.List.add(this.que.elementAt(0));	// add the the first process into execution list
		                	this.timeInterval.add(duplicateBurst.elementAt(0));		// add the time interval of the burst time of the process
		                    
		                    count += duplicateBurst.elementAt(0);	// add the burst time to count
		                    duplicateBurst.set(0, 0);	// set the burst time to 0 of that process
		                    CompleteProcess.pop();		// we remove one process since it already completed the burst
		                   
		                    System.out.printf("[%d - %2d]\tprocess %s running\n", this.arrival.get(0), count, this.que.elementAt(0));	// display the process on gantt chart
		                    
		                    
		                    // check for other process
		                    
		                    int F_size = takenProcess.size();	// take the initial size of the taken process vector
		                    
		                    // check for other process that arrived in the particular time count
		                    for(int k = 0; k < this.process.size(); k++){
		    		            if(!takenProcess.isEmpty()){
		    		                if(this.arrival.get(k) <= count && check(takenProcess, this.process.get(k))){	// if process arrived ready and then add to que vector
		    		                    this.que.add(this.process.get(k));
		    		                    for(int j = 0; j < takenProcess.size();j++){
		    		                        if(takenProcess.elementAt(j).compareTo(this.process.get(k)) == 0){	// Remove the processes taken
		    		                        	takenProcess.remove(j);		                            
		    		                            }
		    		                        }
		    		                    }
		    		                
		    		            }

		    		        }
		                    
		                    int L_size = takenProcess.size();	// check the taken process size after checking for processes
		                    
		                    if(F_size == L_size && !takenProcess.isEmpty()){       // if bot size equal and taken process vector in not empty than no processes have been taken
		                    	
		                    	this.que.add(this.process.get(1));	// if no process taken, than add the next available process into que
		                        System.out.printf("[%d - %2d]\tCPU IDLE\n", count, this.arrival.get(1));	// check for idle time and display the idle	                        
		                        count += this.arrival.get(1) - count;	// add the idle time to count track
		                        this.timeInterval.add(this.arrival.get(1) - 0);	// add the idle time to time interval vector
		                        this.List.add("I");		// add the idle into executed list vector
		                        
		                        if(!takenProcess.isEmpty()){
		                        	takenProcess.remove(0);		// remove the taken process from the vector	                            
		                        }
		                        
		                        
		                    }		                    
		                    

		                } 
		                else{		// if the proces's burst time is greater than time quantum
		                   
		                	this.timeInterval.add(tq);		// add the tq to time interval	
		                    this.List.add(que.elementAt(0));	// add the first process in the que to list	                   
		                    count += tq;	// add the tq to count track
		                    duplicateBurst.set(0, (duplicateBurst.elementAt(0) - tq));	// reduce the burst time by tq
		                    
		                    
		                    // again check the process that arrived within the count times take if its ready and if not taken in the taken process
		                    for(int k = 0; k < this.process.size(); k++){
		                        if(!takenProcess.isEmpty()){
		                            if(this.arrival.get(k) <= count && check(takenProcess, this.process.get(k))){
		                            	this.que.add(this.process.get(k));
		                                
		                                for(int j=0; j < takenProcess.size();j++){
		                                    if(takenProcess.elementAt(j).compareTo(this.process.get(k)) == 0){
		                                    	takenProcess.remove(j);		                                       
		                                        }
		                                    }
		                                }
		                            
		                        }

		                    }
		                    this.que.add(this.que.elementAt(0));	// add the same process back into que
		                    System.out.printf("[%d - %2d]\tprocess %s running\n", this.arrival.get(0),count, this.que.elementAt(0));	// display the current process
		                   
		                    
		                }// else for tq
      
		            } // if i == 0
		            
		            
		            
		            
		            else{ // else i != 0    // for other process than first in the que

		                int m = getPos(this.process, this.que.elementAt(i));	// get the position of the next process in the process array
		                
		                if(duplicateBurst.elementAt(m) <= tq){		// check the process's burst time and check if it less than tq
		                    
		                	System.out.printf("[%d - %2d]\tprocess %s running\n", count, (count + duplicateBurst.elementAt(m)), this.que.elementAt(i));	// diplay the current process
		                    this.timeInterval.add(duplicateBurst.elementAt(m));		// add the time into time interval
		                    
		                    this.List.add(que.elementAt(i));	// add current process into execution list
		                    
		                    count += duplicateBurst.elementAt(m);		// add the current burst time of the process to count
		                    duplicateBurst.set(m, 0);	// set the current burst time of the process to 0
		                    CompleteProcess.pop();		// delete one process that completed
		                   
		                    
		                    int F_size = takenProcess.size();		
		                    
		                    for(int k = 0; k < this.process.size(); k++){
		                        if(!takenProcess.isEmpty()){
		                            if(this.arrival.get(k) <= count && check(takenProcess, this.process.get(k))){
		                            	this.que.add(this.process.get(k));		                                
		                                for(int j=0; j < takenProcess.size();j++){
		                                    if(takenProcess.elementAt(j).compareTo(this.process.get(k)) == 0){
		                                    	takenProcess.remove(j);		                                        
		                                        }
		                                    }
		                                }
		                            
		                        }

		                    }
		                    
		                    int L_size = takenProcess.size();
		                    
		                    if(F_size == L_size && !takenProcess.isEmpty()){       // no process came
		                    	
		                    	this.que.add(takenProcess.elementAt(0));		// add the next available process to que                      
		                        int n = getPos(this.process, takenProcess.elementAt(0));	// get that process's postion
		                        System.out.printf("[%d - %2d]\tCPU IDLE\n", count, this.arrival.get(n));	 // display idle time                      
		                        this.timeInterval.add(this.arrival.get(n) - count);		// add the time to time interval
		                        count += (this.arrival.get(n) - count);		   	// add the idle time to count                    
		                        this.List.add("I");		// add idle to execution
		                        
		                        if(!takenProcess.isEmpty()){
		                        	takenProcess.remove(0);			// remove the process taken from taken process vector                            
		                        }
		                        
		                        
		                    }
		                    
		                    
		                    

		                }else{		// if the burst time is greater than tq
		                	
		                	System.out.printf("[%d - %2d]\tprocess %s running\n", count, (count+tq), this.que.elementAt(i));	// display the current process and interval
		                    
		                    this.timeInterval.add(tq);		// add the tq into time interval
		                    this.List.add(que.elementAt(i));		// add the current process back into que since it has remaining burst time                    
		                    count += tq;		// add the tq to count
		                    duplicateBurst.set(m, (duplicateBurst.elementAt(m) - tq));		// set the current process's burst taken by tq
		                   
		                    
		                    // check again the process in the taken process with count if its ready to add in the que
		                    for(int k = 0; k < this.process.size(); k++){
		                        if(!takenProcess.isEmpty()){
		                            if(this.arrival.get(k) <= count && check(takenProcess, this.process.get(k))){
		                            	this.que.add(this.process.get(k));		                                
		                                for(int j=0; j < takenProcess.size();j++){
		                                	
		                                    if(takenProcess.elementAt(j).compareTo(this.process.get(k)) == 0){
		                                    	takenProcess.remove(j);		                                        
		                                        }
		                                    }
		                                }
		                        }

		                    }
		                    this.que.add(que.elementAt(i));		// add the current process back into que vector
		                    
		                }
		                        
		            }

		        }// end of que for loop

		    }// end while main
			
			
		}
			
		
		public void displayAverageTimes() {
			
			Vector<Integer>accumulativeTotal = new Vector<Integer>(); // vector that take accumulative total of time interval
			int sum = 0;
			for(int i=0; i < this.timeInterval.size(); i++) {
				sum += this.timeInterval.elementAt(i);
				accumulativeTotal.add(sum);		// add the accumulative sum to accumulative vector
			}

			Vector<Integer>WT = new Vector<Integer>();
			Vector<Vector<Integer>>arp = new Vector<Vector<Integer>>();		// two dimensional vector take the process's position in the gantt chart

			for(int i = 0 ; i < this.process.size(); i++) {
				
				Vector<Integer>temp = new Vector<Integer>();
				
				for(int k = 0; k < this.List.size(); k++) {
					if(this.process.get(i).compareTo(this.List.elementAt(k)) == 0) { // going order with sorted array list and take the position of the process in the gantt chart
						temp.add(k);
	
					}
				}
				arp.add(temp);
			
			}			

			Vector<Integer>waitTimes = new Vector<Integer>();	// collect all the process wait times
			
			int wt = 0;
			
			for(int i=0; i < arp.size();i++) {
				
				// check and calculate the wait times based of the position in the gantt chart for each processs
				for(int k=0; k < arp.get(i).size(); k++) {
					if(arp.get(i).get(k) == 0 && k == 0) {
						wt += this.arrival.get(0) - 0;
					}
					else if(arp.get(i).get(k) != 0 && k == 0) {
						
						wt += accumulativeTotal.elementAt(arp.get(i).get(k) - 1) -  this.arrival.get(i);
					}
					else {
						
						wt += (accumulativeTotal.elementAt(arp.get(i).get(k) - 1) -  accumulativeTotal.elementAt(arp.get(i).get(k-1)));
					}
				}
				waitTimes.add(wt);
				wt = 0;
			}
			
		    double total_wait_times = 0, total_turnaround_times = 0, total_response_times = 0;
		    
			System.out.println();
			System.out.println("Turnaround times:");
			int m = 0;
			
			for(int value: waitTimes){
				total_turnaround_times += (value + this.burst.get(m));	// calculation turnaround time using wait time and burst time
				System.out.printf("%s = %d\n", this.process.get(m), value + this.burst.get(m));		        
		        m++;
		    }
			System.out.println();
			System.out.println("Wait times:");
		    m = 0;
		    
		    for(int value: waitTimes){
		    	total_wait_times += value;
		    	System.out.printf("%s = %d\n", this.process.get(m), value);	
		        
		        m++;
		    }
		    System.out.println();
		    
		    Vector<Integer> responseTime = new Vector<Integer>();
		    
		    // calculating response time and add into response time vector
		    
		    for(int i = 0; i < this.process.size(); i++) {
		    	 responseTime.add((accumulativeTotal.elementAt(arp.get(i).elementAt(0)) - this.timeInterval.elementAt(arp.get(i).elementAt(0))) - this.arrival.get(i));
		    }
		   
		    	
		    System.out.println();
			System.out.println("Response Times:");
		    m = 0;
		    
		    for(int value: responseTime){
		    	total_response_times += value;
		    	System.out.printf("%s = %d\n", this.process.get(m), value);		        
		        m++;
		    }
		    System.out.println();
		    
		    double Avg_Turn = total_turnaround_times/this.process.size();
		    double Avg_Resp = total_response_times/this.process.size();
		    double Avg_Wait = total_wait_times/this.process.size();
		    
		    // displaying average times calculated
		    System.out.printf("Average Turnaround time: %.2f\n", Avg_Turn);
		    System.out.printf("Average Wait time: %.2f\n", Avg_Wait);
		    System.out.printf("Average Response time: %.2f\n", Avg_Resp);
		    System.out.println();
		    System.out.println();
			
			
			
		}
		
		
		
		
	};
	
	//------------------------------------------------------------------------------------
	
	
	// Helper methods that help to check a process in the process taken vector
	public boolean check(Vector<String>a, String b){
	    
	    for(int i=0; i<a.size();i++){
	        if((a.elementAt(i)).compareTo(b) == 0){
	            return true;
	        }
	    }
	    return false;
	}

	// helper method to get the particular process's position of the process in the sorted process array
	public int getPos(ArrayList<String>x, String a){
	    for(int i=0; i < x.size(); i++){
	        if(x.get(i).compareTo(a) == 0){
	            return i;
	        }
	    }
	    return -1;
	}
	
	


}
