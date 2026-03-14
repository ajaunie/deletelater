package edu.ncsu.csc216.wolf_grader.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import edu.ncsu.csc216.wolf_grader.model.manager.Assignment;
import edu.ncsu.csc216.wolf_grader.model.submission.Submission;

/** Reads files into assignment data objects. */
public class AssignmentReader {

    public static ArrayList<Assignment> readAssignmentFile(String fileName) {
        Scanner fileReader = null;
        try { fileReader = new Scanner(new FileInputStream(fileName)); } 
        catch (FileNotFoundException e) { throw new IllegalArgumentException("Unable to read file " + fileName); }
        
        String fileContent = "";
        while (fileReader.hasNextLine()) { fileContent += fileReader.nextLine() + "\n"; }
        fileReader.close();
        
        String trimmed = fileContent.trim();
        if (trimmed.isEmpty() || trimmed.charAt(0) != '#') throw new IllegalArgumentException("Unable to read file " + fileName);
        
        ArrayList<Assignment> assignments = new ArrayList<Assignment>();
        Scanner scanner = new Scanner(trimmed);
        scanner.useDelimiter("\\r?\\n#");
        while (scanner.hasNext()) {
            try { assignments.add(processAssignment(scanner.next())); } catch (Exception e) { /* Skip invalid */ }
        }
        scanner.close();
        return assignments;
    }

    private static Assignment processAssignment(String assignmentText) {
        Scanner s = new Scanner(assignmentText);
        String header = s.nextLine();
        if (header.startsWith("#")) header = header.substring(1).trim();
        String[] info = header.split(",");
        if (info.length != 3) { s.close(); throw new IllegalArgumentException(); }
        Assignment a = new Assignment(info[0].trim(), info[1].trim(), Integer.parseInt(info[2].trim()));
        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (line.startsWith("*")) {
                try {
                    String data = line.substring(1).trim();
                    String[] p = data.split(",", -1);
                    if (p.length == 9) {
                        a.addSubmission(new Submission(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(), p[3].trim(), Boolean.parseBoolean(p[4].trim()), p[5].trim(), Boolean.parseBoolean(p[6].trim()), p[7].trim(), p[8].trim()));
                    }
                } catch (Exception e) { /* Skip sub */ }
            }
        }
        s.close();
        return a;
    }
}