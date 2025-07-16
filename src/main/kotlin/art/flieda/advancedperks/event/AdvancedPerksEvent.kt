package art.flieda.advancedperks.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class AdvancedPerksEvent : Event() {

    companion object {
        @JvmStatic
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }

    override fun getHandlers(): HandlerList = getHandlerList()

}
