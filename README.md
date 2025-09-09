# 🧩 Jira Zephyr Regression Cycle Cloner

This utility automates **regression cycle cloning** in Jira using **Zephyr APIs**.  
It reduces manual cloning time from **4–5 hours to just a few minutes** and ensures accuracy by eliminating human error.

---

## 🚀 Features
- 🔄 Clone existing **regression test cycles** in Jira automatically.
- ⚡ Reduce manual effort & errors.
- 🛠️ Supports **both Zephyr Squad** (ZAPI) and **Zephyr Scale** (SmartBear).
- 📦 Delivered as an **executable JAR** (can be wrapped into `.exe` if required).
- ✅ One-click execution after configuration.

---

## 📂 Project Structure
```bash
jira-automation/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── jira/
                    └── cloner/
                        ├── App.java
                        ├── ConfigSquad.java
                        ├── ConfigScale.java
                        ├── ZephyrSquadClient.java
                        └── ZephyrScaleClient.java
```
---

## ⚙️ Prerequisites
- Java **17+**
- Maven **3.8+**
- Jira with **Zephyr Squad** or **Zephyr Scale** installed
- API credentials:
  - **Zephyr Squad** → Jira Cloud **email** + **API token**
  - **Zephyr Scale** → **Bearer token** (from SmartBear → Zephyr Scale settings)

---

## 🔑 Configuration

###  If using **Zephyr Squad**
Edit `ConfigSquad.java`:
```java
public class ConfigSquad {
    public static final String BASE_URL = "https://your-domain.atlassian.net";
    public static final String EMAIL = "your-email@example.com";
    public static final String API_TOKEN = "YOUR_JIRA_API_TOKEN";

    public static final int PROJECT_ID = 10000;
    public static final int VERSION_ID = -1; // -1 = Unscheduled
    public static final int CLONED_CYCLE_ID = 12345; // existing cycle ID
    public static final String NEW_CYCLE_NAME = "Automated Regression Clone (Squad)";
}
```

### If using **Zephyr Scale**
Edit `ConfigScale.java`:
```java
public class ConfigScale {
    public static final String BASE_URL = "https://api.zephyrscale.smartbear.com/v2";
    public static final String BEARER_TOKEN = "YOUR_SCALE_BEARER_TOKEN";

    public static final String PROJECT_KEY = "SCRUM";
    public static final String CLONED_CYCLE_ID = "12345"; // UUID of cycle
    public static final String NEW_CYCLE_NAME = "Automated Regression Clone (Scale)";
}
```

### Select the mode
In App.java:
```java
String mode = "squad"; // or "scale"
```

##▶️ Running the Tool
1. Build the jar
```java
mvn clean package
```

2. Run the executable
```java
java -jar target/jira-automation-1.0-SNAPSHOT.jar
```

3. ✅ Success message:
```java
✅ Success message:
```

## 🔍 Troubleshooting
- 401 Unauthorized

Wrong email, API token, or bearer token.

- Ensure you have permission to access Zephyr APIs.

404 Not Found

- Wrong endpoint or cycle ID.

Check whether the client uses Squad or Scale.

- UnresolvedAddressException

Incorrect BASE_URL.

Use https://<yourdomain>.atlassian.net (Squad)
or https://api.zephyrscale.smartbear.com/v2 (Scale).

## 📌 Notes

- Only one of the two modes (squad or scale) should be active at a time.

- This is a Proof of Concept (POC) ready for scaling into a one-click .exe utility.

- Logs print the status code and API response for debugging.

## Support

For setup or questions, please contact your developer.
He will understand and provide you the confidential details, and will handle the setup if you follow the instructions provided in this README.
