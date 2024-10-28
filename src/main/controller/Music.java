package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Music {

    private void playMusic(String theMusicLocation) {
        try {
            File musicPath = new File(theMusicLocation);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();

            } else {
                System.out.println("Can't find file:" + musicPath.getPath());

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void getMusic (String theMusicLocation) {
        playMusic(theMusicLocation);
    }

}
