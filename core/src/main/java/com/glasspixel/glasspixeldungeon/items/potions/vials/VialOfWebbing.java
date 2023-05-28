package com.glasspixel.glasspixeldungeon.items.potions.vials;

import com.glasspixel.glasspixeldungeon.Assets;
import com.glasspixel.glasspixeldungeon.Dungeon;
import com.glasspixel.glasspixeldungeon.actors.blobs.Blob;
import com.glasspixel.glasspixeldungeon.actors.blobs.Web;
import com.glasspixel.glasspixeldungeon.scenes.GameScene;
import com.glasspixel.glasspixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class VialOfWebbing extends Vial {

    {
        icon = ItemSpriteSheet.Icons.VIAL_WEBBING;
        defaultAction = AC_THROW;
    }
    @Override
    public void shatter( int cell ) {

        if (Dungeon.level.heroFOV[cell]) {
            identify();

            splash( cell );
            Sample.INSTANCE.play( Assets.Sounds.SHATTER );
        }

        for (int offset : PathFinder.NEIGHBOURS9){
            if ((!Dungeon.level.solid[cell+offset]) && (cell+offset != cell)) {
               GameScene.add(Blob.seed(cell+offset, 10, Web.class));
            }
        }
    }

}
