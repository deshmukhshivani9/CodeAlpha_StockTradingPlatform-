import java.util.*;
import java.io.*;

// Stock class
class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

// Transaction class
class Transaction {
    String type;
    String symbol;
    int qty;
    double price;

    Transaction(String type, String symbol, int qty, double price) {
        this.type = type;
        this.symbol = symbol;
        this.qty = qty;
        this.price = price;
    }

    public String toString() {
        return type + " | " + symbol + " | Qty: " + qty + " | ₹" + price;
    }
}

// User class
class User {
    double balance = 10000;
    HashMap<String, Integer> portfolio = new HashMap<>();
    ArrayList<Transaction> history = new ArrayList<>();

    void buy(Stock s, int qty) {
        double cost = s.price * qty;

        if (cost > balance) {
            System.out.println("❌ Insufficient balance!");
            return;
        }

        balance -= cost;
        portfolio.put(s.symbol, portfolio.getOrDefault(s.symbol, 0) + qty);
        history.add(new Transaction("BUY", s.symbol, qty, s.price));

        System.out.println("✅ Bought " + qty + " shares of " + s.symbol);
    }

    void sell(Stock s, int qty) {
        int owned = portfolio.getOrDefault(s.symbol, 0);

        if (owned < qty) {
            System.out.println("❌ Not enough stocks!");
            return;
        }

        balance += s.price * qty;
        portfolio.put(s.symbol, owned - qty);
        history.add(new Transaction("SELL", s.symbol, qty, s.price));

        System.out.println("✅ Sold " + qty + " shares of " + s.symbol);
    }

    void showPortfolio(HashMap<String, Stock> market) {
        System.out.println("\n===== PORTFOLIO =====");

        double total = balance;

        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            System.out.printf("%-10s %-10s %-10s\n", "Stock", "Qty", "Value");

            for (String s : portfolio.keySet()) {
                int qty = portfolio.get(s);
                double value = qty * market.get(s).price;
                total += value;

                System.out.printf("%-10s %-10d ₹%-10.2f\n", s, qty, value);
            }
        }

        System.out.println("----------------------------");
        System.out.println("Balance: ₹" + balance);
        System.out.println("Total Value: ₹" + total);
    }

    void showHistory() {
        System.out.println("\n===== TRANSACTIONS =====");

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction t : history) {
            System.out.println(t);
        }
    }
}

// Main class
public class StockTradingPlatform {

    static HashMap<String, Stock> market = new HashMap<>();
    static User user = new User();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadMarket();

        int choice;

        do {
            System.out.println("\n==== STOCK TRADING PLATFORM ====");
            System.out.println("1. View Market");
            System.out.println("2. Buy");
            System.out.println("3. Sell");
            System.out.println("4. Portfolio");
            System.out.println("5. History");
            System.out.println("6. Exit");
            System.out.print("Choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    showMarket();
                    break;

                case 2:
                    trade(true);
                    break;

                case 3:
                    trade(false);
                    break;

                case 4:
                    user.showPortfolio(market);
                    break;

                case 5:
                    user.showHistory();
                    break;

                case 6:
                    saveToFile();
                    System.out.println("Saved. Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    static void loadMarket() {
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("TSLA", new Stock("TSLA", 700));
        market.put("GOOG", new Stock("GOOG", 2800));
    }

    static void showMarket() {
        System.out.println("\nStock   Price");
        for (Stock s : market.values()) {
            System.out.println(s.symbol + "   ₹" + s.price);
        }
    }

    static void trade(boolean isBuy) {
        System.out.print("Enter stock: ");
        String sym = sc.next().toUpperCase();

        if (!market.containsKey(sym)) {
            System.out.println("Stock not found!");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();

        if (isBuy)
            user.buy(market.get(sym), qty);
        else
            user.sell(market.get(sym), qty);
    }

    static void saveToFile() {
        try {
            FileWriter fw = new FileWriter("portfolio.txt");
            fw.write("Balance: " + user.balance + "\n");

            for (String s : user.portfolio.keySet()) {
                fw.write(s + " " + user.portfolio.get(s) + "\n");
            }

            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving file!");
        }
    }
}
