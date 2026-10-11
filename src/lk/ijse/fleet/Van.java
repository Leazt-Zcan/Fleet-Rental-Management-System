
package lk.ijse.fleet;


public class Van extends Vehicle {

    private int passengerCapacity;
    
    public Van(String registrationNumber, String brand, String model, double dailyRate, FuelType fuelType, int passengerCapacity) {
        super(registrationNumber, brand, model, dailyRate, fuelType);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public double calculateRentalCost(int rentalDays) {
        
        double rentalCost = (getDailyRate() * rentalDays * getFuelType().getCostMultiplier()) + (getDailyRate() * 0.20);
        return rentalCost;
    }
    
    
}
