package com.jira.cloner;

public class App {
    public static void main(String[] args) {
        System.out.println("🚀 Zephyr Cycle Cloner");

        // 🔹 Change "scale" to "squad" depending on which plugin you are using
        String mode = "scale"; // or "squad"

        if ("squad".equalsIgnoreCase(mode)) {
            ZephyrSquadClient client = new ZephyrSquadClient();
            if (client.cloneCycle()) {
                System.out.println("✅ Squad cycle cloned successfully.");
            } else {
                System.out.println("❌ Squad cycle clone failed.");
            }
        } else {
            ZephyrScaleClient client = new ZephyrScaleClient();
            if (client.cloneCycle()) {
                System.out.println("✅ Scale cycle cloned successfully.");
            } else {
                System.out.println("❌ Scale cycle clone failed.");
            }
        }
    }
}
