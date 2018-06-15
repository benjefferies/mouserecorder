import org.jnativehook.GlobalScreen
import org.jnativehook.dispatcher.DefaultDispatchService
import org.jnativehook.mouse.NativeMouseAdapter
import org.jnativehook.mouse.NativeMouseEvent
import java.util.*

class MouseListener : NativeMouseAdapter() {
    private val events = ArrayList<MouseEvent>()
    private var lastAction = System.nanoTime()

    init {
        GlobalScreen.setEventDispatcher(DefaultDispatchService())
        GlobalScreen.registerNativeHook()

        GlobalScreen.addNativeMouseListener(this)
    }

    override fun nativeMouseClicked(nativeMouseEvent: NativeMouseEvent?) {
        println("click happened at ${nativeMouseEvent!!.x}, ${nativeMouseEvent.y}")
        events.add(MouseEvent(nativeMouseEvent.x.toDouble(), nativeMouseEvent.y.toDouble(), calculateDelay()))
        lastAction = System.nanoTime()
    }

    private fun calculateDelay(): Long {
        return System.nanoTime() - lastAction
    }

    fun getEvents(): List<MouseEvent> {
        return events
    }

}