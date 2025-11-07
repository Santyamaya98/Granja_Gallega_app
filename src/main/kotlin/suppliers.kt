
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant


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

// --- New function to get suppliers, calling getToken first ---
suspend fun getSuppliers(username: String, password: String, BASE_URL: String): List<Supplier>? {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // To handle fields not in Supplier data class
                isLenient = true
            })
        }
    }

    return try {
        // 1. Call getTokenSuspend to get the access and refresh tokens
        val tokens = getTokenSuspend(username, password, BASE_URL)

        if (tokens == null) {
            println("❌ Failed to get tokens. Cannot fetch suppliers.")
            return null
        }

        val (accessToken, _) = tokens
        println("Using access token to fetch suppliers.")

        // 2. Use the access token to fetch the list of suppliers
        val response: HttpResponse = client.get("$BASE_URL/api/suppliers/") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }

        if (response.status == HttpStatusCode.OK) {
            val suppliers: List<Supplier> = response.body()
            println("✅ Suppliers fetched successfully.")
            suppliers
        } else {
            println("❌ Request to /suppliers/ failed with status ${response.status}")
            println("Response body: ${response.bodyAsText()}")
            null
        }

    } catch (e: Exception) {
        println("❌ Error fetching suppliers: ${e.message}")
        e.printStackTrace()
        null
    } finally {
        client.close()
    }
}




@Serializable
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val expiration_date: String,
    val promotion: Boolean,
    val price: Float,
    val stock: Int,
    val created_at: String,
    val updadted_at: String?=null,
)

// function to get products Calling GetToken first ---

suspend fun getProducts(username: String, password: String, BASE_URL: String): List<Product>?{
    val client = HttpClient (CIO){
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    return try {
        // 1. Call getTokenSuspend to get the access and refresh tokens
        val tokens = getTokenSuspend(username, password, BASE_URL)

        if (tokens == null) {
            println("❌ Failed to get tokens. Cannot fetch suppliers.")
            return null
        }

        val (accessToken, _) = tokens
        println("Using access token to fetch suppliers.")

        // 2. Use the access token to fetch the list of suppliers
        val response: HttpResponse = client.get("$BASE_URL/api/products/") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }

        if (response.status == HttpStatusCode.OK) {
            val products: List<Product> = response.body()
            println("✅ Suppliers fetched successfully.")
            products
        } else {
            println("❌ Request to /products/ failed with status ${response.status}")
            println("Response body: ${response.bodyAsText()}")
            null
        }

    } catch (e: Exception) {
        println("❌ Error fetching suppliers: ${e.message}")
        e.printStackTrace()
        null
    } finally {
        client.close()
    }
}