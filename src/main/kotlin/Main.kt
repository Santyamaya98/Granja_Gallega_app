

const val BASE_URL = "http://127.0.0.1:8000"
suspend fun main() {
    val suppliers = getSuppliers("admin", "admin", BASE_URL) // Replace with actual username/password
    if (suppliers != null) {
        println("\n--- Retrieved Suppliers ---")
        suppliers.forEach {
            println("ID: ${it.id}, Name: ${it.full_name}, Company: ${it.company_name}, Approved: ${it.approved}")
        }
    } else {
        println("\n❌ Failed to retrieve suppliers.")
    }
}