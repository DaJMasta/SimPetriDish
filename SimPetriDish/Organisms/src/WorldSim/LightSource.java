/*
	LightSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a fixed point light source that can vary randomly between specified bounds and recalculates after a full period.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package WorldSim;

import java.util.Random;

public class LightSource extends Entity{
	
	boolean bVariable = false ;
	int nextVaryTime = 0 ;
	int varyTime = 0 ;
	int varyAmount = 150 ;
	int timeVaryAmount = 100 ;
	int minVaryAmount = 200 ;
	int minVaryDuration = 50 ;
	int curCycleVariance = 0 ;
	double[] facingTranslucence = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0} ;

	LightSource(){
		entityType = EntityType.LIGHT_SOURCE ;
		lightEmission = 255 ;
		bLightSource = true ;
		weight = 100 ;
	}
	
	LightSource(int setLight, double newHeat){
		bLightSource = true ;
		lightEmission = setLight ;
		entityType = EntityType.LIGHT_SOURCE ;
		heat = newHeat ;
		weight = 100 ;
	}
	
	//default variable light
	LightSource(int setLight, double newHeat, boolean variable){
		bLightSource = true ;
		entityType = EntityType.LIGHT_SOURCE ;
		heat = newHeat ;
		bVariable = variable ;
		lightEmission = setLight ;
		weight = 100 ;
	}
	
	//Facings have translucence that can be applied to make limited directional light sources
	LightSource(int setLight, int fluctuation, int newMinVaryTime, int newTimeVaryAmount, int newMinVaryAmount, int xyArc, double[] newFacingTrans, double newHeat){
		bLightSource = true ;
		entityType = EntityType.LIGHT_SOURCE ;
		heat = newHeat ;
		bVariable = true ;
		varyAmount = fluctuation ;
		minVaryAmount = newMinVaryAmount ;
		timeVaryAmount = newTimeVaryAmount ;
		minVaryDuration = newMinVaryTime ;
		lightEmission = setLight ;
		for(int i = 0; i < 6; i++)
			facingTranslucence[i] = newFacingTrans[i] ;
		weight = 100 ;
	}
	
	LightSource(int setLight, int fluctuation, int newMinVaryTime, int newTimeVaryAmount, int newMinVaryAmount, double newHeat){
		bLightSource = true ;
		entityType = EntityType.LIGHT_SOURCE ;
		heat = newHeat ;
		bVariable = true ;
		varyAmount = fluctuation ;
		minVaryAmount = newMinVaryAmount ;
		timeVaryAmount = newTimeVaryAmount ;
		minVaryDuration = newMinVaryTime ;
		lightEmission = setLight ;
		weight = 100 ;
	}
	
	public void age(){															//Sine wave based varying with a random period within specified bounds
		super.age() ;
		
		Random rand = new Random() ;
		
		if(bVariable){
			if(nextVaryTime > -1){
				nextVaryTime-- ;
				lightEmission = (int)(Math.sin((2 * Math.PI * (varyTime - nextVaryTime)) / varyTime) * curCycleVariance) + minVaryAmount ;
				
			}
			else {
				varyTime = minVaryDuration + rand.nextInt(timeVaryAmount) ;
				nextVaryTime = varyTime ;
				curCycleVariance = rand.nextInt(varyAmount) ;
				lightEmission = (int)(Math.sin((2 * Math.PI * (varyTime - nextVaryTime)) / varyTime) * curCycleVariance) + minVaryAmount ;
			}
		}
	}
	
}
