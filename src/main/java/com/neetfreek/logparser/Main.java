package com.neetfreek.logparser;

public class Main {

    public static void main(String[] args) {
        LogParser logParser = new LogParser();
        logParser.getFileIssues("/logsfile.txt");
    }
}
