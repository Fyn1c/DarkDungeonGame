package future.code.dark.dungeon.domen;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static future.code.dark.dungeon.config.Configuration.EXIT_SPRITE;

public class Exit extends GameObject{
    public Exit(int xPosition, int yPosition) {
        super(xPosition, yPosition, EXIT_SPRITE);
    }
}
