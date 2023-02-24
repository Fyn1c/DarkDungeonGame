package future.code.dark.dungeon.domen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

import static future.code.dark.dungeon.config.Configuration.SPRITE_SIZE;

public abstract class AnimatedObject extends GameObject implements ActionListener {

    private Timer timer;
    private List<String> animatedFrame;
    private int i = 0;

    public AnimatedObject(int xPosition, int yPosition, String imagePath) {
        super(xPosition, yPosition, imagePath);
        generateLisAnimatedFrame();
        initTimerAO();
    }

    public void initTimerAO(){
        timer = new Timer(250, this);
        timer.start();
    }

    @Override
    public void render(Graphics graphics) {

        Image img = new ImageIcon(animatedFrame.get(i)).getImage();

        graphics.drawImage(img, xPosition * SPRITE_SIZE, yPosition  * SPRITE_SIZE, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        i++;
        i = i % 4;
    }

    public void generateLisAnimatedFrame(){
        if(this instanceof Player){
            animatedFrame = List.of("src/main/resources/assets/hero/tile000.png",
                    "src/main/resources/assets/hero/tile001.png",
                    "src/main/resources/assets/hero/tile002.png",
                    "src/main/resources/assets/hero/tile003.png");
        }
        if(this instanceof Enemy){
            animatedFrame = List.of("src/main/resources/assets/ghost/tile000.png",
                    "src/main/resources/assets/ghost/tile001.png",
                    "src/main/resources/assets/ghost/tile002.png",
                    "src/main/resources/assets/ghost/tile003.png");
        }

    }
}
