package victinix888.beth

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import ktx.assets.getAsset
import ktx.assets.load

enum class TextureAtlasAsset(val path: String) {
    Game("game.atlas");
}

fun AssetManager.load(textureAtlas: TextureAtlasAsset) = load<TextureAtlas>(textureAtlas.path)

operator fun AssetManager.get(textureAtlas: TextureAtlasAsset) = getAsset<TextureAtlas>(textureAtlas.path)