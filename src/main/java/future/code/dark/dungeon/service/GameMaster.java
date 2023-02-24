package future.code.dark.dungeon.service;

import future.code.dark.dungeon.GameFrame;
import future.code.dark.dungeon.Main;
import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.domen.Coin;
import future.code.dark.dungeon.domen.DynamicObject;
import future.code.dark.dungeon.domen.Enemy;
import future.code.dark.dungeon.domen.Exit;
import future.code.dark.dungeon.domen.GameObject;
import future.code.dark.dungeon.domen.Map;
import future.code.dark.dungeon.domen.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static future.code.dark.dungeon.config.Configuration.*;

public class GameMaster {

    private GameFrame gameFrame;

    private static GameMaster instance;

    private final Map map;
    private final List<GameObject> gameObjects;

    public static synchronized GameMaster getInstance() {
        if (instance == null) {
            instance = new GameMaster();
        }
        return instance;
    }

    private GameMaster() {
        try {
            this.map = new Map(Configuration.MAP_FILE_PATH);
            this.gameObjects = initGameObjects(map.getMap());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<GameObject> initGameObjects(char[][] map) {
        List<GameObject> gameObjects = new ArrayList<>();
        Consumer<GameObject> addGameObject = gameObjects::add;
        Consumer<Enemy> addEnemy = enemy -> {
            if (ENEMIES_ACTIVE) gameObjects.add(enemy);
        };

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case EXIT_CHARACTER -> addGameObject.accept(new Exit(j, i));
                    case COIN_CHARACTER -> {
                        addGameObject.accept(new Coin(j, i));
                        getMap().coinsShoudCounting();
                    }
                    case ENEMY_CHARACTER -> addEnemy.accept(new Enemy(j, i));
                    case PLAYER_CHARACTER -> addGameObject.accept(new Player(j, i));
                }
            }
        }

        return gameObjects;
    }

    public void renderFrame(Graphics graphics) {
        getMap().render(graphics);
        getStaticObjects().forEach(gameObject -> gameObject.render(graphics));
        getEnemies().forEach(gameObject -> gameObject.render(graphics));
        getPlayer().render(graphics);
        graphics.setColor(Color.WHITE);
        graphics.drawString(getPlayer().toString(), 10, 20);
        graphics.drawString(getMap().toString(), 1000, 20);
        checkCollise(graphics);

    }

    public void checkCollise(Graphics graphics) {
        List<GameObject> collect = gameObjects.stream()
                .filter(item -> (item instanceof Coin) || (item instanceof Enemy) || (item instanceof Exit))
                .collect(Collectors.toList());
        for (GameObject gameObject : collect) {
            if (gameObject.collise()) {
                if (gameObject instanceof Enemy) {
                    loose(graphics);
                } else if (gameObject instanceof Coin) {
                    gameObjects.remove(gameObject);
                    GameMaster.getInstance().getMap().coinsCounting();
                } else if (gameObject instanceof Exit) {
                    finishGame(graphics);
                }

            }
        }
    }



    public Exit getExit(){
        return (Exit) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Exit)
                .findFirst()
                .orElseThrow();
    }

    public Player getPlayer() {
        return (Player) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Player)
                .findFirst()
                .orElseThrow();
    }



    private List<GameObject> getStaticObjects() {
        return gameObjects.stream()
                .filter(gameObject -> !(gameObject instanceof DynamicObject))
                .collect(Collectors.toList());
    }

    private List<Enemy> getEnemies() {
        return gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Enemy)
                .map(gameObject -> (Enemy) gameObject)
                .collect(Collectors.toList());
    }

    public Map getMap() {
        return map;
    }

    public void setGameFrame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void finishGame(Graphics graphics) {
        int width = GameMaster.getInstance().getMap().getWidth();
        int height = GameMaster.getInstance().getMap().getHeight();
        BufferedImage bufferedImage1 = null;
        try {
            bufferedImage1 = ImageIO.read(new File("src/main/resources/assets/victory.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image image = bufferedImage1.getScaledInstance(width * SPRITE_SIZE,height * SPRITE_SIZE, Image.SCALE_DEFAULT);
        graphics.drawImage(image, 0 ,0, null);
        gameFrame.timerStop();
        buttons();
    }

    public void loose(Graphics graphics){

        int width = GameMaster.getInstance().getMap().getWidth();
        int height = GameMaster.getInstance().getMap().getHeight();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File("src/main/resources/assets/game_over_screen.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Image image = bufferedImage.getScaledInstance(width * SPRITE_SIZE,height * SPRITE_SIZE, Image.SCALE_DEFAULT);
        graphics.drawImage(image, 0 ,0, null);
        gameFrame.timerStop();
        buttons();
    }


    private static void buttons(){
        JButton backToMenu = new JButton("BACK TO MENU");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().frame.dispose();
                Main.getInstance().menu();
            }
        };
        backToMenu.addActionListener(actionListener);

        JButton exit = new JButton("EXIT");
        ActionListener actionListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().closeGame();
            }
        };

        exit.addActionListener(actionListener1);

        backToMenu.setBounds(500, 560, 200, 50);
        backToMenu.setForeground(Color.BLACK);
        backToMenu.setBackground(Color.DARK_GRAY);

        exit.setBounds(500, 620, 200, 50);
        exit.setForeground(Color.BLACK);
        exit.setBackground(Color.DARK_GRAY);

        backToMenu.setVisible(true);
        exit.setVisible(true);

        Main.getInstance().frame.add(backToMenu);
        Main.getInstance().frame.add(exit);

        Main.getInstance().stopMusic();
    }



}
