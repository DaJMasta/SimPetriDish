/*
	TwoDViewer.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Holds a simple UI for visualizing the world, adding components, and reading information from individual cells.  The UI is simple, not the totality of the class...
	
	July 30, 2015
	August 10, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TwoDViewer extends JPanel implements ActionListener{
	Color[][] heatMap ;
	Color[][] lightMap ;
	Color[][] nutrientMap ;
	Color[][] entityMap ;
	Color[][] actionMap ;
	Color[][] subconsciousActionMap ;
	Color[][] energyToxicityMap ;
	int zoomScale = 7 ;
	World world ;
	WorldEngine worldEngine ;
	long graphicsFrameStart,lastGraphicsFrame ;
	int worldXSize, worldYSize, worldZSize, visualizationZPosition, maxLight, minLight ;
	double heatAutoGamma, lightAutoGamma ;
	volatile boolean bSimActive ;
	int simStepDelay = 100 ;
	TextField stepField ;
	boolean bLargeWorld = false ;
	boolean bSpawnToggle ;
	JFrame application ;
	EntityViewer entityViewer ;
	JPanel controls ;
	
	double foodEnergyScale = 10.0 ;
	JCheckBox bigWorldBox, organismsDriftBox ;
	JButton resetButton, goUpButton, goDownButton, setFacingButton, autoSpawnButton ;
	TextField immediateQty, spawnerQty, facingHeat, facingLight ;
	JComboBox<String[]> immediateAdd, spawnerAdd, resetTemplate, depthSelector, driftSelector, addFromSelector, environmentSelector, facingSelector ;
	
	public TwoDViewer(JFrame parentApp, JPanel controlPanel, EntityViewer entityPanel)                      				//Mostly setting up UI components for the three windows
	    {
			super();	        
			application = parentApp ;
			controls = controlPanel ;
			entityViewer = entityPanel ;
			
	        String[] addNowChoices = {"Light", "Variable Light", "Heat Source", "Variable Heat Source", "Toxin Source", "Food", "Nutrient", "Basic Organism", "Advanced Organism", "Cyanobacteria",
	        							"Nitrogen Fixer", "Phytoplankton", "Super Organism", "Zooplankton", "Streptomyces", "Dinoflaggelate", "Yeast", "Coccolithophore", "Diatom", "Custom Organism" } ;
	        String[] addSpawnerChoices = {"Food", "Nutrient"} ;
	        String[] resetChoices = {"Nothing", "Low", "Medium", "High", "Med Nutrients", "High Nutrients"} ;
	        String[] depthChoices = {"1", "3", "5", "10"} ;
	        String[] driftChoices = {"None", "Low", "High", "Gravity", "+X current", "+Y current"} ;
	        String[] addFromChoices = {"Anywhere", "Top", "Random sphere"} ;
	        String[] environmentChoices = {"Normal", "Very hot", "Hot", "Cold", "Very cold", "More sources", "Toxic"} ;
	        String[] facingChoices = {"+X side", "-X side", "+Y side", "-Y side", "+Z side", "-Z side"} ;
	        
	        JButton stepForwardButton = new JButton("Simulation Step") ;
	        resetButton = new JButton("Reset World") ;
	        immediateAdd = new JComboBox(addNowChoices) ;
	        spawnerAdd = new JComboBox(addSpawnerChoices) ;
	        resetTemplate = new JComboBox(resetChoices) ;
	        JButton addNowButton = new JButton("Add to world") ;
	        JButton addSpawnerButton = new JButton("Spawn per step") ;
	        JButton setDriftButton = new JButton("Set drift") ;
	        JButton setSpawnButton = new JButton("Set spawn location") ;
	        
	        goUpButton = new JButton("Up") ;
	        goDownButton = new JButton("Down") ;
	        JLabel worldDepth = new JLabel("World Depth:") ;
	        depthSelector = new JComboBox(depthChoices) ;
	        JLabel heightViewer = new JLabel("View height:") ;
	        
	        facingSelector = new JComboBox(facingChoices) ;
	        JLabel facingHeatLabel = new JLabel("Heat from world edge") ;
	        JLabel facingLightLabel = new JLabel("Light from world edge") ;
	        facingHeat = new TextField("1", 4) ;
	        facingLight = new TextField("0", 4) ;
	        setFacingButton = new JButton("Apply") ;
	        
	    	JButton simStartButton = new JButton("Start sim") ;
	    	JButton simStopButton = new JButton("Stop sim") ;
	    	JButton applyAmbientButton = new JButton("Set environment") ;
	    	stepField = new TextField("100", 4) ;
	    	immediateQty = new TextField("1", 4) ;
	    	spawnerQty = new TextField("1", 4) ;

	    	JLabel simDelayLabel = new JLabel("Frame delay (ms):") ;
	    	JLabel worldDropDown = new JLabel("Resource preset:") ;
	    	
	    	bigWorldBox = new JCheckBox("Large world") ;
	    	
	    	organismsDriftBox = new JCheckBox("Organisms can drift") ;
	    	autoSpawnButton = new JButton("Start auto-populate") ;
	    	driftSelector = new JComboBox(driftChoices) ;
	    	addFromSelector = new JComboBox(addFromChoices) ;
	    	environmentSelector = new JComboBox(environmentChoices) ;
	    	JLabel environmentLabel = new JLabel("Environment settings") ;
	    	JLabel addFromLabel = new JLabel("Food spawn area") ;
	    	JLabel driftLabel = new JLabel("Drift/current settings") ;
	    	
	        setBackground(Color.DARK_GRAY);
	        graphicsFrameStart = 0 ;
	        lastGraphicsFrame = 0 ;
	        worldXSize = 50 ;
	        worldYSize = 50 ;
	        worldZSize = 1 ;
	        visualizationZPosition = 0 ;
	        bSimActive = false ;
	        
	        heatAutoGamma = 4.0 ;
	        lightAutoGamma = 4.5 ;
	        maxLight = 1 ;
	        minLight = 9999 ;
	        
	        bSpawnToggle = false ;
	        
	        controls.setLayout(null) ;
	        
	        stepForwardButton.setBounds(20, 20, 170, 35) ;
	        stepForwardButton.setActionCommand("STEP") ;
	        stepForwardButton.addActionListener(this) ;
	        autoSpawnButton.setBounds(20, 65, 170, 35) ;
	        autoSpawnButton.setActionCommand("TOGGLESPAWN") ;
	        autoSpawnButton.addActionListener(this) ;

	        worldDepth.setBounds(395, 315, 100, 20) ;
	        depthSelector.setBounds(500, 315, 50, 20) ;
	        resetButton.setBounds(440, 410, 130, 60) ;
	        resetButton.setActionCommand("RESET") ;
	        resetButton.addActionListener(this) ;
	        bigWorldBox.setBounds(460, 380, 120, 20) ;
	        resetTemplate.setBounds(475, 350, 90, 20);
	        worldDropDown.setBounds(365, 350, 120, 20);
	        
	        simStartButton.setBounds(230, 15, 100, 50) ;
	        simStartButton.setActionCommand("START") ;
	        simStartButton.addActionListener(this) ;
	        simStopButton.setBounds(340, 15, 100, 50) ;
	        simStopButton.setActionCommand("STOP") ;
	        simStopButton.addActionListener(this) ;
	        stepField.setBounds(485, 40, 50, 20) ;
	        simDelayLabel.setBounds(455, 20, 150, 15);
	        
	        immediateQty.setBounds(145, 120, 50, 20) ;
	        immediateAdd.setBounds(210, 120, 160, 20) ;
	        addNowButton.setBounds(385, 110, 180, 40) ;
	        addNowButton.setActionCommand("ADDNOW") ;
	        addNowButton.addActionListener(this) ;
	        
	        spawnerQty.setBounds(145, 180, 50, 20) ;
	        spawnerAdd.setBounds(210, 180, 160, 20) ;
	        addSpawnerButton.setBounds(385, 170, 180, 40) ;
	        addSpawnerButton.setActionCommand("SPAWN") ;
	        addSpawnerButton.addActionListener(this) ;
	        
	        goUpButton.setBounds(25, 145, 80, 40) ;
	        goDownButton.setBounds(25, 195, 80, 40) ;
	        goUpButton.setActionCommand("UP") ;
	        goUpButton.addActionListener(this) ;
	        goDownButton.setActionCommand("DOWN") ;
	        goDownButton.addActionListener(this) ;
	        heightViewer.setBounds(30, 120, 110, 20) ;
	        
	        organismsDriftBox.setBounds(45, 385, 140, 20) ;
	        driftSelector.setBounds(55, 360, 100, 20) ;
	        driftLabel.setBounds(45, 335, 140, 20) ;
	        setDriftButton.setBounds(210, 350, 100, 40) ;
	        setDriftButton.setActionCommand("DRIFT") ;
	        setDriftButton.addActionListener(this) ;
	        
	        addFromSelector.setBounds(230, 240, 140, 20) ;
	        addFromLabel.setBounds(125, 240, 140, 20) ;
	        setSpawnButton.setBounds(385, 230, 180, 40) ;
	        setSpawnButton.setActionCommand("SPAWNLOCATION") ;
	        setSpawnButton.addActionListener(this) ;
	        
	        environmentSelector.setBounds(50, 300, 110, 20) ;
	        environmentLabel.setBounds(45, 275, 140, 20) ;
	        applyAmbientButton.setBounds(190, 280, 130, 40) ;
	        applyAmbientButton.setActionCommand("ENVIRON") ;
	        applyAmbientButton.addActionListener(this) ;
	        
	        facingSelector.setBounds(230, 435, 80, 20) ;
	        facingHeatLabel.setBounds(20, 425, 140, 20) ;
	        facingLightLabel.setBounds(20, 445, 140, 20) ;
	        facingHeat.setBounds(160, 425, 60, 20) ;
	        facingLight.setBounds(160, 445, 60, 20) ;
	        setFacingButton.setBounds(320, 425, 80, 40) ;
	        setFacingButton.setActionCommand("FACING") ;
	        setFacingButton.addActionListener(this) ; 
	        
	        controls.add(stepForwardButton) ;
	        controls.add(resetButton) ;
	        controls.add(simStartButton) ;
	        controls.add(simStopButton) ;
	        controls.add(stepField) ;
	        controls.add(simDelayLabel) ;
	        controls.add(applyAmbientButton) ;
	        controls.add(bigWorldBox) ;
	        controls.add(resetTemplate);
	        controls.add(worldDropDown);
	        controls.add(immediateQty) ;
	        controls.add(spawnerQty) ;
	        controls.add(immediateAdd) ;
	        controls.add(spawnerAdd) ;
	        controls.add(addNowButton);
	        controls.add(addSpawnerButton);
	        controls.add(goUpButton) ;
	        controls.add(goDownButton); 
	        controls.add(worldDepth) ;
	        controls.add(depthSelector) ;
	        controls.add(organismsDriftBox) ;
	        controls.add(driftSelector) ;
	        controls.add(setDriftButton) ;
	        controls.add(addFromSelector) ;
	        controls.add(setSpawnButton) ;
	        controls.add(environmentSelector) ;
	        controls.add(autoSpawnButton) ;
	        controls.add(environmentLabel) ;
	        controls.add(addFromLabel) ;
	        controls.add(driftLabel) ;
	        controls.add(facingSelector) ;
	        controls.add(facingHeatLabel) ;
	        controls.add(facingLightLabel) ;
	        controls.add(facingHeat) ;
	        controls.add(facingLight) ;
	        controls.add(setFacingButton) ;
	        controls.add(heightViewer) ;
	    }

public void paintComponent(Graphics g)																		  // draw graphics in the visualization panel
	    {
	        int tempX, tempY ;
	        DecimalFormat df = new DecimalFormat("#.##") ;
	        
	        graphicsFrameStart = new Date().getTime() ;

	        //super.paintComponent(g);            
	        
	        autoGamma() ;
	        
	        tempX = 15 ;
	        tempY = 30 ;
	        
	        g.setColor(Color.WHITE) ; 
	        g.drawString("Heat map:", tempX + (worldXSize * (zoomScale + 1)) / 2 - 40, tempY - 12) ;
	        
	        for(int x = 0; x < worldXSize; x++){
	        	for(int y = 0; y < worldYSize; y++){
	        		if(x == entityViewer.viewerX && y == entityViewer.viewerY){
	        			if(visualizationZPosition == entityViewer.viewerZ)
	        				g.setColor(Color.WHITE) ;
	        			else
	        				g.setColor(Color.GRAY) ;
	        			g.fillRect((tempX + x * (zoomScale + 1)) - 1, (tempY + y * (zoomScale + 1)) - 1, zoomScale + 2, zoomScale + 2) ;
	        		}
	        		g.setColor(gammaUp(heatMap[x][y], heatAutoGamma)) ;
	    	        g.fillRect((tempX + x * (zoomScale + 1)), (tempY + y * (zoomScale + 1)), zoomScale, zoomScale);
	        	}
	        }
	        
	        tempX += 15 + worldXSize * (zoomScale + 1) ;
	        tempY = 30 ;
	        
	        g.setColor(Color.WHITE) ; 
	        g.drawString("Light map:", tempX + (worldXSize * (zoomScale + 1)) / 2 - 40, tempY - 12) ;
	        
	        for(int x = 0; x < worldXSize; x++){
	        	for(int y = 0; y < worldYSize; y++){
	        		if(x == entityViewer.viewerX && y == entityViewer.viewerY){
	        			if(visualizationZPosition == entityViewer.viewerZ)
	        				g.setColor(Color.WHITE) ;
	        			else
	        				g.setColor(Color.GRAY) ;
	        			g.fillRect((tempX + x * (zoomScale + 1)) - 1, (tempY + y * (zoomScale + 1)) - 1, zoomScale + 2, zoomScale + 2) ;
	        		}
	        		g.setColor(gammaUp(lightMap[x][y], lightAutoGamma)) ;
	    	        g.fillRect((tempX + x * (zoomScale + 1)), (tempY + y * (zoomScale + 1)), zoomScale, zoomScale) ;
	        	}
	        }
	        
	        tempX += 15 + worldXSize * (zoomScale + 1) ;
	        tempY = 30 ;
	        
	        g.setColor(Color.WHITE) ;
	        g.drawString("Nutrient map:", tempX + (worldXSize * (zoomScale + 1)) / 2 - 40, tempY - 12) ;
	        
	        for(int x = 0; x < worldXSize; x++){
	        	for(int y = 0; y < worldYSize; y++){
	        		if(x == entityViewer.viewerX && y == entityViewer.viewerY){
	        			if(visualizationZPosition == entityViewer.viewerZ)
	        				g.setColor(Color.WHITE) ;
	        			else
	        				g.setColor(Color.GRAY) ;
	        			g.fillRect((tempX + x * (zoomScale + 1)) - 1, (tempY + y * (zoomScale + 1)) - 1, zoomScale + 2, zoomScale + 2) ;
	        		}
	        		g.setColor(gammaUp(nutrientMap[x][y], 2.5)) ;
	    	        g.fillRect((tempX + x * (zoomScale + 1)), (tempY + y * (zoomScale + 1)), zoomScale, zoomScale) ;
	    	        if(energyToxicityMap[x][y] != Color.BLACK){
	    	        	g.setColor(energyToxicityMap[x][y]) ;
		    	        g.fillRect((tempX + (x * (zoomScale + 1)) + (int)((zoomScale + 1) / 4)), (tempY + (y * (zoomScale + 1)) + (int)((zoomScale + 1) / 4)), (int)((zoomScale - 1) / 2), (int)((zoomScale - 1) / 2)) ;
	    	        }
	        	}
	        }
	        
	        tempX = 30 + worldXSize * (zoomScale + 1) ;
	        tempY = 65 + worldXSize * (zoomScale + 1) ;
	        
	        g.setColor(Color.WHITE) ;
	        g.drawString("Entity map:", tempX + (worldXSize * (zoomScale + 1)) / 2 - 35, tempY - 12) ;
	        
	        for(int x = 0; x < worldXSize; x++){
	        	for(int y = 0; y < worldYSize; y++){
	        		if(x == entityViewer.viewerX && y == entityViewer.viewerY){
	        			if(visualizationZPosition == entityViewer.viewerZ)
	        				g.setColor(Color.WHITE) ;
	        			else
	        				g.setColor(Color.GRAY) ;
	        			g.fillRect((tempX + x * (zoomScale + 1)) - 1, (tempY + y * (zoomScale + 1)) - 1, zoomScale + 2, zoomScale + 2) ;
	        		}
	        		g.setColor(entityMap[x][y]) ;
	    	        g.fillRect((tempX + x * (zoomScale + 1)), (tempY + y * (zoomScale + 1)), zoomScale, zoomScale) ;
	        	}
	        }
	        
	        tempX += 15 + worldXSize * (zoomScale + 1) ;
	        tempY = 65 + worldXSize * (zoomScale + 1) ;
	        
	        g.setColor(Color.WHITE) ;
	        g.drawString("Organism action map:", tempX + (worldXSize * (zoomScale + 1)) / 2 - 60, tempY - 12) ;
	        
	        for(int x = 0; x < worldXSize; x++){
	        	for(int y = 0; y < worldYSize; y++){
	        		if(x == entityViewer.viewerX && y == entityViewer.viewerY){
	        			if(visualizationZPosition == entityViewer.viewerZ)
	        				g.setColor(Color.WHITE) ;
	        			else
	        				g.setColor(Color.GRAY) ;
	        			g.fillRect((tempX + x * (zoomScale + 1)) - 1, (tempY + y * (zoomScale + 1)) - 1, zoomScale + 2, zoomScale + 2) ;
	        		}
	        		g.setColor(actionMap[x][y]) ;
	    	        g.fillRect((tempX + x * (zoomScale + 1)), (tempY + y * (zoomScale + 1)), zoomScale, zoomScale) ;
	    	        if(subconsciousActionMap[x][y] != Color.BLACK){
	    	        	g.setColor(subconsciousActionMap[x][y]) ;
		    	        g.fillRect((tempX + (x * (zoomScale + 1)) + (int)((zoomScale + 1) / 4)), (tempY + (y * (zoomScale + 1)) + (int)((zoomScale + 1) / 4)), (int)((zoomScale - 1) / 2), (int)((zoomScale - 1) / 2)) ;
	    	        }
	        	}
	        }
	        
	        g.setColor(Color.WHITE) ;																				//The text stats
	        
	        tempX = 25 ;
	        tempY = 55 + worldXSize * (zoomScale + 1) ;
	        
	        g.drawString("Looking at height: " + (int)visualizationZPosition, tempX + 120, tempY) ;
	        tempY += 25 ;
	        g.drawString("World Stats: ", tempX + 10, tempY) ;
	        tempY += 22 ;
	        g.drawString("Simulation step: " + (int)world.age, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Steps per second: " + worldEngine.fpsCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Graphics update time: " + lastGraphicsFrame + "ms", tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("World step time: " + worldEngine.lastWorldFrame + "ms", tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Delay between frames: " + (int)simStepDelay + "ms", tempX, tempY) ;
	        tempY += 17 ;
	        if(bLargeWorld)
	        	g.drawString("World size: 100 x 100 x " + (int)worldZSize, tempX, tempY) ;
	        else
	        	g.drawString("World size: 50 x 50 x " + (int)worldZSize, tempX, tempY) ;    
	        tempY += 17 ;
	        g.setColor(Color.YELLOW) ;
	        g.drawString("Light count: " + (int)world.lastTotalLights, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(Color.WHITE) ;
	        g.drawString("Total light output: " + (int)world.lastIlluminationTotal, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Max illumination: " + (int)worldEngine.stepMaxLight, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total heat: " + df.format(worldEngine.totalHeat), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Max heat: " + df.format(worldEngine.stepMaxHeat), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Min heat: " + df.format(worldEngine.stepMinHeat), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Ambient heat: " + df.format((world.wallTemps[0] + world.wallTemps[1] + world.wallTemps[2] + world.wallTemps[3] + world.wallTemps[4] + world.wallTemps[5]) / 6.0), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Random drift chance: " + world.driftVariance + "%", tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Current - X: " + world.driftSpeedX + ", Y: " + world.driftSpeedY + ", Z: " + world.driftSpeedZ, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Food per step: " + (int)(worldEngine.getSpawner(new RandomFood(world.heatFloor))).quantity, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Nutrient sources per step: " + (int)(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).quantity, tempX, tempY) ;
	        tempY += 17 ;
	        if(bSpawnToggle)
	        	g.drawString("Organisms randomly added", tempX, tempY) ;
	        else
	        	g.drawString("No active organism adding", tempX, tempY) ;
	        tempY += 17 ;
	        if(worldEngine.getSpawner(new RandomFood(world.heatFloor)).xLocation == -1){
	        	g.drawString("Food spawns anywhere", tempX, tempY) ;
	        	tempY += 17 ;
	        }
	        else if(worldEngine.getSpawner(new RandomFood(world.heatFloor)).radius > 0){
	        	g.drawString("Food spawns in a sphere at", tempX, tempY) ;
	        	tempY += 17 ;
	        	g.drawString((int)(worldEngine.getSpawner(new RandomFood(world.heatFloor)).xLocation) + ", " + (int)(worldEngine.getSpawner(new RandomFood(world.heatFloor)).yLocation) + ", " + 
	        			(int)(worldEngine.getSpawner(new RandomFood(world.heatFloor)).zLocation) + ", with radius " + (int)(worldEngine.getSpawner(new RandomFood(world.heatFloor)).radius), tempX + 10, tempY) ;
	        }
	        else{
	        	g.drawString("Food spawns at the top", tempX, tempY) ;
	        	tempY += 17 ;
	        }
	        tempY += 17 ;
	        g.drawString("Total decays: " + (int)worldEngine.totalDecay, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total consumed: " + (int)worldEngine.totalConsumption, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total reproduction: " + (int)worldEngine.totalReproduction, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total organic energy: " + (int)worldEngine.totalEnergy, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total red nutrients: " + (int)worldEngine.totalRedNut, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total green nutrients: " + (int)worldEngine.totalGreenNut, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total blue nutrients: " + (int)worldEngine.totalBlueNut, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total toxicity: " + (int)worldEngine.totalToxicity, tempX, tempY) ;
	        
	        tempX = 225 ;
	        tempY = 75 + worldXSize * (zoomScale + 1) ;
	        
	        g.drawString("Simulation Totals: ", tempX + 10, tempY) ;
	        tempY += 22 ;
	        g.drawString("Food added: " + (int)(worldEngine.getSpawner(new RandomFood(world.heatFloor))).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Nutrient sources added: " + (int)(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total food added: " + (int)(((worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).spawnCount + (worldEngine.getSpawner(new RandomFood(world.heatFloor))).spawnCount)), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Basic Organisms added: " + (int)(worldEngine.getSpawner(new Organism())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Advanced Organisms added: " + (int)(worldEngine.getSpawner(new AdvancedOrganism())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Cyanobacteria added: " + (int)(worldEngine.getSpawner(new Cyanobacteria())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Nitrogen Fixers added: " + (int)(worldEngine.getSpawner(new NitrogenFixer())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Phytoplankton added: " + (int)(worldEngine.getSpawner(new Phytoplankton())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Super Organisms added: " + (int)(worldEngine.getSpawner(new SuperOrganism())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Zooplankton added: " + (int)(worldEngine.getSpawner(new Zooplankton())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Streptomyces added: " + (int)(worldEngine.getSpawner(new Streptomyces())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Dinoflagellates added: " + (int)(worldEngine.getSpawner(new Dinoflagellate())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Yeast added: " + (int)(worldEngine.getSpawner(new Yeast())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Coccolithophores added: " + (int)(worldEngine.getSpawner(new Coccolithophore())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Diatoms added: " + (int)(worldEngine.getSpawner(new Diatom())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Custom Organisms added: " + (int)(worldEngine.getSpawner(new CustomOrganism())).spawnCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total organisms added: " + (int)(((worldEngine.getSpawner(new Zooplankton())).spawnCount + (worldEngine.getSpawner(new Organism())).spawnCount + 
	        		(worldEngine.getSpawner(new AdvancedOrganism())).spawnCount + (worldEngine.getSpawner(new Cyanobacteria())).spawnCount + (worldEngine.getSpawner(new NitrogenFixer())).spawnCount +
	        		(worldEngine.getSpawner(new Phytoplankton())).spawnCount + (worldEngine.getSpawner(new SuperOrganism())).spawnCount + (worldEngine.getSpawner(new Streptomyces())).spawnCount + (worldEngine.getSpawner(new Diatom())).spawnCount + 
	        		(worldEngine.getSpawner(new Dinoflagellate())).spawnCount + (worldEngine.getSpawner(new Yeast())).spawnCount + (worldEngine.getSpawner(new Coccolithophore())).spawnCount  + (worldEngine.getSpawner(new CustomOrganism())).spawnCount)), tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total deaths: " + (int)worldEngine.totalDeaths, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Deaths of old age: " + (int)worldEngine.totalNaturalCauses, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total killed: " + (int)worldEngine.totalKilled, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total poisoned: " + (int)worldEngine.totalPoisoned, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total starved: " + (int)worldEngine.totalStarved, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total malnutrition deaths: " + (int)worldEngine.totalMalnutrition, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total frozen: " + (int)worldEngine.totalFrozen, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Total cooked: " + (int)worldEngine.totalCooked, tempX, tempY) ;
	        tempY += 22 ;
	        g.drawString("Current step:", tempX + 10, tempY) ;
	        
	        g.setColor(new Color(160, 160, 160)) ;
	        g.fillRect(tempX - 2, tempY + 4, 140, 33) ;
	        tempY += 17 ;
	        g.setColor(new Color(80, 70, 20)) ;
	        g.drawString("Organic objects: " + (int)worldEngine.organicCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(50, 20, 60)) ;
	        g.drawString("Toxic plumes: " + (int)worldEngine.plumeCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(Color.WHITE) ;
	        g.drawString("Organism count: " + (int)worldEngine.organismCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.drawString("Oldest organism: " + (int)worldEngine.oldestOrganism, tempX, tempY) ;
	        
	        tempX += 225 ;
	        tempY -= 107 ;
	        g.drawString("Step organism counts:" , tempX + 10, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(180, 60, 210)) ;
	        g.drawString("Basic organisms: " + worldEngine.lastBasicOrganismCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(235, 210, 120)) ;
	        g.drawString("Advanced organisms: " + worldEngine.lastAdvancedOrganismCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(30, 130, 130)) ;
	        g.drawString("Cyanobacteria: " + worldEngine.lastCyanobacteriaCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(140, 160, 75)) ;
	        g.drawString("Nitrogen Fixers: " + worldEngine.lastNitrogenFixerCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(30, 180, 60)) ;
	        g.drawString("Phytoplankton: " + worldEngine.lastPhytoplanktonCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(255, 180, 20)) ;
	        g.drawString("Super Organisms: " + worldEngine.lastSuperOrganismCount , tempX, tempY) ;     
	        
	        tempX += 200 ;
	        tempY -= 102 ;
	        g.setColor(new Color(70, 130, 250)) ;
	        g.drawString("Zooplankton: " + worldEngine.lastZooplanktonCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(180, 155, 130)) ;
	        g.drawString("Yeast: " + worldEngine.lastYeastCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(180, 60, 100)) ;
	        g.drawString("Streptomyces: " + worldEngine.lastStreptomycesCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(160, 130, 60)) ;
	        g.drawString("Dinoflagellates: " + worldEngine.lastDinoflagellateCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(185, 235, 200)) ;
	        g.drawString("Coccolithophores: " + worldEngine.lastCoccolithophoreCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(220, 255, 80)) ;
	        g.drawString("Diatoms: " + worldEngine.lastDiatomCount , tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new CustomOrganism().displayColor) ;
	        g.drawString("Custom Organisms: " + worldEngine.lastCustomCount , tempX, tempY) ;
	        
	        tempX += 210 ;
	        tempY -= 102 ;
	        g.drawString("Step actions:", tempX + 70, tempY) ;
	        tempY += 17 ;
	        g.setColor(Color.GRAY) ;
	        g.drawString("Idle: " + (int)worldEngine.lastStepIdleCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(220, 40, 40)) ;
	        g.drawString("Attack: " + (int)worldEngine.lastStepAttackCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(220, 190, 150)) ;
	        g.drawString("Signal: " + (int)worldEngine.lastStepSignalCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(240, 250, 80)) ;
	        g.drawString("Toxify: " + (int)worldEngine.lastStepToxCount, tempX, tempY) ;
	        g.setColor(new Color(160, 160, 160)) ;
	        g.fillRect(tempX - 2, tempY + 4, 100, 18) ;
	        tempY += 17 ;
	        g.setColor(new Color(30, 60, 220)) ;
	        g.drawString("Reproduce: " + (int)world.stepReproduction, tempX, tempY) ;
	        tempY += 20 ;
	        g.setColor(Color.WHITE) ;
	        g.drawString("Most virile: " + worldEngine.mostVirile , tempX, tempY) ;
	        
	        tempX += 110 ;
	        tempY -= 89 ;
	        g.setColor(new Color(150, 150, 50)) ;
	        g.drawString("Eat: " + (int)worldEngine.lastStepEatCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(30, 160, 150)) ;
	        g.drawString("Move: " + (int)worldEngine.lastStepMoveCount, tempX, tempY) ;
	        g.setColor(new Color(160, 160, 160)) ;
	        g.fillRect(tempX - 2, tempY + 4, 100, 18) ;
	        tempY += 17 ;
	        g.setColor(new Color(110, 20, 130)) ;
	        g.drawString("Detoxify: " + (int)worldEngine.lastStepDetoxCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(Color.CYAN) ;
	        g.drawString("Created: " + (int)worldEngine.lastStepCreatedCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(255, 210, 80)) ;
	        g.drawString("Emit toxins: " + (int)worldEngine.lastStepEmitToxCount, tempX, tempY) ;
	       
	        tempX += 110 ;
	        tempY -= 86 ;
	        g.setColor(Color.WHITE) ;
	        g.drawString("Subconscious actions:", tempX + 10, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(20, 220, 60)) ;
	        g.drawString("Photosynthesize: " + (int)worldEngine.lastStepPhotoCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(220, 30, 220)) ;
	        g.drawString("Chemosynthesize: " + (int)worldEngine.lastStepChemoCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(100, 160, 220)) ;
	        g.drawString("Regenerate: " + (int)worldEngine.lastStepRegenCount, tempX, tempY) ;
	        tempY += 17 ;
	        g.setColor(new Color(220, 190, 150)) ;
	        g.drawString("Relay signal: " + (int)worldEngine.lastStepSubconsciousSignalCount, tempX, tempY) ;
	        
	        lastGraphicsFrame = new Date().getTime() - graphicsFrameStart ;
	    }

	public void postLinkInit(){
		resetWorld() ;
	}

	public void simLoop(){																			//simulation step

	    int sleepTime = 0 ;
	    while(true){
			if(bSimActive){
				resetButton.setEnabled(false) ;
				simulationStep() ;
				application.repaint() ;
				sleepTime = (int)(simStepDelay - worldEngine.lastWorldFrame - lastGraphicsFrame) ;
				if(sleepTime < 1)
					sleepTime = 0 ;
				try {
					Thread.sleep(sleepTime);               
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}	
			}
			else
				resetButton.setEnabled(true) ;
	    }
	}
	
	public void actionPerformed(ActionEvent event) {												//GUI buttons evaluation
		String cmd = event.getActionCommand() ;
		int tempInt = 0 ;
		Random rand = new Random() ;
		int tempX, tempY, tempZ ;
		
		String addNowChoice = (String)immediateAdd.getSelectedItem();
		String addSpawnChoice = (String)spawnerAdd.getSelectedItem();
		String driftChoice = (String)driftSelector.getSelectedItem();
		String addFromChoice = (String)addFromSelector.getSelectedItem();
		String environmentChoice = (String)environmentSelector.getSelectedItem();
		String facingChoice = (String)facingSelector.getSelectedItem();
		
		switch(cmd){
		case "ADDNOW":	switch(addNowChoice){
						case "Light":		((RandomLightSource)(worldEngine.getSpawner(new RandomLightSource())).toAdd).bVariable = false ;
											for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
											break ;
						case "Variable Light":((RandomLightSource)(worldEngine.getSpawner(new RandomLightSource())).toAdd).bVariable = true ;
												for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
													(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
											((RandomLightSource)(worldEngine.getSpawner(new RandomLightSource())).toAdd).bVariable = false ;
											break ;
						case "Heat Source":	((RandomHeatSource)(worldEngine.getSpawner(new RandomHeatSource())).toAdd).bVariable = false ;
												for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
													(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
											break ;
						case "Variable Heat Source":((RandomHeatSource)(worldEngine.getSpawner(new RandomHeatSource())).toAdd).bVariable = true ;
											for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
											((RandomHeatSource)(worldEngine.getSpawner(new RandomHeatSource())).toAdd).bVariable = false ;
											break ;
						case "Toxin Source":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new ToxicSource(world, -1, -1, -1))).forceAddRandom() ;
											break ;
						case "Food":		for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new RandomFood(world.heatFloor))).forceAddRandom() ;
											break ;
						case "Nutrient":	for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).forceAddRandom() ;
											break ;
						case "Basic Organism":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Organism())).forceAddRandom() ;
											break ;							
						case "Advanced Organism":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
													(worldEngine.getSpawner(new AdvancedOrganism())).forceAddRandom() ;
												break ;
						case "Cyanobacteria":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
													(worldEngine.getSpawner(new Cyanobacteria())).forceAddRandom() ;
												break ;
						case "Nitrogen Fixer":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new NitrogenFixer())).forceAddRandom() ;
											break ;							
						case "Phytoplankton":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Phytoplankton())).forceAddRandom() ;
											break ;							
						case "Super Organism":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new SuperOrganism())).forceAddRandom() ;
											break ;							
						case "Zooplankton" :for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Zooplankton())).forceAddRandom() ;
											break ;
						case "Streptomyces":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Streptomyces())).forceAddRandom() ;
												break ;
						case "Dinoflagellate":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Dinoflagellate())).forceAddRandom() ;
											break ;							
						case "Yeast":		for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Yeast())).forceAddRandom() ;
											break ;							
						case "Coccolithophore":for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Coccolithophore())).forceAddRandom() ;
											break ;							
						case "Diatom":		for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new Diatom())).forceAddRandom() ;
											break ;
						case "Custom Organism":	for(int i = 0; i < Integer.parseInt(immediateQty.getText()); i++)
												(worldEngine.getSpawner(new CustomOrganism())).forceAddRandom() ;
											break ;
						}
						break ;
		case "SPAWN":	switch(addSpawnChoice){
							case "Food":	(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(Integer.parseInt(spawnerQty.getText())) ;
													break ;
							case "Nutrient":	(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).setQuantity(Integer.parseInt(spawnerQty.getText())) ;
													break ;
							}
						break ;
		case "STEP":	simulationStep() ;
						application.repaint() ;
						break ;
		case "RESET":	world.endWorldThreads() ;
						bSimActive = false ;
						resetWorld() ;
						application.repaint() ;
						break ;
		case "START":	tempInt = Integer.parseInt(stepField.getText()) ;
						simStepDelay = tempInt ;
						bSimActive = true ;
						break ;
		case "STOP":	bSimActive = false ;
						break ;
		case "UP":		visualizationZPosition++ ;
						if(visualizationZPosition == world.zBounds - 1)
							goUpButton.setEnabled(false) ;
						else
							goUpButton.setEnabled(true) ;
						if(visualizationZPosition == 0)
							goDownButton.setEnabled(false) ;
						else
							goDownButton.setEnabled(true) ;
						if(!bSimActive){
							readWorld() ;
							application.repaint() ;
						}
						break ;
		case "DOWN":	visualizationZPosition-- ;
						if(visualizationZPosition + 1 == world.zBounds)
							goUpButton.setEnabled(false) ;
						else
							goUpButton.setEnabled(true) ;
						if(visualizationZPosition == 0)
							goDownButton.setEnabled(false) ;
						else
							goUpButton.setEnabled(true) ;
						if(!bSimActive){
							readWorld() ;
							application.repaint() ;
						}
						break ;
		case "TOGGLESPAWN":	bSpawnToggle = !bSpawnToggle ;
						if(bSpawnToggle){
							(worldEngine.getSpawner(new Cyanobacteria())).setQuantity(1) ;
							(worldEngine.getSpawner(new NitrogenFixer())).setQuantity(1) ;
							(worldEngine.getSpawner(new Phytoplankton())).setQuantity(1) ;
							(worldEngine.getSpawner(new SuperOrganism())).setQuantity(1) ;
							(worldEngine.getSpawner(new Zooplankton())).setQuantity(1) ;
							(worldEngine.getSpawner(new Streptomyces())).setQuantity(1) ;
							(worldEngine.getSpawner(new Dinoflagellate())).setQuantity(1) ;
							(worldEngine.getSpawner(new Yeast())).setQuantity(1) ;
							(worldEngine.getSpawner(new Coccolithophore())).setQuantity(1) ;
							(worldEngine.getSpawner(new Diatom())).setQuantity(1) ;
							autoSpawnButton.setText("Stop auto-populate") ;
						}
						else{
							(worldEngine.getSpawner(new Cyanobacteria())).setQuantity(0) ;
							(worldEngine.getSpawner(new NitrogenFixer())).setQuantity(0) ;
							(worldEngine.getSpawner(new Phytoplankton())).setQuantity(0) ;
							(worldEngine.getSpawner(new SuperOrganism())).setQuantity(0) ;
							(worldEngine.getSpawner(new Zooplankton())).setQuantity(0) ;
							(worldEngine.getSpawner(new Streptomyces())).setQuantity(0) ;
							(worldEngine.getSpawner(new Dinoflagellate())).setQuantity(0) ;
							(worldEngine.getSpawner(new Yeast())).setQuantity(0) ;
							(worldEngine.getSpawner(new Coccolithophore())).setQuantity(0) ;
							(worldEngine.getSpawner(new Diatom())).setQuantity(0) ;
							autoSpawnButton.setText("Start auto-populate") ;
						}
						break ;
		case "DRIFT":	switch(driftChoice){
							case "None":		world.bDrift = false ;
												world.driftVariance = 0 ;
												world.driftSpeedX = 0 ;
												world.driftSpeedY = 0 ;
												world.driftSpeedZ = 0 ;
												break ;
							case "Low":			world.bDrift = true ;
												world.driftVariance = 0.25 ;
												break ;
							case "High":		world.bDrift = true ;
												world.driftVariance = 1 ;
												break ;
							case "Gravity":		world.bDrift = true ;
												world.driftSpeedZ = -4.0 ;
												break ;
							case "+X current":	world.bDrift = true ;
												world.driftSpeedX = 2.0 ;
												break ;
							case "+Y current":	world.bDrift = true ;
												world.driftSpeedY = 2.0 ;
												break ;
						}
						if(organismsDriftBox.isSelected())
							world.driftStrength = 10 ;
						else
							world.driftStrength = 2 ;
						break ;
		case "FACING":	switch(facingChoice){
							case "+X side":	world.wallBrightness[0] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[0] = Integer.parseInt(facingHeat.getText()) ;
											break ;
							case "-X side": world.wallBrightness[1] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[1] = Integer.parseInt(facingHeat.getText()) ;
											break ;
							case "+Y side":	world.wallBrightness[2] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[2] = Integer.parseInt(facingHeat.getText()) ;
											break ;
							case "-Y side":	world.wallBrightness[3] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[3] = Integer.parseInt(facingHeat.getText()) ;
											break ;
							case "+Z side":	world.wallBrightness[4] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[4] = Integer.parseInt(facingHeat.getText()) ;
											break ;
							case "-Z side":	world.wallBrightness[5] = Integer.parseInt(facingLight.getText()) ;
											world.wallTemps[5] = Integer.parseInt(facingHeat.getText()) ;
											break ;
						}
						break ;
		case "SPAWNLOCATION":	switch(addFromChoice){
									case "Anywhere":		(worldEngine.getSpawner(new RandomFood(world.heatFloor))).xLocation = -1 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).yLocation = -1 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).zLocation = -1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).xLocation = -1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).yLocation = -1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).zLocation = -1 ;
															break ;
									case "Top":				(worldEngine.getSpawner(new RandomFood(world.heatFloor))).xLocation = 0 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).yLocation = 0 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).zLocation = world.zBounds - 1 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).length = world.xBounds ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).width = world.yBounds ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).height = 1 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).radius = -1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).xLocation = 0 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).yLocation = 0 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).zLocation = world.zBounds - 1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).length = world.xBounds ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).width = world.yBounds ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).height = 1 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).radius = -1 ;
															break ;
									case "Random sphere":	tempX = rand.nextInt(world.xBounds) ;
															tempY = rand.nextInt(world.yBounds) ;
															if(world.zBounds > 0)
																tempZ = rand.nextInt(world.zBounds) ;
															else
																tempZ = 0 ;
															tempInt = rand.nextInt((world.xBounds / 4)) + (world.xBounds / 3) ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).xLocation = tempX ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).yLocation = tempY ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).zLocation = tempZ ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).length = 0 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).width = 0 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).height = 0 ;
															(worldEngine.getSpawner(new RandomFood(world.heatFloor))).radius = tempInt ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).xLocation = tempX ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).yLocation = tempY ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).zLocation = tempZ ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).length = 0 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).width = 0 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).height = 0 ;
															(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).radius = tempInt ;
															break ;
						}
						break ;
		case "ENVIRON": switch(environmentChoice){
							case "Normal":	world.heatFloor = 1 ;
											for(int i = 0; i < 6; i++)
												world.wallTemps[i] = world.heatFloor ;
											break ;
							case "Hot":		world.heatFloor = 15 ;
											for(int i = 0; i < 6; i++)
												world.wallTemps[i] = world.heatFloor ;
											break ;
							case "Cold":	world.heatFloor = -15 ;
											for(int i = 0; i < 6; i++)
												world.wallTemps[i] = world.heatFloor ;
											break ;
							case "Very cold":world.heatFloor = -40 ;
											for(int i = 0; i < 6; i++)
												world.wallTemps[i] = world.heatFloor ;
											break ;
							case "More sources": for(int i = 0; i < 4; i++)
													(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
												((RandomHeatSource)(worldEngine.getSpawner(new RandomHeatSource())).toAdd).bVariable = true ;
												for(int i = 0; i < 4; i++)
													(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
												((RandomHeatSource)(worldEngine.getSpawner(new RandomHeatSource())).toAdd).bVariable = false ;
											break ;
							case "Toxic": 	for(int i = 0; i < 4; i++)
												(worldEngine.getSpawner(new ToxicSource(world, -1, -1, -1))).forceAddRandom() ;
											break ;
						}
						break ;
		}
	} 
	
	public void simulationStep(){																	//Makes everything happen
		worldEngine.worldStep() ;
		entityViewer.updateEntityPanel() ;
		readWorld() ;
	}
	
	public void readWorld(){																		//Read values from the appropriate Z level of the world and translate them into colors to display when redrawn
		Entity tempEnt ;
		int temp, temp2 ;
		
		minLight = 256 ;
		maxLight = 0 ;
		
		for(int x = 0; x < worldXSize; x++){
			for(int y = 0; y < worldYSize; y++){
				temp = (int)Math.max(-255, Math.min(255, world.allThings[x][y][visualizationZPosition].heat)) ;
				heatMap[x][y] = new Color(Math.max(0, temp), 0, Math.max(0, temp * -1)) ;
				temp = Math.max(0, Math.min(255, world.readIllumination(x,y,visualizationZPosition))) ;
				lightMap[x][y] = new Color(temp, temp, temp / 4) ;
				
				if(temp > maxLight)
					maxLight = temp ;
				else if(temp < minLight)
					minLight = temp ;
				tempEnt = world.getEntity(x,y,visualizationZPosition) ;
				if(tempEnt.entityType == EntityType.ORGANIC_OBJECT || tempEnt.entityType == EntityType.ORGANISM)
					nutrientMap[x][y] = ((OrganicEntity)world.getEntity(x,y,visualizationZPosition)).nutrientHue ;
				else
					nutrientMap[x][y] = Color.BLACK ;
				
				if(tempEnt.entityType == EntityType.ORGANIC_OBJECT || tempEnt.entityType == EntityType.ORGANISM){
					temp = (int)(Math.min(255, ((OrganicEntity)world.getEntity(x,y,visualizationZPosition)).energy / 2) / 1.2) ;
					temp2 = (int)(Math.min(255, ((OrganicEntity)world.getEntity(x,y,visualizationZPosition)).toxicity * 2.55)) ;
					energyToxicityMap[x][y] = new Color(temp2, temp, (temp + temp2) / 6) ;
				}
				else if(tempEnt.entityType == EntityType.PLUME){
					temp = (int)Math.min(((ToxicPlume)world.getEntity(x,y,visualizationZPosition)).toxicity, 255) ;
					energyToxicityMap[x][y] = new Color(temp, 0, temp / 4) ;
				}
				else
					energyToxicityMap[x][y] = Color.BLACK ;
				
				actionMap[x][y] = Color.BLACK ;
				subconsciousActionMap[x][y] = Color.BLACK ;
				
				switch((world.getEntity(x, y, visualizationZPosition)).entityType){
				case NOTHING:		if(world.getEntity(x, y, visualizationZPosition) instanceof ToxicPlume)
										entityMap[x][y] = new Color(60, 20, 50) ;
									else
										entityMap[x][y] = Color.BLACK ;
									break ;
				case LIGHT_SOURCE:	entityMap[x][y] = Color.YELLOW ;
									break ;
				case OBJECT:		entityMap[x][y] = Color.DARK_GRAY ;
									break ;
				case ORGANIC_OBJECT:entityMap[x][y] = new Color(80, 70, 20) ;
									break ;
				case ORGANISM:		entityMap[x][y] = new Color(180, 60, 210) ;
									break ;
				case PLUME:			entityMap[x][y] = new Color(50, 20, 60) ;
									break ;
				default:			entityMap[x][y] = Color.WHITE ;
				}
				if(world.getEntity(x, y, visualizationZPosition) instanceof AdvancedOrganism){
					if(world.getEntity(x, y, visualizationZPosition) instanceof Cyanobacteria)
						entityMap[x][y] = new Color(30, 130, 130) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof NitrogenFixer)
						entityMap[x][y] = new Color(140, 160, 75) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Phytoplankton)
						entityMap[x][y] = new Color(30, 180, 60) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof SuperOrganism)
						entityMap[x][y] = new Color(255, 180, 20) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Zooplankton)
						entityMap[x][y] = new Color(70, 130, 250) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Streptomyces)
						entityMap[x][y] = new Color(180, 60, 100) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Dinoflagellate)
						entityMap[x][y] = new Color(160, 130, 60) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Yeast)
						entityMap[x][y] = new Color(180, 155, 130) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Coccolithophore)
						entityMap[x][y] = new Color(185, 235, 200) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof Diatom)
						entityMap[x][y] = new Color(220, 255, 80) ;
					else if(world.getEntity(x, y, visualizationZPosition) instanceof CustomOrganism)
						entityMap[x][y] = ((CustomOrganism)world.getEntity(x, y, visualizationZPosition)).displayColor ;
					else
						entityMap[x][y] = new Color(235, 210, 120) ;
					
					switch(((AdvancedOrganism)(world.getEntity(x, y, visualizationZPosition))).subconsciousAction){
					case SIGNAL:		subconsciousActionMap[x][y] = new Color(220, 190, 150) ;
										break ;
					case PHOTOSYNTHESIS:subconsciousActionMap[x][y] = new Color(20, 220, 60) ;
										break ;
					case CHEMOSYNTHESIS:subconsciousActionMap[x][y] = new Color(220, 30, 220) ;
										break ;
					case REGENERATE:	subconsciousActionMap[x][y] = new Color(100, 160, 220) ;
										break ;
					default:			break ;
					}
				}
				if(world.getEntity(x, y, visualizationZPosition) instanceof Organism){
					switch(((Organism)(world.getEntity(x, y, visualizationZPosition))).currentAction){
					case SIGNAL:		actionMap[x][y] = new Color(220, 190, 150) ;
										break ;
					case REPRODUCE:		actionMap[x][y] = new Color(30, 60, 220) ;
										break ;
					case DIE:			actionMap[x][y] = new Color(100, 100, 20) ;
										break ;
					case MOVE:			actionMap[x][y] = new Color(30, 160, 150) ;
										break ;
					case IDLE:			actionMap[x][y] = Color.GRAY ;
										break ;
					case CREATED:		actionMap[x][y] = Color.CYAN ;
										break ;
					case EAT:			actionMap[x][y] = new Color(150, 150, 50) ;
										break ;
					case ATTACK:		actionMap[x][y] = new Color(220, 40, 40) ;
										break ;
					case DETOX:			actionMap[x][y] = new Color(110, 20, 130) ;
										break ;
					case TOXIFY:		actionMap[x][y] = new Color(240, 250, 80) ;
										break ;
					case EMIT_TOXINS:	actionMap[x][y] = new Color(255, 210, 80) ;
										break ;
					}
				}
			}
		}
	}
	
	public void resetWorld(){																								//Generate a new world and world engine
		visualizationZPosition = 0 ;
		if(bigWorldBox.isSelected()){
			bLargeWorld = true ;
			worldXSize = 100 ;
			worldYSize = 100 ;
			zoomScale = 3 ;
		}
		else {
			bLargeWorld = false ;
			worldXSize = 50 ;
			worldYSize = 50 ;
			zoomScale = 7 ;
		}
		if(world != null)
			world.endWorldThreads();
		worldZSize = Integer.parseInt((String)depthSelector.getSelectedItem()) ;
		
		world = new World(worldXSize, worldYSize, worldZSize, 1, 0, 3.0, 1.0) ;
		worldEngine = new WorldEngine(world) ;
		
		heatMap = new Color[worldXSize][worldYSize] ;
		lightMap = new Color[worldXSize][worldYSize] ;
		nutrientMap = new Color[worldXSize][worldYSize] ;
		entityMap = new Color[worldXSize][worldYSize] ;
		actionMap = new Color[worldXSize][worldYSize] ;
		subconsciousActionMap = new Color[worldXSize][worldYSize] ;
		energyToxicityMap = new Color[worldXSize][worldYSize] ;
		
		worldEngine.addSpawner(new SpawnRandomizer(world, new RandomLightSource(), 0, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new RandomHeatSource(), 0, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new ToxicSource(world, -1, -1, -1), 0, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new RandomFood(world.heatFloor), 0, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new RandomNutrientSource(world.heatFloor, true), 0, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Organism(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new AdvancedOrganism(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Cyanobacteria(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new NitrogenFixer(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Phytoplankton(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new SuperOrganism(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Zooplankton(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Streptomyces(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Dinoflagellate(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Yeast(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Coccolithophore(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new Diatom(), true, 20, 200, 0)) ;
		worldEngine.addSpawner(new SpawnRandomizer(world, new CustomOrganism(), true, 20, 200, 0)) ;
		
		String worldTemplateChoice = (String)resetTemplate.getSelectedItem();
		switch(worldTemplateChoice){
		case "Nothing":	break ;
		case "Low":		for(int i = 0; i < 2; i++)
							(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(1) ;
						break ;
		case "Medium":	for(int i = 0; i < 5; i++)
							(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
						for(int i = 0; i < 3; i++)
							(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(3) ;
						(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).setQuantity(1) ;
						break ;
		case "High":	for(int i = 0; i < 8; i++)
							(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
						for(int i = 0; i < 5; i++)
							(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(7) ;
						(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).setQuantity(2) ;
						break ;
		case "Med Nutrients":for(int i = 0; i < 5; i++)
							(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
						for(int i = 0; i < 3; i++)
							(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(1) ;
						(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).setQuantity(3) ;
						break ;
		case "High Nutrients":for(int i = 0; i < 8; i++)
							(worldEngine.getSpawner(new RandomLightSource())).forceAddRandom() ;
						for(int i = 0; i < 5; i++)
							(worldEngine.getSpawner(new RandomHeatSource())).forceAddRandom() ;
						(worldEngine.getSpawner(new RandomFood(world.heatFloor))).setQuantity(2) ;
						(worldEngine.getSpawner(new RandomNutrientSource(world.heatFloor, true))).setQuantity(7) ;
						break ;
		}
		
		if(visualizationZPosition + 1 == world.zBounds)
			goUpButton.setEnabled(false) ;
		else
			goUpButton.setEnabled(true) ;
		if(visualizationZPosition == 0)
			goDownButton.setEnabled(false) ;
		else
			goUpButton.setEnabled(true) ;
		
		worldEngine.worldStep() ;
		readWorld() ;
		entityViewer.entityPanelRead() ;
	}
	
	public Color gammaUp(Color toUp, double factor){											//A gamma correction method for making the light/heat visualization more visually meaningful
		int red, green, blue ;
		double gammaFactor = 1 / factor ;
		red = toUp.getRed();
		green = toUp.getGreen();
		blue = toUp.getBlue();
		
		red = (int) (255 * (Math.pow((double) red / (double) 255, gammaFactor)));
        green = (int) (255 * (Math.pow((double) green / (double) 255, gammaFactor)));
        blue = (int) (255 * (Math.pow((double) blue / (double) 255, gammaFactor)));
        
        return new Color(red, green, blue) ;
	}
	
	public void autoGamma(){																		//Gamma correction for heat/light maps
		if(worldEngine.stepMaxLight - worldEngine.stepMinLight < 40)
			lightAutoGamma = 1.0 ;
		else if(worldEngine.stepMaxLight - worldEngine.stepMinLight < 80)
			lightAutoGamma = 1.5 ;
		else if(worldEngine.stepMaxLight - worldEngine.stepMinLight < 140)
			lightAutoGamma = 2.5 ;
		else if(worldEngine.stepMaxLight - worldEngine.stepMinLight < 220)
			lightAutoGamma = 3.0 ;
		else
			lightAutoGamma = 3.5 ;
		
		if(worldEngine.stepMinHeat > 40)
			heatAutoGamma = 0.5 ;
		else if(worldEngine.stepMinHeat > 25)
			heatAutoGamma = 1.0 ;
		else if(worldEngine.stepMinHeat > 15)
			heatAutoGamma = 2.1 ;
		else if(worldEngine.stepMinHeat > 6)
			heatAutoGamma = 3.3 ;
		else
			heatAutoGamma = 4.5 ;		
	}
	
	public void attachShutDownHook(){																//Called to end threads, haven't noticed any difference from before this was added.
		Runtime.getRuntime().addShutdownHook(new Thread() {
		@Override
		public void run() {
			world.endWorldThreads() ;
		    }
		});
	}
}
