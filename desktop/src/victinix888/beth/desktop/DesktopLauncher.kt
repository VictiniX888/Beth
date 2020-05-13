package victinix888.beth.desktop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import victinix888.beth.Beth

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "Beth"
            width = 800
            height = 480
        }
        LwjglApplication(Beth(), config)
    }
}