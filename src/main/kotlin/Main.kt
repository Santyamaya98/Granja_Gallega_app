import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    val id: String,
    val user: Int?,
    val full_name: String,
    val company_name: String,
    val approved: Boolean,
    val date_joined: String,
    val tax_id: String,
    val email: String,
    val phone: String,
    val address: String,
    val zip_code: String,
    val province: String,
    val location: String,
    val production_activity: String
)

suspend fun main() {
    val client = HttpClient(CIO) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = "admin", password = "admin")
                }
                sendWithoutRequest { true }
            }
        }
        install(ContentNegotiation) {
            json()
        }
    }

    try {
        val response: HttpResponse = client.get("http://127.0.0.1:8000/api/suppliers/")

        println("Status: ${response.status}")

        if (response.status == HttpStatusCode.OK) {
            val suppliers: List<Supplier> = response.body()
            println("✅ Suppliers from Django:")
            println(suppliers)
            suppliers.forEach { println("${it.id}: ${it.full_name} - ${it.company_name}") }
        }
        else {
            println("❌ Request failed with status ${response.status}")
            println("Response body: ${response.bodyAsText()}")
        }

    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
    } finally {
        client.close()
    }
}