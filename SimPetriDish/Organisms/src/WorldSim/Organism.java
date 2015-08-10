/*
	Organism.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models the physical portion of a basic organism, taking care of automatic functions and storing body information and characteristics.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import java.util.Random;

public class Organism extends OrganicEntity {

	double photosyntheticCoefficient = 90.0 ;
	int maxLifetime = 10000 ;
	boolean bPhotosynthetic = true ;
	double eatingEfficiency = 0.35 ;
	double photosyntheticEfficiency = 0.55 ;
	double photosyntheticBuildup = 0.0 ;
	Color reproductionMinNutrients ;
	int reproductionMinEnergy = 50 ;
	int hungerTime = 8 ;
	int heatTolerance = 130 ;
	int coldTolerance = -25 ;
	World habitat ;
	ActionType currentAction = ActionType.CREATED ;
	ActionType subconsciousAction = ActionType.IDLE ;
	int actionDelay = 30 ;
	int timeToMature = 900 ;
	int reReproduce = 600 ;
	int reproductionCountdown ;
	
	Organism(){
		super() ;
		nutrientHue = new Color(10,10,10) ;
		reproductionMinNutrients = new Color(10,10,10) ;
		insulationFactor = 10.0 ;
		energy = 20 ;
		entityType = EntityType.ORGANISM ;
		bDecay = false ;
		translucence = 0.7 ;
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		reproductionCountdown = timeToMature ;
		weight = 10 ;
		maxDecayLifetime = 600 ;
		bTrackLocation = true ;
	}
	
	Organism(World myWorld, int xLoc, int yLoc, int zLoc, int energy, int redNutr, int greenNutr, int blueNutr){
		super(energy, redNutr, greenNutr, blueNutr) ;
		reproductionMinNutrients = new Color(redNutr,greenNutr,blueNutr) ;
		insulationFactor = 10.0 ;
		entityType = EntityType.ORGANISM ;
		bDecay = false ;
		translucence = 0.7 ;
		habitat = myWorld ;
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		reproductionCountdown = timeToMature ;
		weight = 10 ;
		maxDecayLifetime = 600 ;
		bTrackLocation = true ;
	}
	
	Organism(World myWorld, int xLoc, int yLoc, int zLoc, boolean photosynthetic, int minReproEnergy, int energy, int redNutr, int greenNutr, int blueNutr){
		super(energy, redNutr, greenNutr, blueNutr) ;
		reproductionMinNutrients = new Color(redNutr,greenNutr,blueNutr) ;
		bPhotosynthetic = photosynthetic ;
		reproductionMinEnergy = minReproEnergy ;
		insulationFactor = 10.0 ;
		entityType = EntityType.ORGANISM ;
		bDecay = false ;
		translucence = 0.7 ;
		habitat = myWorld ;
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		reproductionCountdown = timeToMature ;
		weight = 10 ;
		maxDecayLifetime = 600 ;
		bTrackLocation = true ;
	}
	
	Organism(World myWorld, int xLoc, int yLoc, int zLoc, boolean photosynthetic, int minReproEnergy, double eatEffi, double photoEffi, int maxLife, int timeToMaturity, int reproductionDelay, int hunger, double startHeat,
			int energy, int redNutr, int greenNutr, int blueNutr, int redMinReproNutr, int greenMinReproNutr, int blueMinReproNutr, int heatTol, int coldTol, int actnDelay){
		super(energy, redNutr, greenNutr, blueNutr) ;
		reproductionMinNutrients = new Color(redMinReproNutr,greenMinReproNutr,blueMinReproNutr) ;
		bPhotosynthetic = photosynthetic ;
		reproductionMinEnergy = minReproEnergy ;
		eatingEfficiency = eatEffi ;
		photosyntheticEfficiency = photoEffi ;
		maxLifetime = maxLife ;
		timeToMature = timeToMaturity ;
		reReproduce = reproductionDelay ;
		hungerTime = hunger ;
		heat = startHeat ;
		insulationFactor = 10.0 ;
		entityType = EntityType.ORGANISM ;
		bDecay = false ;
		translucence = 0.7 ;
		habitat = myWorld ;
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		reproductionCountdown = timeToMature ;
		weight = 10 ;
		maxDecayLifetime = 600 ;
		bTrackLocation = true ;
		heatTolerance = heatTol ;
		coldTolerance = coldTol ;
		actionDelay = actnDelay ;
	}
	
	public void age(){																			//Called from the world, handles subconscious actions
		super.age();
		
		if(reproductionCountdown > 0)
			reproductionCountdown-- ;
		
		if(lifetime % hungerTime == 0){
			useEnergy() ;
		}
		subconsciousAction() ;
		
		checkAlive() ;
	}
	
	public boolean checkAlive(){
		if(lifetime > maxLifetime || energy < 1 || nutrientHue.getRed() < 1 || nutrientHue.getGreen() < 1 || nutrientHue.getBlue() < 1 || heat > heatTolerance || heat < coldTolerance){
			bChangeType = true ;
			return false ;
		}
		return true ;
	}
	
	public void subconsciousAction(){															//Mostly a stub for use in subclasses
		photosynthesize() ;
	}
	
	public boolean checkReproduce(){
		if(reproductionMinEnergy > energy || reproductionMinNutrients.getRed() > nutrientHue.getRed() || reproductionMinNutrients.getGreen() > nutrientHue.getGreen() || 
				reproductionMinNutrients.getBlue() > nutrientHue.getBlue() || reproductionCountdown > 0)
			return false ;
		return true ;
	}
	
	public boolean reproduce(int xLoc, int yLoc, int zLoc){						//The Organism class (and subclasses) handle the generation of a new Organism and OrganismEngine, but the OrganismEngine controls when its done (though it can be blocked by 'physical' organism restrictions)
		int tempR, tempG, tempB, tempEnergy ;
		double tempHeat ;
		Organism spawn ;
		
		tempR = nutrientHue.getRed() / 2 ;
		tempG = nutrientHue.getGreen() / 2 ;
		tempB = nutrientHue.getBlue() / 2 ;
		tempEnergy = energy / 2 ;
		tempHeat = heat / 2 ;
		nutrientHue = new Color(tempR, tempG, tempB) ;
		energy = tempEnergy ;
		heat = tempHeat ;
		
		spawn = new Organism(habitat, xLoc, yLoc, zLoc, bPhotosynthetic, reproductionMinEnergy, eatingEfficiency, photosyntheticEfficiency, maxLifetime, timeToMature, reReproduce, hungerTime,
							tempHeat, tempEnergy, tempR, tempG, tempB, reproductionMinNutrients.getRed(), reproductionMinNutrients.getGreen(), reproductionMinNutrients.getBlue(), heatTolerance, coldTolerance, actionDelay) ;
		habitat.setEntity(xLoc, yLoc, zLoc, spawn) ;
		habitat.addController(new OrganismEngine(habitat, spawn)) ;
		
		currentAction = ActionType.REPRODUCE ;
		habitat.incrementReproduction() ;
		
		reproductionCountdown = reReproduce ;
		
		return true ;
	}
	
	public void eat(OrganicEntity food){										//Eating an organic entity, taking its nutrients and destroying it
		int tempR, tempG, tempB, tempEnergy ;
		
		if((OrganicEntity)food == null)
			return ;
		
		tempR = (int)(((OrganicEntity)food).nutrientHue.getRed() * eatingEfficiency) ;
		tempG = (int)(((OrganicEntity)food).nutrientHue.getGreen() * eatingEfficiency) ;
		tempB = (int)(((OrganicEntity)food).nutrientHue.getBlue() * eatingEfficiency) ;
		tempEnergy = (int)(((OrganicEntity)food).energy * eatingEfficiency) ;
		tempR += nutrientHue.getRed();
		tempG += nutrientHue.getGreen();
		tempB += nutrientHue.getBlue();
		if(tempR > 255)
			tempR = 255 ;
		if(tempG > 255)
			tempG = 255 ;
		if(tempB > 255)
			tempB = 255 ;
		nutrientHue = new Color(tempR, tempG, tempB) ;
		energy += tempEnergy ;
		toxicity += ((OrganicEntity)food).toxicity ;
		
		food.bChangeType = true ;
		currentAction = ActionType.EAT ;
		habitat.incrementConsumption() ;
	}
	
	public boolean nutrientReduction(){										//Called every time hunger makes an energy reduction and whenever you need a chance to lose a nutrient
		int temp, tempR, tempG, tempB ;
		Random rand = new Random() ;
		temp = rand.nextInt(48) ;
		if(temp == 0){
			temp = rand.nextInt(3) ;
			tempR = nutrientHue.getRed();
			tempG = nutrientHue.getGreen();
			tempB = nutrientHue.getBlue();
			switch(temp){
			case 0: tempR-- ;
					break ;
			case 1: tempG-- ;
					break ;
			case 2: tempB-- ;
					break ;
			}
			if(checkAlive()){
				nutrientHue = new Color(tempR, tempG, tempB) ;
				return true ;
			}
			else
				return false ;
		}
		return false ;
	}
	
	public boolean photosynthesize(){										//Generate energy form illumination intensity
		photosyntheticBuildup += (illumination / photosyntheticCoefficient) * photosyntheticEfficiency ;
		if(photosyntheticBuildup >= 1.0){
			energy++ ;
			while(photosyntheticBuildup >= 1.0)
				photosyntheticBuildup-- ;
			return true ;
		}
		return false ;
	}
	
	public void useEnergy(){												//Called every hungerTime, by default
		energy-- ;
		heat++ ;
		nutrientReduction() ;
	}
	
	public Organism newCopy(){												//Important for the way reproduction works
		return new Organism(habitat, xLocation, yLocation, zLocation, bPhotosynthetic, reproductionMinEnergy, eatingEfficiency, photosyntheticEfficiency, maxLifetime, timeToMature, reReproduce, hungerTime,
				heat, energy, nutrientHue.getRed(), nutrientHue.getGreen(), nutrientHue.getBlue(), reproductionMinNutrients.getRed(), reproductionMinNutrients.getGreen(), reproductionMinNutrients.getBlue(), heatTolerance, coldTolerance, actionDelay) ;
	}
}
