package com.jira.cloner;

public class CycleCloner {

    private final JiraService jiraService;
    private final Config config;

    public CycleCloner(JiraService jiraService, Config config) {
        this.jiraService = jiraService;
        this.config = config;
    }

    public void cloneRegressionSuite() throws Exception {
        String sourceVersionId = jiraService.getVersionId(config.getSourceVersion());
        String targetVersionId = jiraService.getVersionId(config.getTargetVersion());
        String newCycleName = config.getCycleName() + " " + config.getSuffix();

        if (config.isDryRun()) {
            System.out.println("[DryRun] Would clone '" + config.getCycleName() +
                    "' from version '" + config.getSourceVersion() +
                    "' to version '" + config.getTargetVersion() + "' as '" + newCycleName + "'");
        } else {
            jiraService.cloneCycle(sourceVersionId, targetVersionId, newCycleName);
        }
    }
}
