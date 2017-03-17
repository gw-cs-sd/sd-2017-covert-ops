package signing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * Class responsible for handling all graphical events
 */
public class PaintBase {
	// Brush related variables
	public static final int DEFAULT_SIZE = 4;
	public static final double DEFAULT_ROTATION = 0;
	public static final Color DEFAULT_COLOR = Color.black;
	public static final CustomStroke DEFAULT_STROKE = new CustomStroke(new Rectangle2D.Float(0, 0, DEFAULT_SIZE, DEFAULT_SIZE), DEFAULT_SIZE / 2);
	
	protected Graphics2D g;
	protected Brush brush;

	public PaintBase(){
		brush = new Brush();
		brush.setColor(DEFAULT_COLOR);
		brush.setSize(DEFAULT_SIZE);
		brush.setRotation(DEFAULT_ROTATION);
		brush.setCustomStroke(DEFAULT_STROKE);
	}
	
	public PaintBase(Graphics2D graphics){
		this();
		setGraphics(graphics);	
	}
	
	// This allows the user to draw on the screen but does not center where on the hand to get the source
	// or place the user wants to draw
	// Draw from first x and y to second x and y
	public void drawLine(int x1, int y1, int x2, int y2){
		g.drawLine(x1, y1, x2, y2);	
	}
	
	public void drawVCenteredLine(int x1, int y1, int x2, int y2){
		int hs = brush.getSize() / 2;
		y1 -= hs; 
		y2 -= hs;
		drawLine(x1, y1, x2, y2);
	}
	
	// This method will be used to draw on the screen and let the program know to use the center point
	// of wherever the user is writing
	public void drawCenteredLine(int x1, int y1, int x2, int y2){
		int hs = brush.getSize() / 2;
		x1 -= hs; 
		y1 -= hs;
		x2 -= hs; 
		y2 -= hs;
		drawLine(x1, y1, x2, y2);
	}
	
	public void setCustomStroke(CustomStroke cc){
		g.setStroke(cc);
		brush.setCustomStroke(cc);
	}
	
	public CustomStroke getCustomStroke(){
		return brush.getCustomStroke();
	}
	
	public void setBrushRotation(double r){
		brush.setRotation(r);
	}
	
	public double getBrushRotation(){
		return brush.getRotation();
	}
	
	public void setBrushColor(Color c){
		brush.setColor(c);
		g.setColor(c);
	}
	
	public Color getBrushColor(){
		return brush.getColor();
	}
	
	public int getBrushSize(){
		return brush.getSize();
	}
	
	public void setBrushSize(int size){
		brush.setSize(size);
	}
	
	public Brush getBrush(){
		return brush;
	}
	
	public void setBrush(Brush brush){
		setBrushColor(brush.getColor());
		setBrushRotation(brush.getRotation());
		setBrushSize(brush.getSize());
		setCustomStroke(brush.getCustomStroke());
	}
	
	public Graphics2D getGraphics(){
		return g;
	}
	
	public void setGraphics(Graphics2D g){
		this.g = g;
		RenderingHints r = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				  RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(r);
		setBrushColor(brush.getColor());
		setBrushSize(brush.getSize());
		setCustomStroke(brush.getCustomStroke());
	}
}