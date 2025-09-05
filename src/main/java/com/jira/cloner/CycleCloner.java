package com.jira.cloner;

public class CycleCloner {

    private JiraService jiraService;

    public CycleCloner(JiraService jiraService) {
        this.jiraService = jiraService;
    }

    public void cloneRegressionSuite(String sourceCycleId, String newCycleName) {
        try {
            System.out.println("Cloning cycle: " + sourceCycleId + " → " + newCycleName);
            String response = jiraService.cloneCycle(sourceCycleId, newCycleName);
            System.out.println("Clone successful! Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
