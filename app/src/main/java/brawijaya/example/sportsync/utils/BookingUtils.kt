package brawijaya.example.sportsync.utils

object BookingUtils {
    /**
     * Generates a unique booking ID
     */
    fun generateBookingId(): String {
        return "ORDR-${System.currentTimeMillis()}"
    }

    /**
     * Generates a unique booking reference
     */
    fun generateBookingReference(): String {
        return "REF-${System.currentTimeMillis()}"
    }
}