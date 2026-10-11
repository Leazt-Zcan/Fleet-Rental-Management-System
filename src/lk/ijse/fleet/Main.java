package lk.ijse.fleet;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static FleetManager fleetManager = new FleetManager();
    public static UserManager userManager = new UserManager();
    public static Scanner scanner = new Scanner(System.in);
    public static SystemUser loggedInUser = null;
    
    
    public static void main(String[] args) { 
        run();
    }
    
    public static void run(){
        fleetManager.setRentalEventListener(agreement -> System.out.println("[EVENT] New rental created: " + agreement.getAgreementId()));
        
        Thread statusThread = new Thread(new VehicleStatusMonitor(fleetManager));
        statusThread.setDaemon(true);
        statusThread.start();
        
        Thread overdueThread = new Thread(new OverdueReminderWorker(fleetManager));
        overdueThread.setDaemon(true);
        overdueThread.start();
        
        showAuthMenu();
    }
    
    public static void showAuthMenu(){
        
        while(loggedInUser == null){
            System.out.println("========================================================");
            System.out.println("FLEET RENTAL MANAGEMENT SYSTEM (FRMS)");
            System.out.println("========================================================");
            System.out.println("[1] Login");
            System.out.println("[2] Register New User");
            System.out.println("[0] Exit");
            System.out.print("Enter choice > ");
            String choice = scanner.nextLine();

            switch(choice){
                case "1":
                    login();
                    break;
                
                case "2":
                    registerUser();
                    break;
                
                case "0":
                    System.exit(0);
                
                default:
                    System.out.println("Invalid choice!\n");
                    
            }
        }
    }
    
    public static void showMainMenu(){
        while(true){
            System.out.println("========================================================");
            System.out.println("FRMS MAIN MENU | Logged in as: " + loggedInUser.getFullName());
            
            System.out.println("========================================================");
            System.out.println("[1] Add Vehicle");
            System.out.println("[2] Add Customer");
            System.out.println("[3] View Avalable Vehicles");
            System.out.println("[4] Create Rental Agreement");
            System.out.println("[5] Return Vehicle");
            System.out.println("[6] View Revenue Report");
            System.out.println("[7] View Vehicles by Fuel Type");
            System.out.println("[8] View Combined Fleet & Customer Report");
            System.out.println("[9] View All Customers");
            System.out.println("[10] View All Rental Agreements");
            System.out.println("[0] Logout");
            System.out.print("Enter choice > ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addVehicle();
                case "2" -> addCustomer();
                case "3" -> viewAvailableVehicles();
                case "4" -> createAgreement();
                case "5" -> returnVehicle();
                case "6" -> viewRevenueReport();
                case "7" -> viewVehicleByFuelType();
                case "8" -> combineFleetCustomerReport();
                case "9" -> viewAllCustomers();
                case "10" -> viewAllRentalAgreements();
                case "0" -> {
                    loggedInUser = null;
                    showAuthMenu();
                }
                
                default -> System.out.println("Invalid choice!\n");
            }
        }        
    }
    
    
    public static void registerUser(){
        System.out.println("================================================================");
        System.out.println("REGISTER NEW USER");
        System.out.println("================================================================");

        System.out.print("Enter Full Name > ");
        String regName = scanner.nextLine();
        System.out.print("Enter Username > ");
        String regUser = scanner.nextLine();
        System.out.print("Enter Password > ");
        String regPass = scanner.nextLine();

        SystemUser user = userManager.registerUser(regUser, regPass, regName);
        userManager.setUsers(user);
    }
    
    public static void login(){
        System.out.println("================================================================");
        System.out.println("LOGIN");
        System.out.println("================================================================");

        System.out.print("Enter Username > ");
        String inputUser = scanner.nextLine();
        System.out.print("Enter Password > ");
        String inputPass = scanner.nextLine();

        loggedInUser = userManager.login(inputUser, inputPass);

        if (loggedInUser != null) {
            System.out.println("Welcome, " + loggedInUser.getFullName() +"! Loading fleet dashboard...");
            showMainMenu();
        } else {
            System.out.println("Invalid username or password\n");
        }
    }
    
    public static void viewAvailableVehicles(){
        System.out.println("================================================================");
        System.out.println("AVAILABLE VEHICLES");
        System.out.println("================================================================");

        
        ArrayList<Vehicle> availableVehicles = fleetManager.getAvailableVehicles();
        
        if (availableVehicles == null) {
            System.out.println("No available vehicles!\n");
        }
        
        displayVehicleListInfo(availableVehicles);
    }
    
    public static void viewRevenueReport(){
        System.out.println("""
                           ================================================================
                           FLEET REVENUE REPORT
                           ================================================================
                           Total Revenue: """ + fleetManager.generateRevenueReport() + " \n"
                + "================================================================");

    }
    
    public static void combineFleetCustomerReport(){
        System.out.println("================================================================");
        System.out.println("Combined Fleet & Customer Report");
        System.out.println("================================================================");
        fleetManager.printAllReportables();
    }
    
    public static void viewAllRentalAgreements(){
        System.out.println("================================================================");
        System.out.println("RENTAL AGREEMENTS");
        System.out.println("================================================================");
        
        ArrayList<RentalAgreement> printAgreements = fleetManager.getAllAgreements();
        if (printAgreements == null) {
            System.out.println("No rental agreements found!\n");
        } else {
            for (RentalAgreement printAgreement : printAgreements) {
                System.out.println(printAgreement.getAgreementId() + printAgreement.getCustomer().getCustomerId() + printAgreement.getVehicle().getVehicleId() + printAgreement.getTotalCost() + printAgreement.getDaysElapsed() + "/" + printAgreement.getRentalDays());
            }
        }
    }
    
    public static void viewAllCustomers(){
        System.out.println("================================================================");
        System.out.println("CUSTOMERS");
        System.out.println("================================================================");
        
        ArrayList<Customer> printCustomers = fleetManager.getAllCustomers();
        if (printCustomers == null) {
            System.out.println("No customers found!\n");
        } else {
            for (Customer printCustomer : printCustomers) {
                if (printCustomer instanceof IndividualCustomer) {
                    System.out.println(printCustomer.getCustomerId() + " - " + printCustomer.getFullName() + " - " + printCustomer.getContactNumber() + " (Individual)");
                } else if (printCustomer instanceof CorporateCustomer) {
                    System.out.println(printCustomer.getCustomerId() + " - " + printCustomer.getFullName() + " - " + printCustomer.getContactNumber() + " (Corporate)");
                }
            }
        }
    }
    
    private static void displayVehicleListInfo(ArrayList<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {

            System.out.println("Vehicle ID: " + v.getVehicleId());
            System.out.println("Registration number: " + v.getRegistrationNumber());
            System.out.println("Brand: " + v.getBrand());
            System.out.println("Model: " + v.getModel());

            if (v instanceof Car) {
                System.out.println("Type: Car");
            } else if (v instanceof Van) {
                System.out.println("Type: Van");
            } else if (v instanceof Truck) {
                System.out.println("Type: Truck");
            }
            System.out.println("========================================================");
        }
    }
    
    public static void viewVehicleByFuelType() {
        System.out.println("========================================================");
        System.out.println("FRMS VIEW VEHICLES BY FUEL TYPE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        System.out.println("Fuel Type:\n[1] Petrol\n[2] Diesel\n[3] Electric\n[4] Hybrid\nChoice: ");
        String c = scanner.nextLine();
        switch (c) {
            case "1" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.PETROL) == null) {
                    System.out.println("No Petrol Vehicles..!\n");
                    return;
                }
                System.out.println("===== Petrol Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.PETROL));
            }
            case "2" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.DIESEL) == null) {
                    System.out.println("No Diesel Vehicles..!\n");
                    return;
                }
                System.out.println("===== Diesel Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.DIESEL));
            }
            case "3" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC) == null) {
                    System.out.println("No Electric Vehicles..!\n");
                    return;
                }
                System.out.println("===== Electric Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC));
            }
            case "4" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.HYBRID) == null) {
                    System.out.println("No Hybrid Vehicles..!\n");
                    return;
                }
                System.out.println("===== Hybrid Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.HYBRID));
            }
            default -> System.out.println("Invalid fuel type..!\n");
        }
    }
    
    public static void returnVehicle(){
        System.out.println("================================================================");
        System.out.println("RETURN VEHICLE");
        System.out.println("================================================================");
        
        System.out.println("Enter agreement ID > ");
        String id = scanner.nextLine();
        fleetManager.returnVehicle(id);

    }
    
    public static void createAgreement(){
        System.out.println("================================================================");
        System.out.println("CREATE RENTAL AGREEMENT");
        System.out.println("================================================================");
        
        try {
            System.out.print("Enter vehicle ID > ");
            String vehicleId = scanner.nextLine();
            System.out.print("Enter Customer ID > ");
            String customerId = scanner.nextLine();
            System.out.print("Enter rental days > ");
            int days = scanner.nextInt();
            scanner.nextLine();
            RentalAgreement agreement = fleetManager.createRentalAgreement(vehicleId, customerId, days);
            if (agreement != null) {
                System.out.println(agreement);
            }
        } catch (VehicleNotAvailableException | InsufficientCreditException | NumberFormatException e) {
            System.out.println("Rentail failed: " + e.getMessage());
        }
    }
    
    public static void addVehicle(){
        
        System.out.println("========================================================");
        System.out.println("FRMS ADD VEHICLE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");

        System.out.print("Enter Reg. number: ");
        String regNum = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter daily rate: ");
        double dailyRate = scanner.nextDouble();
        scanner.nextLine();

        FuelType fuelType = null;

        while (fuelType == null) {
            
            System.out.println("Fuel Type:\n[1] Petrol\n[2] Diesal\n[3] Electric\n[4] Hybrid\nChoice: ");
            String choice2 = scanner.nextLine();
            
            switch (choice2) {
                case "1" ->
                    fuelType = FuelType.PETROL;
                case "2" ->
                    fuelType = FuelType.DIESEL;
                case "3" ->
                    fuelType = FuelType.ELECTRIC;
                case "4" ->
                    fuelType = FuelType.HYBRID;
                default ->
                    System.out.println("Invalid choice!\n");
            }
        }

        while (true) {

            System.out.println("Vehicle Types: ");
            System.out.println("[1]. Car");
            System.out.println("[2]. Van");
            System.out.println("[3]. Truck");
            System.out.print("Choice > ");

            String c = scanner.nextLine();

            switch (c) {
                case "1" -> {
                    Car car = new Car(regNum, brand, model, dailyRate, fuelType);
                    fleetManager.addVehicle(car);
                    return;
                }
                case "2" -> {
                    int passengerCapacity = 0;
                    
                    try{
                        System.out.print("Enter passenger capacity: ");
                        passengerCapacity = scanner.nextInt();
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                        return;
                    }
                    
                    
                    Van van = new Van(regNum, brand, model, dailyRate, fuelType, passengerCapacity);
                    fleetManager.addVehicle(van);
                    return;
                }
                case "3" -> {
                    double cargoCapacity = 0;
                    try {
                        System.out.print("Enter cargo capacity(Tons): ");
                        cargoCapacity = scanner.nextDouble();
                    } catch(Exception e){
                        System.out.println(e.getMessage());
                        return;
                    }                    
                    Truck truck = new Truck(cargoCapacity, regNum, brand, model, dailyRate, fuelType);
                    fleetManager.addVehicle(truck);
                    return;
                }
                default ->
                    System.out.println("Invalid choice!\n");
            }
        }        
    }

    private static void addCustomer() {

        System.out.println("========================================================");
        System.out.println("FRMS ADD CUSTOMER | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");

        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine();

        while (true) {
            System.out.println("Customer Types: ");
            System.out.println("[1]. Individual");
            System.out.println("[2]. Corporate");
            System.out.print("Choice > ");

            String c = scanner.nextLine();

            switch (c) {
                case "1" -> {
                    System.out.print("Enter loyal points: ");
                    int loyalPoints = scanner.nextInt();
                    scanner.nextLine();
                    IndividualCustomer ic = new IndividualCustomer(fullName, contact, loyalPoints);
                    fleetManager.addCustomer(ic);
                    return;
                }
                case "2" -> {
                    System.out.print("Enter credit limit: ");
                    double cl = scanner.nextDouble();
                    scanner.nextLine();
                    CorporateCustomer cc = new CorporateCustomer(cl, fullName, contact);
                    fleetManager.addCustomer(cc);
                    return;
                }
                default ->
                    System.out.println("Invalid choice!\n");
            }
        }

    }
}
