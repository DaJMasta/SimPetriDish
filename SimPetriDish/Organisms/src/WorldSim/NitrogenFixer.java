/*
	NitrogenFixer.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a sessile, chemotrophic organism, a set of default properties for an Advanced Organism
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class NitrogenFixer extends AdvancedOrganism{

	NitrogenFixer(){
		super() ;
		name = "NO3FIX" ;
		id = 1 ;
		bPhotosynthetic = false ;
		bChemotrophic = true ;
		eatingEfficiency = 0.7 ;
		detoxCost = 3 ;
		damageResistance = 1 ;
		heatTolerance = 250 ;
		coldTolerance = -75 ;
		hungerTime = 13 ;
		redNutMod = 1.5 ;
		greenNutMod = 1.5 ;
		blueNutMod = 1.5 ;
		numBites = 5 ;
		actionDelay = 20 ;
		maxLifetime = 11000 ;
	}
	
	NitrogenFixer(int xLoc, int yLoc, int zLoc, NitrogenFixer parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public NitrogenFixer newCopy(){														//Important for SpawnRandomizer
		return new NitrogenFixer(xLocation, yLocation, zLocation, this) ;
	}
}
