package victinix888.beth

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import ktx.app.KtxScreen
import ktx.assets.invoke
import ktx.assets.pool
import ktx.collections.iterate
import ktx.graphics.use
import kotlin.math.exp

private const val DROP_EXPONENT_STEP = 0.02F
private const val MAX_DROP_SPEED = 700F
private const val TIME_LINEAR_DECREASE = 5_000_000L
private const val MIN_TIME = 100_000_000L

class GameScreen(private val game: Beth) : KtxScreen {

    private val camera = OrthographicCamera().apply { setToOrtho(false, 800F, 480F) }

    private val dropImage = game.assets[TextureAtlasAsset.Game].findRegion("drop")
    private val bucketImage = game.assets[TextureAtlasAsset.Game].findRegion("bucket")

    private val bucket = Rectangle(800F / 2F - 64F / 2F, 20F, 64F, 64F)
    private val poolRaindrops = pool { Rectangle() }
    private val activeRaindrops = Array<Rectangle>()

    private val touchPos = Vector3()
    private var lastDropTime = 0L
    private var collected = 0

    private var timeBetweenDrops = 1_000_000_000L
    private var dropExponent = 5.3F
    private var dropSpeed = exp(dropExponent)

    override fun render(delta: Float) {
        camera.update()

        game.batch.use(camera) {
            it.draw(bucketImage, bucket.x, bucket.y)
            activeRaindrops.forEach { raindrop ->
                it.draw(dropImage, raindrop.x, raindrop.y)
            }
            game.font.draw(it, "Drops collected: $collected", 0F, 480F)
        }

        touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0F)
        camera.unproject(touchPos)
        bucket.x = touchPos.x - (64F / 2F)

        if (dropSpeed < MAX_DROP_SPEED) {
            dropExponent += DROP_EXPONENT_STEP * delta
            dropSpeed = exp(dropExponent)
        }

        activeRaindrops.iterate { raindrop, iterator ->
            raindrop.y -= dropSpeed * delta
            if (raindrop.y + 64 < 0) {
                iterator.remove()
                poolRaindrops(raindrop)
            } else if (raindrop.overlaps(bucket)) {
                iterator.remove()
                poolRaindrops(raindrop)
                collected++
            }
        }

        if (timeBetweenDrops > MIN_TIME) {
            timeBetweenDrops -= (TIME_LINEAR_DECREASE * delta).toLong()
        }

        val timeDiff = TimeUtils.nanoTime() - lastDropTime
        if (timeDiff > timeBetweenDrops) {
            spawnRaindrop()
        }
    }

    override fun show() {
        spawnRaindrop()
    }

    private fun spawnRaindrop() {
        val raindrop = poolRaindrops().set(MathUtils.random(0, 800-64).toFloat(), 480F, 64F, 64F)
        activeRaindrops.add(raindrop)
        lastDropTime = TimeUtils.nanoTime()
    }
}