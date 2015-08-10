/*
	Yeast.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a fast-growing organism that generates toxins (alcohol) when it metabolizes.

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.util.Random;

public class Yeast extends AdvancedOrganism{

	Yeast(){
		super() ;
		name = "YEAST" ;
		bEmitToxins = true ;
		bPhotosynthetic = false ;
		bSessile = true ;
		bCanSignal = false ;
		bRepeatSignal = false ;
		bCanAttack = false ;
		bChemotrophic = false ;
		hungerTime = 50 ;
		actionDelay = 25 ;
		heatTolerance = 230 ;
		coldTolerance = -45 ;
		timeToMature = 300 ;
		reReproduce = 250 ;
		redNutMod = 1.3 ;
		greenNutMod = 1.3 ;
		blueNutMod = 1.3 ;
		numBites = 3 ;
		eatingEfficiency = 0.95 ;
		detoxMin = 30 ;
		detoxMax = 70 ;
		detoxCost = 4 ;
		toxicTolerance = 130 ;
		reproductionCost = 2 ;
		maxLifetime = 9000 ;
	}
	
	Yeast(int xLoc, int yLoc, int zLoc, Yeast parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public void useEnergy(){
		Random rand = new Random() ;
		super.useEnergy() ;
		
		if(rand.nextInt(100) < 16){
			toxicity += 6 ;
			emitToxins(1) ;
		}
	}
	
	public Yeast newCopy(){														//Important for SpawnRandomizer
		return new Yeast(xLocation, yLocation, zLocation, this) ;
	}
}
