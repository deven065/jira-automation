# Jira Cycle Cloner

A **Java command-line tool** to safely and conveniently clone test cycles in Jira, supporting both simulation (dry run) and real cloning. **No technical experience required.**

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting)
- [Support](#support)
- [Screenshots](#screenshots)

---

## Features

- Automatically fetches all Jira versions via REST API
- Dynamically detects the latest released version
- Clones a specified test cycle between versions
- **Dry Run mode:** preview actions without making changes
- Configurable via a simple properties file
- Detailed, easy-to-read logs for verification and troubleshooting

---

## Prerequisites

- Java **11 or higher**  
  _Check version:_

```bash
java -version
```

- Jira account with API access & permissions
- _(Optional)_ GitHub access to clone source

---

## Getting Started

1. **Download**
  - Download the JAR: `jira-cycle-cloner-1.0-SNAPSHOT.jar`
  - _or_ clone the private repository (if you have GitHub access)
2. **Configure**
  - Edit `config.properties` in any text editor _or_ configure directly in `App.java`

---

## Configuration

| Field           | Description                     | Example                             |
|-----------------|---------------------------------|-------------------------------------|
| Jira Base URL   | Your Jira instance URL          | https://yourcompany.atlassian.net   |
| Email           | Jira login email                | yourname@company.com                |
| API Token       | Jira API token                  | ATATT...                            |
| Project Key     | Project key                     | SCRUM                               |
| Source Version  | Version to clone from           | RELEASED                            |
| Target Version  | Version to clone into           | UNRELEASED                          |
| Cycle Name      | Name of the test cycle          | Regression Suite                    |
| Suffix          | (Optional) Suffix for new cycle | R5                                  |
| Dry Run         | true/false for simulation       | true                                |

**Tip:** For first-time users, set `Dry Run = true` to preview actions safely.

---

## Usage

Open a terminal in the JAR directory and run:

```bash
java -jar jira-cycle-cloner-1.0-SNAPSHOT.jar
```

**Dry Run Output Example:**
```
[DryRun] Would clone cycle 'Regression Suite' from source version 'RELEASED' to target version 'UNRELEASED'
```


Set `dryRun=false` in your config to perform actual cloning.

---

## Troubleshooting

- _Version not found_: Ensure the source version exists in Jira
- _Authentication errors_: Check your email and API token credentials
- _Permissions_: Verify your Jira account can view/clone test cycles

---

## Support

For questions or issues, contact:  
**Deven Rikame** – [devenrikame55@gmail.com](mailto:devenrikame55@gmail.com)

---

_Jira Cycle Cloner — Automate, Simulate, and Clone with Confidence._
