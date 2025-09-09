package com.jira.cloner;

public class ConfigSquad {
    public static final String BASE_URL = "https://your-domain.atlassian.net";
    public static final String EMAIL = "your-email@example.com";
    public static final String API_TOKEN = "YOUR_JIRA_API_TOKEN";

    public static final int PROJECT_ID = 10000;
    public static final int VERSION_ID = -1; // -1 = Unscheduled
    public static final int CLONED_CYCLE_ID = 12345; // existing cycle ID
    public static final String NEW_CYCLE_NAME = "Automated Regression Clone (Squad)";
}
