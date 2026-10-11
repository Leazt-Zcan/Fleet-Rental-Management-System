
package lk.ijse.fleet;


public class Truck extends Vehicle {
    
    private double cargoCapacityTons;

    public Truck(double cargoCapacityTons, String registrationNumber, String brand, String model, double dailyRate, FuelType fuelType) {
        super(registrationNumber, brand, model, dailyRate, fuelType);
        this.cargoCapacityTons = cargoCapacityTons;
    }

    public double getCargoCapacityTons() {
        return cargoCapacityTons;
    }

    public void setCargoCapacityTons(double cargoCapacityTons) {
        this.cargoCapacityTons = cargoCapacityTons;
    }

    @Override
    public double calculateRentalCost(int rentalDays) {
        double rentalCost = (getDailyRate() * rentalDays * getFuelType().getCostMultiplier()) + (cargoCapacityTons * 10.00 * rentalDays);
        return rentalCost;
    }
    
}
