package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.Random;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class SensorlabLogoAnimation extends BaseCanvasAnimation {
	
	private ArrayList<Square> squares = new ArrayList<>();

	public SensorlabLogoAnimation(PApplet applet) {
		super(applet, BaseCanvasAnimation.DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Create squares
		int width = getGeometry().width;
		int height = getGeometry().height;
		final int squaresize = height;
		
		for (int y = 0; y < height; y += squaresize) {
			for (int x = 0; x < width; x += squaresize) {
				squares.add(new Square(new PVector(x + squaresize / 2, y + squaresize / 2), applet, squaresize));
			}
		}
		
		
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		for (Square s: squares) {
			s.draw(g);
		}
	}
	
	public class Square {
		
		private int size;
		private int halfsize;
		
		private Pattern pattern;
		private PVector pos;
		private float rotation;
		private boolean blackOnWhite;
		
		private AniSequence inOutTransition;
		private float transitionProgress = 0f;
		
		public Square(PVector pos, PApplet applet, int size) {
			this.size = size;
			this.halfsize = size / 2;
			
			this.pos = pos;
//			this.blackOnWhite = randomColorStyle();
//			rotation = randomRotation();
			
			float t = (float) (4f + Math.random() * 8f); 
			
			// Setup transition sequence
			inOutTransition = new AniSequence(applet);
			inOutTransition.beginSequence();
			inOutTransition.add(Ani.to(this, .5f, "transitionProgress", 1f, Ani.LINEAR)); // Fade in
			inOutTransition.add(Ani.to(this, .5f, t, "transitionProgress", 0f, Ani.LINEAR, "onEnd:change")); // Fade out
			inOutTransition.endSequence();
			
			// Randomize (and start transition)
			change();
			
		}
		
		public void draw(PGraphics g) {
			g.pushMatrix();
			g.pushStyle();
			g.translate(pos.x, pos.y);
			g.rotate(rotation);
			
			transitionProgress = Math.min(1f, Math.max(0f, transitionProgress));
			
			// Determine colors
			int colorF = blackOnWhite ? PColor.color(0) : PColor.color(255);
			int colorB = blackOnWhite ? PColor.color(255) : PColor.color(0);
			
			// BG
			g.fill(colorB);
			g.noStroke();
			g.rect(-halfsize, -halfsize, size, size);
			
			switch (pattern) {
				case BLANK: 
					break;
//				case CIRCLES_CONCENTRIC: {
//					g.noFill();
//					g.stroke(colorF);
//					g.strokeWeight(size / 10);
//					g.strokeCap(PConstants.SQUARE);
//					
//					float start = PConstants.PI;
//					float end = PConstants.PI + transitionProgress * PConstants.PI * 0.5f;
//					float diff = end - start;
//					
//					g.arc(halfsize, halfsize, size * 1.4f, size * 1.4f, start, end);
//					g.arc(halfsize, halfsize, size * 1f, size * 1f,  PConstants.PI + PConstants.PI * 0.5f - diff, PConstants.PI + PConstants.PI * 0.5f);
//					g.arc(halfsize, halfsize, size * .6f, size * .6f,  start, end);
//				} break;
				case LINES: {
					g.noFill();
					g.stroke(colorF);
					g.strokeWeight(size / 8);
					g.strokeCap(PConstants.SQUARE);
					
					g.line(-halfsize, -halfsize * .5f, -halfsize + transitionProgress * size, -halfsize * .5f);
					g.line(halfsize, 0, halfsize - transitionProgress * size, 0);
					g.line(-halfsize, halfsize * .5f, -halfsize + transitionProgress * size, halfsize * .5f);
					
				} break;
				default:
					System.err.println("Unimplemented pattern: " + pattern);
					break;
				
			}
			
			// Solid part
			g.noStroke();
			g.fill(colorF);
			g.beginShape(PConstants.TRIANGLE_FAN);
			g.vertex(-halfsize, -halfsize);
			g.vertex(-halfsize, halfsize);
			g.vertex(halfsize, -halfsize);
			g.endShape();
			
			// Edge
			g.noFill();
			g.strokeWeight(2);
			g.stroke(0);
			g.rect(-halfsize, -halfsize, size, size);
			
			g.popStyle();
			g.popMatrix();
		}
		
		public void change() {
			pattern = randomPattern();
			rotation = randomRotation();
//			blackOnWhite = randomColorStyle();
			
			// Start transition
			inOutTransition.start();
		}
		
		private Pattern randomPattern() {
			int pick = new Random().nextInt(Pattern.values().length);
			return Pattern.values()[pick];
		}
		
		private float randomRotation() {
			int random = new Random().nextInt(4);
			return ((float) random) * 0.5f * PConstants.PI;
		}
		
		private boolean randomColorStyle() {
			return (new Random().nextInt(2) == 1);
		}
			
	}
	
	public enum Pattern {
		BLANK,
//		CIRCLES_CONCENTRIC,
		LINES
		
	}

}