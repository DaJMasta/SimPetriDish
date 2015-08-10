/*
	Streptomyces.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a toxic organism that can emit toxins for defense

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class Streptomyces extends AdvancedOrganism {

	Streptomyces(){
		super() ;
		name = "STREPTO" ;
		bEmitToxins = true ;
		bGenerateToxins = true ;
		bPhotosynthetic = false ;
		eatingEfficiency = 0.95 ;
		translucence = 0.75 ;
		hungerTime = 30 ;
		timeToMature = 600 ;
		reReproduce = 450 ;
		reproductionCost = 3 ;
		redNutMod = 1.5 ;
		greenNutMod = 1.35 ;
		blueNutMod = 1.35 ;
		actionDelay = 15 ;
		detoxMin = 70 ;
		detoxMax = 170 ;
		toxifyTarget = 150 ;
		toxicTolerance = 200 ;
		bCanSignal = true ;
		bRepeatSignal = true ;
		toxicityGenerateAmount = 20 ;
	}
	
	Streptomyces(int xLoc, int yLoc, int zLoc, Streptomyces parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Streptomyces newCopy(){														//Important for SpawnRandomizer
		return new Streptomyces(xLocation, yLocation, zLocation, this) ;
	}
}
