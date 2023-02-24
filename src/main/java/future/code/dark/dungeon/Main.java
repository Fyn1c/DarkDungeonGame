package future.code.dark.dungeon;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.controller.MovementController;
import future.code.dark.dungeon.service.GameMaster;

import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static future.code.dark.dungeon.config.Configuration.SPRITE_SIZE;

public class Main {

    public static void main(String[] args) {
        Main.getInstance().menu();
    }

    public JFrame frame;
    private Clip clip;
    public static Main instance;
    public static synchronized Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }


    public void menu(){
        frame = new JFrame(Configuration.GAME_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);

        JButton startGame = new JButton("Start Game");
        JButton settings = new JButton("Settings");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (frame != null) frame.dispose();

                frame = new JFrame(Configuration.GAME_NAME);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.add(new GameFrame(frame));
                frame.setVisible(true);

                startGame.setVisible(false);
            }
        };


        Font font = new Font(null, Font.BOLD, 90);
        JLabel darkDungeon = new JLabel("DARK DUNGEON");
        darkDungeon.setFont(font);
        darkDungeon.setBounds(200, 100, 800, 100);
        darkDungeon.setForeground(Color.WHITE);

        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(null);

        startGame.setBounds(450, 350, 300, 50);
        settings.setBounds(450, 410, 300, 50);

        startGame.setBackground(Color.DARK_GRAY);
        settings.setBackground(Color.DARK_GRAY);

        startGame.setForeground(Color.BLACK);
        settings.setForeground(Color.BLACK);

        ActionListener actionListener1 = actionListener1(frame);

        settings.addActionListener(actionListener1);
        startGame.addActionListener(actionListener);

        frame.setBackground(Color.BLACK);
        frame.add(startGame);
        frame.add(settings);
        frame.add(darkDungeon);
        frame.setVisible(true);
    }

    public void closeGame(){
        frame.dispose();
    }

    private static ActionListener actionListener2(JButton music){
            final int[] i = {0};

            ActionListener actionListener2 = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    i[0]++;
                    if(i[0] == 1) {
                        music.setText("ON");
                        getInstance().startMusic();
                        return;
                    }
                    if(i[0] == 2){
                        music.setText("OFF");
                        getInstance().stopMusic();
                        i[0] = 0;
                    }
                }
            };
            return actionListener2;
    }


    private ActionListener actionListener1(Frame frame){
        ActionListener actionListener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                JFrame frame1 = new JFrame("Settings");
                frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame1.setResizable(false);
                frame1.getContentPane().setBackground(Color.BLACK);
                frame1.setLayout(null);
                frame1.setSize(1200, 800);
                frame1.setVisible(true);

                Font font = new Font(null, Font.BOLD, 90);
                JLabel settings = new JLabel("SETTINGS");
                settings.setFont(font);
                settings.setBounds(350, 100, 500, 100);
                settings.setForeground(Color.WHITE);

                JButton music = new JButton();
                music.setText("OFF");
                ActionListener actionListener2 = actionListener2(music);
                music.addActionListener(actionListener2);
                music.setBounds(650, 370, 60, 20);
                music.setBackground(Color.DARK_GRAY);
                music.setForeground(Color.BLACK);

                JButton backToMenu = new JButton("BACK TO MENU");
                ActionListener actionListener3 = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame1.dispose();
                        menu();
                    }
                };
                backToMenu.addActionListener(actionListener3);
                backToMenu.setBounds(500, 700, 200, 50);
                backToMenu.setBackground(Color.DARK_GRAY);
                backToMenu.setForeground(Color.BLACK);

                Font font1 = new Font(null, Font.PLAIN, 40);
                JLabel musicNadpis = new JLabel("MUSIC:");
                musicNadpis.setFont(font1);
                musicNadpis.setBounds(450, 350, 300, 60);
                musicNadpis.setForeground(Color.WHITE);

                frame1.add(backToMenu);
                frame1.add(musicNadpis);
                frame1.add(settings);
                frame1.add(music);
            }
        };
        return actionListener1;
    }

    public void startMusic(){
        try {
            File music = new File("src/main/resources/assets/music/239084.wav");

            AudioInputStream ais = AudioSystem.getAudioInputStream(music);

            clip = AudioSystem.getClip();

            clip.open(ais);
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }

    }


    public void stopMusic(){
        clip.stop();
        clip.close();
    }



}
