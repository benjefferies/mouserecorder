import com.google.gson.Gson

class MouseEvent(private val x: Double, private val y: Double, private val delay: Long) {

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
