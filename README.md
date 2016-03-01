# 

<p align="center">
<img align="center" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/highwirelogo.png" width="800" height="200">
</p>
<p align="center">
Model 3D wireframe scenes 
with different projections, edit objects with dynamic controls and specific matrix transformations, and import or export as a simple CSV. </p>
<p align="center">
<b><a href="#overview">Overview</a></b>
|
<b><a href="#features">Features</a></b>
|
<b><a href="#hotkeys">Hotkeys</a></b>
|
<b><a href="#setup">Setup</a></b>
|
<b><a href="#structure">Structure</a></b>
|
<b><a href="#credits">Credits</a></b>
</p>
![Alt text](https://raw.githubusercontent.com/jakewebber/HighWire3D/master/houserotation.gif "Demo")

#Overview
What started as my curiosity with computer graphics solidified into ideas that built this 3D graphics editor. It's built on top of Java's graphics package - no preexisting 3D Java APIs went into creating the scene - using an original backend transformation class, custom line class and GUI. It is capable of translation, scale, and rotation of wire objects along the x, y, and z axis in a 3 dimensional space. Transformations are concatenated using matrix functions and applied to lines stored in a 3D space as separate matrices. All matrices are handled in the <a href="http://math.nist.gov/javanumerics/jama/">Jama Matrix package</a> for instantiating a matrix data structure and computing simple low-level matrix math (such as multiplication and inversion) within the graphic transformations. All GUI Components are Java Swing. 

#Features
<p>
<img align="left" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/res/Import-64.png" width="30" height="30">
<b>Import</b> new wires from a CSV file or other basic text format. 
</p><p>
<img align="left" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/res/Export-64.png" width="30" height="30">
<b>Export</b> all wires in the scene to a CSV file saved in their current transformation. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/Do%20Not%20Tilt-64.png" width="30" height="30">
<b>Perspective Project</b> the wire scene, giving a customizable depth to objects further away (along z axis) 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/Rhombus-64.png" width="30" height="30">
<b>Parallel Project</b> the wire scene, giving infinite focal length and essentially 'flattening' the z-axis. Useful for blueprints and aligning wires. 
</p><p>
<img align="left" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/res/copy.png" width="27" height="27">
<b>Copy</b> all selected wires and objects, spawning the duplication translated 50x and 50y as one new unified. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/Undo.png" width="30" height="30">
<b>Undo</b> the last complete addition to the scene, from a single wire to a fresh concatenated import. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/LineSelect.png" width="30" height="30">
<b>Select Wires</b> individually, across different objects. This will turn wires from white to red and unaffected by subsequent transformations to the scene. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/ObjectSelect.png" width="27" height="27">
<b>Select Objects</b> as a whole to be editable or not. Imported scenes count as entire objects. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/selectAllNon.png" width="27" height="27">
<b>Select All</b> wires in the scene to be editable or not. 
</p>
<img align="left" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/res/screenshot.png" width="30" height="30">
<b>Screenshot</b> The current state of the scene and save as a jpg or png. 
</p>

#Hotkeys
- <kbd>Ctrl</kbd> <kbd>O</kbd>: Import/Open
- <kbd>Ctrl</kbd> <kbd>S</kbd>: Export/Save
- <kbd>Ctrl</kbd> <kbd>A</kbd>: Select/Deselect all wires
- <kbd>Ctrl</kbd> <kbd>C</kbd>: Copy all selected wires as a new object
- <kbd>Ctrl</kbd> <kbd>Z</kbd>: Undo last object addition
- <kbd>Ctrl</kbd> <kbd>N</kbd>: Parallel projection
- <kbd>Ctrl</kbd><kbd>M</kbd>: Perspective projection
- <kbd>Ctrl</kbd> <kbd>D</kbd>: Set perspective depth value
- <kbd>Ctrl</kbd> <kbd>LMB</kbd>: Drag to translate objects along z-axis
- <kbd>Ctrl</kbd>l <kbd>RMB</kbd>: Drag to rotate objects along z-axis
- <kbd>LMB</kbd>: Drag to translate objects on X-Y plane
- <kbd>RMB</kbd>: Drag to rotate objects on X-Y plane
- <kbd>MMB</kbd>: Scroll to increase/decrease scale on all axis


#Setup
<b>To run:</b> __<a href="https://github.com/jakewebber/HighWire3D/raw/master/HighWire3D.jar">Download</a>__ the executable JAR file of the packaged HighWire3D. 
<p>HighWire3D was built in Ecipse Mars IDE and has project files supplied. Simply download HighWire3D as a zip and import as existing project into your Eclipse workspace to best view and edit the source code. 
</p>

#Credits
<p> <a href="http://math.nist.gov/javanumerics/jama/">JAMA: A Java Matrix Package</a> gives the matrix constructor from a double array and the basic matrix multiplication method needed to compute more complicated transformation matrices. 
</p>
<p>  <a href="https://icons8.com/">Icons8</a> provided the menu bar icons, and hosts tens of thousands more for free use.
