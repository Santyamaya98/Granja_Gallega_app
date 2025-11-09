

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
    val products = getProducts(username="admin", password="admin", BASE_URL)
    if (products != null){
        println("\n --- Retrieeved Products ---")
        products.forEach {
            println("ID ${it.id}, Name:${it.name}, Price:${it.price}")
        }
    } else {
        println("\n❌ Failed to retrieve products")
    }
    val promos = getPromos(username="admin", password="admin", BASE_URL)
    if (promos != null){
        println("\n --- Retrieved Promos----")
        promos.forEach {
            println("ID ${it.id}, ID_product: ${it.product_name}, end_date: ${it.end_promo_date}")
        }
    }else{
        println("\n ❌ Failed to retrieve promos")
    }
}