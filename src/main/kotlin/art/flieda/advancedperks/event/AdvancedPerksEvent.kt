package art.flieda.advancedperks.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

open class AdvancedPerksEvent : Event() {

    override fun getHandlers(): HandlerList = getHandlerList()

    companion object {
        @JvmStatic
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }

}
