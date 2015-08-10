/*
	RandomFood.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Randomizes a food source and allows for new random copies to be generated (same parameters, new randomization).
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package WorldSim;

import java.awt.Color;
import java.util.Random;

public class RandomFood extends OrganicEntity{
	int maxRandEnergy, minRandEnergy, maxNutRed, minNutRed, maxNutGreen, minNutGreen, maxNutBlue, minNutBlue, maxTox, minTox, maxDecayTime, minDecayTime, floor ;

	RandomFood(int heatFloor){
		super() ;
		Random rand = new Random() ;
		maxRandEnergy = 220 ;
		minRandEnergy = 20 ;
		maxNutRed = 15 ;
		minNutRed = 3 ;
		maxNutGreen = 15 ;
		minNutGreen = 3 ;
		maxNutBlue = 15 ;
		minNutBlue = 3 ;
		maxTox = 7 ;
		minTox = 0 ;
		maxDecayTime = 500 ;
		minDecayTime = 150 ;
		energy = rand.nextInt(maxRandEnergy - minRandEnergy) + minRandEnergy  ;
		nutrientHue = new Color(rand.nextInt(maxNutRed - minNutRed) + minNutRed ,rand.nextInt(maxNutGreen - minNutGreen) + minNutGreen ,rand.nextInt(maxNutBlue - minNutBlue) + minNutBlue) ;
		maxDecayLifetime = rand.nextInt(maxDecayTime - minDecayTime) + minDecayTime ;
		toxicity = rand.nextInt(maxTox - minTox) + minTox ;
		heat = heatFloor ;
		calcDecayRate() ;
	}
	
	RandomFood(int energyMax, int energyMin,  int nutrientMax, int nutrientMin, int toxicityMax, int toxicityMin, int decayTimeMax, int decayTimeMin, int heatFloor){
		super() ;
		maxRandEnergy = energyMax ;
		minRandEnergy = energyMin ;
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
		energy = rand.nextInt(energyMax - energyMin) + energyMin  ;
		nutrientHue = new Color(rand.nextInt(nutrientMax - nutrientMin) + nutrientMin ,rand.nextInt(nutrientMax - nutrientMin) + nutrientMin ,rand.nextInt(nutrientMax - nutrientMin) + nutrientMin) ;
		maxDecayLifetime = rand.nextInt(decayTimeMax - decayTimeMin) + decayTimeMin ;
		toxicity = rand.nextInt(toxicityMax - toxicityMin) + toxicityMin ;
		heat = heatFloor ;
		calcDecayRate() ;
	}
	
	//Every max and min specified for randomly generating stats - used for copying stats from a previous RandomFood to generate a new random one within the same bounds
	RandomFood(int energyMax, int energyMin, int redNutMax, int redNutMin, int greenNutMax, int greenNutMin, int blueNutMax, int blueNutMin, int toxicityMax, int toxicityMin, int decayTimeMax, int decayTimeMin, int heatFloor){
		super() ;
		maxRandEnergy = energyMax ;
		minRandEnergy = energyMin ;
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
		energy = rand.nextInt(energyMax - energyMin) + energyMin  ;
		nutrientHue = new Color(rand.nextInt(redNutMax - redNutMin) + redNutMin ,rand.nextInt(greenNutMax - greenNutMin) + greenNutMin ,rand.nextInt(blueNutMax - blueNutMin) + blueNutMin) ;
		maxDecayLifetime = rand.nextInt(decayTimeMax - decayTimeMin) + decayTimeMin ;
		toxicity = rand.nextInt(toxicityMax - toxicityMin) + toxicityMin ;
		heat = heatFloor ;
		calcDecayRate() ;
	}
	
	public RandomFood newCopy(){						//Important for SpawnRandomizer
		return new RandomFood(maxRandEnergy, minRandEnergy, maxNutRed, minNutRed, maxNutGreen, minNutGreen, maxNutBlue, minNutBlue, maxTox, minTox, maxDecayTime, minDecayTime, floor) ;
	}
}
