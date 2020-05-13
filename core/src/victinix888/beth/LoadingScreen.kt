package victinix888.beth

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.app.KtxScreen
import ktx.graphics.use

class LoadingScreen(private val game: Beth) : KtxScreen {

    private val camera = OrthographicCamera().apply { setToOrtho(false, 800F, 480F) }

    override fun render(delta: Float) {
        game.assets.update()

        camera.update()

        game.batch.use(camera) {
            if (game.assets.isFinished) {
                game.font.draw(it, "Loading Complete!", 100F, 150F)
                game.font.draw(it, "Click anywhere to begin.", 100F, 100F)
            } else {
                val progress = game.assets.progress * 100
                game.font.draw(it, "Now Loading: $progress%", 100F, 150F)
            }
        }

        if (Gdx.input.isTouched && game.assets.isFinished) {
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<LoadingScreen>()
            dispose()
        }
    }

    override fun show() {
        TextureAtlasAsset.values().forEach {
            game.assets.load(it)
        }
    }
}