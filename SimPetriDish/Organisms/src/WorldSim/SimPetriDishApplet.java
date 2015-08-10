/*
	SimPetriDishApplet.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	An applet wrapper for the otherwise standalone TwoDViewer main program
	
	July 30, 2015
	August 10, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimPetriDishApplet extends JApplet{
	
	TwoDViewer mySim ;
	JFrame application ;
	
	public void init() {
	   	EntityViewer myEntityView = new EntityViewer() ;
	   	JPanel myControls = new JPanel() ;
	   	JFrame mainFrame = new JFrame("Sim Petri Dish") ;
	   	JFrame controlsFrame = new JFrame("Simulation Controls") ;
	   	JFrame viewerFrame = new JFrame("Entity Viewer") ;
	    mySim = new TwoDViewer(mainFrame, myControls, myEntityView) ;
	    myEntityView.setViewerPanel(mySim) ;
	    mySim.postLinkInit() ;
	    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
	    mainFrame.setSize(1280, 1045);         
	    mainFrame.setVisible(true);
	    mainFrame.setContentPane(mySim) ;
	    mainFrame.setBackground(Color.DARK_GRAY) ;
	           
	    controlsFrame.setSize(600, 520);         
	    controlsFrame.setVisible(true);
	    controlsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    controlsFrame.setContentPane(mySim.controls) ;
	            
	    viewerFrame.setSize(myEntityView.viewerXSize, myEntityView.viewerYSize);         
	    viewerFrame.setVisible(true);
	    viewerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    viewerFrame.setContentPane(myEntityView) ;
	                  
	    mySim.simLoop() ;
	}
}
