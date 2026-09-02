import java.util.Scanner; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.FileNotFoundException;

public class Main 
{
    private static int time; //Set time as 6 minutes, it's accessible within all methods 
    private static boolean employed = true; //Default to the user being employed 
    private static String lastPossession = "score"; //Default to the last defensive possession being a score 

    public static void Intro() //Prints the introduction message 
    {
        System.out.println("This is an NFL head coach simulator, where you get to be the");
        System.out.println("offensive play caller for any team you like! Each game is");
        System.out.println("2 minutes long, and there are 8 games in a season.");
        System.out.println("Which team would you like to coach?");
    }
    public static ArrayList<String> getAllTeams() //Reads from a file to get an ArrayList of all NFL teams
    {
        ArrayList<String> teams = new ArrayList<String>(); 
        try 
        {
            File NFLteams = new File("nflteams"); 
            Scanner reader = new Scanner(NFLteams); //Initialize a scanner to read from the file 
            while (reader.hasNext()) //While the file still has lines to read, 
            {
                teams.add(reader.nextLine()); //Add each team to the ArrayList 
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error! File not found!"); //This should never execute 
        }
        return teams; //return the ArrayList of all NFL teams 
    }

    public static String getTeam(Scanner input, ArrayList<String> allTeams)
    { 
        String team = input.nextLine(); //Ask the user for their preferred team 
        Boolean valid = false; //Default to the user's choice being invalid 
        int a = 0; //This is eventually used to store an index of the ArrayList of teams 
        while (!valid) //While the user has not yet entered a valid team 
        {
            for (int i = 0; i < 32; i++) //Loop through each team in the ArrayList
            {
                if (allTeams.get(i).equalsIgnoreCase(team)) //If the user's input matches a team
                {
                    System.out.println("Got it - Your team will be the " + allTeams.get(i) + "!");
                    valid = true; //Indicate that their choice is valid 
                    a = i; //Store the index at which the user's team occured in the ArrayList
                }
            }
            if (!valid) //If the user doesn't enter a valid team name 
            { 
                System.out.println("Team not found . . . try again:"); //Print an error message 
                team = input.nextLine(); //Let the user try again  
            }
        }
        return allTeams.get(a); //Use the stored index to return the team that the user chose  
    }

    public static ArrayList<String> getSchedule(ArrayList<String> teams, String team)
    {
        teams.remove(team); //Remove the user's team from the ArrayList (so they don't play against themselves)
        ArrayList<String> schedule = new ArrayList<String>(); 
        while (schedule.size() <= 8) //Until the schedule has all 8 games, 
        {
            int rand = (int)(Math.random()*teams.size()); //Generate a random number between 0 and 31, 
            schedule.add(teams.get(rand)); //Use the random number to pick a random team, and add it to the schedule
            teams.remove(rand); //Remove the previously added team, so no repeats occur 
        }
        return schedule; //return the schedule (as an ArrayList of size 8)
    }

    public static boolean coinToss(Scanner input, int i) //Simulate the coin toss 
    {
        System.out.print("It is time for the coin toss. ");
        String toss = "heads"; //Default to the coin toss being heads 
        String tossAbbrev = "h"; //Also store a separate one as just "h", giving the user flexibilty 
        String possession = ""; 
        boolean offense = true; //Return this
        if (Math.random() <= 0.5) //This has a 50% chance of executing
        {
            toss = "tails"; //Convert the coin toss to tails 
            tossAbbrev = "t"; //Also adjust the abbreviated version 
        } //Now the coin toss is 50/50 between heads and tails 
        
        if (i == 0 || i == 2 || i == 4 || i ==6) //For even games, the user is the home team
        {
            System.out.println("You are the home team. The toss was " + toss + ".");
            if (Math.random() < 0.5) //This has a 50% chance of executing 
            {
                System.out.println("Your opponent guessed incorrectly. Would you like to receive or kick?");
                possession = input.nextLine(); //Store the user's decision 
            }
            else //This (obviously) also has a 50% chance of executing 
            {
                System.out.println("Your opponent guessed correctly. You will receive the opening kick.");
                possession = "receive"; //The user will receive to start the game 
            }
        }
        if (i == 1 || i == 3 || i == 5 || i ==7) //For odd games, the user is the away team 
        {
            System.out.println("You are the away team, please call the toss.");
            String guess = input.nextLine(); //Store the user's guess 
            if (guess.equalsIgnoreCase(toss) || guess.equalsIgnoreCase(tossAbbrev)) //If the user's guess matches the coin toss, 
            {
                System.out.println("Your guess was correct. Would you like to receive or kickoff?");
                possession = input.nextLine(); //Store the user's decision 
            }
            if (!guess.equalsIgnoreCase(toss) && !guess.equalsIgnoreCase(tossAbbrev)) //If the user's guess does not match the coin toss, 
            {
                System.out.println("Your guess was incorrect. You will kickoff to start the game.");
                possession = "kick"; //The user will kickoff to start the game 
            }
            if (!guess.equalsIgnoreCase("heads") && !guess.equalsIgnoreCase("tails") && !guess.equalsIgnoreCase("h") && !guess.equalsIgnoreCase("t")) 
            {
                System.out.println("You can't even get the coin toss right. You have been fired as head coach.");
                employed = false; //Fire the user 
                System.out.println("Would you like another chance?");
                input.nextLine();
                System.out.println("You don't get another chance."); 
            }
        }
        if (possession.equalsIgnoreCase("kick") || possession.equalsIgnoreCase("kickoff"))
        {
            offense = false; //If the user chooses to kickoff, change offense to false (it will otherwise be true)
        }
        if (!possession.equalsIgnoreCase("kick") && !possession.equalsIgnoreCase("kickoff") && !possession.equalsIgnoreCase("receive"))
        {
            System.out.println("You've entered an invalid choice. You have been fired as head coach.");
            employed = false; 
            System.out.println("Would you like another chance?");
            input.nextLine();
            System.out.println("You don't get another chance.");
        }
        return offense; //return the boolean (if offense is true, then the user receives the opening kick)
    }
    public static int defenseSimulator() //Simulates each defensive possession 
    {
        int originalTime = time; //Stores the time at the beginning of the possession 
        int opponentScore = 0; //Sets the opponent's points for the drive to 0
        if (originalTime >= 20 && employed) //If the drive started with more than 20 seconds, 
        {
            System.out.println("It's time to play defense!\n...");
            int result = (int)(Math.random()*4); //Any integer between 0 and 3
            if (result == 0) //This has a 25% chance of executing 
            {
                System.out.println("Your defense allowed a touchdown.");
                opponentScore += 7; 
                time -= (int)(15 + Math.random()*31); //Any integer between 15 and 45
                lastPossession = "score"; //Indicate that the last defensive possession resulted in a score
            }
            if (result == 1) //This has a 25% chance of executing
            {
                System.out.println("Your defense allowed a field goal.");
                opponentScore += 3; //The opponent scored 3 points on that drive 
                time -= (int)(10 + Math.random()*21); //Any integer between 10 and 30
                lastPossession = "score"; //Indicate that the last defensive possession resulted in a score 
            }
            if (result == 2) //This has a 25% chance of executing
            {
                System.out.println("Your defense forced a punt.");
                opponentScore = 0; //The opponent scored 0 points on that drive 
                time -= (int)(5 + Math.random()*11); //Any integer between 5 and 15
                lastPossession = "punt"; //Indicate that the last defensive possession resulted in a punt 
            }
            if (result == 3) //This has a 25% chance of executing
            {
                System.out.println("Your defense forced a turnover!");
                opponentScore = 0; //The opponent scored 0 points on this drive 
                time -= (int)(10 + Math.random()*21); //Any integer between 10 and 30
                lastPossession = "turnover"; //Indicate that the last defensive possession ended in a turnover 
            }
        }
        if (originalTime < 20 && originalTime > 0 && employed) //If the drive starts with 0 to 20 seconds remaining,
        {
            System.out.println("It's time to play defense!\n..."); //The defensive possession won't be simulated, there's not enough time left
            System.out.println("Your defense forced the end of the game!"); 
            time = 0; 
        }
        if (originalTime <= 0 && employed) //If the drive starts with less than or equal to 0 seconds remaining
        {
            time = 0; //The defensive possession won't be simulated, because the game is over 
        }
        return opponentScore; //return the number of points the opponent scored on the drive (should be 0, 3, or 7)
    }

    public static int offenseSimulator(Scanner input) //Simulates each offensive possession 
    {
        int myScore = 0; //Return this 
        int down = 1; 
        int yardsFromFirst = 10; //Defaults to 1st and 10 
        int yardsFromGoal = getFieldPosition(); //Get the starting field position 
        boolean stillOnOffense = true; //Keep track of whether or not the possession has ended 
        boolean fourthDown = false; //Keep track of whether or not it's 4th down 
            while (stillOnOffense && employed) //While the possession is still going on, 
            {
                fourthDown = false; //Reset 4th down to false 
                if (down == 1 && time > 0) //If it's 1st down and there's time on the clock,  
                {
                    System.out.print("...\nIt's 1st and " + yardsFromFirst); //Print the situation
                    System.out.println(", " + yardsFromGoal + " yards away from a touchdown.");
                    String playCall = getPlayCall(input, fourthDown); //Prompt for the play call 
                    if (playCall.equals("FIELDGOAL")) //If the user chooses to kick a field goal
                    {
                        time -= 3;
                        boolean success = fieldGoalSimulator(yardsFromGoal); //Call on method, get the result of the FG attempt
                        if (success) //If the field goal is good 
                        {
                            myScore +=3; //Add 3 to the score 
                        }
                        stillOnOffense = false; //End the possession 
                    }
                    if (playCall.equals("PUNT")) //If the user chooses to punt 
                    {
                        time -= (int)(5+Math.random()*4); //Any integer between 5 and 8 
                        System.out.println("You have punted."); 
                        stillOnOffense = false; //Also end the possession 
                    }
                    if (!playCall.equals("FIELDGOAL") && !playCall.equals("PUNT")) //If the user enters a regular play (rather than a FG or punt)
                    {
                        int yardage = getYardage(playCall); //Get the yardage for the given play call 
                        System.out.println("You gained "+ yardage + " yards."); //Print the result of the play 
                        yardsFromFirst -= yardage; //Adjust the distance from the 1st down 
                        yardsFromGoal -= yardage; //Adjust the distance from the goal line
                        System.out.println("There are " + time + " seconds remaining in the game."); //Print the time 
                    }
                }
                if (down == 2 && time > 0) //If it's 2nd down and there's time on the clock 
                {
                    System.out.print("...\nIt's 2nd and " + yardsFromFirst); //Print the situation 
                    System.out.println(", " + yardsFromGoal + " yards away from a touchdown.");
                    String playCall = getPlayCall(input, fourthDown); //Prompt for the play call 
                    if (playCall.equals("FIELDGOAL")) //If the user chooses to kick a field goal
                    { 
                        time -= 3;
                        boolean success = fieldGoalSimulator(yardsFromGoal);
                        if (success)
                        {
                            myScore +=3; //Add 3 to the score 
                        }
                        stillOnOffense = false; //End the possession 
                    }
                    if (playCall.equals("PUNT")) //If the user chooses to punt 
                    {
                        time -= (int)(5+Math.random()*4); //Any integer between 5 and 8 
                        System.out.println("You have punted.");
                        stillOnOffense = false; //End the possession 
                    }
                    if (!playCall.equals("FIELDGOAL") && !playCall.equals("PUNT")) //If the user enters a regular play 
                    {
                        int yardage = getYardage(playCall); //Get the yardage 
                        System.out.println("You gained "+ yardage + " yards.");
                        yardsFromFirst -= yardage; 
                        yardsFromGoal -= yardage; 
                        System.out.println("There are " + time + " seconds remaining in the game.");
                    }
                }
                if (down == 3 && time > 0)
                {
                    System.out.print("...\nIt's 3rd and " + yardsFromFirst);
                    System.out.println(", " + yardsFromGoal + " yards away from a touchdown.");
                    String playCall = getPlayCall(input, fourthDown);
                    if (playCall.equals("FIELDGOAL")) //If the user chooses to kick a field goal
                    {
                        time -= 3;
                        boolean success = fieldGoalSimulator(yardsFromGoal);
                        if (success)
                        {
                            myScore +=3;
                        }
                        stillOnOffense = false;
                    }
                    if (playCall.equals("PUNT")) //If the user chooses to punt 
                    {
                        time -= (int)(5+Math.random()*4); //Any integer between 5 and 8 
                        System.out.println("You have punted.");
                        stillOnOffense = false; 
                    }
                    if (!playCall.equals("FIELDGOAL") && !playCall.equals("PUNT"))
                    {
                        int yardage = getYardage(playCall); //Get the yardage 
                        System.out.println("You gained "+ yardage + " yards.");
                        yardsFromFirst -= yardage; 
                        yardsFromGoal -= yardage; 
                        System.out.println("There are " + time + " seconds remaining in the game.");
                    }
                }
                if (down == 4 && time > 0)
                {
                    fourthDown = true; //Indicate that it's 4th down 
                    System.out.print("...\nIt's 4th and " + yardsFromFirst);
                    System.out.println(", " + yardsFromGoal + " yards away from a touchdown.");
                    String playCall = getPlayCall(input, fourthDown); //Get the play call 
                    if (playCall.equals("FIELDGOAL")) //If the user chooses to kick a field goal
                    {
                        time -= 3;
                        boolean success = fieldGoalSimulator(yardsFromGoal);
                        if (success)
                        {
                            myScore +=3;
                        }
                        stillOnOffense = false;
                    }
                    if (playCall.equals("PUNT")) //If the user chooses to punt 
                    {
                        time -= (int)(5+Math.random()*4); //Any integer between 5 and 8 
                        System.out.println("You have punted.");
                        stillOnOffense = false; 
                    }
                    if (!playCall.equals("FIELDGOAL") && !playCall.equals("PUNT"))
                    {
                        int yardage = getYardage(playCall); //Get the yardage 
                        System.out.println("You gained "+ yardage + " yards.");
                        yardsFromFirst -= yardage; 
                        yardsFromGoal -= yardage; 
                        System.out.println("There are " + time + " seconds remaining in the game.");
                    }
                }
                if (yardsFromFirst > 0) //If the user doesn't get a 1st down 
                {
                    down++; 
                }
                if (yardsFromFirst <= 0) //If the user does get a first down  
                {
                    System.out.println("First down!");
                    down = 1;
                    yardsFromFirst = 10; //Reset the down and distance back to 1st and 10 
                }
                if (yardsFromGoal <= 0) //If the user scores a touchdown 
                {
                    System.out.println("Touchdown!");
                    myScore += 7; 
                    stillOnOffense = false; //End the offensive possession 
                }
                if (down == 5) //If the user fails to convert on 4th down
                {
                    System.out.println("You have turned the ball over");
                    stillOnOffense = false; //End the offensive possession 
                }
            }
        return myScore; //Return the amount of points scored on that drive 
    }

    public static String getPlayCall(Scanner input, boolean fourthDown)
    {
        System.out.println("Please enter a play type:");
        System.out.println("A: Inside run\nB: Outside run\nC: Long pass\nD: Short pass\nE: Play action");
        if (fourthDown || time < 30) //Only print the special teams option if its 4th down or the game is nearly over
        {
             System.out.println("F: Special Teams");
        }
        String playCall = input.nextLine(); //Store the user's selected play type 
        double random = Math.random(); 
        if (playCall.equalsIgnoreCase("A")) //If the user selected inside run 
        {
            System.out.println("You selected inside run, here are your options:");
            System.out.println("1: HB Dive\n2: HB Draw\n3: FB Power");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("B")) //If the user selected outside run 
        {
            System.out.println("You selected outside run, here are your options:");
            System.out.println("1: HB Toss\n2: WR Jet Sweep\n3: Outside Zone left");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("C")) //If the user selected long pass 
        {
            System.out.println("You selected long pass, here are your options:");
            System.out.println("1: Hail Mary\n2: Dagger\n3: Smash");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("D") && random < 0.5) //If the user selected short pass
        {
            System.out.println("You selected short pass, here are your options:"); 
            System.out.println("1: WR Screen\n2: 4 Curls\n3: Digs and Slants");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("D") && random >= 0.5) //Other options for short pass 
        {
            System.out.println("You selected short pass, here are your options:");
            System.out.println("1: Mesh\n2: Quick Outs\n3: Corners and Flats");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("E")) //If the user selected play action pass 
        {
            System.out.println("You selected play action, here are your options:");
            System.out.println("1: Bootleg and Crossers\n2: PA Scissors\n3: Fake Toss and 4 Verticals");
            input.nextLine(); //Give the user the illusion that the specific play call matters 
        }
        if (playCall.equalsIgnoreCase("F"))
        {
            System.out.println("You selected special teams, here are your options:");
            System.out.println("1: Field Goal\n2: Punt\n3: QB Kneel");
            int choice = Integer.parseInt(input.nextLine()); //This time the specific play call matters 
            if (choice == 1)
            {
                playCall = "fieldGoal";  
            }
            if (choice == 2)
            {
                playCall = "punt"; 
            }
            if (choice == 3)
            {
                playCall = "kneel"; 
            }
        }
        return playCall.toUpperCase(); //return the letter (and possibly number) that the user selected 
    }

    public static int getYardage(String playCall)
    {
        int yardage = 0; //Default to 0 yards gained 
        if (time > 0) //If there's still time left in the game 
        {
            if (playCall.equals("A")) //Inside run
            {
                yardage = (int)(-5 + Math.random()*16); //Anything between -5 and 10 yards 
                time -= (int)(1 + Math.random()*3); //Anything between 1 and 3 seconds 
            }
            if (playCall.equals("B")) //Outside run
            {
                yardage = (int)(-5 + Math.random()*21); //Anything between -5 and 15 yards 
                time -= (int)(4 + Math.random()*4); //Anything between 4 and 7 seconds 
            }
            if (playCall.equals("C")) //Long pass
            {
                yardage = (int)(-10 + Math.random()*41); //Anything between -10 and 30 yards 
                time -= (int)(4 + Math.random()*5); //Anything between 4 and 8 seconds
            }
            if (playCall.equals("D")) //Short pass 
            {
                yardage = (int)(-5 + Math.random()*21); //Anything between -5 and 15 yards
                time -= (int)(3 + Math.random()*6); //Anything between 3 and 6 seconds 
            }
            if (playCall.equals("E")) //Play action 
            {
                yardage = (int)(-5 + Math.random()*36); //Anything between -5 and 30 yards
                time -= (int)(3 + Math.random()*6); //Anything between 3 and 8 seconds 
            }
            if (!playCall.equals("A") && !playCall.equals("B") && !playCall.equals("C") && !playCall.equals("D") && !playCall.equals("E"))
            {
                System.out.println("You entered an invalid playcall. You have been fired as head coach.");
                employed = false; //Fire the user 
            }
        }
        return yardage; //Return the number of yards gained on that given play 
    }
    public static int getFieldPosition()
    {
        int yardsFromGoal = 75; //Default to the user starting at the 25 yard line (if the defense allowed a score)
        if (lastPossession.equals("punt")) //If the defense forced a punt 
        {
            yardsFromGoal = (int)(60 + Math.random()*21); //Anything between 60 and 80
        }
        if (lastPossession.equals("turnover")) //If the defense forced a turnover 
        {
            yardsFromGoal = (int)(20 + Math.random()*41); //Anything between 20 and 60
        }
        return yardsFromGoal; //Return the yards from goal (this serves as starting field position)
    }

    public static boolean fieldGoalSimulator(int distanceFromGoal)
    {
        boolean success = false; //Default to the field goal being no good 
        double chance = Math.random();
        if (distanceFromGoal > 50) //68+ yard field goal 
        {
            System.out.println("The " + (18+distanceFromGoal) + " yard attempt is...");
            System.out.println("No good!"); 
        }
        if (distanceFromGoal > 30 && distanceFromGoal < 50) //48 to 68 yard field goal
        {
            if (chance > 0.5)
            {
                System.out.println("Field goal attempt is...no good.");
            }
            if (chance <= 0.5)
            {
                System.out.println("Field goal attempt is...good!");
                success = true;
            }
        }
        if (distanceFromGoal <= 30) //18 to 48 yard field goal
        {
            System.out.println("Field goal is...good!");
            success = true; 
        }
        return success;   
    }
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Intro(); //Print the intro message 
        ArrayList<String> allTeams = getAllTeams(); //Call on method, get the ArrayList of all the NFL teams 
        String team = getTeam(input, allTeams); //Call on method, get the user's team 
        if (team.equals("Browns")) //If the user for some reason chooses the Browns, fire them immediately 
        {
            System.out.println("The season is over. The Browns finished 0-8.");
            System.out.println("You have been fired as head coach.");
            employed = false; 
        }
        ArrayList<String> schedule = getSchedule(allTeams, team); //Generate random schedule 
        int wins = 0; 
        int losses = 0; 
        int ties = 0;
        int i = 0; 
        while (wins + losses + ties < 8 && employed) //While the user hasn't played 8 games yet, and hasn't been fired
        {
            i++; //This executes at the beginning of the loop, which is smart by me 
            time = 120; //Reset time back to 120 
            lastPossession = "score"; 
            int myScore = 0; //reset the score back to 0-0 
            int opponentScore = 0;
            System.out.println("\nWeek " + i + " will be against the " + schedule.get(i-1));
            boolean offense = coinToss(input, i); //Determine if the user starts on offense or not 
            while (time > 0)
            {
                if (offense && employed && time > 0) //If the user is on offense, and hasn't been fired yet 
                {
                    System.out.println("You're now on offense.");
                    myScore += offenseSimulator(input);
                    if (time > 0)
                    {
                        System.out.println("There are now " + time + " seconds remaining in the game.");
                    }
                    if (time <= 0)
                    {
                        System.out.println("The game is over.");
                    }
                    System.out.print("The score is " + team + " - " + myScore); 
                    System.out.println(", " + schedule.get(i-1) + " - " + opponentScore);
                    offense = false; //End the offensive possession 
                    
                }
                if (!offense && employed && time > 0) //If the user is on defense, and hasn't been fired yet 
                {
                    opponentScore += defenseSimulator(); //Call on method, simulate the defensive possession 
                    if (time > 0)
                    {
                        System.out.println("There are now " + time + " seconds remaining in the game.");
                    }
                    if (time <= 0)
                    {
                        System.out.println("The game is over.");
                    }
                    System.out.print("The score is " + team + " - " + myScore); 
                    System.out.println(", " + schedule.get(i-1) + " - " + opponentScore);
                    offense = true; //End the defensive possession 
                }
            } //Closes while time loop 

            if (myScore > opponentScore) //If the user outscored their opponent 
            {
                wins++;
            }
            if (myScore < opponentScore) //If the opponent outscored the user 
            {
                losses++;
            }
            if (myScore == opponentScore) //If the user's score matches the opponent's score 
            {
                ties++;
            }
            if (ties == 0) //If the user hasn't tied a game 
            {
                System.out.println("Your record is " + wins + "-" + losses);
            }
            if (ties > 0) //If the user has tied a game 
            {
                System.out.println("Your record is " + wins +"-" + losses + "-" + ties);
            }
            if (losses > 2) //If the user loses more than 2 games, fire them 
            {
                System.out.println("You have been fired as head coach.");
                employed = false; 
                System.out.println("Would you like another chance?");
                input.nextLine();
                System.out.println("You don't get another chance.");
            }
        } //Closes while wins+losses loop 
    
    } //Closes main method 
    
} //Closes public class Main