/*
	SpawnRandomizer.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	A utility object that can spawn entities with varying random parameters - delay between spawns and quantity spawned, spawn location (anywhere, box, sphere)
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.util.Random;

public class SpawnRandomizer {

	int xLocation, yLocation, zLocation, length, width, height, radius, delay, quantity, minDelay, maxDelay ;
	World toSpawnIn ;
	int nextSpawnIn = 0 ;
	Entity toAdd = null ;
	boolean bControlled = false ;
	boolean bRandomDelay = false ;
	int spawnCount = 0 ;
	
	SpawnRandomizer(Entity add){
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		radius = -1 ;
		delay = 0 ;
		quantity = 0 ;
		toSpawnIn = null ;
		toAdd = add ;
		minDelay = 0 ;
		maxDelay = 10 ;
		bRandomDelay = false ;
	}
	
	SpawnRandomizer(Entity add, boolean controlled){
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		radius = -1 ;
		delay = 0 ;
		quantity = 1 ;
		toSpawnIn = null ;
		toAdd = add ;
		bControlled = controlled ;
		minDelay = 0 ;
		maxDelay = 10 ;
		bRandomDelay = false ;
	}
	
	//basic, adds myQuantity add entities every myDelay to a random place
	SpawnRandomizer(World myWorld, Entity add, int myDelay, int myQuantity){
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = -1 ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		toAdd = add ;
		minDelay = 0 ;
		maxDelay = 10 ;
		bRandomDelay = false ;
	}
	
	//basic, adds myQuantity add entities every myDelay to a random place with associated controllers
	SpawnRandomizer(World myWorld, Entity add, boolean controlled, int myDelay, int myQuantity){
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = -1 ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		toAdd = add ;
		bControlled = controlled ;
		minDelay = 0 ;
		maxDelay = 10 ;
		bRandomDelay = false ;
	}
	
	//with a randomized delay between additions
	SpawnRandomizer(World myWorld, Entity add, boolean controlled, int myMinDelay, int myMaxDelay, int myQuantity){
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		toSpawnIn = myWorld ;
		delay = 0 ;
		quantity = myQuantity ;
		radius = -1 ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		toAdd = add ;
		bControlled = controlled ;
		minDelay = myMinDelay ;
		maxDelay = myMaxDelay ;
		bRandomDelay = true ;
	}
	
	//randomized delay (optional) and a zone add (randomly add within a box starting at a given location with a length (X), width (Y), and height (Z))
	SpawnRandomizer(World myWorld, Entity add, int xLoc, int yLoc, int zLoc, int myLength, int myWidth, int myHeight, int myDelay, int myQuantity, int newMin, int newMax, boolean bRandomize){
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		length = myLength ;
		width = myWidth ;
		height = myHeight ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = -1 ;
		toAdd = add ;
		minDelay = newMin;
		maxDelay = newMax ;
		bRandomDelay = bRandomize ;
	}
	
	//same as above, with controller
	SpawnRandomizer(World myWorld, Entity add, boolean controlled, int xLoc, int yLoc, int zLoc, int myLength, int myWidth, int myHeight, int myDelay, int myQuantity, int newMin, int newMax, boolean bRandomize){
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		length = myLength ;
		width = myWidth ;
		height = myHeight ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = -1 ;
		toAdd = add ;
		bControlled = controlled ;
		minDelay = newMin;
		maxDelay = newMax ;
		bRandomDelay = bRandomize ;
	}
	
	//randomized delay (optional) and a spherical zone add
	SpawnRandomizer(World myWorld, Entity add, int xLoc, int yLoc, int zLoc, int myRadius, int myDelay, int myQuantity, int newMin, int newMax, boolean bRandomize){
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = myRadius ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		toAdd = add ;
		minDelay = newMin;
		maxDelay = newMax ;
		bRandomDelay = bRandomize ;
	}
	//randomized delay (optional) and a randomized spherical zone add
	SpawnRandomizer(World myWorld, Entity add, boolean controlled, int xLoc, int yLoc, int zLoc, int myRadius, int myDelay, int myQuantity, int newMin, int newMax, boolean bRandomize){
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
		toSpawnIn = myWorld ;
		delay = myDelay ;
		quantity = myQuantity ;
		radius = myRadius ;
		length = 0 ;
		width = 0 ;
		height = 0 ;
		toAdd = add ;
		bControlled = controlled ;
		minDelay = newMin;
		maxDelay = newMax ;
		bRandomDelay = bRandomize ;
	}
	
	public void setQuantity(int newQty){
		quantity = newQty ;
		nextSpawnIn = 0 ;
	}
	
	public void setRandomDelay(int newMin, int newMax, boolean bRandomize){
		minDelay = newMin;
		maxDelay = newMax ;
		bRandomDelay = bRandomize ;
	}
	
	public boolean stepAddRandom(){					//called every step through WorldEngine
		boolean toReturn = false ;
		boolean checkReturn = false ;
		Random rand = new Random() ;
		
		if(nextSpawnIn <= 0){
			if(quantity > 0){
				for(int i = 0; i < quantity; i++){
					checkReturn = forceAddRandom() ;
					if(checkReturn)
						toReturn = true ;
				}					
			}
			if(bRandomDelay)
				nextSpawnIn = rand.nextInt(maxDelay - minDelay) + minDelay ;
			else
				nextSpawnIn = delay ;
		}
		else
			nextSpawnIn-- ;
		return toReturn ;
	}
	
	public boolean forceAddRandom(){							//Forcing a spawn attempt (returns whether something was spanwed) according to set location parameters
		int randX, randY, randZ, randRadius ;
		int attempts = 100 ;
		double randAngleI, randAngleJ ;
		Entity newCopy ;
		
		boolean bAdded = false ;
		boolean bMoveOn = false ;
		Random rand = new Random() ;
		
		if(toAdd == null)
			return false ;
		
		if(xLocation < 0 || xLocation > toSpawnIn.xBounds || yLocation < 0 || yLocation > toSpawnIn.yBounds || zLocation < 0 || zLocation > toSpawnIn.zBounds){
			while(!bMoveOn){
				randX = rand.nextInt(toSpawnIn.xBounds) ;
				randY = rand.nextInt(toSpawnIn.yBounds) ;
				randZ = rand.nextInt(toSpawnIn.zBounds) ;
				
				if(!toSpawnIn.isOccupied(randX, randY, randZ)){
					newCopy = toAdd.newCopy() ;
					newCopy.heat = toSpawnIn.heatFloor ;
					if(newCopy instanceof Organism){
						((Organism)newCopy).xLocation = randX ;
						((Organism)newCopy).yLocation = randY ;
						((Organism)newCopy).zLocation = randZ ;
						((Organism)newCopy).habitat = toSpawnIn ;
					}
					else if(newCopy instanceof ToxicSource){
						((ToxicSource)newCopy).xLocation = randX ;
						((ToxicSource)newCopy).yLocation = randY ;
						((ToxicSource)newCopy).zLocation = randZ ;
						((ToxicSource)newCopy).habitat = toSpawnIn ;
					}
					if(newCopy instanceof OrganicEntity && toSpawnIn.getEntity(randX, randY, randZ) instanceof ToxicPlume)
						((OrganicEntity)newCopy).toxicity += ((ToxicPlume)toSpawnIn.getEntity(randX, randY, randZ)).toxicity ;
					
					toSpawnIn.setEntity(randX, randY, randZ, newCopy) ;
					if(bControlled){
						 if(newCopy instanceof AdvancedOrganism){
							toSpawnIn.addController(new AdvancedOrganismEngine(toSpawnIn, (AdvancedOrganism)newCopy)) ;
							((AdvancedOrganism)newCopy).id = spawnCount ;
						 }
						 else if(newCopy instanceof Organism)
							toSpawnIn.addController(new OrganismEngine(toSpawnIn, (Organism)newCopy)) ;
					}
					bMoveOn = true ;
					bAdded = true ;
				}
				else
					attempts-- ;
				
				if(attempts < 1)
					bMoveOn = true ;
			}
			if(bAdded)
				spawnCount++ ;
			return bAdded ;
		}
		else if(radius < 0){
			while(!bMoveOn){
				randX = rand.nextInt(length) + xLocation ;
				randY = rand.nextInt(width) + yLocation ;
				randZ = rand.nextInt(height) + zLocation ;
				
				if(!toSpawnIn.isOccupied(randX, randY, randZ)){
					newCopy = toAdd.newCopy() ;
					if(newCopy instanceof Organism){
						((Organism)newCopy).xLocation = randX ;
						((Organism)newCopy).yLocation = randY ;
						((Organism)newCopy).zLocation = randZ ;
						((Organism)newCopy).habitat = toSpawnIn ;
					}
					toSpawnIn.setEntity(randX, randY, randZ, newCopy) ;
					if(bControlled){
						 if(newCopy instanceof AdvancedOrganism)
							toSpawnIn.addController(new AdvancedOrganismEngine(toSpawnIn, (AdvancedOrganism)newCopy)) ;
						 else if(newCopy instanceof Organism)
							toSpawnIn.addController(new OrganismEngine(toSpawnIn, (Organism)newCopy)) ;
					}
					bMoveOn = true ;
					bAdded = true ;
				}
				else
					attempts-- ;
				
				if(attempts < 1)
					bMoveOn = true ;
			}
			if(bAdded)
				spawnCount++ ;
			return bAdded ;
			
		}
		else{
			while(!bMoveOn){
				randRadius = rand.nextInt(radius) ;
				randAngleI = rand.nextDouble() * 2 * Math.PI ;
				randAngleJ = rand.nextDouble() * 2 * Math.PI ;
				if(toSpawnIn.xBounds != 1)
					randX = (int)((Math.sin(randAngleI) * Math.cos(randAngleJ) * randRadius) + 0.5 + xLocation) ;
				else
					randX = 0 ;
				if(toSpawnIn.yBounds != 1)
					randY = (int)((Math.cos(randAngleI) * Math.cos(randAngleJ) * randRadius) + 0.5 + yLocation) ;
				else
					randY = 0 ;
				if(toSpawnIn.zBounds != 1)
					randZ = (int)((Math.sin(randAngleJ) * randRadius) + 0.5 + zLocation) ;
				else
					randZ = 0 ;
				
				if(!toSpawnIn.isOccupied(randX, randY, randZ)){
					newCopy = toAdd.newCopy() ;
					if(newCopy instanceof Organism){
						((Organism)newCopy).xLocation = randX ;
						((Organism)newCopy).yLocation = randY ;
						((Organism)newCopy).zLocation = randZ ;
						((Organism)newCopy).habitat = toSpawnIn ;
					}
					toSpawnIn.setEntity(randX, randY, randZ, newCopy) ;
					if(bControlled){
						 if(newCopy instanceof AdvancedOrganism)
							toSpawnIn.addController(new AdvancedOrganismEngine(toSpawnIn, (AdvancedOrganism)newCopy)) ;
						 else if(newCopy instanceof Organism)
							toSpawnIn.addController(new OrganismEngine(toSpawnIn, (Organism)newCopy)) ;
					}
					bMoveOn = true ;
					bAdded = true ;
				}
				else
					attempts-- ;
				
				if(attempts < 1)
					bMoveOn = true ;
			}
			if(bAdded)
				spawnCount++ ;
			return bAdded ;
		}
	}
}
