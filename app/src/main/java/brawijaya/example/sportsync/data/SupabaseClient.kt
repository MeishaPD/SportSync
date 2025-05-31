package brawijaya.example.sportsync.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.gotrue.Auth
import brawijaya.example.sportsync.BuildConfig
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest)
        install(Realtime)
        install(Auth)

        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        })
    }
}