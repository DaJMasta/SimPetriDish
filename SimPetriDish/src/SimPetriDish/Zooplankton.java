/*
	Zooplankton.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Models a mobile carnivorous organism, a set of default properties for an Advanced Organism
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

public class Zooplankton extends AdvancedOrganism{
	
	Zooplankton(){
		super() ;
		name = "ZOO" ;
		id = 1 ;
		bPhotosynthetic = false ;
		bSessile = false ;
		bCanAttack = true ;
		bCanSignal = true ;
		bRepeatSignal = false ;
		eatingEfficiency = 0.85 ;
		translucence = 0.7 ;
		numBites = 3 ;
		attackDamage = 3 ;
		hungerTime = 6 ;
		maxHealthPoints = 10 ;
		actionDelay = 12 ;
		redNutMod = 2.2 ;
		greenNutMod = 1.95 ;
		blueNutMod = 2.2 ;
		toxicTolerance = 110 ;
		detoxMin = 30 ;
		detoxMax = 85 ;
		healthRegenTime = 35 ;
		reReproduce = 500 ;
		maxLifetime = 9000 ;
	}
	
	Zooplankton(int xLoc, int yLoc, int zLoc, Zooplankton parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
	}
	
	public Zooplankton newCopy(){														//Important for SpawnRandomizer
		return new Zooplankton(xLocation, yLocation, zLocation, this) ;
	}
}
