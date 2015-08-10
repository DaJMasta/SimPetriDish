/*
	SuperOrganism.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models an organism with every AdvancedOrganism ability allowed, a set of default properties for an Advanced Organism
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class SuperOrganism extends AdvancedOrganism{
	
	SuperOrganism(){
		super() ;
		name = "SUPER" ;
		id = 1 ;
		bPhotosynthetic = true ;
		bSessile = false ;
		bCanAttack = true ;
		bCanSignal = true ;
		bRepeatSignal = true ;
		bChemotrophic = true ;
		eatingEfficiency = 0.60 ;
		photosyntheticEfficiency = 0.35 ;
		translucence = 0.6 ;
		chemicalEnergyAmount = 3 ;
		signalsPerEnergy = 5 ;
		hungerTime = 8 ;
		reproductionMinEnergy = 75 ;
		reReproduce = 1500 ;
		timeToMature = 2000 ;
		actionDelay = 18 ;
		attackDamage = 3 ;
		numBites = 5 ;
		reproductionCost = 6 ;
		redNutMod = 2.6 ;
		greenNutMod = 2.6 ;
		blueNutMod = 2.6 ;
		healthRegenTime = 45 ;
	}
	
	SuperOrganism(int xLoc, int yLoc, int zLoc, SuperOrganism parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public SuperOrganism newCopy(){														//Important for SpawnRandomizer
		return new SuperOrganism(xLocation, yLocation, zLocation, this) ;
	}
}
