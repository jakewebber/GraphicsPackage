//Jacob Webber
//CSCI 4210
//Line drawing algorithms

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class LinePackage extends Canvas{
	public LinePackage() {
	}
	private static final Random random = new Random();
	private static JTextField X1Field;
	private static JTextField Y1Field;
	private static JTextField X2Field;
	private static JTextField Y2Field;
	private static JTextField dashField;
	private static JTextField runtimeField;
	public static boolean randomize;
	public static int basicBoolean = 1;
	public static Timer timer;

	/** ----------------------------------------------------------
	 * Basic Line Drawing Algorithm. Calls the graphics package 
	 * drawLine algorithm, but only paints one pixel per drawLine call.
	 * This is to paint a line one pixel at a time. 
	 * @param x1 - 	first point x value
	 * @param y1 - first point y value
	 * @param x2 - second point x value
	 * @param x2 - second point y value  */
	public void basicLine(int dash, int x1, int y1, int x2, int y2, Graphics g){
		//to execute for loop, x1 needs to be less than x2. Swap if necessary.
		if (x1 > x2){
			basicLine(dash, x2, y2, x1, y1, g);
			return;
		}   
		int dx = x2 - x1; //delta X
		int dy = y2 - y1; //delta Y
		float yIncrement = (float)dy / (float)dx;
		float c = y1 - (yIncrement * x1);
		for (int x = x1; x < x2; x+= dash+1) { //move along x axis from x1 to x2
			float floatY = (yIncrement * x) + c; //calculating y for current x by multiplication
			int y = Math.round(floatY); //rounding
			g.drawLine(x, y, x, y);
		}
	}

	/** ----------------------------------------------------------
	 * Bresenham Line Algorithm. Calls the graphics package 
	 * drawLine algorithm, but only paints one pixel per drawLine call.
	 * This is to paint a line one pixel at a time. 
	 * Code from wikipedia article on Bresenham's algorithm and class notes.
	 * ONLY DRAWS A FEW SECTORS
	 * @param x1 - 	first point x value
	 * @param y1 - first point y value
	 * @param x2 - second point x value
	 * @param x2 - second point y value  */

	public void brz2(int dash, int x1, int y1, int x2, int y2, Graphics g){
		/*
		if (x1 > x2){
			brz2(dash, x2, y2, x1, y1, g);
			return;
		} 
		 */
		int dx = x2 - x1; //delta X
		int dy = y2 - y1; //delta Y
		int increment = 0;

		//int incrementX = 2 * dx;
		//	int incrementY = 2 * dy;
		int  E = 2 * dy - dx;
		g.drawLine(x1,  y1, x1, y1); //draw initial point
		int y = y1;
		for(int x = x1; x < x2; x++){
			if (E > 0){
				y++;
				if(increment < dash){
					increment++;
				}else{
					increment = 0;
					g.drawLine(x, y, x, y);
				}
				E = E + (2 * dy - 2 * dx); //adding slope
			}else{
				if(increment < dash){
					increment++;
				}else{
					increment = 0;
					g.drawLine(x, y, x, y);
				}
				E = E + (2 * dy);
			}
		}
	}


	/** ----------------------------------------------------------
	 * Bresenham Line Algorithm from techalgo website.
	 * Calls the graphics package drawLine algorithm, but only paints 
	 * one pixel per drawLine call. This is to paint a line one pixel at a time. 
	 * Algorithm obtained and modified from:
	 * http://tech-algorithm.com/articles/drawing-line-using-bresenham-algorithm/
	 * @param x1 - 	first point x value
	 * @param y1 - first point y value
	 * @param x2 - second point x value
	 * @param x2 - second point y value  */
	public void brz(int dash, int x1, int y1, int x2, int y2, Graphics g) {
		int dx = x2 - x1 ;
		int dy = y2 - y1 ;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		//Determining sectors
		if (dx < 0) dx1 = -1 ; 
		else if (dx > 0) dx1 = 1 ;
		if (dy < 0) dy1 = -1 ; 
		else if (dy > 0) dy1 = 1 ;
		if (dx < 0) dx2 = -1 ; 
		else if (dx > 0) dx2 = 1 ;

		int longest = Math.abs(dx) ; //getting absolute length
		int shortest = Math.abs(dy) ; 
		int increment = 0;
		if (!(longest > shortest)) { //If height is longer than width (sectors 1, 4, 5, 8)
			longest = Math.abs(dy) ;
			shortest = Math.abs(dx) ;
			if (dy < 0) dy2 = -1 ; else if (dy > 0) dy2 = 1 ;
			dx2 = 0 ;            
		}
		int numerator = longest >> 1;
				for (int i = 0; i<=longest; i++) { //initializing line draw
					if(increment < dash){
						increment++;
					}else{
						g.drawLine(x1, y1, x1, y1);
						increment = 0;
					}
					numerator += shortest ;
					if (!(numerator<longest)) {
						numerator -= longest ;
						x1 += dx1 ;
						y1 += dy1 ;
					} else {
						x1 += dx2 ;
						y1 += dy2 ;
					}
				}
	}

	/** ----------------------------------------------------------
	 * Calling the line drawing algorithms to interact with 
	 * swing components. Users can either draw lines with their own
	 * coordinates or randomize a set number of lines to draw. */
	public void paint(Graphics g) {
		super.paint(g);

		//Obtain x-y points from text fields
		int x1 = Integer.parseInt(X1Field.getText());
		int y1 = Integer.parseInt(Y1Field.getText());
		int x2 = Integer.parseInt(X2Field.getText());
		int y2 = Integer.parseInt(Y2Field.getText());
		int dash = Integer.parseInt(dashField.getText()); 

		//user pressed randomize button
		if(randomize == true){
			int runtime = Integer.parseInt(runtimeField.getText());
			long startTime = System.currentTimeMillis();
			for(int i = 0; i < runtime; i++){
				g.setColor(new Color(random.nextInt(250), random.nextInt(250), random.nextInt(250)) );

				x1 = random.nextInt(400);
				y1 = random.nextInt(400);
				x2 = random.nextInt(400);
				y2 = random.nextInt(400);
				if(basicBoolean == 1){
					basicLine(dash, x1, y1, x2, y2, g);

				}else if(basicBoolean == 0){
					g.setColor(Color.red);
					brz2(dash, x1, y1, x2, y2, g);
				}else{
					g.setColor(Color.black);
					brz(dash, x1, y1, x2, y2, g);
				}
			}
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(totalTime);
			randomize = false;
		}else{
			long startTime = System.currentTimeMillis();
			if(basicBoolean == 1){
				g.setColor(new Color(random.nextInt(250), random.nextInt(250), random.nextInt(250)) );
				basicLine(dash, x1, y1, x2, y2, g);
			}else if(basicBoolean == 0){
				g.setColor(Color.red);
				brz2(dash, x1, y1, x2, y2, g);
			}else{
				g.setColor(Color.black);
				brz(dash, x1, y1, x2, y2, g);
			}
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println(totalTime);
		}
	}


	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * STARTING MAIN METHOD	
	 * Initializes swing components (buttons, textboxes, etc)
	 * and graphics panel */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		LinePackage scan = new LinePackage();
		frame.setTitle("Jacob Webber Line-Scan-Conversion CSCI 4810");
		frame.setBackground(Color.WHITE);
		frame.setForeground(Color.WHITE);
		frame.setSize(600, 700);
		frame.getContentPane().add(scan);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblX = new JLabel("X1:");
		panel.add(lblX);

		X1Field = new JTextField();
		X1Field.setText("50");
		panel.add(X1Field);
		X1Field.setColumns(5);

		JLabel lblY = new JLabel("Y1:");
		panel.add(lblY);

		Y1Field = new JTextField();
		Y1Field.setText("50");
		Y1Field.setColumns(5);
		panel.add(Y1Field);

		JLabel lblX_1 = new JLabel("X2:");
		panel.add(lblX_1);

		X2Field = new JTextField();
		X2Field.setColumns(5);
		X2Field.setText("100");
		panel.add(X2Field);

		JLabel lblY_1 = new JLabel("Y2:");
		panel.add(lblY_1);

		Y2Field = new JTextField();
		Y2Field.setColumns(5);
		Y2Field.setText("100");
		panel.add(Y2Field);
		panel.add(new JSeparator(SwingConstants.VERTICAL));

		JLabel dashLabel = new JLabel("Dash Length:");
		panel.add(dashLabel);

		dashField = new JTextField();
		dashField.setColumns(5);
		dashField.setText("0");
		panel.add(dashField);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);

		JButton drawButton = new JButton("Draw");
		drawButton.setBackground(Color.YELLOW);
		drawButton.setToolTipText("Draw current line");
		panel.add(drawButton);

		JButton randomButton = new JButton("Randomize");
		randomButton.setBackground(Color.CYAN);
		randomButton.setToolTipText("Generate random lines");
		panel_1.add(randomButton);

		runtimeField = new JTextField();
		runtimeField.setColumns(5);
		runtimeField.setText("1");
		panel_1.add(new JLabel ("Number of lines to generate:"));
		panel_1.add(runtimeField);

		String[] algorithms = {"Basic", "Bresenham's from notes", "Bresenham's from techAlgo"};
		JComboBox algorithmBox = new JComboBox(algorithms);
		panel_1.add(algorithmBox);

		//algorithm box actions
		algorithmBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				JComboBox src = (JComboBox)arg0.getSource();
				String source = (String)src.getSelectedItem();
				if(source.equalsIgnoreCase("basic")){
					basicBoolean = 1;
				}else if(source.equalsIgnoreCase("Bresenham's from notes")){
					basicBoolean = 0;
				}else{
					basicBoolean = -1;
				}
			}
		});
		//draw button actions
		drawButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scan.repaint();
			}
		});
		//random button actions
		randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				randomize = true;
				scan.repaint();
			}
		});    
		frame.setVisible(true);
	}


}