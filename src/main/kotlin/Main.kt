
fun main() {
    getToken("admin", "admin") { access, refresh ->
        if (access != null) {
            println("Access Token: $access")
        } else {
            println("Failed to get token")
        }
    }
}
