/*
	OrganismEngine.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Controls an organism's actions - the logic that makes it tick.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package WorldSim;

import java.util.Random;

public class OrganismEngine {
	Organism host ;
	World habitat ;
	double lastActionStep = 0 ;
	
	OrganismEngine(){
		habitat = null ;
		host = null ;
		lastActionStep = 0 ;
	}
	OrganismEngine(World inhabitedWorld, Organism controlled){
		habitat = inhabitedWorld ;
		lastActionStep = 0 ;
		host = controlled ;
	}
	
	public boolean act(){										//Called by the world at every step
		Entity tempTarget ;										//Important that it runs in one direction, so more critical potential actions are closer to the beginning of the method
		int foodTargets = 0 ;									//Once an action is taken, it ends - only one action happens per step
		int reproduceTargets = 0 ;
		int randomChoice ;
		int tempCoordX, tempCoordY, tempCoordZ ;
		Random rand = new Random() ;
		Entity[] surrounding ;
		
		host.currentAction = ActionType.IDLE ;
		
		if(host.lifetime >= lastActionStep + host.actionDelay){
			lastActionStep = host.lifetime ;
			
			tempCoordX = host.xLocation ;
			tempCoordY = host.yLocation ;
			tempCoordZ = host.zLocation ;
			
			surrounding = habitat.getSurrounding(host.xLocation, host.yLocation, host.zLocation) ;
			
			for(int i = 0; i < 6 ; i++){
				tempTarget = surrounding[i] ;
				if(tempTarget.entityType == EntityType.ORGANIC_OBJECT)
					foodTargets++ ;
				if(tempTarget.entityType == EntityType.NOTHING)
					reproduceTargets++ ;
			}
			if(foodTargets > 0){																							//Find something to eat and eat it
				randomChoice = rand.nextInt(foodTargets) ;
				for(int i = 0; i < 6; i++){
					if(randomChoice == 0 && surrounding[i].entityType == EntityType.ORGANIC_OBJECT){
						switch(i){
						case 0: tempCoordX++ ;
								break ;
						case 1: tempCoordX-- ;
								break ;
						case 2: tempCoordY++ ;
								break ;
						case 3: tempCoordY-- ;
								break ;
						case 4: tempCoordZ++ ;
								break ;
						case 5: tempCoordZ-- ;
								break ;
						}
						host.eat((OrganicEntity)habitat.getEntity(tempCoordX, tempCoordY, tempCoordZ)) ;
						
						return true ;
					}
					else if(surrounding[i].entityType == EntityType.ORGANIC_OBJECT)
						randomChoice-- ;
				}
			}else if(reproduceTargets > 0){																				//Find open space and reproduce
				randomChoice = rand.nextInt(reproduceTargets) ;
				for(int i = 0; i < 6; i++){
					if(randomChoice == 0 && surrounding[i].entityType == EntityType.NOTHING){
						switch(i){
						case 0: tempCoordX++ ;
								break ;
						case 1: tempCoordX-- ;
								break ;
						case 2: tempCoordY++ ;
								break ;
						case 3: tempCoordY-- ;
								break ;
						case 4: tempCoordZ++ ;
								break ;
						case 5: tempCoordZ-- ;
								break ;
						}
						if(host.checkReproduce()){
							if(host.reproduce(tempCoordX, tempCoordY, tempCoordZ)){							
								return true ;
							}
						}
					}
					else if(surrounding[i].entityType == EntityType.NOTHING)
						randomChoice-- ;
				}
			}
		}
		return false ;
	}
	
	OrganismEngine newCopy(Organism toControl){																//To copy OrganismEngine in SpawnRandomizer
		return new OrganismEngine(habitat, toControl) ;
	}
}
