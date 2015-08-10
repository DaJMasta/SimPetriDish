/*
	Phytyplankton.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a sessile, photosynthetic organism that reproduces quickly and consumes little energy, a set of default properties for an Advanced Organism
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

public class Phytoplankton extends AdvancedOrganism{

	Phytoplankton(){
		super() ;
		name = "PHYTO" ;
		id = 1 ;
		bPhotosynthetic = true ;
		photosyntheticEfficiency = 1.0 ;
		eatingEfficiency = 0.9 ;
		translucence = 0.75 ;
		damageResistance = 1 ;
		insulationFactor = 15 ;
		timeToMature = 300 ;
		reReproduce = 300 ;
		hungerTime = 30 ;
		actionDelay = 20 ;
		reproductionCost = 2 ;
		redNutMod = 0.8 ;
		greenNutMod = 0.95 ;
		blueNutMod = 0.8 ;
	}
	
	Phytoplankton(int xLoc, int yLoc, int zLoc, Phytoplankton parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Phytoplankton newCopy(){														//Important for SpawnRandomizer
		return new Phytoplankton(xLocation, yLocation, zLocation, this) ;
	}
}
