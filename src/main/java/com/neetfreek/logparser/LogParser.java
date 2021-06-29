package com.neetfreek.logparser;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    // Issues and their count contained in two arrays of same size
    private final ArrayList<String> issues = new ArrayList<>();
    private final ArrayList<Integer> issuesCount = new ArrayList<>();
    private final String NO_MATCH = "";
    private final Pattern pattern = Pattern.compile("(INFO|ERROR) [0-9]:");

    // Handler for finding, collecting, sorting, printing issues in passed file based on pattern
    public void getFileIssues(String filepath) {
        File file = new File(Objects.requireNonNull(this.getClass().getResource(filepath)).getFile());

        addFoundIssuesToLists(file);
        sortIssuesByCount(issues, issuesCount);
        printIssues(filepath, sortIssuesByCount(issues, issuesCount), sortIssuesCountByCount(issuesCount));
    }


    private void addFoundIssuesToLists(File file) {
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            int issueCount;
            int issueIndex;
            String issue;
            String line;

            while ((line = input.readLine()) != null) {
                issue = getIssueIfFound(line);
                if (!issue.equals(NO_MATCH)) {
                    issue = getIssueIfFound(line);
                    if (issues.contains(issue)) {
                        // Increment issue count if already found, added
                        issueIndex = issues.indexOf(issue);
                        issueCount = issuesCount.get(issueIndex);
                        issueCount++;
                        issuesCount.set(issueIndex, issueCount);
                    } else {
                        // Add issue, set count to 1 if not already found, added
                        issues.add(issue);
                        issuesCount.add(1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /****************
     Issue matching
     ***************/

    // Return whether an issue match was found
    private String getIssueIfFound(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return NO_MATCH;
        }
    }


    /****************
     Helper methods
     ***************/

    // Sort issues items in descending order based on counts at same index in issuesCount
    private ArrayList<String> sortIssuesByCount(ArrayList<String> issues, ArrayList<Integer> issuesCount) {
        ArrayList<String> issuesSortedByCount = new ArrayList<>();
        int highestIssueCount = 0;
        int highestCountIndex = 0;

        while (issuesSortedByCount.size() != issuesCount.size()) {
            for (Integer issueCount : issuesCount) {
                // If current issue count is highest encountered in this loop and not in sorted list, set as highest
                if (issueCount > highestIssueCount
                        && !issuesSortedByCount.contains(issues.get(issuesCount.indexOf(issueCount)))) {
                    highestIssueCount = issueCount;
                    highestCountIndex = issuesCount.indexOf(issueCount);
                }
            }
            // Add issue corresponding to index of highest count for this loop
            issuesSortedByCount.add(issues.get(highestCountIndex));
            highestIssueCount = 0;
        }

        return issuesSortedByCount;
    }

    // Sort issueCounts in descending order
    private ArrayList<Integer> sortIssuesCountByCount(ArrayList<Integer> issuesCount) {
        issuesCount.sort(Collections.reverseOrder());
        return issuesCount;
    }


    /****************
     Print file issues
     ***************/

    // Print issues and their counts to user
    private void printIssues(String filepath, ArrayList<String> issues, ArrayList<Integer> issuesCount) {
        if (issues.size() > 0) {
            System.out.println("<START> ISSUES FOUND FOR " + filepath + ":");
            for (String issue : issues) {
                System.out.println("Issue: " + issue);
                System.out.println("Count: " + issuesCount.get(issues.indexOf(issue)));
            }
            System.out.println("<END>\n");
        }
    }
}
