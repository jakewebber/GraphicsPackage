<p align="center">
<img align="center" src="https://raw.githubusercontent.com/jakewebber/HighWire3D/master/highwirelogo.png" width="800" height="200">
</p>
#HighWire3D
<p align="center">
Model 3D wireframe scenes 
with different projections, edit objects with dynamic controls and specific matrix transformations, and import or export as a simple CSV. </p>
<p align="center">
<b><a href="#overview">Overview</a></b>
|
<b><a href="#features">Features</a></b>
|
<b><a href="#key shortcuts">Key Shortcuts</a></b>
|
<b><a href="#setup">Setup</a></b>
|
<b><a href="#structure">Structure</a></b>
|
<b><a href="#credits">Credits</a></b>

</p>

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
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/Undo.png" width="30" height="30">
<b>Undo</b> the last complete addition to the scene, from a single wire to a fresh concatenated import. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/LineSelect.png" width="30" height="30">
<b>Select Wires</b> individually, across different objects. This will turn wires from white to red and unaffected by subsequent transformations to the scene. 
</p><p>
<img align="left" src="https://github.com/jakewebber/HighWire3D/blob/master/res/ObjectSelect.png" width="30" height="30">
<b>Select Objects</b> as a whole to be editable or not. Imported scenes count as entire objects. 

![Alt text](https://raw.githubusercontent.com/jakewebber/HighWire3D/master/houserotation.gif?raw=true "Demo")
