/*
	HeatSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Generates heat (or cold) and can vary, randomized between specified limits, in a sine function, recalculating after a full period.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package SimPetriDish;

import java.util.Random;

public class HeatSource extends Entity {
	boolean bVariable = false ;
	int nextVaryTime = 0 ;
	int varyTime = 0 ;
	int varyAmount = 70 ;
	int timeVaryAmount = 500 ;
	int minVaryAmount = 30 ;
	int minVaryDuration = 250 ;
	int curCycleVariance = 0 ;
	int heatOutput = 1 ;

	HeatSource(){
		entityType = EntityType.OBJECT ;
		heatOutput = 100 ;
		translucence = 0.0 ;
		weight = 100 ;
	}
	
	//default variable heat source
	HeatSource(boolean variable){
		entityType = EntityType.OBJECT ;
		bVariable = variable ;
		translucence = 0.0 ;
		weight = 100 ;
	}
	
	//fixed heat source
	HeatSource(int output){
		entityType = EntityType.OBJECT ;
		bVariable = false ;
		heatOutput = output ;
		translucence = 0.0 ;
		weight = 100 ;
	}
	
	HeatSource(int fluctuation, int newMinVaryTime, int newTimeVaryAmount, int newMinVaryAmount){
		entityType = EntityType.OBJECT ;
		bVariable = true ;
		varyAmount = fluctuation ;
		minVaryAmount = newMinVaryAmount ;
		timeVaryAmount = newTimeVaryAmount ;
		minVaryDuration = newMinVaryTime ;
		translucence = 0.0 ;
		weight = 100 ;
	}
	
	public void age(){										//sine wave based variance with a random period within specified bounds
		super.age() ;
		
		Random rand = new Random() ;
		
		if(bVariable){
			if(nextVaryTime > -1){
				nextVaryTime-- ;
				heat = (int)(Math.sin((2 * Math.PI * (varyTime - nextVaryTime)) / varyTime) * curCycleVariance) + minVaryAmount ;
				
			}
			else {
				varyTime = minVaryDuration + rand.nextInt(timeVaryAmount) ;
				nextVaryTime = varyTime ;
				curCycleVariance = rand.nextInt(varyAmount) ;
				heat = (int)(Math.sin((2 * Math.PI * (varyTime - nextVaryTime)) / varyTime) * curCycleVariance) + minVaryAmount ;
			}
		}
		else
			heat = heatOutput ;
	}
}
