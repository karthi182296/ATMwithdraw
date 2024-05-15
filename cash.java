package testdata;

import java.util.HashMap;
import java.util.Map;

class ATM {
    private Map<Integer, Integer> notesInventory;

    public ATM() {
        // Initialize ATM with some default denominations and quantities
        notesInventory = new HashMap<>();
        notesInventory.put(10, 1);
        notesInventory.put(20, 1);
        notesInventory.put(50, 1);
        // Add more denominations as needed
    }

    public synchronized boolean dispenseCash(int amount) {
        // Ensure the amount is a multiple of 10
        if (amount % 10 != 0) {
            System.out.println("Amount must be a multiple of 10.");
            return false;
        }

        // Calculate the number of notes to dispense
        Map<Integer, Integer> tempInventory = new HashMap<>(notesInventory);
        Map<Integer, Integer> withdrawnNotes = new HashMap<>();
        int remainingAmount = amount;
        for (int denomination : notesInventory.keySet()) {
            int notesRequired = remainingAmount / denomination;
            if (notesRequired > 0 && tempInventory.containsKey(denomination) && tempInventory.get(denomination) >= notesRequired) {
                remainingAmount -= notesRequired * denomination;
                tempInventory.put(denomination, tempInventory.get(denomination) - notesRequired);
                withdrawnNotes.put(denomination, notesRequired);
            }
        }

        // Check if exact amount is dispensed
        if (remainingAmount == 0) {
            notesInventory = tempInventory;
            System.out.println("Dispensed: " + withdrawnNotes);
            return true;
        } else {
            System.out.println("Insufficient funds.");
            return false;
        }
    }

    public synchronized void updateInventory(int denomination, int count) {
        notesInventory.put(denomination, notesInventory.getOrDefault(denomination, 0) + count);
    }

    public Map<Integer, Integer> getNotesInventory() {
        return notesInventory;
    }
}

class Customer {
    private int accountId;
    private double balance;

    public Customer(int accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public boolean withdrawCash(ATM atm, int amount) {
        if (balance >= amount) {
            if (atm.dispenseCash(amount)) {
                balance -= amount;
                System.out.println("Withdrawn $" + amount + " from account " + accountId);
                return true;
            } else {
                System.out.println("Failed to withdraw cash from ATM.");
            }
        } else {
            System.out.println("Insufficient balance in account " + accountId);
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }
}

public class cash {
    public static void main(String[] args) {
        ATM atm = new ATM();
        Customer customer1 = new Customer(12345, 50);
        Customer customer2 = new Customer(67890, 1000);

        // Withdraw cash from ATM and update customer's account balance
        customer1.withdrawCash(atm, 70);
        customer2.withdrawCash(atm, 150);
    }
}
