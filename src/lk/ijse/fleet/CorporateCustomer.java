
package lk.ijse.fleet;

public class CorporateCustomer extends Customer {
    
    private double creditLimit;
    private double outstandingBalance = 0.0;

    public CorporateCustomer(double creditLimit, String fullName, String contactNumber) {
        super(fullName, contactNumber);
        this.creditLimit = creditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    @Override
    public double applyDiscount(double subtotal) {
        
        return subtotal * 0.85;
    }
    
    public boolean hasSufficientCredit(double cost){
        return (outstandingBalance + cost) <= creditLimit;
    }
    
    
}
