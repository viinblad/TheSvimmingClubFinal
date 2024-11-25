package swimclub.models;

public enum PaymentStatus {
    PENDING, // The member has yet to pay.
    COMPLETE, // the payment is completed.
    FAILED // the payment has expired.
}
