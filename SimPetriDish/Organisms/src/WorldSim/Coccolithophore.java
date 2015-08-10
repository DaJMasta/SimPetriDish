/*
	Coccolithophore.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	Models an armored photosynthetic organism

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class Coccolithophore extends AdvancedOrganism {

	Coccolithophore(){
		super() ;
		name = "COCCO" ;
		damageResistance = 2 ;
		bPhotosynthetic = true ;
		photosyntheticEfficiency = 0.85 ;
		eatingEfficiency = 0.8 ;
		translucence = 0.60 ;
		insulationFactor = 20 ;
		timeToMature = 600 ;
		reReproduce = 600 ;
		hungerTime = 30 ;
		actionDelay = 20 ;
		reproductionCost = 5 ;
		redNutMod = 1.0 ;
		greenNutMod = 1.3 ;
		blueNutMod = 1.0 ;
		maxLifetime = 12000 ;
	}
	
	Coccolithophore(int xLoc, int yLoc, int zLoc, Coccolithophore parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Coccolithophore newCopy(){														//Important for SpawnRandomizer
		return new Coccolithophore(xLocation, yLocation, zLocation, this) ;
	}
}
