package clueGame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {
	 private Clip clip;

	    public void playMusic(String musicFileName) {
	        try {
	            // Use getResourceAsStream to load the audio from the JAR
	            InputStream musicStream = getClass().getResourceAsStream("data/" + musicFileName);
	            if (musicStream == null) {
	                System.out.println("Music file not found!");
	                return;
	            }

	            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicStream);
	            clip = AudioSystem.getClip();
	            clip.open(audioStream);
	            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music indefinitely
	        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
	            e.printStackTrace();
	        }
	    }

	    public void stopMusic() {
	        if (clip != null && clip.isRunning()) {
	            clip.stop();
	        }
	    }
}
