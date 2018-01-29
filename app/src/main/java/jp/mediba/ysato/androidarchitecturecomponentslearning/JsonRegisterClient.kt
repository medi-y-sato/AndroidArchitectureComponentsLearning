package jp.mediba.ysato.androidarchitecturecomponentslearning

/**
 * Created by ysato on 2017/12/27.
 */
data class JsonRegisterClient (
        val id: Int,
        val redirect_uri: String,
        val client_id: String,
        val client_secret: String
)

//                         {"id":516,"redirect_uri":"urn:ietf:wg:oauth:2.0:oob","client_id":"7d2f9ec86a018d5bb8bb7944beb3c547560a03bac67f79e3f4756e1a090f6dbb","client_secret":"9b961bf054e357730ef312b3dc4ee9404259dfb787a1839daaee30edf4226079"}
