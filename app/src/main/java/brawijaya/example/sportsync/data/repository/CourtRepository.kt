package brawijaya.example.sportsync.data.repository

import brawijaya.example.sportsync.data.models.CourtData
import brawijaya.example.sportsync.ui.screens.findcourt.utils.TimeSlots.generateTimeSlots
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CourtRepository {
    private val sampleCourts = listOf(
        CourtData(
            name = "Lapangan SM Futsal",
            address = "Jln Sudimoro",
            pricePerHour = "Rp. 100.000/jam",
            timeSlots = generateTimeSlots(),
            isAvailable = true
        ),
        CourtData(
            name = "Champion Futsal",
            address = "Jln Tlogomas",
            pricePerHour = "Rp. 100.000/jam",
            timeSlots = generateTimeSlots(),
            isAvailable = true
        ),
        CourtData(
            name = "Olimpico Futsal Arena",
            address = "Jln Bendungan Sutami",
            pricePerHour = "Rp. 100.000/jam",
            timeSlots = generateTimeSlots(),
            isAvailable = true
        ),
        CourtData(
            name = "Wijaya Putra Futsal",
            address = "Jln Tenaga Selatan",
            pricePerHour = "",
            timeSlots = emptyList(),
            isAvailable = false
        ),
        CourtData(
            name = "Viva Futsal",
            address = "Jln Bunga Andong",
            pricePerHour = "Rp. 100.000/jam",
            timeSlots = generateTimeSlots(),
            isAvailable = true
        )
    )

    fun getAllCourts(): Flow<List<CourtData>> = flow {
        delay(500)
        emit(sampleCourts)
    }

    fun getCourtsByName(name: String): Flow<List<CourtData>> = flow {
        delay(300)
        val filteredCourts = sampleCourts.filter {
            it.name.contains(name, ignoreCase = true)
        }
        emit(filteredCourts)
    }

    fun getCourtByName(name: String): CourtData? {
        return sampleCourts.find { it.name == name }
    }
}