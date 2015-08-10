/*
	AdvancedOrganismEngine.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Controls an AdvancedOrganism to use all of its functions automatically - much more complex behavior than the basic OrganismEngine, but still fixed based on organism parameters.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.Random;

public class AdvancedOrganismEngine extends OrganismEngine {
	
	int chemosynthesisNutMin = 6 ;
	int chemosynthesisMinEnergy = 70 ;
	int foodSeekMin = 30 ;
	int foodSeekMax = 400 ;
	int nutSeekMin = 10 ;
	int nutSeekMax = 200 ;
	int minIlluminationToStay = 14 ;
	int lastX, lastY, lastZ ;
	boolean bIsHungry = false ;
	boolean bIsStarving = false ;
	int targetX, targetY, targetZ ;
	boolean bDidEmit = false ;
	double coldRunFactor = 0.75 ;
	double heatRunFactor = 0.85 ;
	
	AdvancedOrganismEngine(){
		targetX = -1 ;
		targetY = -1 ;
		targetZ = -1 ;
		lastX = -1 ;
		lastY = -1 ;
		lastZ = -1 ;
	}
	
	AdvancedOrganismEngine(World inhabitedWorld, AdvancedOrganism controlled){
		habitat = inhabitedWorld ;
		host = controlled ;
		
		targetX = host.xLocation ;
		targetY = host.yLocation ;
		targetZ = host.zLocation ;
		lastX = host.xLocation ;
		lastY = host.yLocation ;
		lastZ = host.zLocation ;
	}
	
	public AdvancedOrganismEngine newCopy(AdvancedOrganism toControl){					//Important for SpawnRandomizer
		return new AdvancedOrganismEngine(habitat, (AdvancedOrganism)host) ;
	}
	
	public boolean act(){
		Entity tempTarget ;
		int foodTargets = 0 ;
		int reproduceMoveTargets = 0 ;
		int attackTargets = 0 ;
		int signalTargets = 0 ;
		int randomChoice ;
		ChemicalSignal toSend ;
		Random rand = new Random() ;
		int tempInt ;
		
		if(host.lifetime < 5)
			return false ;
		
		targetX = host.xLocation ;
		targetY = host.yLocation ;
		targetZ = host.zLocation ;
		
		bIsHungry = false ;
		bIsStarving = false ;
		host.currentAction = ActionType.IDLE ;
		
		Entity[] surrounding = habitat.getSurrounding(host.xLocation,  host.yLocation,  host.zLocation) ;
		
		for(int i = 0; i < 6 ; i++){
			tempTarget = surrounding[i] ;
			if(tempTarget.entityType == EntityType.ORGANISM)
				attackTargets++ ;
			else if(tempTarget.entityType == EntityType.ORGANIC_OBJECT)
				foodTargets++ ;
			else if(tempTarget.entityType == EntityType.NOTHING || tempTarget.entityType == EntityType.PLUME)
				reproduceMoveTargets++ ;
			if(surrounding[i] instanceof AdvancedOrganism)
				signalTargets++ ;
		}
		
		//Are they hungry
		if(host.energy <= foodSeekMin || host.nutrientHue.getRed() <= nutSeekMin ||  host.nutrientHue.getGreen() <= nutSeekMin ||  host.nutrientHue.getBlue() <= nutSeekMin)
			bIsStarving = true ;
		if(host.energy < foodSeekMax || host.nutrientHue.getRed() < nutSeekMax ||  host.nutrientHue.getGreen() < nutSeekMax ||  host.nutrientHue.getBlue() < nutSeekMax)
			bIsHungry = true ;
		
		//Chemosynthesis allowed
		if(((AdvancedOrganism)host).bChemotrophic){
			if(host.energy <= chemosynthesisMinEnergy && (host.nutrientHue.getRed() >= chemosynthesisNutMin && host.nutrientHue.getGreen() >= chemosynthesisNutMin && host.nutrientHue.getBlue() >= chemosynthesisNutMin))
				((AdvancedOrganism)host).bDoChemosynthesis = true ;
			else
				((AdvancedOrganism)host).bDoChemosynthesis = false ;
		}
		
		//Reactionary signaling				-- subconscious, but evaluated here
		if(((AdvancedOrganism)host).bCanSignal && signalTargets > 0){
			if(((AdvancedOrganism)host).attacker != null && ((AdvancedOrganism)host).lastSignalSent.message != 'E'){
				toSend = new ChemicalSignal('E', 100, 4, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
			else if(((AdvancedOrganism)host).heat > host.heatTolerance * (heatRunFactor + 0.05) && ((AdvancedOrganism)host).lastSignalSent.message != 'H'){
				toSend = new ChemicalSignal('H', 80, 3, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
			else if(((AdvancedOrganism)host).heat < host.coldTolerance * (coldRunFactor + 0.05) && ((AdvancedOrganism)host).lastSignalSent.message != 'C'){
				toSend = new ChemicalSignal('C', 80, 3, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
		}
		
		if(((lastActionStep + host.actionDelay) * 2 > host.lifetime  && ((AdvancedOrganism)host).bVeryCold) || ((lastActionStep + host.actionDelay) * 1.25 > host.lifetime  && ((AdvancedOrganism)host).bCold) || (lastActionStep + host.actionDelay > host.lifetime)){
			return false ;
		}
		
		//Degrade memory
		
		if(!((AdvancedOrganism)host).recievedSignal.degrade()){
			((AdvancedOrganism)host).bSignaled = false ;
			((AdvancedOrganism)host).recievedSignal = new ChemicalSignal() ;
			((AdvancedOrganism)host).recievedSignalSender = null ;
		}
		if(((AdvancedOrganism)host).attackMemoryCountdown > 0)
			((AdvancedOrganism)host).attackMemoryCountdown-- ;
		
		//Run from attacker
		if(!((AdvancedOrganism)host).bSessile && ((AdvancedOrganism)host).attackMemoryCountdown > 0 && reproduceMoveTargets > 0 && !(((AdvancedOrganism)host).lastVictim == ((AdvancedOrganism)host).attacker)){
			if(((AdvancedOrganism)host).attacker == null && host.bPhotosynthetic){
				randomChoice = bestLight(surrounding) ;
			}
			else if(((AdvancedOrganism)host).attacker == null){
				randomChoice = bestHeat(surrounding) ;
			}
			else
				randomChoice = bestRun(surrounding, ((AdvancedOrganism)host).attacker, false) ;
			
			if(randomChoice >= 0){
				target(randomChoice) ;
				if(moveToTarget())
					return true ;
			}
		}
		
		//Run from heat/cold
		if(!((AdvancedOrganism)host).bSessile && (host.heat > host.heatTolerance * heatRunFactor || host.heat < host.coldTolerance * coldRunFactor) && reproduceMoveTargets > 0){
			target(bestHeat(surrounding)) ;
			moveToTarget() ;
			return true ;
		}
		
		//Run from toxic plumes
		if(!((AdvancedOrganism)host).bSessile && host.toxicity > ((AdvancedOrganism)host).detoxMax && reproduceMoveTargets > 0){
			if(host.bPhotosynthetic){
				target(bestLight(surrounding)) ;
				if(moveToTarget())
					return true ;
			}
			else{
				target(bestHeat(surrounding)) ;
				if(moveToTarget())
					return true ;
			}	
		}
		
		//Emit toxins
		if((((AdvancedOrganism)host).bEmitToxins && ((AdvancedOrganism)host).attackMemoryCountdown > 0) || host.toxicity > ((AdvancedOrganism)host).detoxMax){
			if(!bDidEmit){
				((AdvancedOrganism)host).emitToxins((((AdvancedOrganism)host).toxifyTarget - ((AdvancedOrganism)host).detoxMin) / 6) ;
				bDidEmit = true ;
			}
		}
		else if(bDidEmit)
			bDidEmit = false ;
		
		//Run from signaled Enemy (or emit toxins
		if(!bIsStarving && ((AdvancedOrganism)host).bSignaled){
			if(((AdvancedOrganism)host).recievedSignal.message == 'E'){
				if(!((AdvancedOrganism)host).bSessile){
					if(((AdvancedOrganism)host).recievedSignalSender == null && host.bPhotosynthetic){
						randomChoice = bestLight(surrounding) ;
					}
					else if(((AdvancedOrganism)host).recievedSignalSender == null){
						randomChoice = bestHeat(surrounding) ;
					}
					else
						randomChoice = bestRun(surrounding, ((AdvancedOrganism)host).recievedSignalSender, false) ;
					
					if(randomChoice >= 0){
						target(randomChoice) ;
						if(moveToTarget())
							return true ;
					}
				}
				else if(((AdvancedOrganism)host).bEmitToxins){
					if(!bDidEmit){
						((AdvancedOrganism)host).emitToxins(((AdvancedOrganism)host).toxifyTarget / 6) ;
						bDidEmit = true ;
					}
					else
						bDidEmit = false ;
				}
			}
		}
		
		//Detox
		if(!bIsStarving && host.toxicity > ((AdvancedOrganism)host).detoxMin){
			if(!((AdvancedOrganism)host).bGenerateToxins){
				((AdvancedOrganism)host).detox() ;
				return true ;
			}
			else if(host.toxicity > ((AdvancedOrganism)host).detoxMax){
				((AdvancedOrganism)host).detox() ;
				return true ;
			}
		}
		
		//Reproduce
		if(host.checkReproduce() && reproduceMoveTargets > 0){
			if(host.bPhotosynthetic){
				tempInt = bestLight(surrounding) ;
				if(tempInt == -1){
					randomChoice = rand.nextInt(reproduceMoveTargets) ;
					for(int i = 0; i < 6; i++){
						if(randomChoice == 0 && surrounding[i].entityType == EntityType.NOTHING){
							target(i) ;
							host.reproduce(targetX, targetY, targetZ) ;
							return true ;
							}
						else if(surrounding[i].entityType == EntityType.NOTHING)
							randomChoice-- ;
					}
				}
				else{
					target(tempInt) ;
					host.reproduce(targetX, targetY, targetZ) ;
					return true ;
				}
			}
			else{
				randomChoice = rand.nextInt(reproduceMoveTargets) ;
				for(int i = 0; i < 6; i++){
					if(randomChoice == 0 && surrounding[i].entityType == EntityType.NOTHING){
						target(i) ;
						host.reproduce(targetX, targetY, targetZ) ;
						return true ;
						}
					else if(surrounding[i].entityType == EntityType.NOTHING)
						randomChoice-- ;
				}
			}
		}
		
		//Eat
		if(bIsHungry && foodTargets > 0){
			tempInt = bestFood(surrounding) ;
			if(tempInt >= 0){
				host.eat((OrganicEntity)surrounding[tempInt]) ;
				return true ;
			}
			else{
				randomChoice = rand.nextInt(foodTargets) ;
				for(int i = 0; i < 6; i++){
					if(randomChoice == 0 && (surrounding[i] instanceof OrganicEntity) && !(surrounding[i] instanceof Organism)){
						host.eat((OrganicEntity)surrounding[i]) ;
						return true ;
						}
					else 
						randomChoice-- ;
				}
			}
		}
		
		//Move towards signaled food
		if(bIsHungry && ((AdvancedOrganism)host).bSignaled){
			if(((AdvancedOrganism)host).recievedSignal.message == 'F'){
				target(bestRun(surrounding, ((AdvancedOrganism)host).recievedSignalSender, true)) ;
				if(moveToTarget())
					return true ;
			}
		}
		
		//Attack
		if(bIsHungry && ((AdvancedOrganism)host).bCanAttack && attackTargets > 0){
			tempInt = bestAttack(surrounding, bIsStarving) ;
			if(tempInt >= 0)
				((AdvancedOrganism)host).attackTarget((Organism)surrounding[tempInt]) ;
			return true ;
		}
		
		//Toxify
		if(!bIsStarving && ((AdvancedOrganism)host).bGenerateToxins && host.toxicity < ((AdvancedOrganism)host).toxifyTarget){
			((AdvancedOrganism)host).toxify() ;
			return true ;
		}
		
		//Look for food or light
		if(bIsHungry && !((AdvancedOrganism)host).bSessile){
			if(host.bPhotosynthetic && ((host.illumination / host.photosyntheticCoefficient) * host.photosyntheticEfficiency) < (1.01 / host.hungerTime) && reproduceMoveTargets > 0){
				target(bestLight(surrounding)) ;
				if(moveToTarget())
					return true ;
			}
			else if(host.bPhotosynthetic && bIsStarving && reproduceMoveTargets > 0){
				target(randomMove(surrounding)) ;
				if(moveToTarget())
					return true ;
			}
			else if(!bIsStarving && !(host.bPhotosynthetic && ((host.illumination / host.photosyntheticCoefficient) * host.photosyntheticEfficiency) < (1.15 / host.hungerTime))){
				if(reproduceMoveTargets > 0){
					target(randomMove(surrounding)) ;
					if(moveToTarget())
						return true ;
				}
			}
		}
		
		//Signal friendlies
		if(((AdvancedOrganism)host).bCanSignal && signalTargets > 0 && !bIsStarving){
			if(foodTargets > 0 && !bIsHungry && ((AdvancedOrganism)host).lastSignalSent.message != 'F'){
				toSend = new ChemicalSignal('F', 40, 4, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend)){
					host.currentAction = ActionType.SIGNAL ;
					return true ;
				}
			}
		}
		
		return false ;
	}
	
	public int bestFood(Entity[] surrounding){											//rates food options
		double high = -10000 ;
		int best = -1 ;
		double tastiness = 0 ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i] instanceof OrganicEntity && !(surrounding[i] instanceof Organism)){
				tastiness = ((OrganicEntity)surrounding[i]).energy + (((OrganicEntity)surrounding[i]).nutrientHue.getRed() * 1.25) + (((OrganicEntity)surrounding[i]).nutrientHue.getGreen() * 1.25) + 
							(((OrganicEntity)surrounding[i]).nutrientHue.getBlue() * 1.25) - (((OrganicEntity)surrounding[i]).toxicity * 3) ;
				if(tastiness < 1)
					tastiness = 1 ;
				if(tastiness > high){
					high = tastiness ;
					best = i ;
				}
			}
		}
		return best ;
	}
	
	public int bestLight(Entity[] surrounding){											//rates empty cell illumination options
		int high = host.illumination ;
		int best = -1 ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i].entityType == EntityType.NOTHING  || surrounding[i].entityType == EntityType.PLUME){
				if(surrounding[i].illumination > high && surrounding[i].entityType == EntityType.NOTHING){
					high = surrounding[i].illumination ;
					best = i ;
				}
				else if(surrounding[i].entityType == EntityType.PLUME){
					if(surrounding[i].illumination - ((ToxicPlume)surrounding[i]).toxicity * 8 > high && surrounding[i].entityType == EntityType.NOTHING){
						high = surrounding[i].illumination - (int)((ToxicPlume)surrounding[i]).toxicity * 8 ;
						best = i ;
					}
				}
			}
		}
		return best ;
	}
	
	public int bestAttack(Entity[] surrounding, boolean bStarving){						//rates attack targets based on how much hp/resistence they have
		int high = 9999 ;
		int best = -1 ;
		int defenseScore = 0 ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i] instanceof Organism){
				if(surrounding[i] instanceof AdvancedOrganism){
					defenseScore = ((AdvancedOrganism)surrounding[i]).healthPoints + ((AdvancedOrganism)surrounding[i]).damageResistance * ((AdvancedOrganism)host).numBites ;
					if(((AdvancedOrganism)surrounding[i]).bSpiky)
						if(((AdvancedOrganism)host).healthPoints < 2)
							defenseScore = 9999 ;
						else
							defenseScore += ((AdvancedOrganism)host).numBites * 2 ;
					if(((AdvancedOrganism)surrounding[i]).bEmitToxins)
						defenseScore += ((AdvancedOrganism)surrounding[i]).toxicity / 6 ;
					if(bStarving && ((AdvancedOrganism)surrounding[i]).name.equals(((AdvancedOrganism)host).name)){
						if( defenseScore * 4 < high){
							high = defenseScore * 4 ;
							best = i ;
						}
					}
					else if((defenseScore < high) && !((AdvancedOrganism)surrounding[i]).name.equals(((AdvancedOrganism)host).name)){
						high = defenseScore ;
						best = i ;
					}
				}
				else {
					best = i ;
					high = 0 ;
				}
			}
		}
		return best ;
	}
	
	public int bestHeat(Entity[] surrounding){											//finds a suitable location to move at a better temperature than here
		double high = 9999 ;
		int best = -1 ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i].entityType == EntityType.NOTHING || surrounding[i].entityType == EntityType.PLUME){
				
				if(Math.abs(surrounding[i].heat) < high && surrounding[i].heat > host.coldTolerance * 0.75 && surrounding[i].entityType == EntityType.NOTHING){
					high = Math.abs(surrounding[i].heat) ;
					best = i ;
				}
				else if(surrounding[i].entityType == EntityType.PLUME){
					if(Math.abs(surrounding[i].heat) < high && surrounding[i].heat > host.coldTolerance * 0.75 && surrounding[i].entityType == EntityType.NOTHING && ((ToxicPlume)surrounding[i]).toxicity + host.toxicity < ((AdvancedOrganism)host).detoxMax){
						high = Math.abs(surrounding[i].heat) ;
						best = i ;
					}
				}
			}
		}
		return best ;
	}
	
	public int bestRun(Entity[] surrounding, AdvancedOrganism attacker, boolean bTowards){							//Finds an open cell to run that's away from the attacker (or towards the target)
		boolean[] possible = new boolean[6] ;
		int options = 0 ;
		int best = -1 ;
		Random rand = new Random() ;
		int randomChoice ;
		int relativeX = host.xLocation - attacker.xLocation ;
		int relativeY = host.yLocation - attacker.yLocation ;
		int relativeZ = host.zLocation - attacker.zLocation ;
		int moveDistance = Math.abs(relativeX) + Math.abs(relativeY) + Math.abs(relativeZ) ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i].heat < host.heatTolerance * 0.85 &&  surrounding[i].heat > host.coldTolerance * 0.75 && (surrounding[i].entityType == EntityType.NOTHING || surrounding[i].entityType == EntityType.PLUME)){
				possible[i] = true ;
				options++ ;
			}
			else
				possible[i] = false ;
		}
		if(options == 0 || (moveDistance <= 1 && bTowards))
			return -1 ;
		
		if(bTowards){
			if(relativeX > 0)
				possible[1] = false ;
			else if(relativeX < 0)
				possible[0] = false ;
			if(relativeY > 0)
				possible[3] = false ;
			else if(relativeY < 0)
				possible[2] = false ;
			if(relativeZ > 0)
				possible[5] = false ;
			else if(relativeZ < 0)
				possible[4] = false ;
		}
		else{
			if(relativeX < 0)
				possible[1] = false ;
			else if(relativeX > 0)
				possible[0] = false ;
			if(relativeY < 0)
				possible[3] = false ;
			else if(relativeY > 0)
				possible[2] = false ;
			if(relativeZ < 0)
				possible[5] = false ;
			else if(relativeZ > 0)
				possible[4] = false ;
		}
		
		randomChoice = rand.nextInt(options) ;
		for(int i = 0; i < options; i++){
			if(possible[i] && randomChoice == 0)
				best = i ;
			else
				randomChoice-- ;
		}
		
		return best ;
	}
	
	public int randomMove(Entity[] surrounding){
		Random rand = new Random() ;
		int randomChoice ;
		int randomTargets = 0 ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i].entityType == EntityType.NOTHING || (surrounding[i].entityType == EntityType.PLUME && 
			((ToxicPlume)surrounding[i]).toxicity + host.toxicity < ((AdvancedOrganism)host).toxicTolerance) && surrounding[i].heat < host.heatTolerance && surrounding[i].heat > host.coldTolerance)
				randomTargets++ ;
		}
		
		if(randomTargets < 1)
			return -1 ;
		
		randomChoice = rand.nextInt(randomTargets) ;
		for(int i = 0; i < 6; i++){
			if(surrounding[i].entityType == EntityType.NOTHING || (surrounding[i].entityType == EntityType.PLUME && 
					((ToxicPlume)surrounding[i]).toxicity + host.toxicity < ((AdvancedOrganism)host).toxicTolerance) && surrounding[i].heat < host.heatTolerance && surrounding[i].heat > host.coldTolerance){
				if(randomChoice == 0)
					return i ;
				else
					randomChoice-- ;
			}
		}
		return -1 ;
	}
	
	public void target(int position){
		targetX = host.xLocation ;
		targetY = host.yLocation ;
		targetZ = host.zLocation ;
		switch(position){
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
	}
	
	public boolean moveToTarget(){
		double tempHeat = habitat.getEntity(targetX,  targetY,  targetZ).heat ;
		if(targetX == host.xLocation && targetY == host.yLocation && targetZ == host.zLocation)
			return false ;
		if(habitat.getEntity(targetX,  targetY,  targetZ).entityType == EntityType.PLUME){
			host.toxicity += ((ToxicPlume)habitat.getEntity(targetX,  targetY,  targetZ)).toxicity ;
		}
		habitat.setEntity(targetX,  targetY,  targetZ, host) ;
		habitat.setEntity(host.xLocation,  host.yLocation,  host.zLocation, new Entity(tempHeat)) ;
		host.xLocation = targetX ;
		host.yLocation = targetY ;
		host.zLocation = targetZ ;
		host.currentAction = ActionType.MOVE ;
		
		return true ;
	}
}
