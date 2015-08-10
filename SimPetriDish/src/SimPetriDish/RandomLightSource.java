/*
	RandomLightSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Randomizes a light source and allows for new random copies to be generated (same parameters, new randomization).
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.Random;

public class RandomLightSource extends LightSource {
	
	int minLightEmission = 5 ;
	int maxLightEmission = 235 ;
	int minVaryLowBound = 10 ;
	int minVaryHighBound = 100 ;
	int maxVaryLowBound = 20 ;
	int maxVaryHighBound = 200 ;
	int minTimeLowBound = 50 ;
	int minTimeHighBound = 150 ;
	int maxTimeLowBound = 150 ;
	int maxTimeHighBound = 700 ;
	
	
	RandomLightSource(){
		Random rand = new Random() ;
		bVariable = false ;
		entityType = EntityType.LIGHT_SOURCE ;
		lightEmission = rand.nextInt(maxLightEmission - minLightEmission) + minLightEmission ;
	}
	
	RandomLightSource(int minEmission, int maxEmission, double startHeat){
		Random rand = new Random() ;
		entityType = EntityType.LIGHT_SOURCE ;
		minLightEmission = minEmission ;
		maxLightEmission = maxEmission ;
		bVariable = false ;
		lightEmission = rand.nextInt(maxLightEmission - minLightEmission) + minLightEmission ;
		heat = startHeat ;
	}
	
	//For a variable light, randomized max and min brightness and max and min period lengths
	RandomLightSource(int minEmission, int maxEmission, int minMaxTimeVary, int maxMaxTimeVary, int minMinTimeVary, int maxMinTimeVary, int minMaxVary, int maxMaxVary,  int minMinVary, int maxMinVary, boolean bVary, double startHeat){
		Random rand = new Random() ;
		minLightEmission = minEmission ;
		maxLightEmission = maxEmission ;
		maxTimeLowBound = minMaxTimeVary ;
		maxTimeHighBound = maxMaxTimeVary ;
		minTimeLowBound = minMinTimeVary ;
		minTimeHighBound = maxMinTimeVary ;
		maxVaryLowBound = minMaxVary ;
		maxVaryHighBound = maxMaxVary ;
		minVaryLowBound = minMinVary ;
		minVaryHighBound = maxMinVary ;
		
		entityType = EntityType.LIGHT_SOURCE ;
		timeVaryAmount = rand.nextInt(maxTimeHighBound - maxTimeLowBound) + maxTimeLowBound ;
		minVaryDuration = rand.nextInt(minTimeHighBound - minTimeLowBound) + minTimeLowBound ;
		varyAmount = rand.nextInt(maxVaryHighBound - maxVaryLowBound) + maxVaryLowBound ;
		minVaryAmount = rand.nextInt(minVaryHighBound - minVaryLowBound) + minVaryLowBound ;
		lightEmission = rand.nextInt(maxLightEmission - minLightEmission) + minLightEmission ;
		bVariable = bVary ;
		heat = startHeat ;
	}
	
	public Entity newCopy(){				//Important for the way SpawnRandomizer works
		return new RandomLightSource(minLightEmission, maxLightEmission, maxTimeLowBound, maxTimeHighBound, minTimeLowBound, minTimeHighBound, maxVaryLowBound, maxVaryHighBound, minVaryLowBound, minVaryHighBound, bVariable, heat) ;
	}
}
