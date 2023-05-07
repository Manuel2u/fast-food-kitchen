

import java.io.*;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.List;


/**
 *
 * ITSC 1213
 * University of North Carolina at Charlotte
 */

public class FastFoodKitchen {

    String line = "";
    String splitBy = ",";

    private ArrayList<BurgerOrder> orderList = new ArrayList<BurgerOrder>();
    private ArrayList<BurgerOrder> completedOrderList = new ArrayList<BurgerOrder>();


    private static int nextOrderNum = 1;

    // Get current working directory
    Path currentDir = Paths.get(System.getProperty("user.dir"));

    // Append filename to path
    Path ordersFilePath = currentDir.resolve("src/burgerOrders.csv");

    /**
     * Constructor for Fast Food Kitchen class
     * @throws Exception
     */

    FastFoodKitchen() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(ordersFilePath.toString()));
            String line;
            // Skip the first line (header) of the CSV file
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int orderNum = Integer.parseInt(values[0]);
                int numHamburgers = Integer.parseInt(values[1]);
                int numCheeseburgers = Integer.parseInt(values[2]);
                int numVeggieburgers = Integer.parseInt(values[3]);
                int numSodas = Integer.parseInt(values[4]);
                boolean toGo = Boolean.parseBoolean(values[5]);
                BurgerOrder order = new BurgerOrder(numHamburgers, numCheeseburgers, numVeggieburgers, numSodas, toGo, orderNum);
                orderList.add(order);
                System.out.println(order);
                nextOrderNum = Math.max(nextOrderNum, orderNum + 1);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets Next order Number
     * @return
     */
    public static int getNextOrderNum() {
        return nextOrderNum;
    }

    /**
     * increases next order number
     */
    private void incrementNextOrderNum() {
        nextOrderNum++;
    }

    /**
     *
     * @param ham
     * @param cheese
     * @param veggie
     * @param soda
     * @param toGo
     * @return
     */
    public int addOrder(int ham, int cheese, int veggie, int soda, boolean toGo) {
        int orderNum = getNextOrderNum();
        orderList.add(new BurgerOrder(ham, cheese, veggie, soda, toGo, orderNum));
        incrementNextOrderNum();
        orderCallOut(orderList.get(orderList.size() - 1));
        return orderNum;

    }

    /**
     *
     * @param orderID
     * @return
     */
    public boolean isOrderDone(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                return false;
            }
        }
        return true;
    }

    /**
     * cancels order
     * @param orderID
     * @return
     */
    public boolean cancelOrder(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                orderList.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * gets number of orders pending
     * @return
     */
    public int getNumOrdersPending() {
        return orderList.size();
    }

    /**
     * cancels last order
     * @return
     */
    public boolean cancelLastOrder() {

        if (!orderList.isEmpty()) { // same as  if (orderList.size() > 0)
            orderList.remove(orderList.size() - 1);
            return true;
        }

        return false;
    }

    /**
     * calls out order
     * @param order
     */
    private void orderCallOut(BurgerOrder order) {
        if (order.getNumCheeseburgers() > 0) {
            System.out.println("You have " + order.getNumHamburger() + " hamburgers");
        }
        if (order.getNumCheeseburgers() > 0) {
            System.out.println("You have " + order.getNumCheeseburgers() + " cheeseburgers");
        }
        if (order.getNumVeggieburgers() > 0) {
            System.out.println("You have " + order.getNumVeggieburgers() + " veggieburgers");
        }
        if (order.getNumSodas() > 0) {
            System.out.println("You have " + order.getNumSodas() + " sodas");
        }

    }

    /**
     * completes a specific order
     * @param orderID
     */
    public void completeSpecificOrder(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                System.out.println("Order number " + orderID + " is done!");
                if (orderList.get(i).isOrderToGo()) {
                    orderCallOut(orderList.get(i));
                }
                completedOrderList.add(orderList.get(i)); // add completed order to the completed order list
                orderList.remove(i);
            }
        }

    }

    public void completeNextOrder() {
        int nextOrder = orderList.get(0).getOrderNum();
        completeSpecificOrder(nextOrder);

    }

    // Part 2
    public ArrayList<BurgerOrder> getOrderList() {
        return orderList;
    }

    public int findOrderSeq(int whatWeAreLookingFor) {
        for (int j = 0; j < orderList.size(); j++) {
            if (orderList.get(j).getOrderNum() == whatWeAreLookingFor) {
                return j;
            }
        }
        return -1;
    }

    /**
     * generates end of the day report
     */
    public void generateEndOfDayReport() {
        try {
            // Get current working directory
            Path currentDir = Paths.get(System.getProperty("user.dir"));

            // Create filename for report
            String filename = "endOfDayReport.txt";

            // Append filename to path
            Path reportFilePath = currentDir.resolve(filename);

            // Create FileWriter and BufferedWriter to write to file
            FileWriter fw = new FileWriter(reportFilePath.toString());
            BufferedWriter bw = new BufferedWriter(fw);

            // Write header to file
            bw.write("Fast Food Kitchen End of Day Report\n\n");

            // Write order details to file
            bw.write("Orders:\n\n");
            for (int i = 0; i < orderList.size(); i++) {
                BurgerOrder order = orderList.get(i);
                bw.write("Order " + order.getOrderNum() + " - ");
                bw.write("Not Completed\n");
                bw.write("Hamburgers: " + order.getNumHamburger() + "\n");
                bw.write("Cheeseburgers: " + order.getNumCheeseburgers() + "\n");
                bw.write("Veggieburgers: " + order.getNumVeggieburgers() + "\n");
                bw.write("Sodas: " + order.getNumSodas() + "\n");
                bw.write("To Go: " + order.isOrderToGo() + "\n");
                bw.write("\n");
            }

            // Write completed order details to file
            bw.write("Completed Orders:\n\n");
            for (int i = 0; i < completedOrderList.size(); i++) {
                BurgerOrder order = completedOrderList.get(i);
                bw.write("Order " + order.getOrderNum() + " - ");
                bw.write("Completed\n");
                bw.write("Hamburgers: " + order.getNumHamburger() + "\n");
                bw.write("Cheeseburgers: " + order.getNumCheeseburgers() + "\n");
                bw.write("Veggieburgers: " + order.getNumVeggieburgers() + "\n");
                bw.write("Sodas: " + order.getNumSodas() + "\n");
                bw.write("To Go: " + order.isOrderToGo() + "\n");
                bw.write("\n");
            }

            // Write item totals to file
            bw.write("Item Totals:\n\n");
            int totalHamburgers = 0;
            int totalCheeseburgers = 0;
            int totalVeggieburgers = 0;
            int totalSodas = 0;
            for (int i = 0; i < completedOrderList.size(); i++) {
                BurgerOrder order = completedOrderList.get(i);
                totalHamburgers += order.getNumHamburger();
                totalCheeseburgers += order.getNumCheeseburgers();
                totalVeggieburgers += order.getNumVeggieburgers();
                totalSodas += order.getNumSodas();
            }
            bw.write("Hamburgers: " + totalHamburgers + "\n");
            bw.write("Cheeseburgers: " + totalCheeseburgers + "\n");
            bw.write("Veggieburgers: " + totalVeggieburgers + "\n");
            bw.write("Sodas: " + totalSodas + "\n");
            // Close FileWriter and BufferedWriter
            bw.close();
            fw.close();

            System.out.println("End of day report generated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<BurgerOrder> getCompletedOrders() {
        List<BurgerOrder> completedOrders = new ArrayList<BurgerOrder>();
        for (BurgerOrder order : orderList) {
            if (isOrderDone(order.getOrderNum())) {
                completedOrders.add(order);
            }
        }
        return completedOrders;
    }


    /**
     * generates updated csv file for orders
     */
    public void generateUpdatedCsv() {
        // Write remaining orders to a new CSV file
        try {
            FileWriter writer = new FileWriter("burgerOrders2.csv");
            writer.write("orderID,numHamburgers,numCheeseburgers,numVeggieburgers,numSodas,toGo\n");
            for (BurgerOrder order : orderList) {
                if (!isOrderDone(order.getOrderNum())) {
                    writer.write(order.getOrderNum() + "," + order.getNumHamburger() + "," + order.getNumCheeseburgers()
                            + "," + order.getNumVeggieburgers() + "," + order.getNumSodas() + "," + order.isOrderToGo() + "\n");
                }
            }
            writer.close();
            System.out.println("Orders file updated.");
        } catch (IOException e) {
            System.out.println("Error writing orders file.");
            e.printStackTrace();
        }
    }


//    public int findOrderBin(int whatWeAreLookingFor) {
//        int left = 0;
//        int right = orderList.size() - 1;
//        while (left <= right) {
//            int middle = (left + right) / 2;
//            if (whatWeAreLookingFor < orderList.get(middle).getOrderNum()) {
//                right = middle - 1;
//            } else if (whatWeAreLookingFor > orderList.get(middle).getOrderNum()) {
//                left = middle + 1;
//            } else {
//                return middle;
//            }
//        }
//        return -1;
//    }

    public int findOrderBin(int orderID){
        int left = 0;
        int right = orderList.size()-1;
        while (left <= right){
            int middle = ((left + right)/2);
            if (orderID < orderList.get(middle).getOrderNum()){
                right = middle-1;
            }
            else if(orderID > orderList.get(middle).getOrderNum()){
                left = middle +1;
            }
            else{
                return middle;
            }
        }
        return -1;

    }
    public void selectionSort(){
        for (int i = 0; i< orderList.size()-1; i++){
            int minIndex = i;
            for (int k = i+1; k < orderList.size(); k++){
                if (orderList.get(minIndex).getTotalBurgers() > orderList.get(k).getTotalBurgers()){
                    minIndex = k;
                }
            }
            BurgerOrder temp = orderList.get(i);
            orderList.set(i , orderList.get(minIndex));
            orderList.set(minIndex, temp);
        }
    }

    public void insertionSort() {
        for (int j = 1; j < orderList.size(); j++) {
            BurgerOrder temp = orderList.get(j);
            int possibleIndex = j;
            while (possibleIndex > 0 && temp.getTotalBurgers() < orderList.get(possibleIndex - 1).getTotalBurgers()) {
                orderList.set(possibleIndex, orderList.get(possibleIndex - 1));
                possibleIndex--;
            }
            orderList.set(possibleIndex, temp);
        }
    }

//    public void selectionSort() { //weird method!
//
//        for (int j = 0; j < orderList.size() - 1; j++) {
//            int minIndex = j;
//            for (int k = j + 1; k < orderList.size(); k++) {
//
//                 if (orderList.get(minIndex).getTotalBurgers() > orderList.get(j).getTotalBurgers()){
//                    minIndex = k;
//                }
//            }
//            BurgerOrder temp = orderList.get(j);
//            orderList.set(j, orderList.get(minIndex));
//            orderList.set(minIndex, temp);
//
//        }
//    }

}
