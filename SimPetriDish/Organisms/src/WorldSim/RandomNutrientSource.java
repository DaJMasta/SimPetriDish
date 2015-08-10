/*
	RandomNutrientSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Randomizes a nutrient source and allows for new random copies to be generated (same parameters, new randomization).
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import java.util.Random;

public class RandomNutrientSource extends NutrientSource {
	
	int maxNutRed, minNutRed, maxNutGreen, minNutGreen, maxNutBlue, minNutBlue, maxTox, minTox, maxDecayTime, minDecayTime, floor ;
	
	//random stats based on defaults
	RandomNutrientSource(int heatFloor, boolean decay){
		super() ;
		maxNutRed = 40 ;
		minNutRed = 5 ;
		maxNutGreen = 40 ;
		minNutGreen = 5 ;
		maxNutBlue = 40 ;
		minNutBlue = 5 ;
		maxTox = 4 ;
		minTox = 0 ;
		maxDecayTime = 650 ;
		minDecayTime = 200 ;
		floor = heatFloor ;
		
		Random rand = new Random() ;
		nutrientHue = new Color(rand.nextInt(maxNutRed),rand.nextInt(maxNutGreen),rand.nextInt(maxNutBlue)) ;
		maxDecayLifetime = rand.nextInt(maxDecayTime - minDecayTime) + minDecayTime ;
		toxicity = rand.nextInt(maxTox - minTox) + minTox ;
		heat = heatFloor ;
		bDecay = decay ;
		calcDecayRate() ;
	}
	
	//random stats based on specified defaults
	RandomNutrientSource(int nutrientMax, int nutrientMin, int toxicityMax, int toxicityMin, int decayTimeMax, int decayTimeMin, int heatFloor, boolean decay){
		super() ;
		maxNutRed = nutrientMax ;
		minNutRed = nutrientMin ;
		maxNutGreen = nutrientMax ;
		minNutGreen = nutrientMin ;
		maxNutBlue = nutrientMax ;
		minNutBlue = nutrientMin ;
		maxTox = toxicityMax ;
		minTox = toxicityMin ;
		maxDecayTime = decayTimeMax ;
		minDecayTime = decayTimeMin ;
		floor = heatFloor ;
		
		Random rand = new Random() ;
		nutrientHue = new Color(rand.nextInt(nutrientMax - nutrientMin) + nutrientMin ,rand.nextInt(nutrientMax - nutrientMin) + nutrientMin ,rand.nextInt(nutrientMax - nutrientMin) + nutrientMin) ;
		maxDecayLifetime = rand.nextInt(decayTimeMax - decayTimeMin) + decayTimeMin ;
		toxicity = rand.nextInt(toxicityMax - toxicityMin) + toxicityMin ;
		heat = heatFloor ;
		bDecay = decay ;
		calcDecayRate() ;
	}
	
	//random stats (all) based on specified defaults
	RandomNutrientSource(int redNutMax, int redNutMin, int greenNutMax, int greenNutMin, int blueNutMax, int blueNutMin, int toxicityMax, int toxicityMin, int decayTimeMax, int decayTimeMin, int heatFloor, boolean decay){
		super() ;
		maxNutRed = redNutMax ;
		minNutRed = redNutMin ;
		maxNutGreen = greenNutMax ;
		minNutGreen = greenNutMin ;
		maxNutBlue = blueNutMax ;
		minNutBlue = blueNutMin ;
		maxTox = toxicityMax ;
		minTox = toxicityMin ;
		maxDecayTime = decayTimeMax ;
		minDecayTime = decayTimeMin ;
		floor = heatFloor ;
		
		Random rand = new Random() ;
		nutrientHue = new Color(rand.nextInt(redNutMax - redNutMin) + redNutMin, rand.nextInt(greenNutMax - greenNutMin) + greenNutMin, rand.nextInt(blueNutMax - blueNutMin) + blueNutMin) ;
		maxDecayLifetime = rand.nextInt(decayTimeMax - decayTimeMin) + decayTimeMin ;
		toxicity = rand.nextInt(toxicityMax - toxicityMin) + toxicityMin ;
		heat = heatFloor ;
		bDecay = decay ;
		calcDecayRate() ;
	}
	
	public RandomNutrientSource newCopy(){													//Important for the way SpawnRandomizers work
		return new RandomNutrientSource(maxNutRed, minNutRed, maxNutGreen, minNutGreen, maxNutBlue, minNutBlue, maxTox, minTox, maxDecayTime, minDecayTime, floor, bDecay) ;
	}
}
