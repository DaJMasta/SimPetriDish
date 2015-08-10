/*
	Diatom.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	Models an spiky photosynthetic organism (deals damage to attackers)

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class Diatom extends AdvancedOrganism {

	Diatom(){
		super() ;
		name = "DIATOM" ;
		bSpiky = true ;
		bPhotosynthetic = true ;
		photosyntheticEfficiency = 0.95 ;
		eatingEfficiency = 0.8 ;
		translucence = 0.65 ;
		insulationFactor = 10 ;
		timeToMature = 500 ;
		reReproduce = 400 ;
		hungerTime = 30 ;
		actionDelay = 20 ;
		reproductionCost = 4 ;
		redNutMod = 1.0 ;
		greenNutMod = 1.1 ;
		blueNutMod = 1.0 ;
	}
	
	Diatom(int xLoc, int yLoc, int zLoc, Diatom parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Diatom newCopy(){														//Important for SpawnRandomizer
		return new Diatom(xLocation, yLocation, zLocation, this) ;
	}
}
