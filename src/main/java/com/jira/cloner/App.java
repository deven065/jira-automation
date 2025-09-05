package com.jira.cloner;

public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar jira-cycle-cloner.jar <sourceCycleId> <newCycleName>");
            return;
        }

        String sourceCycleId = args[0];
        String newCycleName = args[1];

        JiraService jiraService = new JiraService();
        CycleCloner cloner = new CycleCloner(jiraService);
        cloner.cloneRegressionSuite(sourceCycleId, newCycleName);
    }
}
