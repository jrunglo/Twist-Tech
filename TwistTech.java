/**
 *
 * @author Jaru  Roces
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Calendar;
import java.util.TimeZone;

public class RubiksCube {
    private static final String[] MOVES = {"F", "R", "U", "L", "B", "D"};
    private static final String[] PRIMITIVES = {"", "'", "2"};
    private String scramble;
    private double time;
    private String name;
    private String brand;
    private int counter=0;
    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Scramble Generator");
    while (true) {
        System.out.println("\n1. Start solving");
        System.out.println("2. View records");
        System.out.println("3. Search record");
        System.out.println("4. Delete all records");
        System.out.println("5. View ranking");
        System.out.println("6. Exit program");
        System.out.print("\nEnter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // to clear the buffer
        if (choice == 1) {
            // existing code for generating a scramble, timing the solving, and allowing the user to save their record
            System.out.print("User Name: ");
            String name = sc.nextLine();
            System.out.print("Rubik's Cube Brand & Type : ");
            String brand = sc.nextLine();
            while (true) {
                RubiksCube cube = new RubiksCube();
                cube.name = name;
                cube.brand = brand;
                StringBuilder sb = new StringBuilder();
                System.out.println("\nChoose averaging method :");
                System.out.print("Ao5 or Ao12 : ");
                String method = sc.nextLine();
                if (method.equalsIgnoreCase("Ao5")) {
                    sb.append(cube.ao5());
                } else if (method.equalsIgnoreCase("Ao12")) {
                    sb.append(cube.ao12());
                } else {
                    System.out.println("Invalid Method");
                }
                System.out.print("\nDo you want to save record ? (y/n) : ");
                String saveChoice = sc.nextLine();
                if (saveChoice.equalsIgnoreCase("y")) {
                    cube.storeRecord(sb.toString());
                }
                System.out.print("\nDo you want solve again ? (y/n) : ");
            String repeat = sc.nextLine();
            if (!repeat.equalsIgnoreCase("y")) {
                break;
            }
        }
    } else if (choice == 2) {
        try {
            File file = new File("record.txt");
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("record.txt doesn't exist, creating new file");
            }
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println(line);
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error reading record.txt file");
        }
    } else if (choice == 3) {
try {
System.out.print("Enter name to search : ");
String searchName = sc.nextLine();
System.out.print("\n");
File file = new File("record.txt");
if (!file.exists()) {
System.out.println("record.txt doesn't exist, no records to search.");
continue;
}
Scanner fileScanner = new Scanner(file);
boolean found = false;
while (fileScanner.hasNextLine()) {
String line = fileScanner.nextLine();
if(line.contains(searchName)) {
found = true;
System.out.println(line);
while (!line.equals("**********************")) {
line = fileScanner.nextLine();
System.out.println(line);
}
}
}
fileScanner.close();
if (!found) {
System.out.println("No record found for user: " + searchName);
}
} catch (IOException e) {
System.out.println("Error reading record.txt file");
}
} else if (choice == 4) {
        try {
            File file = new File("record.txt");
            if (file.exists()) {
                file.delete();
                System.out.println("All records deleted.");
            } else {
                System.out.println("record.txt doesn't exist, no records to delete.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting record.txt file");
        }
    } else if (choice == 5) {
    try {
        File file = new File("record.txt");
        if (!file.exists()) {
            System.out.println("record.txt doesn't exist, no records to show.");
            return;
        }
        Scanner fileScanner = new Scanner(file);
        List<String> records = new ArrayList<>();
        String name = "";
        String ao5 = "";
        String line;
        while (fileScanner.hasNextLine()) {
            line = fileScanner.nextLine();
            if(line.contains("User Name: ")) {
                name = line.substring(line.indexOf("User Name: ") + 11);
            }
            if(line.contains("Ao5: ")) {
                ao5 = line.substring(line.indexOf("Ao5: ") + 5);
                records.add(name + " : " + ao5);
            }
        }
        Collections.sort(records, (a, b) -> {
            double aTime = Double.parseDouble(a.split(":")[1]);
            double bTime = Double.parseDouble(b.split(":")[1]);
            return Double.compare(aTime, bTime);
        });
        System.out.println("\nRankings | Ao5 \n");
        System.out.println("#  Name             Ao5");
        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            System.out.println(String.format("%d. %-15s %s",(i + 1), records.get(i).split(":")[0], records.get(i).split(":")[1]));
        }
        fileScanner.close();
    } catch (IOException e) {
        System.out.println("Error reading record.txt file");
    }
}else if (choice == 6) {
System.out.println("Exiting program...");
break;
} else {
System.out.println("Invalid choice. Please enter a valid number.");
}
}
}
public String ao5() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Name: " + name + "\n");
        sb.append("Brand & Type: " + brand + "\n");
        List < Double > times = new ArrayList < > ();
        String lastMove = " ";
        for (int i = 0; i < 5; i++) {
            generateScramble(lastMove);
            counter++;
            System.out.println("\nScramble " + counter + ": " + scramble);
            sb.append("\nScramble " + counter + ": " + scramble + "\n");    
            System.out.print("Press enter to start/stop the timer : ");
            new Scanner(System.in).nextLine();
            long startTime = System.nanoTime();
            System.out.print("Solving... ");
            new Scanner(System.in).nextLine();
            time = (System.nanoTime() - startTime) / 1_000_000_000.0;
            times.add(time);
DecimalFormat df = new DecimalFormat("0.00");
String formattedTime = df.format(time);
sb.append("Time: " + formattedTime + "s" + "\n");
System.out.println("Time: " + df.format(time) + "s");
        }
        Collections.sort(times);
        times.remove(0);
        times.remove(3);
        double total = 0;
        for (double i: times) {
            total += i;
        }
        double ao5 = total / 3.0;
        DecimalFormat df = new DecimalFormat("0.00");
        sb.append("\nAo5: " + df.format(ao5) + "\n");
        System.out.println("\nAo5: " + df.format(ao5));
        return sb.toString();
    }
public String ao12() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Name : " + name + "\n");
        sb.append("Brand & Type : " + brand + "\n");
        List < Double > times = new ArrayList < > ();
        String lastMove = " ";
        for (int i = 0; i < 12; i++) {
            generateScramble(lastMove);
            counter++;
            System.out.println("\nScramble " + counter + ": " + scramble); 
            sb.append("\nScramble " + counter + ": " +  scramble + "\n");
             System.out.print("Press enter to start/stop the timer : ");
            new Scanner(System.in).nextLine();
            long startTime = System.nanoTime();
            System.out.print("Solving... ");
            new Scanner(System.in).nextLine();
            time = (System.nanoTime() - startTime) / 1_000_000_000.0;
            times.add(time);
DecimalFormat df = new DecimalFormat("0.00");
String formattedTime = df.format(time);
sb.append("Time: " + formattedTime + "s" + "\n");
System.out.println("Time: " + df.format(time) + "s");

        }
        Collections.sort(times);
        times.remove(0);
        times.remove(times.size() - 1);
        double total = 0;
        for (double i: times) {
            total += i;
        }
        double ao12 = total / 10.0;
        DecimalFormat df = new DecimalFormat("0.00");
        sb.append("\nAo12: " + df.format(ao12) + "\n");
        System.out.println("\nAo12: " + df.format(ao12));
        return sb.toString();
    }

private void generateScramble(String lastMove) {
        scramble = "";
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            String move;
            do {
                move = MOVES[rand.nextInt(6)];
            } while (move.equals(lastMove));
            lastMove = move;
            scramble += move + PRIMITIVES[rand.nextInt(3)] + " ";
        }
    }

private void storeRecord(String record) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        dateFormat.setCalendar(calendar);
        String currentDateTime = dateFormat.format(calendar.getTime());
        record = currentDateTime + "\n" + record + "\n**********************\n";
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter("record.txt", true));
            w.write(record);
            w.newLine();
            w.close();
            System.out.println("Record saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}