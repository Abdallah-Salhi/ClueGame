package clueGame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {
	private Clip clip;

	// Constructor - load the music
	public MusicPlayer(String filepath) {
		try {
			File musicPath = new File(filepath);
			if (musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				clip = AudioSystem.getClip();
				clip.open(audioInput);
			} else {
				System.out.println("File not found: " + filepath);
			}
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	// Play the music
	public void play() {
		if (clip != null) {
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop forever
		}
	}

	// Stop the music
	public void stop() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
		}
	}

	// Close and release resources
	public void close() {
		if (clip != null) {
			clip.close();
		}
	}
}
