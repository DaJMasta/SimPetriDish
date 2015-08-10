/*
	ToxicPlume.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	A plume entity - contains toxicity and blocks light according to its concentration, can randomly spread to adjacents, and fades over time.  If spread to another object, it toxifies it.

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.util.Random;

public class ToxicPlume extends Entity {
	double toxicity ;
	double decayRate = 0.08;											//toxicity lost per step
	double spreadChance = 0.15 ;								//chance per step (1.0 = 100%) before drift and world push
	int maxTox = 100 ;
	
	ToxicPlume(){
		super(1.0, 1.0, false, 0, EntityType.PLUME) ;
		toxicity = 10 ;
		translucence = Math.min((1.0 - (toxicity / maxTox)), 0.95) ;
		weight = 1 ;
		lifetime = 0 ;
	}
	
	ToxicPlume(double newTox, double newHeat){
		super(1.0, 1.0, false, 0, EntityType.PLUME) ;
		toxicity = newTox ;
		translucence = Math.min((1.0 - (toxicity / maxTox)), 0.95) ;
		weight = 1 ;
		heat = newHeat ;
		lifetime = 0 ;
	}
	
	ToxicPlume(double newTox, double newSpread, double newHeat){
		super(1.0, 1.0, false, 0, EntityType.PLUME) ;
		toxicity = newTox ;
		spreadChance = newSpread ;
		translucence = Math.max(Math.min((1.0 - (toxicity / maxTox)), 0.99), 0.0) ;
		weight = 1 ;
		heat = newHeat ;
		lifetime = 0 ;
	}
	
	public double doSpread(double randomDrift){ 
		Random rand = new Random() ;
		double temp = 0 ;
		
		if(toxicity > maxTox)
			toxicity = maxTox ;
		
		if(spreadChance + randomDrift <= 0 || toxicity < 2)
			return 0 ;
		if(rand.nextInt(10000) < ((spreadChance * 100) + randomDrift) * 100){
			temp = toxicity / 2.0 ;
			toxicity = temp ;
			return temp ;				
		}
		return 0 ;
	}
	
	public void age(){
		super.age() ;
		if(toxicity > maxTox)
			toxicity = maxTox ;
		toxicity -= decayRate ;
		translucence = Math.min((1.0 - (toxicity / maxTox)), 0.99) ;
	}
	
	public void setToxicity(double newTox){
		toxicity = newTox ;
		translucence = Math.min((1.0 - (toxicity / maxTox)), 0.99) ;
	}
}
