/*
	Dinoflagellate.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	Models an poisonous, motile photosynthetic organism

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

public class Dinoflagellate extends AdvancedOrganism {

	Dinoflagellate(){
		super() ;
		name = "DINOF" ;
		bGenerateToxins = true ;
		bSessile = false ;
		bPhotosynthetic = true ;
		photosyntheticEfficiency = 0.55 ;
		eatingEfficiency = 0.8 ;
		translucence = 0.7 ;
		hungerTime = 20 ;
		timeToMature = 500 ;
		reReproduce = 400 ;
		reproductionCost = 3 ;
		redNutMod = 1.6 ;
		greenNutMod = 1.5 ;
		blueNutMod = 1.4 ;
		actionDelay = 15 ;
		detoxMin = 20 ;
		detoxMax = 75 ;
		toxifyTarget = 35 ;
		toxicTolerance = 100 ;
		numBites = 3 ;
		maxLifetime = 9000 ;
	}
	
	Dinoflagellate(int xLoc, int yLoc, int zLoc, Dinoflagellate parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Dinoflagellate newCopy(){														//Important for SpawnRandomizer
		return new Dinoflagellate(xLocation, yLocation, zLocation, this) ;
	}
}
