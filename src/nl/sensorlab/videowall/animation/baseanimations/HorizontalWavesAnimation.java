package nl.sensorlab.videowall.animation.baseanimations;
import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * 
 * Examples based on: https://www.openprocessing.org/sketch/133048 and https://www.openprocessing.org/sketch/500317
 *
 */
public class HorizontalWavesAnimation extends BaseAnimation {
	
	// Settings
	private static final int[] WAVE_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};
	private static final int AMOUNT_OF_WAVES = 3;
	private ArrayList<HorizontalWave> waves;

	public HorizontalWavesAnimation(PApplet applet) {
		super(applet);
		this.waves = new ArrayList<HorizontalWave>();

		// Create the waves
		for(int i = 0; i < AMOUNT_OF_WAVES; i++) {
			// Set random color
			int color = WAVE_COLORS[(int)Math.floor(Math.random()*WAVE_COLORS.length)];
			
			// How much can the line deviate vertically
			float variation = (float) (0.25f + Math.random() * 0.35f);
			
			// How quickly the wave moves vertically 
			float speed =  (float) (0.00004f + Math.random() * 0.00008f);

			waves.add(new HorizontalWave(variation, speed, color));
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.fill(0, 40);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw the waves
		for(HorizontalWave wave : waves) {
			wave.draw(g, dt);
		}
	}
	
	public class HorizontalWave {
		
		private float offsetIntensity = 0; // How much can the line deviate vertically
		private float offsetIntensityIncrements;
		private float offsetVariation = 0; // How quickly the wave moves vertically
		private float offsetVariationIncrements;

		private int color;

		public HorizontalWave(float offsetIntensityIncrements, float offsetVariationIncrements, int color) {
			this.offsetIntensityIncrements = offsetIntensityIncrements;
			this.offsetVariationIncrements = offsetVariationIncrements;
			this.color = color;
		}

		public void draw(PGraphics g, double dt) {
			// Reset intensity to keep within bounds
			offsetIntensity = 0;
			
			g.noFill();
			g.stroke(color);
			g.strokeWeight(1);
			
			// Start the shape
			g.beginShape();
			for(int x = 0; x <= BaseAnimation.PIXEL_RESOLUTION_X; x++) {
				// Returns the Perlin noise value at specified coordinates. 
				// Perlin noise is a random sequence generator producing a more natural, harmonic succession of numbers than that of the standard random() function. 
				// It was developed by Ken Perlin in the 1980s and has been used in graphical applications to generate procedural textures, shapes, terrains, and other seemingly organic forms.
				float perlinNoise = applet.noise(offsetIntensity, offsetVariation);
				float y = PApplet.map(perlinNoise, 0, 1, 0, BaseAnimation.PIXEL_RESOLUTION_Y);
				
				// Set the vertex
				g.vertex(x, y);
				
				// Update the offset intensity
				offsetIntensity += offsetIntensityIncrements;
			}
			// Close the shape
			g.endShape();
			
			// Increase the offset so it creates a wave
			offsetVariation += offsetVariationIncrements * dt;
		}
	}

}
