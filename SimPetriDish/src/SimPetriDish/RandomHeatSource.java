/*
	RandomHeatSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Randomizes a heat source and allows for new random copies to be generated (same parameters, new randomization).
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.Random;

public class RandomHeatSource extends HeatSource {

	int minHeatOutput = -100 ;
	int maxHeatOutput = 250 ;
	int minVaryLowBound = -100 ;
	int minVaryHighBound = 100 ;
	int maxVaryLowBound = 10 ;
	int maxVaryHighBound = 200 ;
	int minTimeLowBound = 30 ;
	int minTimeHighBound = 100 ;
	int maxTimeLowBound = 150 ;
	int maxTimeHighBound = 900 ;
	
	
	RandomHeatSource(){
		Random rand = new Random() ;
		bVariable = false ;
		entityType = EntityType.OBJECT ;
		heatOutput = rand.nextInt(maxHeatOutput - minHeatOutput) + minHeatOutput ;
	}
	
	RandomHeatSource(int minOutput, int maxOutput){
		Random rand = new Random() ;
		entityType = EntityType.OBJECT ;
		minHeatOutput = minOutput ;
		maxHeatOutput = maxOutput ;
		bVariable = false ;
		heatOutput = rand.nextInt(maxOutput - minOutput) + minOutput ;
	}
	
	//Fully specified bounds for random stats
	RandomHeatSource(int minOutput, int maxOutput, int minMaxTimeVary, int maxMaxTimeVary, int minMinTimeVary, int maxMinTimeVary, int minMaxVary, int maxMaxVary,  int minMinVary, int maxMinVary, boolean bVary){
		Random rand = new Random() ;
		minHeatOutput = minOutput ;
		maxHeatOutput = maxOutput ;
		maxTimeLowBound = minMaxTimeVary ;
		maxTimeHighBound = maxMaxTimeVary ;
		minTimeLowBound = minMinTimeVary ;
		minTimeHighBound = maxMinTimeVary ;
		maxVaryLowBound = minMaxVary ;
		maxVaryHighBound = maxMaxVary ;
		minVaryLowBound = minMinVary ;
		minVaryHighBound = maxMinVary ;
		
		entityType = EntityType.OBJECT ;
		timeVaryAmount = rand.nextInt(maxTimeHighBound - maxTimeLowBound) + maxTimeLowBound ;
		minVaryDuration = rand.nextInt(minTimeHighBound - minTimeLowBound) + minTimeLowBound ;
		varyAmount = rand.nextInt(maxVaryHighBound - maxVaryLowBound) + maxVaryLowBound ;
		minVaryAmount = rand.nextInt(minVaryHighBound - minVaryLowBound) + minVaryLowBound ;
		heatOutput = rand.nextInt(maxOutput - minOutput) + minOutput ;
		bVariable = bVary ;
	}
	
	public Entity newCopy(){				//Important for SpawnRandomizer
		return new RandomHeatSource(minHeatOutput, maxHeatOutput, maxTimeLowBound, maxTimeHighBound, minTimeLowBound, minTimeHighBound, maxVaryLowBound, maxVaryHighBound, minVaryLowBound, minVaryHighBound, bVariable) ;
	}
	
}
