/*
	OrganicEntity.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Adds nourishment characteristics to an Entity and incorporates a decay function to lose nutritional value over time.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package SimPetriDish;

import java.awt.Color;

public class OrganicEntity extends Entity {
	int energy = 1 ;
	int maxEnergy = 1000 ;
	int toxicity = 0 ;
	Color nutrientHue ;
	int maxDecayLifetime = 500 ;
	boolean bChangeType = false ;
	boolean bDecay = true ;
	int energyDecayRate, redDecayRate, greenDecayRate, blueDecayRate ;
	int burnTemp = 400 ;
	
	OrganicEntity(){
		super() ;
		nutrientHue = new Color(0,0,0) ;
		entityType = EntityType.ORGANIC_OBJECT ;
		translucence = 0.8 ;
		insulationFactor = 5.0 ;
		weight = 2 ;
		calcDecayRate() ;
	}
	
	OrganicEntity(int startEnergy, int redNutr, int greenNutr, int blueNutr){
		super() ;
		energy = startEnergy ;
		nutrientHue = new Color(redNutr,greenNutr,blueNutr) ;
		entityType = EntityType.ORGANIC_OBJECT ;
		translucence = 0.8 ;
		insulationFactor = 5.0 ;
		weight = 2 ;
		calcDecayRate() ;
	}
	
	OrganicEntity(int startEnergy, int redNutr, int greenNutr, int blueNutr, int maxDecay, int tox){
		super() ;
		energy = startEnergy ;
		nutrientHue = new Color(redNutr,greenNutr,blueNutr) ;
		entityType = EntityType.ORGANIC_OBJECT ;
		translucence = 0.8 ;
		maxDecayLifetime = maxDecay ;
		insulationFactor = 5.0 ;
		toxicity = tox ;
		weight = 2 ;
		calcDecayRate() ;
	}
	
	OrganicEntity(int startEnergy, int redNutr, int greenNutr, int blueNutr, int maxDecay, int tox, double startHeat){
		super() ;
		energy = startEnergy ;
		nutrientHue = new Color(redNutr,greenNutr,blueNutr) ;
		entityType = EntityType.ORGANIC_OBJECT ;
		translucence = 0.8 ;
		maxDecayLifetime = maxDecay ;
		insulationFactor = 5.0 ;
		toxicity = tox ;
		heat = startHeat ;
		weight = 2 ;
		calcDecayRate() ;
	}
	
	public void calcDecayRate(){																	//how quickly each stat deteriorates
		if(energy < 0)
			energy = 0 ;
		energyDecayRate = maxDecayLifetime / (energy + 1) ;
		redDecayRate = maxDecayLifetime / (nutrientHue.getRed() + 1) ;
		greenDecayRate = maxDecayLifetime / (nutrientHue.getGreen() + 1) ;
		blueDecayRate = maxDecayLifetime / (nutrientHue.getBlue() + 1) ;
	}
	
	public void age(){
		super.age();
		
		if(energy > maxEnergy)
			energy = maxEnergy ;
		
		if(bDecay)
			checkDecay() ;
	}
	
	public void checkDecay(){
		if(lifetime > maxDecayLifetime || (energy < 1 && nutrientHue.getRed() < 1 && nutrientHue.getGreen() < 1 && nutrientHue.getBlue() < 1) || heat > burnTemp){
			bChangeType = true ;
		}
		else
			decay() ;
	}
	
	public void decay(){																//reduce nutritional value over time until all zero, then destroy
		int tempR, tempG, tempB ;
		tempR = nutrientHue.getRed() ;
		tempG = nutrientHue.getGreen();
		tempB = nutrientHue.getBlue();
		if(lifetime % energyDecayRate == 0 && energy > 0)
			energy-- ;
		if(lifetime % redDecayRate == 0 && tempR > 0)
			tempR-- ;
		if(lifetime % greenDecayRate == 0 && tempG > 0)
			tempG-- ;
		if(lifetime % blueDecayRate == 0 && tempB > 0)
			tempB-- ;
		nutrientHue = new Color(tempR, tempG, tempB) ;
	}	
	
	public OrganicEntity newCopy(){														//To copy in SpawnRandomizer
		return new OrganicEntity(energy, nutrientHue.getRed(), nutrientHue.getGreen(), nutrientHue.getBlue(), maxDecayLifetime, toxicity, heat) ;
	}
}
