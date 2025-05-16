package com.mycompany.kvk;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMatcher {
    
    static class Scenario {
        int number;
        String weather;
        String price;
        String load;
        String batteryState;
        String reasoning;

        public Scenario(int number, String weather, String price, String load, 
                       String batteryState, String reasoning) {
            this.number = number;
            this.weather = weather;
            this.price = price;
            this.load = load;
            this.batteryState = batteryState;
            this.reasoning = reasoning;
        }
    }
    
    static ArrayList<Scenario> scenarios = new ArrayList<>();
    
    public void fillTheScenarios (ArrayList <Scenario> scenarios) {
        scenarios.add(new Scenario(1, "Good", "Low", "Low", "Charge",
                "Plenty of solar generation (SE-90 kW), low load, and cheap electricity - perfect time to charge battery."));
        scenarios.add(new Scenario(2, "Good", "High", "High", "Discharge", 
            "High load, electricity is expensive - use battery to reduce grid purchase."));
        scenarios.add(new Scenario(3, "Good", "High", "Low", "Charge", 
            "Price is high but load is low - solar is strong, so charge battery to use or sell later."));
        scenarios.add(new Scenario(4, "Good", "Low", "High", "Discharge", 
            "Solar helps, but load is high - battery supports load while energy is cheap."));
        scenarios.add(new Scenario(5, "Bad", "High", "High", "Discharge", 
            "Poor solar production, high price, high load - battery discharges to reduce cost."));
        scenarios.add(new Scenario(6, "Bad", "Low", "High", "Inactive", 
            "Energy is cheap, so no need to use battery. Battery rests."));
        scenarios.add(new Scenario(7, "Bad", "High", "Low", "Inactive", 
            "Price is high but load is low and solar is weak - avoid draining battery unnecessarily."));
        scenarios.add(new Scenario(8, "Bad", "Low", "Low", "Charge", 
            "Low cost and low load - even in poor weather, charge slowly from grid if needed."));
    }

    
    
        
        
   

    public static Scenario findMatchingScenario(String weather, String price, String load, ArrayList <Scenario> scenarios) {
        for (Scenario s : scenarios) {
            if (s.weather.equalsIgnoreCase(weather.trim()) && 
               s.price.equalsIgnoreCase(price.trim()) && 
               s.load.equalsIgnoreCase(load.trim())) {
                return s;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Test cases to verify all scenarios
        System.out.println("=== Running Test Cases ===");
        
        // Test case 1: Should match scenario 1
        testScenario("Good", "Low", "Low", 1);
        
        // Test case 2: Should match scenario 2
        testScenario("Good", "High", "High", 2);
        
        // Test case 3: Should match scenario 3
        testScenario("Good", "High", "Low", 3);
        
        // Test case 4: Should match scenario 4
        testScenario("Good", "Low", "High", 4);
        
        // Test case 5: Should match scenario 5
        testScenario("Bad", "High", "High", 5);
        
        // Test case 6: Should match scenario 6
        testScenario("Bad", "Low", "High", 6);
        
        // Test case 7: Should match scenario 7
        testScenario("Bad", "High", "Low", 7);
        
        // Test case 8: Should match scenario 8
        testScenario("Bad", "Low", "Low", 8);
        
        // Test case 9: No matching scenario (invalid inputs)
        testScenario("Rainy", "Medium", "Medium", -1);
    }

    private static void testScenario(String weather, String price, String load, int expectedScenarioNumber) {
        System.out.println("\nTesting - Weather: " + weather + ", Price: " + price + ", Load: " + load);
        
        Scenario match = findMatchingScenario(weather, price, load, scenarios);

        if (match != null) {
            System.out.println("Selected Scenario #" + match.number);
            System.out.println("Weather      : " + match.weather);
            System.out.println("Price        : " + match.price);
            System.out.println("Load         : " + match.load);
            System.out.println("Battery State: " + match.batteryState);
            System.out.println("Reasoning    : " + match.reasoning);
            
            if (match.number == expectedScenarioNumber) {
                System.out.println("✅ Test PASSED - Correct scenario matched");
            } else {
                System.out.println("❌ Test FAILED - Expected scenario " + expectedScenarioNumber + 
                                   " but got scenario " + match.number);
            }
        } else {
            if (expectedScenarioNumber == -1) {
                System.out.println("✅ Test PASSED - Correctly found no matching scenario");
            } else {
                System.out.println("❌ Test FAILED - Expected scenario " + expectedScenarioNumber + 
                                   " but found no match");
            }
        }
    }
}