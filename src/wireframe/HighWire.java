package wireframe;
//Jacob Webber
//CSCI 4210
//Line drawing algorithms

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.event.FocusAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.beans.PropertyVetoException;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.TableColumn;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import Jama.Matrix;
import javafx.scene.input.KeyCode;

public class HighWire extends JPanel{
	public HighWire() {
	}
	public static 		HighWire scan = new HighWire();
	public static ArrayList<Line> lines = new ArrayList<Line>();
	public static ArrayList<Line2D> lines2D = new ArrayList<Line2D>();

	public static JTextField vx1field = new JTextField("Vx1");
	public  static JTextField vy1field = new JTextField("Vy1");
	public static JTextField vx2field = new JTextField("Vx2");
	public static JTextField vy2field = new JTextField("Vy2");
	public static JScrollPane tableContainer;
	public static boolean drawfileswitch;
	public static boolean firstTrans = true;
	public static boolean perspectiveProjection = true;
	public static boolean selectObject = true;
	public static boolean selectAll = true;
	public static boolean delete = false;
	public static boolean enableDrag = true;
	public static boolean moveZAxis = false;
	public static Timer timer;
	public static 		JFileChooser chooser = new JFileChooser();
	public static ImageIcon perspectiveIcon;
	public static ImageIcon parallelIcon;
	public static ImageIcon lineSelectIcon;
	public static ImageIcon objectSelectIcon;
	public static ImageIcon eraserIcon;
	public static ImageIcon eraserSelectedIcon;
	public static ImageIcon selectAllNonIcon;
	public static ImageIcon selectAllIcon;


	public static JButton depthImage = new JButton();
	public static JButton projectionButton = new JButton();
	public static JButton xRotateButton = new JButton();
	public static JButton yRotateButton = new JButton();
	public static JButton zRotateButton = new JButton();
	public static int depthValue = 1000;
	public static boolean closeDepthDialog = false;
	public static ArrayList<Integer> history = new ArrayList<Integer>();
	public static ArrayList<Boolean> linesHighlight = new ArrayList<Boolean>();
	public static Image eraserImage = new ImageIcon(HighWire.class.getResource("/eraser.png")).getImage();

	private static final int HIT_BOX_SIZE = 3;

	//Previous rotation slider values
	public static int xRotation = 0;
	public static int yRotation = 0;
	public static int zRotation = 0;

