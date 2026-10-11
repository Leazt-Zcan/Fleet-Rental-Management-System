
package lk.ijse.fleet;

@FunctionalInterface
public interface RentalEventListener {
    
    public void onRentalCreated(RentalAgreement agreement);
}
