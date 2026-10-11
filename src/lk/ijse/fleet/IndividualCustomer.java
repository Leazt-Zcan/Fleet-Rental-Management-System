
package lk.ijse.fleet;

public class IndividualCustomer extends Customer {
    
    private int loyalPoints = 0;

    public int getLoyalPoints() {
        return loyalPoints;
    }

    public void setLoyalPoints(int loyalPoints) {
        this.loyalPoints = loyalPoints;
    }
    
    public IndividualCustomer(String fullName, String contactNumber, int loyaltyPoints) {
        super(fullName, contactNumber);
        this.loyalPoints = loyaltyPoints;
    }

    @Override
    public double applyDiscount(double subtotal) {
        
        double discountRate;
        
        if(loyalPoints < 100){
            discountRate = 0.0;
        }else if(loyalPoints < 300){
            discountRate = 0.05;
        }else {
            discountRate = 0.10;
        }
        
        return subtotal * (1 - discountRate);
    }
    
    
}