	public static int mousex;
	public static int mousey;
	public static int pressmousex;
	public static int pressmousey;
	public static int dragmousex;
	public static int dragmousey;

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
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);		
		/* Draw XY coordinate system */
		Dimension panelDimensions = scan.getSize();
		int width = panelDimensions.width;
		int height = panelDimensions.height;
		g.translate(width/2, height/2);		
		if(delete && mousex > -width/2 && mousex < width/2 && mousey > -height/2 && mousey < height/2){
			g.drawImage(eraserImage, mousex, mousey, null );
		}
		g.drawString(String.valueOf(height/2), 0, -(height/2)+10 );
		if(width < 1000){
			g.drawString(String.valueOf(width/2), (width/2)-20, 0);
		}else{
			g.drawString(String.valueOf(width/2), (width/2)-26, 0);
		}
		g.setColor(Color.gray);
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
		lines2D.clear();
		for(int i = 0; i < lines.size(); i++){
			Line line = lines.get(i);
			g.setColor(Color.white);
			int x1 = (int) line.x1;
			int y1 = (int) line.y1;
			int x2 = (int) line.x2;
			int y2 = (int) line.y2;
			if(perspectiveProjection){ //modify
				x1 = (int) ((line.x1 * depthValue) / (line.z1 + depthValue));
				y1 = (int) ((line.y1 * depthValue) / (line.z1 + depthValue));
				x2 = (int) ((line.x2 * depthValue) / (line.z2 + depthValue));
				y2 = (int) ((line.y2 * depthValue) / (line.z2 + depthValue));
			}
			if(line.isEditable){
				g.setColor(Color.white);
			}else{
				g.setColor(Color.red);
			}
			g.drawLine(x1, -y1, x2, -y2);
			lines2D.add(new Line2D.Float(x1, -y1, x2, -y2)); //coordinates for visual representation of line
		}
	}

	public static boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		}
		catch (NumberFormatException e) {
			System.out.println(input + "is not double");
			return false;
		}
	}


	/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * STARTING MAIN METHOD	
	 * Initializes swing components (buttons, textboxes, etc) and graphics desktop */
	public static void main(String[] args)  {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setTitle("HighWire3D");
		frame.setBackground(Color.WHITE);
		frame.setForeground(Color.WHITE);
		frame.setSize(1500, 900);
		JPanel northPanel = new JPanel(new GridLayout(0, 1));
		JPanel eastPanel = new JPanel(new GridLayout(0, 1));
		JPanel southPanel = new JPanel(new GridLayout(0, 1));
		frame.add(northPanel,  BorderLayout.NORTH);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.add(eastPanel, BorderLayout.EAST);

		/* ------- Mouse click listener ------------ */
		scan.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e) {
				Dimension panelDimensions = scan.getSize();
				int wchange = panelDimensions.width / 2;
				int hchange = panelDimensions.height / 2;
				dragmousex = e.getPoint().x - wchange;
				dragmousey = e.getPoint().y - hchange;
				int x = (int) ((e.getPoint().x - wchange) - HIT_BOX_SIZE / 2);
				int y = (int) ((e.getPoint().y - hchange) - HIT_BOX_SIZE / 2);
				int width = HIT_BOX_SIZE;
				int height = HIT_BOX_SIZE;
				int upperBound = 0; //Upper boundary of object line selection
				int lowerBound = 0; //Lower boundary of object line selection
				boolean lineIsEditable = true;
				Rectangle rect = new Rectangle(x, y, width, height); //Mouse click area as rect
				//Selecting Entire Object
				if(selectObject){
					for(int i = 0; i < lines.size(); i++){
						Line line = lines.get(i);
						Line2D line2D = lines2D.get(i);
						if(line2D.intersects(rect)){ //Line clicked
							enableDrag = false;
							lineIsEditable = line.isEditable;
							int j = 0;
							while(upperBound <= i){
								upperBound += history.get(j);
								j++;
							}
							lowerBound = upperBound - history.get(j-1);
							if(delete){
								history.remove(j-1);
							}
							i = lines.size();
						}
					}
					for(int i = upperBound-1; i >= lowerBound; i--){
						if(delete){
							lines.remove(i);
							lines2D.remove(i);
						}else{
							Line line = lines.get(i);
							line.isEditable = !lineIsEditable;
							lines.set(i, line);
						}
					}
					scan.repaint();
				}else{ //Selecting Individual lines
					for(int i = 0; i < lines.size(); i++){
						Line line = lines.get(i);
						Line2D line2D = lines2D.get(i);
						if(line2D.intersects(rect)){ //Line clicked
							enableDrag = false;
							lineIsEditable = line.isEditable;
							int j = 0;
							if(delete){
								while(upperBound <= i){
									upperBound += history.get(j);
									j++;
								}
								if(history.get(j-1) > 1){
									history.set(j-1, history.get(j-1)-1);
								}else{
									history.remove(j-1);
								}
								lines.remove(i);
								lines2D.remove(i);
							}else{
								line.isEditable = !line.isEditable;
								lines.set(i, line);
							}
							scan.repaint();
						}
					}
				} //end of individual line selection
			}//end of actionListener
			public void mouseReleased(MouseEvent e){
				enableDrag = true;
			}
		});

		/* ------- Mouse movement listener ------------ */
		scan.addMouseMotionListener(new MouseAdapter(){
			public void mouseMoved(MouseEvent e) { // Following mouse movement
				Dimension panelDimensions = scan.getSize();
				int wchange= panelDimensions.width / 2;
				int hchange= panelDimensions.height / 2;
				mousex = (int) ((e.getPoint().x - wchange)) - 2;
				mousey = (int) ((e.getPoint().y - hchange)) - 25;
				scan.repaint();
			}
			public void mouseDragged(MouseEvent e){
				Dimension panelDimensions = scan.getSize();
				int wchange= panelDimensions.width / 2;
				int hchange= panelDimensions.height / 2;
				if(!delete && enableDrag && SwingUtilities.isLeftMouseButton(e)){
					if(e.isControlDown()){
						TransformPackage.applyTransformation(TransformPackage.translate3D(0, 0, -(e.getPoint().y - hchange - dragmousey)), lines);
					}else{
						TransformPackage.applyTransformation(TransformPackage.translate3D(e.getPoint().x - wchange - dragmousex, 0, 0), lines);
						TransformPackage.applyTransformation(TransformPackage.translate3D(0, -(e.getPoint().y - hchange - dragmousey), 0), lines);
					}
				}else if(!delete && enableDrag && SwingUtilities.isRightMouseButton(e)){
					if(e.isControlDown()){
						TransformPackage.applyTransformation(TransformPackage.rotate3D((e.getPoint().y - hchange - dragmousey), 'z'), lines);
					}else{
						TransformPackage.applyTransformation(TransformPackage.rotate3D((e.getPoint().y - hchange - dragmousey), 'x'), lines);
						TransformPackage.applyTransformation(TransformPackage.rotate3D((e.getPoint().x - wchange - dragmousex), 'y'), lines);
					}
				}
				dragmousex = e.getPoint().x - wchange;
				dragmousey = e.getPoint().y - hchange;
				scan.repaint();
			}
		});
		scan.addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent e) {
				if(!delete){
					if(e.getWheelRotation() > 0){
						TransformPackage.applyTransformation(TransformPackage.scale3D(1.05, 1, 1), lines);
						TransformPackage.applyTransformation(TransformPackage.scale3D(1, 1.05, 1), lines);
						TransformPackage.applyTransformation(TransformPackage.scale3D(1, 1, 1.05), lines);
					}else{
						TransformPackage.applyTransformation(TransformPackage.scale3D(.95, 1, 1), lines);
						TransformPackage.applyTransformation(TransformPackage.scale3D(1, .95, 1), lines);
						TransformPackage.applyTransformation(TransformPackage.scale3D(1, 1, .95), lines);
					}
				}
				scan.repaint();
			}

		});

		scan.setBackground(Color.darkGray);
		scan.setOpaque(true);

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
		/* Highlight TextField */
		FocusAdapter focusedTextField = new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						JTextField event = (JTextField) evt.getSource();
						event.selectAll();
						//sxField.selectAll();
					}
				});
			}
		};

		vx1field.addFocusListener(focusedTextField);
		vx2field.addFocusListener(focusedTextField);
		vy1field.addFocusListener(focusedTextField);
		vy2field.addFocusListener(focusedTextField);

		/* ------------------ Translation Panel -------------------- */
		JPanel translatePanel = new JPanel();
		JLabel txLabel = new JLabel("Tx");
		JLabel tyLabel = new JLabel("Ty");
		JLabel tzLabel = new JLabel("Tz");
		JTextField txField = new JTextField("0");
		JTextField tyField = new JTextField("0");
		JTextField tzField = new JTextField("0");
		txField.addFocusListener(focusedTextField);
		tyField.addFocusListener(focusedTextField);
		tzField.addFocusListener(focusedTextField);

		JButton translateButton= new JButton("Add Translation");
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
				matrixTable = (TransformPackage.ArrayToTable(transformationMatrix.getArrayCopy()));
				matrixTable.setTableHeader(null);
				tableContainer.getViewport().add(matrixTable);
				txField.setText("0");
				tyField.setText("0");
				tzField.setText("0");
			}
		}
		});    
		translatePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(25,4,4,4);
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		translatePanel.add(txLabel, c);
		c.gridx = 1;
		translatePanel.add(txField, c);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		translatePanel.add(tyLabel, c);
		c.gridx = 1;
		translatePanel.add(tyField, c);
		c.gridx = 0;
		c.gridy = 2;
		translatePanel.add(tzLabel, c);
		c.gridx = 1;
		translatePanel.add(tzField, c);
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.SOUTH;
		translatePanel.add(translateButton, c);


		/* ------------------ Rotation Panel -------------------- */
		JPanel rotatePanel = new JPanel();
		JLabel angleLabel = new JLabel("Angle");
		JTextField angleField = new JTextField("0");
		angleField.addFocusListener(focusedTextField);

		@SuppressWarnings({ })
		JLabel axismenuLabel = new JLabel("Axis");
		JComboBox<Object> axisMenu = new JComboBox<Object>(new String[]{"x", "y", "z"});
		JButton rotateButton = new JButton("Add Rotation");
		rotateButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {
			if(angleField.getText().isEmpty() || !angleField.getText().matches("^-?\\d+$")){
				System.out.println("No valid rotation from input");
			}else{
				int angle = Integer.valueOf(angleField.getText());
				char axis = 'x';
				if( ((String)axisMenu.getSelectedItem()).equalsIgnoreCase("x") ){
					axis = 'x';
				}else if( ((String)axisMenu.getSelectedItem()).equalsIgnoreCase("y") ){
					axis = 'y';
				}else if( ((String)axisMenu.getSelectedItem()).equalsIgnoreCase("z") ){
					axis = 'z';
				}
				if(firstTrans){
					transformationMatrix = TransformPackage.rotate3D(angle, axis);
					firstTrans = false;
				}else{
					transformationMatrix = TransformPackage.concatenate(transformationMatrix, TransformPackage.rotate3D(angle, axis));
				}
				matrixTable = (TransformPackage.ArrayToTable(transformationMatrix.getArrayCopy()));
				matrixTable.setTableHeader(null);
				tableContainer.getViewport().add(matrixTable);
				angleField.setText("0");

			}
		}
		});    

		rotatePanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		rotatePanel.add(angleLabel, c);
		c.gridx = 1;
		rotatePanel.add(angleField, c);
		c.weightx = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.gridy = 1;
		rotatePanel.add(axismenuLabel, c);
		c.gridx = 1;
		rotatePanel.add(axisMenu, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.SOUTH;
		rotatePanel.add(rotateButton, c);

		//Scale
		JPanel scalePanel = new JPanel();
		JLabel sxLabel = new JLabel("Sx");
		JLabel syLabel = new JLabel("Sy");
		JLabel szLabel = new JLabel("Sz");

		JTextField sxField = new JTextField("1");
		JTextField syField = new JTextField("1");
		JTextField szField = new JTextField("1");

		sxField.addFocusListener(focusedTextField);
		syField.addFocusListener(focusedTextField);
		szField.addFocusListener(focusedTextField);


		JButton scaleButton = new JButton("Add Scaling");
		scaleButton.addActionListener(new ActionListener() { @Override
			public void actionPerformed(ActionEvent arg0) {

			if(sxField.getText().isEmpty() || syField.getText().isEmpty() || szField.getText().isEmpty() 
					|| !isDouble(sxField.getText()) || !isDouble(syField.getText()) || !isDouble(szField.getText()) 
					|| (sxField.getText().equalsIgnoreCase("0") && syField.getText().equalsIgnoreCase("0") && szField.getText().equalsIgnoreCase("0"))){
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
				matrixTable = (TransformPackage.ArrayToTable(transformationMatrix.getArrayCopy()));
				matrixTable.setTableHeader(null);
				tableContainer.getViewport().add(matrixTable);
				sxField.setText("1");
				syField.setText("1");
				szField.setText("1");
			}
		}
		});    
		scalePanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(25,4,4,4);
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		scalePanel.add(sxLabel, c);
		c.gridx = 1;
		scalePanel.add(sxField, c);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 1;
		scalePanel.add(syLabel, c);
		c.gridx = 1;
		scalePanel.add(syField, c);
		c.gridx = 0;
		c.gridy = 2;
		scalePanel.add(szLabel, c);
		c.gridx = 1;
		scalePanel.add(szField, c);
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weighty = 1;
		c.anchor = GridBagConstraints.SOUTH;
		scalePanel.add(scaleButton, c);


		JTabbedPane transformOptions = new JTabbedPane();
		transformOptions.addTab("Translate", null, translatePanel, "Basic translation matrix");
		transformOptions.addTab("Rotate", null,	rotatePanel, "Basic rotation matrix"); 
		transformOptions.addTab("Scale", null,	scalePanel, "Basic scaling matrix"); 

		transformPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(0,0,0,0);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 4;
		c.weightx = 20;
		c.weighty = 20;
		transformPanel.add(transformOptions, c);
		c.gridy = 1;
		c.fill = GridBagConstraints.CENTER;
		c.insets = new Insets(20,0,5,0);
		JLabel viewportLabel = new JLabel("Viewport: ");
		transformPanel.add(viewportLabel, c);
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 1;
		c.gridy = 2;
		c.gridx = 0;
		transformPanel.add(vx1field, c);
		c.gridx = 1;
		transformPanel.add(vy1field, c);
		c.gridx = 2;
		transformPanel.add(vx2field, c);
		c.gridx = 3;
		transformPanel.add(vy2field, c);
		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 5;
		c.insets = new Insets(50,4,4,4);
		c.fill = GridBagConstraints.HORIZONTAL;

		matrixTable = (TransformPackage.ArrayToTable(transformationMatrix.getArrayCopy()));
		matrixTable.setFillsViewportHeight(true);
		matrixTable.setDragEnabled(false);
		matrixTable.setTableHeader(null);
		TableColumn column = null;
		for (int i = 0; i <= 3; i++) {
			column = matrixTable.getColumnModel().getColumn(i);
			column.setWidth(5);
			column.setPreferredWidth(5);

		}   
		transformPanel.add(Box.createRigidArea(new Dimension(0,100)));
		tableContainer = new JScrollPane();
		tableContainer.getViewport().add(matrixTable);


		transformToolBar.add(tableContainer);
		transformToolBar.setPreferredSize(new Dimension(200, 800));


		//	transformPanel.add(applyTransformButton);
		JButton applyTransButton = new JButton("Apply Transformation");
		applyTransButton.setBackground(new Color(150, 150, 250));
		applyTransButton.setToolTipText("Generate random lines");
		transformPanel.add(applyTransButton, c);


		/* South desktop */
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
		xRotateSlider.setPreferredSize(new Dimension(400, 40));
		setSliderTickFont(xRotateSlider, new Font("Arial", Font.PLAIN, 9));
		//y rotation slider
		JSlider yRotateSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		yRotateSlider.setMinorTickSpacing(5);
		yRotateSlider.setPaintTicks(true);
		yRotateSlider.setPaintLabels(true);
		yRotateSlider.setLabelTable(degreesLabel);
		yRotateSlider.setPreferredSize(new Dimension(400, 40));
		setSliderTickFont(yRotateSlider, new Font("Arial", Font.PLAIN, 9));
		//z rotation slider
		JSlider zRotateSlider = new JSlider(JSlider.HORIZONTAL, -180, 180, 0);
		zRotateSlider.setMinorTickSpacing(5);
		zRotateSlider.setPaintTicks(true);
		zRotateSlider.setPaintLabels(true);
		zRotateSlider.setLabelTable(degreesLabel);
		zRotateSlider.setPreferredSize(new Dimension(400, 40));
		setSliderTickFont(zRotateSlider, new Font("Arial", Font.PLAIN, 9));

		xRotateButton = new JButton(new ImageIcon(HighWire.class.getResource("/zero.png")));
		xRotateButton.setPressedIcon(new ImageIcon(HighWire.class.getResource("/zeropressed.png")));
		xRotateButton.setRolloverIcon(new ImageIcon(HighWire.class.getResource("/zerohover.png")));
		yRotateButton = new JButton(new ImageIcon(HighWire.class.getResource("/zero.png")));
		yRotateButton.setPressedIcon(new ImageIcon(HighWire.class.getResource("/zeropressed.png")));
		yRotateButton.setRolloverIcon(new ImageIcon(HighWire.class.getResource("/zerohover.png")));
		zRotateButton = new JButton(new ImageIcon(HighWire.class.getResource("/zero.png")));
		zRotateButton.setPressedIcon(new ImageIcon(HighWire.class.getResource("/zeropressed.png")));
		zRotateButton.setRolloverIcon(new ImageIcon(HighWire.class.getResource("/zerohover.png")));
		ActionListener sliderResetAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(event.getSource() == xRotateButton){
					xRotateSlider.setValue(0);
					scan.repaint();
				}else if(event.getSource() == yRotateButton){
					yRotateSlider.setValue(0);
					scan.repaint();
				}if(event.getSource() == zRotateButton){
					zRotateSlider.setValue(0);
					scan.repaint();
				}
			}
		};
		xRotateButton.addActionListener(sliderResetAction);
		xRotateButton.setBorderPainted(false);
		xRotateButton.setFocusPainted(false);
		xRotateButton.setContentAreaFilled(false);
		zRotateButton.addActionListener(sliderResetAction);
		zRotateButton.setBorderPainted(false);
		zRotateButton.setFocusPainted(false);
		zRotateButton.setContentAreaFilled(false);
		yRotateButton.addActionListener(sliderResetAction);
		yRotateButton.setBorderPainted(false);
		yRotateButton.setFocusPainted(false);
		yRotateButton.setContentAreaFilled(false);

		sliderPanel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.CENTER;
		c.weightx = c.weighty = 10;
		c.gridx = 0;
		c.gridy = 0;
		JLabel xLabel = new JLabel("               Rotate X");
		sliderPanel.add(xLabel,  c);
		c.gridx = 1;
		c.weightx = c.weighty = 0;
		sliderPanel.add(xRotateButton, c);
		c.gridx = 2;
		c.weightx = c.weighty = 10;
		JLabel yLabel = new JLabel("               Rotate Y");
		sliderPanel.add(yLabel,  c);
		c.gridx = 3;
		c.weightx = c.weighty = 0;
		sliderPanel.add(yRotateButton,  c);
		c.gridx = 4;
		c.weightx = c.weighty = 10;
		JLabel zLabel = new JLabel("               Rotate Z");
		sliderPanel.add(zLabel,  c);
		c.gridx = 5;
		c.weightx = c.weighty = 0;
		sliderPanel.add(zRotateButton, c);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		sliderPanel.add(xRotateSlider, c);
		c.gridx = 2;
		sliderPanel.add(yRotateSlider, c);
		c.gridx = 4;
		sliderPanel.add(zRotateSlider, c);


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

		//applyTransButton button actions
		applyTransButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!firstTrans){
					drawfileswitch = true;
					TransformPackage.applyTransformation(transformationMatrix, lines);
					//TRANSFORM HERE
					scan.repaint();
					transformationMatrix = new Matrix(new double[][]{
						{0, 0, 0, 0},
						{0, 0, 0, 0}, 
						{0, 0, 0, 0}, 
						{0, 0, 0, 0}
					});
					matrixTable = (TransformPackage.ArrayToTable(transformationMatrix.getArrayCopy()));
					matrixTable.setTableHeader(null);
					tableContainer.getViewport().add(matrixTable);
					firstTrans = true;
				}
			}
		});    

		/* Creating Main ToolBar*/
		JToolBar toolbar = new JToolBar("Menu");
		JButton importButton = null;
		JButton exportButton = null;
		JButton undoButton =  new JButton(new ImageIcon(HighWire.class.getResource("/Undo.png")));
		JButton selectButton = new JButton(new ImageIcon(HighWire.class.getResource("/LineSelect.png")));
		JButton selectAllButton = new JButton(new ImageIcon(HighWire.class.getResource("/selectAllNon.png")));
		JButton eraserButton = new JButton(new ImageIcon(HighWire.class.getResource("/eraserDeselectIcon.png")));
		/* Creating Menu Bar*/
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu addMenu = new JMenu("Add");
		JMenu viewMenu = new JMenu("View");
		JMenuItem importItem = new JMenuItem("Import");
		JMenuItem exportItem = new JMenuItem("Export");
		JMenuItem undoItem = new JMenuItem("Undo");
		JRadioButtonMenuItem parallelItem = new JRadioButtonMenuItem("Parallel Projection");
		JRadioButtonMenuItem perspectiveItem = new JRadioButtonMenuItem("Perspective Projection");
		JRadioButtonMenuItem lineSelectItem = new JRadioButtonMenuItem("Select lines");
		JRadioButtonMenuItem objectSelectItem = new JRadioButtonMenuItem("Select objects");
		JRadioButtonMenuItem allSelectItem = new JRadioButtonMenuItem("Select All");

		JMenuItem setPerspectiveDepthItem = new JMenuItem("Set Perspective Depth");
		JMenuItem addCubeItem = new JMenuItem("Load Cube");
		JMenuItem addPyramidItem = new JMenuItem("Load Pyramid");

		depthImage = new JButton(new ImageIcon(HighWire.class.getResource("/depth.png")));
		perspectiveIcon = new ImageIcon(HighWire.class.getResource("/Do Not Tilt-64.png"));
		parallelIcon = new ImageIcon(HighWire.class.getResource("/Rhombus-64.png"));
		lineSelectIcon = new ImageIcon(HighWire.class.getResource("/LineSelect.png"));
		objectSelectIcon = new ImageIcon(HighWire.class.getResource("/ObjectSelect.png"));
		eraserSelectedIcon = new ImageIcon(HighWire.class.getResource("/eraserIcon.png"));
		eraserIcon  = new ImageIcon(HighWire.class.getResource("/eraserDeselectIcon.png"));
		selectAllIcon  = new ImageIcon(HighWire.class.getResource("/selectAll.png"));
		selectAllNonIcon  = new ImageIcon(HighWire.class.getResource("/selectAllNon.png"));


		importButton = new JButton(new ImageIcon(HighWire.class.getResource("/Import-64.png")));
		exportButton = new JButton(new ImageIcon(HighWire.class.getResource("/Export-64.png")));
		projectionButton = new JButton(new ImageIcon(HighWire.class.getResource("/Rhombus-64.png")));
		JButton applyDepthButton = new JButton("Apply");
		JTextField depthField = new JTextField(Integer.toString(depthValue));
		JDialog depthDialog = new JDialog(frame, "Set Perspective Projection Depth");
		depthField.addFocusListener(focusedTextField);
		depthField.setPreferredSize(new Dimension(100, 20));
		depthDialog.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		JLabel depthLabel = new JLabel("Set Depth: ");
		depthDialog.add(depthLabel,  c);
		c.gridx = 1;
		c.weightx = 100;
		depthDialog.add(depthField, c);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 2;
		depthImage.setBorder(BorderFactory.createEmptyBorder());
		depthImage.setContentAreaFilled(false);
		depthDialog.add(depthImage, c);
		c.gridy = 2;
		depthDialog.add(applyDepthButton, c);
		depthDialog.setSize(300, 300);
		depthDialog.setLocationRelativeTo(frame);
		applyDepthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!depthField.getText().matches("^-?\\d+$") || depthField.getText().isEmpty() ||
						Integer.parseInt(depthField.getText()) < 0){
					JOptionPane.showMessageDialog(frame, "Please enter an integer value greater than 0.");
				}else{
					depthValue = Integer.parseInt(depthField.getText());
					depthDialog.setVisible(false);
					scan.repaint();
				}
			}
		});
		depthDialog.getRootPane().setDefaultButton(applyDepthButton);

		ActionListener selectAllAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				selectAll = !selectAll;
				if(selectAll){
					for(int i = 0; i < lines.size(); i++){
						lines.get(i).isEditable = true;
					}
					selectAllButton.setIcon(selectAllNonIcon);
				}else{
					for(int i = 0; i < lines.size(); i++){
						lines.get(i).isEditable = false;
					}
					selectAllButton.setIcon(selectAllIcon);
				}
				scan.repaint();
			}
		};
		ActionListener undoAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if(history.size() > 0){
					int historyCount = history.get(history.size() - 1); //get number of lines in last edit
					int totalLines = lines.size() - 1;
					for(int i = totalLines; i > (totalLines - historyCount); i--){
						System.out.print("Removing wires: " + i);
						lines.remove(i);
					}
					history.remove(history.size() - 1);
					scan.repaint();
				}
			}
		};
		ActionListener exportAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int response = chooser.showSaveDialog(frame);
				if(response == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					if ( ! (file.getName().endsWith(".txt") || file.getName().endsWith(".csv")) ) {
						file = new File(file.getParent(), file.getName() +".csv");
					}
					TransformPackage.exportLines(file, lines);
				}
			}
		};
		ActionListener importAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int response = chooser.showOpenDialog(frame);
				if(response == JFileChooser.APPROVE_OPTION){
					try {
						xRotateSlider.setValue(0);
						yRotateSlider.setValue(0);
						zRotateSlider.setValue(0);
						history.add(TransformPackage.inputLines3D(chooser.getSelectedFile()).size());
						lines.addAll(TransformPackage.inputLines3D(chooser.getSelectedFile()));

						scan.repaint();
					} catch (FileNotFoundException e) {
						System.out.println("Error: File import failed.");
					}
				}
			}
		};
		ActionListener demoAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == addCubeItem){
					lines.addAll(DemoObjects.packageCube(0, 0, 0));
					history.add(DemoObjects.packageCube(0, 0, 0).size());
					scan.repaint();
				}else if(evt.getSource() == addPyramidItem){
					lines.addAll(DemoObjects.packagePyramid());
					history.add(DemoObjects.packagePyramid().size());
					scan.repaint();
				}
			}
		};

		ActionListener deleteAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				delete = !delete;
				if(delete){
					eraserButton.setIcon(eraserSelectedIcon);
				}else{
					eraserButton.setIcon(eraserIcon);
				}
			}

		};
		ActionListener selectAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if(evt.getSource() == selectButton){
					selectObject = !selectObject;
					if(selectObject){
						selectButton.setIcon(lineSelectIcon);
						lineSelectItem.setSelected(false);
						objectSelectItem.setSelected(true);
						selectButton.setToolTipText("Select single wires");
						eraserButton.setToolTipText("Erase entire objects");
					}else{
						selectButton.setIcon(objectSelectIcon);
						lineSelectItem.setSelected(true);
						objectSelectItem.setSelected(false);
						selectButton.setToolTipText("Select entire objects");
						eraserButton.setToolTipText("Erase single wires");
					}
				}else if(evt.getSource() == lineSelectItem){
					selectButton.setToolTipText("Select single wires");
					eraserButton.setToolTipText("Erase entire objects");
					lineSelectItem.setSelected(true);
					objectSelectItem.setSelected(false);
					selectObject = false;
					selectButton.setIcon(objectSelectIcon);
				}else if(evt.getSource() == objectSelectItem){
					selectButton.setToolTipText("Select entire objects");
					eraserButton.setToolTipText("Erase single wires");
					lineSelectItem.setSelected(false);
					objectSelectItem.setSelected(true);
					selectObject = true;
					selectButton.setIcon(lineSelectIcon);
				}
			}
		};

		ActionListener projectionAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getSource() == projectionButton){
					perspectiveProjection = !perspectiveProjection;
					if(perspectiveProjection){
						projectionButton.setIcon(parallelIcon);
						projectionButton.setToolTipText("Set Parallel Projection");
					}else{
						projectionButton.setToolTipText("Set Perspective Projection");
						projectionButton.setIcon(perspectiveIcon);
					}
				}else if(arg0.getSource() == parallelItem){
					parallelItem.setSelected(true);
					perspectiveItem.setSelected(false);
					perspectiveProjection = false;
					projectionButton.setIcon(perspectiveIcon);
					projectionButton.setToolTipText("Set Perspective Projection");
				}else if(arg0.getSource() == perspectiveItem){
					parallelItem.setSelected(false);
					perspectiveItem.setSelected(true);
					perspectiveProjection = true;
					projectionButton.setIcon(parallelIcon);
					projectionButton.setToolTipText("Set Parallel Projection");
				}else if(arg0.getSource() == setPerspectiveDepthItem){
					parallelItem.setSelected(false);
					perspectiveItem.setSelected(true);
					perspectiveProjection = true;
					projectionButton.setIcon(parallelIcon);
					depthDialog.setVisible(true);
					projectionButton.setToolTipText("Set Parallel Projection");
				}
				scan.repaint();
			}
		};

		importButton.addActionListener(importAction);
		importButton.setToolTipText("Import a file");

		exportButton.addActionListener(exportAction);
		exportButton.setToolTipText("Export to CSV format");

		undoButton.addActionListener(undoAction);
		undoButton.setToolTipText("Undo last object addition");

		projectionButton.addActionListener(projectionAction);
		projectionButton.setToolTipText("Set Parallel Projection");

		eraserButton.addActionListener(deleteAction);
		eraserButton.setToolTipText("Erase entire objects");

		selectButton.addActionListener(selectAction);
		selectButton.setToolTipText("Select single wires");
		
		selectAllButton.addActionListener(selectAllAction);
		selectAllButton.setToolTipText("Select All");
		toolbar.add(importButton);
		toolbar.add(exportButton);
		toolbar.add(projectionButton);
		toolbar.add(undoButton);
		toolbar.addSeparator();
		toolbar.add(selectButton);
		toolbar.add(selectAllButton);
		toolbar.addSeparator();
		toolbar.add(eraserButton);

		northPanel.add(toolbar);

		//MenuBar Actions
		importItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		importItem.addActionListener(importAction); 

		exportItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		exportItem.addActionListener(exportAction);
		lineSelectItem.addActionListener(selectAction);
		objectSelectItem.addActionListener(selectAction);
		allSelectItem.addActionListener(selectAllAction);
		allSelectItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		undoItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		undoItem.addActionListener(undoAction);

		parallelItem.addActionListener(projectionAction);
		parallelItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		perspectiveItem.addActionListener(projectionAction);
		perspectiveItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_M, ActionEvent.CTRL_MASK));
		setPerspectiveDepthItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		setPerspectiveDepthItem.addActionListener(projectionAction);

		addCubeItem.addActionListener(demoAction);
		addPyramidItem.addActionListener(demoAction);

		fileMenu.add(importItem);
		fileMenu.add(exportItem);
		editMenu.add(undoItem);
		editMenu.addSeparator();
		editMenu.add(lineSelectItem);
		editMenu.add(objectSelectItem);
		editMenu.add(allSelectItem);
		viewMenu.add(parallelItem);
		viewMenu.add(perspectiveItem);
		viewMenu.addSeparator();
		viewMenu.add(setPerspectiveDepthItem);
		addMenu.add(addCubeItem);
		addMenu.add(addPyramidItem);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(viewMenu);
		menuBar.add(addMenu);
		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
}