package future.code.dark.dungeon.domen;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.service.GameMaster;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy extends DynamicObject implements ActionListener {

    private static final int stepSize = 1;


    public Enemy(int xPosition, int yPosition) {
        super(xPosition, yPosition, Configuration.GHOST_SPRITE);

        Timer t = new java.util.Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                stepEnemy();
            }
        };

        t.schedule(tt, new Date(),1000);
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
    }

    private void stepEnemy(){
        Direction directionX = null;
        Direction directionY = null;

        int tmpXPosition = getXPosition();
        int tmpYPosition = getYPosition();

        int pXPosition = GameMaster.getInstance().getPlayer().getXPosition();
        int pYPosition = GameMaster.getInstance().getPlayer().getYPosition();

        int deltaX = 1;
        int deltaY = 1;

        if (tmpXPosition > pXPosition) deltaX = -1;
        if(tmpYPosition > pYPosition) deltaY = -1;

        switch (deltaX){
            case 1 -> directionX = Direction.RIGHT;
            case -1 -> directionX = Direction.LEFT;
        }
        switch (deltaY){
            case 1 -> directionY = Direction.DOWN;
            case -1 -> directionY = Direction.UP;
        }

        if(pYPosition == tmpYPosition)super.move(directionX, stepSize);

        if(pXPosition == tmpXPosition)super.move(directionY, stepSize);

        if(pYPosition > tmpYPosition)super.move(directionY, stepSize);

        if(pXPosition > tmpXPosition) super.move(directionX, stepSize);

        if(pYPosition < tmpYPosition)super.move(directionX, stepSize);

        if(pXPosition < tmpXPosition) super.move(directionY, stepSize);
    }

}

