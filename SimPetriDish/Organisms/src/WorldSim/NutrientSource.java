/*
	NutrientSource.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	A set of defaults to extend OrganicEntity to make useful nutrient sources (no energy, just nutrients).
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package WorldSim;

import java.awt.Color;

public class NutrientSource extends OrganicEntity {

	NutrientSource(){
		energy = 0 ;
		bDecay = false ;
		nutrientHue = new Color(20,20,20) ;
	}
	
	NutrientSource(boolean bDecays, Color nutrients, double startHeat){
		energy = 0 ;
		bDecay = bDecays ;
		nutrientHue = nutrients ;
	}
	
	NutrientSource(boolean bDecays, Color nutrients, double startHeat, int tox){
		energy = 0 ;
		bDecay = bDecays ;
		nutrientHue = nutrients ;
		toxicity = tox ;
		heat = startHeat ;
	}
	
	NutrientSource(int decayTime, Color nutrients, double startHeat){
		energy = 0 ;
		bDecay = true ;
		nutrientHue = nutrients ;
		maxDecayLifetime = decayTime ;
		heat = startHeat ;
	}
	
	NutrientSource(int decayTime, Color nutrients, double startHeat, int tox){
		energy = 0 ;
		bDecay = true ;
		nutrientHue = nutrients ;
		maxDecayLifetime = decayTime ;
		toxicity = tox ;
		heat = startHeat ;
	}
}
