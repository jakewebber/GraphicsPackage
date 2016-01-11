//Jacob Webber
//CSCI 4210
//Line drawing algorithms

import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import java.awt.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyVetoException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import Jama.Matrix;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class LineScan extends Canvas{
	public LineScan() {
	}
	private static JTextField X1Field;
	private static JTextField Y1Field;
	private static JTextField X2Field;
	private static JTextField Y2Field;
	private static JTextField dashField;
	private static JTextField vx1field = new JTextField("Vx1");
	private static JTextField vy1field = new JTextField("Vy1");
	private static JTextField vx2field = new JTextField("Vx2");
	private static JTextField vy2field = new JTextField("Vy2");
	public static boolean drawfileswitch;
	public static boolean firstTrans = true;
	public static Timer timer;
	public static ArrayList<Line> lines = TransformPackage.packageCube();
	public static 		LineScan scan = new LineScan();
	@SuppressWarnings("deprecation")
	Plane planeX = new Plane(new Vector3D(0, 0, 1));
	@SuppressWarnings("deprecation")
	Plane planeY = new Plane(new Vector3D(0, 1, 0));

	//Previous rotation slider values
	public static int xRotation = 0;
	public static int yRotation = 0;
	public static int zRotation = 0;

	public static Matrix transformationMatrix = new Matrix(new double[][]{
		{0, 0, 0, 0},
		{0, 0, 0, 0}, 
		{0, 0, 0, 0}, 
		{0, 0, 0, 0}
	});

	public static JTable matrixTable = new JTable();
	/** ----------------------------------------------------------
	 * Set a specific font for the a JSlider's ticks, not provided as JSlider method */
	public static void setSliderTickFont(JSlider slider, Font font){
		Dictionary<?, ?> d = slider.getLabelTable();
		Enumeration<?> e = d.elements();
		while (e.hasMoreElements()) {
			Object o = e.nextElement();
			if (o instanceof JComponent) ((JComponent)o).setFont(font);
		}
	}
	/** ----------------------------------------------------------
	 * Calling the line drawing algorithms to interact with 
	 * swing components. Users can either draw lines with their own
	 * coordinates or drawfileswitch a set number of lines to draw. */
	public void paint(Graphics g) {
		super.paint(g);
		/* Draw XY coordinate system */
		Dimension panelDimensions = scan.getSize();
		int width = panelDimensions.width;
		int height = panelDimensions.height;
		g.translate(width/2, height/2);
		g.drawString(String.valueOf(height), 0, -(height/2)+10 );
		if(width < 1000){
			g.drawString(String.valueOf(width), (width/2)-20, 0);
		}else{
			g.drawString(String.valueOf(width), (width/2)-26, 0);
		}
		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(-1000, 0, 1000, 0);
		g.drawLine(0, -1000,  0,  1000);
		/* Draw specified viewport */
		if(vx1field.getText().isEmpty() || vy1field.getText().isEmpty() || 
				vx2field.getText().isEmpty() || vy2field.getText().isEmpty() ||
				!vx1field.getText().matches("^-?\\d+$") || !vy1field.getText().matches("^-?\\d+$") ||
				!vx2field.getText().matches("^-?\\d+$") || !vy2field.getText().matches("^-?\\d+$")){
		}else{
			int vx1 = Integer.parseInt(vx1field.getText());
			int vy1 = Integer.parseInt(vy1field.getText());
			int vx2 = Integer.parseInt(vx2field.getText());
			int vy2 = Integer.parseInt(vx2field.getText());
			g.setClip(vx1, vy1, vx2, vy2);
			g.setColor(Color.red);
			g.drawRect(vx1, vy1, vx2-1, vy2-1);
		}

		if(drawfileswitch == true){
			TransformPackage.applyTransformation(transformationMatrix, lines);
			drawfileswitch = false;
		}
		for(int i = 0; i < lines.size(); i++){
			Line line = lines.get(i);
			int factor = 1000;
			g.setColor(Color.black);
			int x1 = (int) ((line.x1 * factor) / (line.z1 + factor));
			int y1 = (int) ((line.y1 * factor) / (line.z1 + factor));
			int x2 = (int) ((line.x2 * factor) / (line.z2 + factor));
			int y2 = (int) ((line.y2 * factor) / (line.z2 + factor));
			g.drawLine(x1, -y1, x2, -y2);
		}

	}


	/** ----------------------------------------------------------
	 * ArrayToString - convert a double array to an html formatted string
	 * that is printable in a JLabel. 
	 * @param matrix[][]: the double array to print */
	public static JTable ArrayToTable(double matrix[][]) {
		JTable table = new JTable();
		//table.setTableHeader(null);
		Object[][] values;
		Object[] header;
		Object[] columnNames = new Object[1];
		if(matrix.length > 3){
			values = new Object[4][4];
		}else{
			values = new Object[3][3];
		}
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				values[row][column] = (Object) (matrix[row][column]);	
				System.out.println(values[row][column].toString());
			}
		}
		if(matrix.length > 3){
			Object [] x = { "1", "2", "3", "4"};
			header = x;
		}else{
			Object [] x = { "1", "2", "3"};
			header = x;
		}
		table.setModel(new javax.swing.table.DefaultTableModel(values, header));
		return table;
	}


	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * STARTING MAIN METHOD	
	 * Initializes swing components (buttons, textboxes, etc) and graphics desktop */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("Graphics Package");
		frame.setBackground(Color.WHITE);
		frame.setForeground(Color.WHITE);
		frame.setSize(1100, 900);
		JPanel northPanel = new JPanel(new GridLayout(0, 1));
		JPanel eastPanel = new JPanel(new GridLayout(0, 1));
		JPanel southPanel = new JPanel(new GridLayout(0, 1));
		frame.add(northPanel,  BorderLayout.NORTH);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.add(eastPanel, BorderLayout.EAST);


		JToolBar toolbar = new JToolBar("Still draggable");
		JButton displaySlider = new JButton("Display Transformation Sliders");
		toolbar.add(displaySlider);
		northPanel.add(toolbar);

		scan.setBackground(Color.DARK_GRAY);

		JDesktopPane desktop = new JDesktopPane();
		frame.getContentPane().add(desktop, BorderLayout.CENTER);
		desktop.setBorder(new LineBorder(Color.black));

		JToolBar sliderToolBar = new JToolBar("Transformation Sliders");
		JPanel sliderPanel = new JPanel();
		sliderPanel.setBorder(new LineBorder(Color.gray));
		southPanel.add(sliderToolBar);
		sliderToolBar.setOrientation(1);
		sliderToolBar.setBorder(new LineBorder(Color.black));
		sliderToolBar.setFloatable(true);
		sliderToolBar.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				sliderToolBar.setOrientation(JToolBar.VERTICAL);
			}
		});
		sliderToolBar.addSeparator();
		sliderToolBar.add(sliderPanel);

		JInternalFrame canvasFrame = new JInternalFrame("Canvas", false, false, false, false);
		desktop.add(canvasFrame);

		//sliderToolBar.setPreferredSize(new Dimension(700, 150));
		JToolBar transformToolBar = new JToolBar("Coordinate Transformations");
		eastPanel.add(transformToolBar);
		JPanel transformPanel = new JPanel();
		transformToolBar.setOrientation(1);
		transformToolBar.addSeparator();
		transformToolBar.add(transformPanel);
		transformToolBar.setFloatable(true);
		transformToolBar.setBorder(new LineBorder(Color.black));
		transformToolBar.addHierarchyListener(new HierarchyListener() {
			public void hierarchyChanged(HierarchyEvent e) {
				transformToolBar.setOrientation(JToolBar.VERTICAL);
			}
		});
		transformPanel.setBorder(new LineBorder(Color.gray));
		


		BasicInternalFrameUI canvas = (BasicInternalFrameUI) canvasFrame.getUI();
		canvas.setNorthPane(null);
		canvasFrame.getContentPane().add(scan);
		canvasFrame.setSize(200, 200);
		canvasFrame.setResizable(true);
		canvasFrame.setLocation(500,500);
		canvasFrame.setVisible(true);
		canvasFrame.setBorder(new LineBorder(Color.black));
		canvasFrame.toBack();


		frame.addComponentListener(new ComponentListener() { 
			public void componentResized(ComponentEvent e) {
				//transformToolBar.setLocation(frame.getContentPane().getWidth()-220, 0);
				//transformToolBar.setSize(220, frame.getContentPane().getHeight()-200);
				//sliderToolBar.setLocation(0, frame.getHeight()-200);
				//sliderToolBar.setSize(frame.getContentPane().getWidth(), 150);
			}
			public void componentHidden(ComponentEvent e) {
			}
			public void componentMoved(ComponentEvent e) {
			}
			public void componentShown(ComponentEvent e) {
			}});

		//frame.getContentPane().add(desktop, BorderLayout.NORTH);
		//frame.getContentPane().add(sliderToolBar, BorderLayout.SOUTH);
		//frame.getContentPane().add(transformPanel, BorderLayout.EAST);
		//desktop.add(scan);

		try {
			canvasFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			System.out.println("ERROR: setMaximum canvasFrame PropertyVetoException.");
		}
		canvasFrame.addInternalFrameListener(new InternalFrameListener() {
			public void internalFrameActivated(InternalFrameEvent e) {
				canvasFrame.toBack();
			}
			public void internalFrameClosed(InternalFrameEvent e) {		
			}
			public void internalFrameClosing(InternalFrameEvent e) {			
			}
			public void internalFrameDeactivated(InternalFrameEvent e) {			
			}
			public void internalFrameDeiconified(InternalFrameEvent e) {			
			}
			public void internalFrameIconified(InternalFrameEvent e) {			
			}
			public void internalFrameOpened(InternalFrameEvent e) {			
			}
		});    
		/* East desktop */
		transformPanel.setLayout(new BoxLayout(transformPanel, BoxLayout.PAGE_AXIS));

		/* South (Slider) desktop */
		transformPanel.setLayout(new BoxLayout(transformPanel, BoxLayout.PAGE_AXIS));

		//Translation
		JPanel translatePanel = new JPanel();
		translatePanel.setLayout(new BoxLayout(translatePanel, BoxLayout.PAGE_AXIS));
		JLabel txLabel = new JLabel("X displacement (Tx)");
		JLabel tyLabel = new JLabel("Y displacement (Ty)");
		JLabel tzLabel = new JLabel("Z displacement (Tz)");

		JTextField txField = new JTextField();
		JTextField tyField = new JTextField();
		JTextField tzField = new JTextField();


		JButton translateButton= new JButton("Add Translate");
		translateButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {
			if(txField.getText().isEmpty() || tyField.getText().isEmpty() || tzField.getText().isEmpty()
					|| !txField.getText().matches("^-?\\d+$") || !tyField.getText().matches("^-?\\d+$")){
				System.out.println("No valid translation from input");
			}else{
				int tx = Integer.valueOf(txField.getText());
				int ty = Integer.valueOf(tyField.getText());
				int tz = Integer.valueOf(tzField.getText());

				if(firstTrans){
					transformationMatrix = TransformPackage.translate3D(tx, ty, tz); //FIX
					firstTrans = false;
				}else{
					transformationMatrix = TransformPackage.concatenate(transformationMatrix, TransformPackage.translate3D(tx, ty, tz));
				}
				matrixTable = (ArrayToTable(transformationMatrix.getArrayCopy()));
				
			}
		}
		});    
		translatePanel.add(Box.createRigidArea(new Dimension(0,10)));
		translatePanel.add(txLabel);
		translatePanel.add(txField);
		translatePanel.add(tyLabel);
		translatePanel.add(tyField);
		translatePanel.add(tzLabel);
		translatePanel.add(tzField);
		translatePanel.add(translateButton);

		//Rotation
		JPanel rotatePanel = new JPanel();
		rotatePanel.setLayout(new BoxLayout(rotatePanel, BoxLayout.PAGE_AXIS));
		JLabel angleLabel = new JLabel("Angle (in degrees)");
		JTextField angleField = new JTextField();
		@SuppressWarnings({ "rawtypes" })
		JLabel axismenuLabel = new JLabel("Axis");
		JComboBox menu = new JComboBox(new String[]{"x", "y", "z"});
		JButton rotateButton = new JButton("Add Rotation");
		rotateButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {
			if(angleField.getText().isEmpty() || !angleField.getText().matches("^-?\\d+$")){
				System.out.println("No valid rotation from input");
			}else{
				int angle = Integer.valueOf(angleField.getText());
				char axis = 'x';
				if( ((String)menu.getSelectedItem()).equalsIgnoreCase("x") ){
					axis = 'x';
				}else if( ((String)menu.getSelectedItem()).equalsIgnoreCase("y") ){
					axis = 'y';
				}else if( ((String)menu.getSelectedItem()).equalsIgnoreCase("z") ){
					axis = 'z';
				}
				if(firstTrans){
					transformationMatrix = TransformPackage.rotate3D(angle, axis);
					firstTrans = false;
				}else{
					transformationMatrix = TransformPackage.concatenate(transformationMatrix, TransformPackage.rotate3D(angle, axis));
				}
				matrixTable = (ArrayToTable(transformationMatrix.getArrayCopy()));
			}
		}
		});    
		rotatePanel.add(Box.createRigidArea(new Dimension(0,10)));
		rotatePanel.add(angleLabel);
		rotatePanel.add(angleField);
		rotatePanel.add(axismenuLabel);
		rotatePanel.add(menu);
		rotatePanel.add(rotateButton);


		//Scale
		JPanel scalePanel = new JPanel();
		scalePanel.setLayout(new BoxLayout(scalePanel, BoxLayout.PAGE_AXIS));
		JLabel sxLabel = new JLabel("Scaling x axis");
		JLabel syLabel = new JLabel("Scaling y axis");
		JLabel szLabel = new JLabel("Scaling z axis");

		JTextField sxField = new JTextField();
		JTextField syField = new JTextField();
		JTextField szField = new JTextField();

		JButton scaleButton = new JButton("Add Scaling");
		scaleButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {
			if(sxField.getText().isEmpty() || syField.getText().isEmpty() || szField.getText().isEmpty()
					){
				System.out.println("No valid scaling from input");
			}else{
				double sx = Double.valueOf(sxField.getText());
				double sy = Double.valueOf(syField.getText());
				double sz = Double.valueOf(szField.getText());

				if(firstTrans){
					transformationMatrix = TransformPackage.scale3D(sx, sy, sz);
					firstTrans = false;
				}else{
					transformationMatrix = TransformPackage.concatenate(transformationMatrix, TransformPackage.scale3D(sx, sy, sz));
				}
				matrixTable = (ArrayToTable(transformationMatrix.getArrayCopy()));
			}
		}
		});    
		scalePanel.add(Box.createRigidArea(new Dimension(0,10)));
		scalePanel.add(sxLabel);
		scalePanel.add(sxField);
		scalePanel.add(syLabel);
		scalePanel.add(syField);
		scalePanel.add(szLabel);
		scalePanel.add(szField);
		scalePanel.add(scaleButton);

		JTabbedPane transformOptions = new JTabbedPane();
		transformOptions.addTab("Translate", null, translatePanel, "Basic translation matrix");
		transformOptions.addTab("Rotate", null,	rotatePanel, "Basic rotation matrix"); 
		transformOptions.addTab("Scale", null,	scalePanel, "Basic scaling matrix"); 
		transformPanel.add(transformOptions);

		transformPanel.add(Box.createRigidArea(new Dimension(0,100)));
		JLabel viewportLabel = new JLabel("Viewport: ");

		transformPanel.add(viewportLabel);
		transformPanel.add(vx1field);
		transformPanel.add(vy1field);
		transformPanel.add(vx2field);
		transformPanel.add(vy2field);

		matrixTable = (ArrayToTable(transformationMatrix.getArrayCopy()));
		matrixTable.setFillsViewportHeight(true);
		matrixTable.setDragEnabled(false);
		matrixTable.setTableHeader(null);
		
		transformPanel.add(Box.createRigidArea(new Dimension(0,100)));
		JScrollPane tableContainer = new JScrollPane(matrixTable);
	

		tableContainer.setViewportView(matrixTable);
		transformToolBar.add(tableContainer);
		//transformPanel.add(tableContainer);
		JButton applyTransformButton = new JButton("TRANSFORM FILE LINES");
		applyTransformButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {
			TransformPackage.applyTransformation(transformationMatrix, lines);
			firstTrans = true;
			transformationMatrix = new Matrix(new double[][]{
				{0, 0, 0, 0},
				{0, 0, 0, 0}, 
				{0, 0, 0, 0}, 
				{0, 0, 0, 0}
			});
			matrixTable = (ArrayToTable(transformationMatrix.getArrayCopy()));
			}
		});    
		transformPanel.add(applyTransformButton);


		/* North desktop */
		JLabel lblX = new JLabel("X1:");
		desktop.add(lblX);
		X1Field = new JTextField();
		X1Field.setText("50");
		desktop.add(X1Field);
		X1Field.setColumns(5);

		JLabel lblY = new JLabel("Y1:");
		desktop.add(lblY);
		Y1Field = new JTextField();
		Y1Field.setText("50");
		Y1Field.setColumns(5);
		desktop.add(Y1Field);

		JLabel lblX_1 = new JLabel("X2:");
		desktop.add(lblX_1);
		X2Field = new JTextField();
		X2Field.setColumns(5);
		X2Field.setText("100");
		desktop.add(X2Field);

		JLabel lblY_1 = new JLabel("Y2:");
		desktop.add(lblY_1);
		Y2Field = new JTextField();
		Y2Field.setColumns(5);
		Y2Field.setText("100");
		desktop.add(Y2Field);
		desktop.add(new JSeparator(SwingConstants.VERTICAL));

		JLabel dashLabel = new JLabel("Dash Length:");
		desktop.add(dashLabel);
		dashField = new JTextField();
		dashField.setColumns(5);
		dashField.setText("0");
		desktop.add(dashField);
		JButton drawButton = new JButton("Draw");
		drawButton.setBackground(Color.YELLOW);
		drawButton.setToolTipText("Draw current line");
		desktop.add(drawButton);

		/* South desktop */
		JFileChooser chooser = new JFileChooser();

		JButton drawFileButton = new JButton("Display");
		drawFileButton.setBackground(Color.CYAN);
		drawFileButton.setToolTipText("Generate random lines");
		sliderPanel.add(drawFileButton);
		/* Rotation Slider Labels */
		Hashtable<Integer, JLabel> degreesLabel = new Hashtable<Integer, JLabel>();
		degreesLabel.put( new Integer( 0 ), new JLabel("0") );
		degreesLabel.put( new Integer( 180 ), new JLabel("180") );
		degreesLabel.put( new Integer( -180 ), new JLabel("-180") );
		//x rotation slider
		JSlider xRotateSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		xRotateSlider.setMinorTickSpacing(5);
		xRotateSlider.setPaintTicks(true);
		xRotateSlider.setPaintLabels(true);
		xRotateSlider.setLabelTable(degreesLabel);
		xRotateSlider.setPreferredSize(new Dimension(600, 50));
		setSliderTickFont(xRotateSlider, new Font("Arial", Font.PLAIN, 9));
		//y rotation slider
		JSlider yRotateSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		yRotateSlider.setMinorTickSpacing(5);
		yRotateSlider.setPaintTicks(true);
		yRotateSlider.setPaintLabels(true);
		yRotateSlider.setLabelTable(degreesLabel);
		yRotateSlider.setPreferredSize(new Dimension(600, 50));
		setSliderTickFont(yRotateSlider, new Font("Arial", Font.PLAIN, 9));
		//z rotation slider
		JSlider zRotateSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		zRotateSlider.setMinorTickSpacing(5);
		zRotateSlider.setPaintTicks(true);
		zRotateSlider.setPaintLabels(true);
		zRotateSlider.setLabelTable(degreesLabel);
		zRotateSlider.setPreferredSize(new Dimension(600, 50));
		setSliderTickFont(zRotateSlider, new Font("Arial", Font.PLAIN, 9));

		sliderPanel.add(xRotateSlider);
		sliderPanel.add(yRotateSlider);
		sliderPanel.add(zRotateSlider);


		//x rotation slider actions
		xRotateSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				TransformPackage.applyTransformation(TransformPackage.rotate3D(xRotateSlider.getValue() - xRotation, 'x'), lines);
				xRotation = xRotateSlider.getValue();
				scan.repaint();
				System.out.println(xRotateSlider.getValue());
			}
		});
		//y rotation slider actions
		yRotateSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				TransformPackage.applyTransformation(TransformPackage.rotate3D(yRotateSlider.getValue() - yRotation, 'y'), lines);
				yRotation = yRotateSlider.getValue();
				scan.repaint();
				System.out.println(yRotateSlider.getValue());
			}
		});
		//z rotation slider actions
		zRotateSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				TransformPackage.applyTransformation(TransformPackage.rotate3D(zRotateSlider.getValue() - zRotation, 'z'), lines);
				zRotation = zRotateSlider.getValue();
				scan.repaint();
				System.out.println(zRotateSlider.getValue());
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
		drawFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawfileswitch = true;
				//TRANSFORM HERE
				scan.repaint();
			}
		});    


		frame.setVisible(true);
	}

}