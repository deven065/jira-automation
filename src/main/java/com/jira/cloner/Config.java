package com.jira.cloner;

public class Config {
    // Jira Cloud
    public static final String BASE_URL = "https://yourdomain.atlassian.net"; // replace with your Jira site
    public static final String JIRA_EMAIL = "your-email@example.com";         // your Atlassian email
    public static final String JIRA_API_TOKEN = "your-jira-api-token";        // Jira API token

    // Project info
    public static final String PROJECT_KEY = "SCRUM"; // replace with your project key
    public static final String PROJECT_ID = "10600";  // replace with your project ID
    public static final String VERSION_ID = "-1";     // -1 for "Unscheduled", or real versionId

    // Zephyr Essential
    public static final String ZEPHYR_ACCESS_KEY = "your-zephyr-access-key";
    public static final String ZEPHYR_SECRET_KEY = "your-zephyr-secret-key";
    public static final String ACCOUNT_ID = "your-account-id";
}
