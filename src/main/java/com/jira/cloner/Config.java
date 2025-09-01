package com.jira.cloner;

public class Config {
    private final String baseUrl;
    private final String email;
    private final String apiToken;
    private final String projectKey;
    private final String sourceVersion;
    private final String targetVersion;
    private final String cycleName;
    private final String suffix;
    private final boolean dryRun;

    public Config(String baseUrl, String email, String apiToken, String projectKey,
                  String sourceVersion, String targetVersion, String cycleName,
                  String suffix, boolean dryRun) {
        this.baseUrl = baseUrl;
        this.email = email;
        this.apiToken = apiToken;
        this.projectKey = projectKey;
        this.sourceVersion = sourceVersion;
        this.targetVersion = targetVersion;
        this.cycleName = cycleName;
        this.suffix = suffix;
        this.dryRun = dryRun;
    }

    public String getBaseUrl() { return baseUrl; }
    public String getEmail() { return email; }
    public String getApiToken() { return apiToken; }
    public String getProjectKey() { return projectKey; }
    public String getSourceVersion() { return sourceVersion; }
    public String getTargetVersion() { return targetVersion; }
    public String getCycleName() { return cycleName; }
    public String getSuffix() { return suffix; }
    public boolean isDryRun() { return dryRun; }
}
