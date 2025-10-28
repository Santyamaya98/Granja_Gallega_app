
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
fun getToken(username: String, password: String, BASE_URL:String, callback: (access: String?, refresh: String?) -> Unit) {
    val client = OkHttpClient()
    val url = "$BASE_URL/api/token/" // usa 10.0.2.2 si estás en Android Emulator

    // Construimos el JSON con credenciales
    val json = JSONObject().apply {
        put("username", username)
        put("password", password)
    }

    // Creamos el RequestBody correctamente
    val mediaType = "application/json; charset=utf-8".toMediaType()
    val body = json.toString().toRequestBody(mediaType)

    // Construimos la petición
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()

    // Ejecutamos la petición de manera asíncrona
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            callback(null, null)
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) {
                    println("❌ Failed: ${response.code}")
                    callback(null, null)
                } else {
                    val resBody = response.body?.string()
                    val jsonRes = JSONObject(resBody ?: "{}")
                    val access = jsonRes.optString("access", null)
                    val refresh = jsonRes.optString("refresh", null)
                    println("✅ Access Token: $access")
                    callback(access, refresh)
                }
            }
        }
    })
}


// --- Wrapper to turn getToken into a suspend function ---
suspend fun getTokenSuspend(username: String, password: String, BASE_URL: String): Pair<String, String>? =
    suspendCancellableCoroutine { continuation ->
        getToken(username, password, BASE_URL) { access, refresh ->
            if (access != null && refresh != null) {
                continuation.resume(access to refresh)
            } else {
                continuation.resume(null)
            }
        }
    }