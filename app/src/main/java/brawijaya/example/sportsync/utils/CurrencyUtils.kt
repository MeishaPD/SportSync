package brawijaya.example.sportsync.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    fun formatCurrency(amount: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount).replace("IDR", "Rp")
    }
}