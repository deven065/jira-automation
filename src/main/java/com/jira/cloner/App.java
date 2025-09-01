package com.jira.cloner;

public class App {
    public static void main(String[] args) {
        try {
            Config config = new Config(
                    "https://devenrikame.atlassian.net",
                    "devenrikame55@gmail.com",
                    "ATATT3xFfGF0hrbAT1EyBFzFH-1To7Eyh3HMR7wO_dXG_DxOnQigdFg-BKlvyrRB_2qWyVjpjaTXCucBcCDM5hvWhtEPcWLsxdSLBxyblg-mi-AkWDBYR3JH83TyveV0iEnhkxY2vxjPv6q5zbbc08RfDpxStG6d_oRXHWXOphdLH6k4jbLAkWw=3234ED8A",
                    "SCRUM",
                    "Release 1",       // source version
                    "Release 2",       // target version
                    "Regression Suite",
                    "R5",
                    true              // set false to actually clone
            );

            JiraService jiraService = new JiraService(
                    config.getBaseUrl(),
                    config.getEmail(),
                    config.getApiToken(),
                    config.getProjectKey()
            );

            CycleCloner cloner = new CycleCloner(jiraService, config);
            cloner.cloneRegressionSuite();

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
