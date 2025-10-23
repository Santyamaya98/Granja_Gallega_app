
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

fun getToken(username: String, password: String, callback: (access: String?, refresh: String?) -> Unit) {
    val client = OkHttpClient()
    val url = "http://127.0.0.1:8000/api/token/" // usa 10.0.2.2 si estás en Android Emulator

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