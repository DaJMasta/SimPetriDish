/*
	Cyannobacteria.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a mobile photosynthetic organism, a set of default properties for an Advanced Organism
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.awt.Color;

public class Cyanobacteria extends AdvancedOrganism{

	Cyanobacteria(){
		super() ;
		name = "CYANO" ;
		id = 1 ;
		bSessile = false ;
		bCanSignal = true ;
		bPhotosynthetic = true ;
		photosyntheticEfficiency = 0.75 ;
		eatingEfficiency = 0.8 ;
		translucence = 0.6 ;
		hungerTime = 22 ;
		timeToMature = 550 ;
		reReproduce = 350 ;
		reproductionCost = 3 ;
		redNutMod = 1.2 ;
		greenNutMod = 1.2 ;
		blueNutMod = 0.95 ;
		actionDelay = 15 ;
		maxLifetime = 8500 ;
	}
	
	Cyanobacteria(int xLoc, int yLoc, int zLoc, Cyanobacteria parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	
	public Cyanobacteria newCopy(){														//Important for SpawnRandomizer
		return new Cyanobacteria(xLocation, yLocation, zLocation, this) ;
	}
}
