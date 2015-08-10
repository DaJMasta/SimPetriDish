/*
	EntityViewer.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	A means (seperate JFrame window) to navigate the world and get fairly detailed information on whatever is in the cell being examined.

	August 10, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EntityViewer extends JPanel implements ActionListener{
	
	JButton entityViewXUp, entityViewXDown, entityViewYUp, entityViewYDown, entityViewZUp, entityViewZDown, entityViewGoButton ;
	JLabel entityViewXLabel, entityViewYLabel, entityViewZLabel, entityViewType, entityViewName, entityViewHeat, entityViewLight, entityViewEnergy, entityViewNutrients, entityViewTranslucence,
	entityViewAction, entityViewSubAction, entityViewLifetime, entityViewHP, entityViewProgeny, entityViewAttacker, entityViewSignalRecieved, entityViewAbilities, entityViewAbilities2 ;
	TextField entityViewX, entityViewY, entityViewZ ;
	TwoDViewer worldViewer ;
	int viewerX, viewerY, viewerZ ;
	Entity entityPanelViewed ;
	final int viewerXSize = 500 ;
	final int viewerYSize = 320 ;
	
	EntityViewer(){
		super() ;
		
        viewerX = 0 ;
        viewerY = 0 ;
        viewerZ = 0 ;
		
		entityViewXLabel = new JLabel("X:") ;
    	entityViewYLabel = new JLabel("Y:") ;
    	entityViewZLabel = new JLabel("Z:") ;
    	entityViewType = new JLabel("Entity") ;
    	entityViewName = new JLabel("This Entity") ;
    	entityViewHeat = new JLabel("Heat: ") ;
    	entityViewLight = new JLabel("Illumination: ") ;
    	entityViewEnergy = new JLabel("Energy: 0, Toxicity: 0") ;
    	entityViewNutrients = new JLabel("Nutrients - Red: 0, Green: 0, Blue: 0") ;
    	entityViewAction = new JLabel("No Action") ;
    	entityViewLifetime = new JLabel("Lifetime: 0") ;
    	entityViewSubAction = new JLabel("No Action") ;
    	entityViewHP = new JLabel("Health: 0") ;
    	entityViewXUp = new JButton("X+") ;
    	entityViewXDown = new JButton("X-") ;
    	entityViewYUp = new JButton("Y+") ;
    	entityViewYDown = new JButton("Y-") ;
    	entityViewZUp = new JButton("Z+") ;
    	entityViewZDown = new JButton("Z-") ;
    	entityViewGoButton = new JButton("Go") ;
    	entityViewX = new TextField("0", 3) ;
    	entityViewY = new TextField("0", 3) ;
    	entityViewZ = new TextField("0", 3) ;
    	entityViewProgeny = new JLabel("No children") ;
    	entityViewAttacker = new JLabel("Not attacked") ;
    	entityViewSignalRecieved = new JLabel("Not signaled") ;
    	entityViewAbilities = new JLabel("No abilities") ;
    	entityViewAbilities2 = new JLabel("") ;
    	entityViewTranslucence = new JLabel("Translucence: 1.0") ;
    	
        setLayout(null) ;
    	
    	entityViewXUp.setBounds(110, 100, 50, 50) ;
        entityViewXDown.setBounds(10, 100, 50, 50) ;
        entityViewYUp.setBounds(60, 150, 50, 50) ;
        entityViewYDown.setBounds(60, 50, 50, 50) ;
        entityViewZUp.setBounds(90, 205, 50, 50) ;
        entityViewZDown.setBounds(30, 205, 50, 50) ;
        entityViewXUp.setActionCommand("XUP") ;
        entityViewXUp.addActionListener(this) ;
        entityViewXDown.setActionCommand("XDOWN") ;
        entityViewXDown.addActionListener(this) ;
        entityViewYUp.setActionCommand("YUP") ;
        entityViewYUp.addActionListener(this) ;
        entityViewYDown.setActionCommand("YDOWN") ;
        entityViewYDown.addActionListener(this) ;
        entityViewZUp.setActionCommand("ZUP") ;
        entityViewZUp.addActionListener(this) ;
        entityViewZDown.setActionCommand("ZDOWN") ;
        entityViewZDown.addActionListener(this) ;
        entityViewGoButton.setBounds(150, 5, 50, 40) ;
        entityViewGoButton.setActionCommand("GO");
        entityViewGoButton.addActionListener(this) ;
        entityViewXLabel.setBounds(10, 15, 15, 20) ;
        entityViewYLabel.setBounds(55, 15, 15, 20) ;
        entityViewZLabel.setBounds(100, 15, 15, 20) ;
        entityViewX.setBounds(25, 15, 30, 20) ;
        entityViewY.setBounds(70, 15, 30, 20) ;
        entityViewZ.setBounds(115, 15, 30, 20) ;
        entityViewType.setBounds(245, 10, 300, 20) ;
        entityViewName.setBounds(215, 35, 300, 20) ;
        entityViewLifetime.setBounds(375, 35, 300, 20) ;
        entityViewHeat.setBounds(215, 55, 300, 20) ;
        entityViewLight.setBounds(360, 55, 300, 20) ;
        entityViewTranslucence.setBounds(215, 75, 300, 20) ;
        entityViewEnergy.setBounds(215, 95, 300, 20) ;
        entityViewNutrients.setBounds(215, 115, 300, 20) ;
        entityViewHP.setBounds(215, 135, 300, 20) ;
        entityViewProgeny.setBounds(355, 135, 300, 20) ;
        entityViewAction.setBounds(215, 155, 300, 20) ;
        entityViewSubAction.setBounds(215, 175, 300, 20) ;
        entityViewAttacker.setBounds(215, 195, 300, 20) ;
        entityViewSignalRecieved.setBounds(215, 215, 300, 20) ;
        entityViewAbilities.setBounds(215, 235, 300, 20) ;
        entityViewAbilities2.setBounds(215, 255, 300, 20) ;
        
        add(entityViewXUp) ;
        add(entityViewXDown) ;
        add(entityViewYUp) ;
        add(entityViewYDown) ;
        add(entityViewZUp) ;
        add(entityViewZDown) ;
        add(entityViewXUp) ;
        add(entityViewX) ;
        add(entityViewY) ;
        add(entityViewZ) ;
        add(entityViewXLabel) ;
        add(entityViewYLabel) ;
        add(entityViewZLabel) ;
        add(entityViewGoButton) ;
        add(entityViewType) ;
        add(entityViewName) ;
        add(entityViewHeat) ;
        add(entityViewTranslucence) ;
        add(entityViewLight) ;
        add(entityViewEnergy) ;
        add(entityViewNutrients) ;
        add(entityViewAction) ;
        add(entityViewLifetime) ;
        add(entityViewHP) ;
        add(entityViewSubAction) ;
        add(entityViewProgeny) ;
        add(entityViewAttacker) ;
        add(entityViewSignalRecieved) ;
        add(entityViewAbilities) ;
        add(entityViewAbilities2) ;
	}
	
	public void setViewerPanel(TwoDViewer newView){
		worldViewer = newView ;
	}
	
	public void actionPerformed(ActionEvent event) {												//GUI buttons evaluation
		String cmd = event.getActionCommand() ;

		switch(cmd){
		case "XUP":		viewerX++ ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		case "XDOWN":	viewerX-- ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;		
		case "YUP":		viewerY++ ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		case "YDOWN":	viewerY-- ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		case "ZUP":		viewerZ++ ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		case "ZDOWN":	viewerZ-- ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		case "GO":		viewerX = Integer.parseInt(entityViewX.getText()) ;
						viewerY = Integer.parseInt(entityViewY.getText()) ;
						viewerZ = Integer.parseInt(entityViewZ.getText()) ;
						entityPanelRead() ;
						if(!worldViewer.bSimActive)
							worldViewer.repaint() ;
						break ;
		}
	} 
	
	public void entityPanelRead(){																//Update the location of the cursor
		if(viewerX >= worldViewer.world.xBounds - 1){
			viewerX = worldViewer.world.xBounds - 1 ;
			entityViewXUp.setEnabled(false) ;
		}
		else
			entityViewXUp.setEnabled(true) ;		
		if(viewerX <= 0){
			viewerX = 0 ;
			entityViewXDown.setEnabled(false) ;
		}
		else
			entityViewXDown.setEnabled(true) ;	
		
		if(viewerY >= worldViewer.world.yBounds - 1){
			viewerY = worldViewer.world.yBounds - 1 ;
			entityViewYUp.setEnabled(false) ;
		}
		else
			entityViewYUp.setEnabled(true) ;		
		if(viewerY <= 0){
			viewerY = 0 ;
			entityViewYDown.setEnabled(false) ;
		}
		else
			entityViewYDown.setEnabled(true) ;	
		
		if(viewerZ >= worldViewer.world.zBounds - 1){
			viewerZ = worldViewer.world.zBounds - 1 ;
			entityViewZUp.setEnabled(false) ;
		}
		else
			entityViewZUp.setEnabled(true) ;		
		if(viewerZ <= 0){
			viewerZ = 0 ;
			entityViewZDown.setEnabled(false) ;
		}
		else
			entityViewZDown.setEnabled(true) ;	
		
		entityViewX.setText(Integer.toString(viewerX)) ;
		entityViewY.setText(Integer.toString(viewerY)) ;
		entityViewZ.setText(Integer.toString(viewerZ)) ;
		
		updateEntityPanel() ;
	}
	
	public void updateEntityPanel(){																			//Update the entityViewer with current data
		DecimalFormat df = new DecimalFormat("#.##") ;
		String abilitiesText = null ;
		String abilitiesText2 = null ;
		
		entityPanelViewed = worldViewer.world.getEntity(viewerX,  viewerY,  viewerZ) ;
		
        entityViewType.setText(entityPanelViewed.getClass().getName()) ;
        if(entityPanelViewed instanceof AdvancedOrganism){
        	entityViewName.setText(((AdvancedOrganism)entityPanelViewed).name + " " + (int)((AdvancedOrganism)entityPanelViewed).id + ", generation " + (int)((AdvancedOrganism)entityPanelViewed).generation) ;    
	        entityViewHP.setText("Health points: " + ((AdvancedOrganism)entityPanelViewed).healthPoints) ;
	        entityViewAction.setText("Action: " + ((Organism)entityPanelViewed).currentAction) ;
	        entityViewSubAction.setText("Subconscious action: " + ((Organism)entityPanelViewed).subconsciousAction) ;
	        
	        if(!((AdvancedOrganism)entityPanelViewed).bSessile)
	        	abilitiesText = "Can move" ;
	        if(((AdvancedOrganism)entityPanelViewed).bPhotosynthetic)
	        	abilitiesText2 = "Can photosynthesize" ;
	        if(((AdvancedOrganism)entityPanelViewed).bChemotrophic){
	        	if(abilitiesText2 != null)
	        		abilitiesText2 += ", chemosynthesize" ;
	        	else
	        		abilitiesText2 = "Can chemosynthesize" ;
	        }
	        if(((AdvancedOrganism)entityPanelViewed).bCanSignal){
	        	if(abilitiesText != null)
	        		abilitiesText += ", signal" ;
	        	else
	        		abilitiesText = "Can signal" ;
	        }
	        if(((AdvancedOrganism)entityPanelViewed).bCanAttack){
	        	if(abilitiesText != null)
	        		abilitiesText += ", attack" ;
	        	else
	        		abilitiesText = "Can attack" ;
	        }
	        if(((AdvancedOrganism)entityPanelViewed).bGenerateToxins){
	        	if(abilitiesText != null)
	        		abilitiesText += ", make toxins" ;
	        	else
	        		abilitiesText = "Can make toxins" ;
	        }
	        if(((AdvancedOrganism)entityPanelViewed).bEmitToxins){
	        	if(abilitiesText != null)
	        		abilitiesText += ", emit toxins" ;
	        	else
	        		abilitiesText = "Can emit toxins" ;
	        }
	        if(((AdvancedOrganism)entityPanelViewed).bSpiky){
	        	if(abilitiesText != null)
	        		abilitiesText2 += ", is spiky" ;
	        	else
	        		abilitiesText2 = "Spiky" ;
	        }
	        
	        if(abilitiesText == null)
	        	abilitiesText = "" ;
	        if(abilitiesText2 == null)
	        	abilitiesText2 = "" ;
	        
	        entityViewAbilities.setText(abilitiesText) ;
	        entityViewAbilities2.setText(abilitiesText2) ;
	        if(((AdvancedOrganism)entityPanelViewed).progeny > 0)
	        	entityViewProgeny.setText("Has " + (int)((AdvancedOrganism)entityPanelViewed).progeny + " offspring") ;
	        else
	        	entityViewProgeny.setText("Has no offspring") ;
	        if(((AdvancedOrganism)entityPanelViewed).attacker != null)
	        	entityViewAttacker.setText("Was attacked by " + ((AdvancedOrganism)entityPanelViewed).attacker.name + " " + ((AdvancedOrganism)entityPanelViewed).attacker.id + "." + ((AdvancedOrganism)entityPanelViewed).attacker.generation) ;
	        else
	        	entityViewAttacker.setText("Has not been recently attacked") ;
	        if(((AdvancedOrganism)entityPanelViewed).recievedSignal.message != ' ')
	        	entityViewSignalRecieved.setText("Last recieved: " + ((AdvancedOrganism)entityPanelViewed).recievedSignal.message + " at strength " + ((AdvancedOrganism)entityPanelViewed).recievedSignal.strength + " from " + ((AdvancedOrganism)entityPanelViewed).recievedSignal.sender) ;
	        else
	        	entityViewSignalRecieved.setText("Has not been recently signaled") ;
        }
        else{
        	if(entityPanelViewed instanceof Organism){
        		entityViewName.setText("Unnamed Basic Organism") ;    
    	        entityViewHP.setText("Health points: 1") ;
    	        entityViewAction.setText("Action: " + ((Organism)entityPanelViewed).currentAction) ;
    	        entityViewSubAction.setText("Subconscious action: " + ((Organism)entityPanelViewed).subconsciousAction) ;
    	        entityViewAbilities.setText("Can photosynthesize") ;
    	        entityViewAbilities2.setText("") ;
    	        entityViewProgeny.setText("Has an unknown number of offspring") ;
    	        entityViewAttacker.setText("Dies if attacked") ;
    	        entityViewSignalRecieved.setText("Cannot recieve signals") ;
        	}
        	else{
        		entityViewName.setText("No name") ;    
    	        entityViewHP.setText("No health points") ;
    	        entityViewAction.setText("No action") ;
    	        entityViewSubAction.setText("No subconscious action") ;
    	        entityViewAbilities.setText("No abilities") ;
    	        entityViewAbilities2.setText("") ;
    	        entityViewProgeny.setText("Cannot reproduce") ;
    	        entityViewAttacker.setText("Cannot be attacked") ;
    	        entityViewSignalRecieved.setText("Cannot recieve signals") ;
        	}
        }
        
        if(entityPanelViewed instanceof OrganicEntity){
        	entityViewEnergy.setText("Energy: " + (int)((OrganicEntity)entityPanelViewed).energy + ", Toxicity: " + (int)((OrganicEntity)entityPanelViewed).toxicity) ;
            entityViewNutrients.setText("Nutrients - Red: " + (int)((OrganicEntity)entityPanelViewed).nutrientHue.getRed() + ", Green: " + 
            		(int)((OrganicEntity)entityPanelViewed).nutrientHue.getGreen() + ", Blue: " + (int)((OrganicEntity)entityPanelViewed).nutrientHue.getBlue()) ;
        }
        else if(entityPanelViewed instanceof ToxicPlume){
        	entityViewEnergy.setText("No organic energy, Toxicity: " + (int)((ToxicPlume)entityPanelViewed).toxicity) ;
            entityViewNutrients.setText("No nutrients") ;
        }
        else{
        	entityViewEnergy.setText("No organic energy") ;
            entityViewNutrients.setText("No nutrients") ;
        }
        
        entityViewHeat.setText("Heat: " + df.format(entityPanelViewed.heat)) ;
        entityViewLight.setText("Illumination: " + (int)entityPanelViewed.illumination) ;
        entityViewLifetime.setText("Age: " + (int)entityPanelViewed.lifetime) ;
        entityViewTranslucence.setText("Translucence: " + df.format(entityPanelViewed.translucence)) ;
	}
}
