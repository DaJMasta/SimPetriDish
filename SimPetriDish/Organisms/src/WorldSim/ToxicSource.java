/*
	ToxicSource.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	A simple generator for toxic plumes.

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.util.Random;

public class ToxicSource extends Entity {

	int plumeMin ;
	int plumeMax ;
	World habitat ;
	double plumeChance ;															//Percent, 1.0 = 100%
	
	ToxicSource(World newHabitat, int xLoc, int yLoc, int zLoc){
		plumeMin = 4 ;
		plumeMax = 14 ;
		plumeChance = 0.55 ;
		entityType = EntityType.OBJECT ;
		translucence = 0.0 ;
		weight = 100 ;
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		habitat = newHabitat ;
		bTrackLocation = true ;
	}
	
	ToxicSource(World newHabitat, int xLoc, int yLoc, int zLoc, int minPlume, int maxPlume, double newPlumeChance){
		plumeMin = minPlume ;
		plumeMax = maxPlume ;
		plumeChance = newPlumeChance ;
		entityType = EntityType.OBJECT ;
		translucence = 0.0 ;
		weight = 100 ;
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		habitat = newHabitat ;
		bTrackLocation = true ;
	}
	
	public void age(){										
		super.age() ;
		
		Random rand = new Random() ;
		
		if(rand.nextInt(100) < plumeChance * 100){
			emitToxins(rand.nextInt(plumeMax - plumeMin) + plumeMin) ;
		}		
	}
	
	public void emitToxins(int toxinStrength){															
		Entity[] surrounding = habitat.getSurrounding(xLocation,  yLocation,  zLocation) ;
		int targetX, targetY, targetZ, toxCreateAt ;
		Random rand = new Random() ;
		double targetHeat ;
		targetX = xLocation ;
		targetY = yLocation ;
		targetZ = zLocation ;
		
		toxCreateAt = rand.nextInt(6) ;
		
		switch(toxCreateAt){
		case 0: targetX++ ;
				break ;
		case 1: targetX-- ;
				break ;
		case 2: targetY++ ;
				break ;
		case 3: targetY-- ;
				break ;
		case 4: targetZ++ ;
				break ;
		case 5: targetZ-- ;
				break ;
		}
		
		if(surrounding[toxCreateAt] instanceof OrganicEntity)
			((OrganicEntity)habitat.getEntity(targetX, targetY, targetZ)).toxicity += toxinStrength ;
		else if(surrounding[toxCreateAt] instanceof ToxicPlume)
			((ToxicPlume)habitat.getEntity(targetX, targetY, targetZ)).toxicity += toxinStrength ;
		else if(surrounding[toxCreateAt].entityType == EntityType.NOTHING){
			targetHeat = habitat.getEntity(targetX, targetY, targetZ).heat ;
			habitat.setEntity(targetX, targetY, targetZ, new ToxicPlume(toxinStrength, targetHeat)) ;
		}
	}
	
	public Entity newCopy(){				//Important for SpawnRandomizer
		return new ToxicSource(habitat, xLocation, yLocation, zLocation, plumeMin, plumeMax, plumeChance) ;
	}
}
