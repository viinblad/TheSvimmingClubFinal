package TreasurerDashboard;
import swimclub.controllers.PaymentController;

public class TreasurerDashboard {
    private final PaymentController paymentController;

    public TreasurerDashboard(PaymentController paymentController) {
        this.paymentController = paymentController;
    }

    // Display the total expected amount
    public void displayTotalExpectedAmount() {
        double total = paymentController.calculateTotalExpectedPayments();
        System.out.println("Total expected payments: " + total + " DKK");
    }

    // Display detailed payment information for all members
    public void displayMemberPayment(){
        System.out.println("Member payment details");
        paymentController.getMembersPaidList().forEach(member -> {
            System.out.println("Member ID: " + member.getMemberId() +
                    ", Name: " + member.getName() +
                    ", Membership type: " + member.getMembershipType() +
                    ", Phone: " + member.getPhoneNumber() +
                    ", Email: " + member.getEmail() +
                    ", Fee: " + paymentController.calculateMembershipFeeForMember(member.getMemberId()) + " DKK" +
                    ", Payment status: " + member.getPaymentStatus());
        });
    }
    public void filterByPaymentStatus(String paymentStatus) {
        System.out.println("Members filtered by payment status: " + paymentStatus);
        if(paymentStatus.equalsIgnoreCase("Complete")) {
            paymentController.getMembersPaidList().forEach(member ->
                System.out.println("Member ID: " + member.getMemberId() +
                        ", Name: " + member.getName() +
                        ", Fee: " + paymentController.calculateMembershipFeeForMember(member.getMemberId()) + " DKK"));
        } else if (paymentStatus.equalsIgnoreCase("Pending")) {
            paymentController.getMembersPendingList().forEach(member ->
                System.out.println("Member ID: " + member.getMemberId() +
                        ", Name: " + member.getName() +
                        ", Phone number: " + member.getPhoneNumber() +
                        ", Fee: " + paymentController.calculateMembershipFeeForMember(member.getMemberId()) + " DKK"));
        }
        else {
            System.out.println("Invalid payment status. Please enter either 'Complete' or 'Pending'");
        }
    }

}
