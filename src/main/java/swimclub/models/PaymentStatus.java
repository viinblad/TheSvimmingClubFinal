package swimclub.models;
/**
 * Enum representing the status of a payment in the swim club.
 * This includes whether the payment is pending, completed, or failed.
 */
public enum PaymentStatus {
    /**
     * The payment is pending and has not yet been completed.
     */
    PENDING, // The member has yet to pay.
    /**
     * The payment is complete and successfully processed.
     */
    COMPLETE, // the payment is completed.
    /**
     * The payment has failed, either due to expiration or other issues.
     */
    FAILED // the payment has expired.
}
