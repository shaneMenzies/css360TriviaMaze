package controller;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Handles audio playback in game.
 * Can be used for background music or sound effects as needed.
 *
 * @author Cynthia Lopez
 * @version 10/27/24
 */
public class Music {

    /**
     * Audio Clip for game.
     */
    private Clip myClip;

    /**
     * Plays music from the file specified by the given file path.
     *
     * @param theMusicLocation The path to the music file to be played.
     */
    private void playMusic(final String theMusicLocation) {
        try {
            final File musicPath = new File(theMusicLocation);
            final AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
            myClip = AudioSystem.getClip();

            if (musicPath.exists()) {
                myClip.open(audioInput);
                myClip.start();
                myClip.loop(Clip.LOOP_CONTINUOUSLY);

            } else {
                System.out.println("Can't find file:" + musicPath.getPath());
            }
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Stops background music.
     */
    private void stopMusic() {
        myClip.stop();
    }

    /**
     * Getter for playMusic method.
     *
     * @param theMusicLocation The path to the music file to be played.
     */
    public void getMusic(final String theMusicLocation) {
        playMusic(theMusicLocation);
    }

    /**
     * Getter to stop music.
     */
    public void getMusicStop() {
        stopMusic();
    }

}
