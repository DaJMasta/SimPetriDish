/*
	AdvancedOrganism.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	The physical entity for an AdvancedOrganism that can be capable of moving, attacking, sending signals, chemosynthesis, detoxification, reproduction and eating.
		Includes several new variables to monitor actions and activity externally as well as many supporting variables to modify behavior between variants while using a single base Engine class.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import java.util.Random;

public class AdvancedOrganism extends Organism {

	String name =  "ORG" ;
	int id = 0 ;
	int generation = 0 ;
	int numBites = 4 ;
	boolean bSessile = true ;
	boolean bCanSignal = false ;
	boolean bRepeatSignal = true ;
	boolean bCanAttack = false ;
	boolean bChemotrophic = false ;
	boolean bDoChemosynthesis = false ;
	int chemicalEnergyAmount = 7 ;
	int healthPoints = 10 ;
	int maxHealthPoints = 10 ;
	int attackDamage = 4 ;
	int damageResistance = 0 ;
	int maxEnergy = 500 ;
	int progeny = 0 ;
	int reproductionCost = 4 ;
	double redNutMod = 1.0 ;
	double greenNutMod = 1.0 ;
	double blueNutMod = 1.0 ;
	int signalsPerEnergy = 8 ;
	int signalCount = 0 ;
	int toxicTolerance = 100 ;
	int healthRegenTime = 40 ;
	int healthRegenCounter = 0 ;
	DeathType deathState = DeathType.ALIVE ;
	ChemicalSignal recievedSignal = new ChemicalSignal() ;
	ChemicalSignal lastSignalSent = new ChemicalSignal() ;
	boolean bSignaled = false ;
	int chemosynthesisCountdown = 0 ;
	int chemosynthesisDelay = 15 ;
	int signalCountdown = 0 ;
	int signalDelay = 6 ;
	int detoxCost = 5 ;
	boolean bCold = false ;
	boolean bVeryCold = false ; 
	AdvancedOrganism attacker = null ;
	AdvancedOrganism recievedSignalSender = null ;
	AdvancedOrganism lastVictim = this ;
	int attackMemory = 4 ;
	int attackMemoryCountdown = 0 ;
	boolean bSpiky = false ;
	boolean bEmitToxins = false ;
	boolean bGenerateToxins = false ;
	int toxicityGenerateAmount = 4 ;
	boolean bDoRegen = true ;
	int toxifyTarget = 40 ;
	int detoxMin = 20 ;
	int detoxMax = 80 ;
	
	AdvancedOrganism(){
		super() ;
		nutrientHue = new Color(20,20,20) ;
		reproductionMinNutrients = new Color(30,30,30) ;
		insulationFactor = 10.0 ;
		energy = 35 ;
		translucence = 0.7 ;
		xLocation = -1 ;
		yLocation = -1 ;
		zLocation = -1 ;
		reproductionMinEnergy = 70 ;
		reproductionCountdown = timeToMature ;
		eatingEfficiency = 0.65 ;
		hungerTime = 13 ;
		actionDelay = 18 ;
		heatTolerance = 190 ;
		coldTolerance = -45 ;
		timeToMature = 800 ;
		reReproduce = 400 ;
		photosyntheticEfficiency = 0.7 ;
		
		chemosynthesisCountdown = chemosynthesisDelay ;
		healthRegenCounter = healthRegenTime ;
		signalCountdown = signalDelay ;
	}
	
	AdvancedOrganism(int xLoc, int yLoc, int zLoc, AdvancedOrganism parentOrganism){					//Copy constructor, for reproduce
		super(parentOrganism.habitat, xLoc, yLoc, zLoc, parentOrganism.bPhotosynthetic, parentOrganism.reproductionMinEnergy, parentOrganism.eatingEfficiency, parentOrganism.photosyntheticEfficiency, 
				parentOrganism.maxLifetime, parentOrganism.timeToMature, parentOrganism.reReproduce, parentOrganism.hungerTime, parentOrganism.heat,  parentOrganism.energy, parentOrganism.nutrientHue.getRed(),
				parentOrganism.nutrientHue.getGreen(), parentOrganism.nutrientHue.getBlue(), parentOrganism.reproductionMinNutrients.getRed(), parentOrganism.reproductionMinNutrients.getGreen(), parentOrganism.reproductionMinNutrients.getBlue(),
				parentOrganism.heatTolerance, parentOrganism.coldTolerance, parentOrganism.actionDelay) ;
		name = parentOrganism.name ;
		id = parentOrganism.id ;
		numBites = parentOrganism.numBites ;
		bSessile = parentOrganism.bSessile ;
		bCanAttack = parentOrganism.bCanAttack ;
		bCanSignal = parentOrganism.bCanSignal ;
		maxHealthPoints = parentOrganism.maxHealthPoints ;
		healthPoints = maxHealthPoints ;
		attackDamage = parentOrganism.attackDamage ;
		damageResistance = parentOrganism.damageResistance ;
		maxEnergy = parentOrganism.maxEnergy ;
		reproductionCost = parentOrganism.reproductionCost ;
		redNutMod = parentOrganism.redNutMod ;
		greenNutMod = parentOrganism.greenNutMod ;
		blueNutMod = parentOrganism.blueNutMod ;
		signalsPerEnergy = parentOrganism.signalsPerEnergy ;
		toxicTolerance = parentOrganism.toxicTolerance ;
		healthRegenTime = parentOrganism.healthRegenTime ;
		heatTolerance = parentOrganism.heatTolerance ;
		coldTolerance = parentOrganism.coldTolerance ;
		insulationFactor = parentOrganism.insulationFactor ;
		bChemotrophic = parentOrganism.bChemotrophic ;
		chemicalEnergyAmount = parentOrganism.chemicalEnergyAmount ;
		currentAction = ActionType.CREATED ;
		subconsciousAction = ActionType.IDLE ;
		signalDelay = parentOrganism.signalDelay ;
		detoxCost = parentOrganism.detoxCost ;
		bRepeatSignal = parentOrganism.bRepeatSignal ;
		reproductionCountdown = timeToMature ;
		generation = parentOrganism.generation + 1 ;
		bSpiky = parentOrganism.bSpiky ;
		bEmitToxins = parentOrganism.bEmitToxins ;
		bGenerateToxins = parentOrganism.bGenerateToxins ;
		toxicityGenerateAmount = parentOrganism.toxicityGenerateAmount ;
		toxifyTarget = parentOrganism.toxicityGenerateAmount ;
		detoxMin = parentOrganism.detoxMin ;
		detoxMax = parentOrganism.detoxMax ;
		chemosynthesisDelay = parentOrganism.chemosynthesisDelay ;
		attackMemory = parentOrganism.attackMemory ;
		
		chemosynthesisCountdown = chemosynthesisDelay ;
		healthRegenCounter = healthRegenTime ;
		signalCountdown = signalDelay ;
	}
	
	public void age(){
		super.age() ;
		
		if(heat < coldTolerance / 2.5)												//slows action speed (in AdvancedOrganismEngine) if cold
			bCold = true ;
		if(heat < coldTolerance / 1.5)
			bVeryCold = true ;
	}
	
	public void subconsciousAction(){												//All possible subconscious actions except for reactionary signaling
		ChemicalSignal toSend ;
		subconsciousAction = ActionType.IDLE ;
		
		if(bVeryCold)
			return ;
		
		if(healthPoints < maxHealthPoints && bDoRegen){
			if(healthRegenCounter < 1){
				healthPoints++ ;
				healthRegenCounter = healthRegenTime ;
				energy-- ;
				nutrientReduction() ;
				subconsciousAction = ActionType.REGENERATE ;
			}
			else
				healthRegenCounter-- ;
		}
		else if(bCanSignal && bSignaled && bRepeatSignal){
			toSend = new ChemicalSignal(recievedSignal) ;
			if(recievedSignal.message != lastSignalSent.message && toSend.strength > 1){
				if(signal(toSend))
					lastSignalSent = toSend ;
				bSignaled = false ;
				recievedSignal = new ChemicalSignal() ;
				signalCount-- ;
				subconsciousAction = ActionType.SIGNAL ;
			}
		}
		else if(bDoChemosynthesis && bChemotrophic && chemosynthesisCountdown < 1){
			generateEnergy() ;
			chemosynthesisCountdown = chemosynthesisDelay ;
		}
		
		if(signalCountdown > 1)
			signalCountdown-- ;
		if(chemosynthesisCountdown >= 1)
			chemosynthesisCountdown-- ;
			
		if(bPhotosynthetic){
			if(photosynthesize() && subconsciousAction == ActionType.IDLE)
				subconsciousAction = ActionType.PHOTOSYNTHESIS ;
		}
		
		if(signalCount >= signalsPerEnergy){
			signalCount -= signalsPerEnergy ;
			energy-- ;
			nutrientReduction() ;
		}
	}
	
	public boolean reproduce(int xLoc, int yLoc, int zLoc){	
		int tempR, tempG, tempB, tempEnergy ;
		AdvancedOrganism spawn ;
		
		tempR = nutrientHue.getRed() / 2 ;
		tempG = nutrientHue.getGreen() / 2 ;
		tempB = nutrientHue.getBlue() / 2 ;
		tempEnergy = energy / 2 ;
		nutrientHue = new Color(tempR, tempG, tempB) ;
		energy = tempEnergy ;
		
		heat += 2.0 ;
		
		spawn = newCopy() ;
		spawn.xLocation = xLoc ;
		spawn.yLocation = yLoc ;
		spawn.zLocation = zLoc ;
		spawn.currentAction = ActionType.CREATED ;
		habitat.setEntity(xLoc, yLoc, zLoc, spawn) ;
		habitat.addController(new AdvancedOrganismEngine(habitat, spawn)) ;	
		
		progeny++ ;
		
		currentAction = ActionType.REPRODUCE ;
		habitat.incrementReproduction() ;
		
		reproductionCountdown = reReproduce ;
		
		for(int i = 0; i < reproductionCost; i++)
			nutrientReduction() ;
		energy -= reproductionCost ;
		
		return true ;
	}
	
	public void emitToxins(int toxinStrength){																//Toxify or generate ToxicPlumes surrounding the organism
		Entity[] surrounding = habitat.getSurrounding(xLocation,  yLocation,  zLocation) ;
		int generateCount ;
		int toxinTargetCount = 0 ;
		int temp, targetX, targetY, targetZ ;
		Random rand = new Random() ;
		double tempHeat ;
		
		if(toxinStrength < 1)
			return ;
			
		generateCount = Math.min(toxicity / toxinStrength, 6) ;
		
		for(int i = 0; i < 6; i++)
			if(surrounding[i].entityType == EntityType.ORGANISM || surrounding[i].entityType == EntityType.NOTHING || surrounding[i].entityType == EntityType.PLUME || surrounding[i].entityType == EntityType.ORGANIC_OBJECT)
				toxinTargetCount++ ;
		
		while(toxinTargetCount > generateCount && toxinTargetCount > 0){
			temp = rand.nextInt(6) ;
			if(surrounding[temp].entityType == EntityType.ORGANISM || surrounding[temp].entityType == EntityType.NOTHING || surrounding[temp].entityType == EntityType.PLUME || surrounding[temp].entityType == EntityType.ORGANIC_OBJECT){
				surrounding[temp] = new Entity(habitat.boundaryInsulationFactor, habitat.heatFloor) ;
				toxinTargetCount-- ;
			}
		}
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i] instanceof OrganicEntity){
				targetX = xLocation ;
				targetY = yLocation ;
				targetZ = zLocation ;
				switch(i){
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
				((OrganicEntity)habitat.getEntity(targetX, targetY, targetZ)).toxicity += toxinStrength ;
				toxicity -= toxinStrength ;
				currentAction = ActionType.EMIT_TOXINS ;
			}
			else if(surrounding[i] instanceof ToxicPlume){
				targetX = xLocation ;
				targetY = yLocation ;
				targetZ = zLocation ;
				switch(i){
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
				((ToxicPlume)habitat.getEntity(targetX, targetY, targetZ)).toxicity += toxinStrength ;
				toxicity -= toxinStrength ;
				currentAction = ActionType.EMIT_TOXINS ;
			}
			else if(surrounding[i].entityType == EntityType.NOTHING){
				
				targetX = xLocation ;
				targetY = yLocation ;
				targetZ = zLocation ;
				switch(i){
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
				tempHeat = habitat.getEntity(targetX, targetY, targetZ).heat ;
				habitat.setEntity(targetX, targetY, targetZ, new ToxicPlume(toxinStrength, tempHeat)) ;
				toxicity -= toxinStrength ;
				currentAction = ActionType.EMIT_TOXINS ;
			}
		}
	}
	
	public boolean signal(ChemicalSignal toSend){															//Signal surrounding AdvancedOrganisms
		Entity[] surrounding = habitat.getSurrounding(xLocation,  yLocation,  zLocation) ;
		boolean bDidSend = false ;
		
		if(toSend.strength < 1)
			return false ;
		
		for(int i = 0; i < 6; i++){
			if(surrounding[i] instanceof AdvancedOrganism){
				((AdvancedOrganism)surrounding[i]).signaled(toSend, this) ; 
				bDidSend = true ;
				lastSignalSent = toSend ;
			}
		}
		
		if(bDidSend){
			signalCount-- ;
			if(signalCount >= signalsPerEnergy){
				signalCount -= signalsPerEnergy ;
				energy-- ;
				nutrientReduction() ;
			}
		}
		
		return bDidSend ;
	}
	
	public void signaled(ChemicalSignal signaled, AdvancedOrganism signaler){
		recievedSignalSender = signaler ;
		recievedSignal = signaled ;
		bSignaled = true ;
	}
	
	public void eat(OrganicEntity food){												//Take a bite of food, takes numBites to fully eat
		int tempR, tempG, tempB, tempEnergy, tempTox ;
		tempR = (int)(food.nutrientHue.getRed() / numBites) ;
		tempG = (int)(food.nutrientHue.getGreen() / numBites) ;
		tempB = (int)(food.nutrientHue.getBlue() / numBites) ;
		tempEnergy = (int)(food.energy / numBites) ;
		tempTox = (int)(food.toxicity  / numBites) ;
		food.nutrientHue = new Color(tempR * (numBites - 1), tempG * (numBites - 1), tempB * (numBites - 1)) ;
		food.energy -= tempEnergy ;
		food.toxicity -= tempTox ;
		tempR *= eatingEfficiency ;
		tempG *= eatingEfficiency ;
		tempB *= eatingEfficiency ;
		tempEnergy *= eatingEfficiency ;
		tempR += nutrientHue.getRed();
		tempG += nutrientHue.getGreen();
		tempB += nutrientHue.getBlue();
		if(tempR > 255)
			tempR = 255 ;
		if(tempG > 255)
			tempG = 255 ;
		if(tempB > 255)
			tempB = 255 ;
		nutrientHue = new Color(tempR, tempG, tempB) ;
		energy += tempEnergy ;
		if(energy > maxEnergy)
			energy = maxEnergy ;
		toxicity += tempTox ;
		currentAction = ActionType.EAT ;
		habitat.incrementConsumption() ;
	}
	
	public boolean nutrientReduction(){										//a chance to randomly deduct one nutrient
		int temp, tempR, tempG, tempB ;
		Random rand = new Random() ;
		temp = rand.nextInt(10000) ;
		tempR = nutrientHue.getRed() ;
		tempG = nutrientHue.getGreen() ;
		tempB = nutrientHue.getBlue() ;
		
		if(100 * redNutMod > temp){
			tempR-- ;
			if(tempR < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
				return false ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
			return true ;
		}
		else if(100 * redNutMod + 100 * greenNutMod > temp){
			tempG-- ;
			if(tempG < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
				return false ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
			return true ;
		}
		else if(100 * redNutMod + 100 * greenNutMod + 100 * blueNutMod > temp){
			tempB-- ;
			if(tempB < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
				return false ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
			return true ;
		}
		return false ;
	}
	
	public void forceNutrientReduction(){
		int temp, tempR, tempG, tempB ;
		Random rand = new Random() ;
		tempR = nutrientHue.getRed() ;
		tempG = nutrientHue.getGreen() ;
		tempB = nutrientHue.getBlue() ;
		temp = rand.nextInt((int)(100 * redNutMod + 100 * greenNutMod + 100 * blueNutMod))  ;
		
		if(100 * redNutMod > temp){
			tempR-- ;
			if(tempR < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
		}
		else if(100 * redNutMod + 100 * greenNutMod > temp){
			tempG-- ;
			if(tempG < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
		}
		else {
			tempB-- ;
			if(tempB < 0){
				deathState = DeathType.NUTRITION ;
				bChangeType = true ;
				currentAction = ActionType.DIE ;
			}
			nutrientHue = new Color(tempR, tempG, tempB) ;
		}
	}
	
	public boolean checkAlive(){
		if(healthPoints < 1){
			deathState = DeathType.KILLED ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(toxicity > toxicTolerance){
			deathState = DeathType.TOXICITY ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(heat < coldTolerance){
			deathState = DeathType.COLD ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(heat > heatTolerance){
			deathState = DeathType.HEAT ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(lifetime > maxLifetime){
			deathState = DeathType.OLD_AGE ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(energy < 1){
			deathState = DeathType.STARVATION ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		if(nutrientHue.getRed() < 1 || nutrientHue.getGreen() < 1 || nutrientHue.getBlue() < 1){
			deathState = DeathType.NUTRITION ;
			bChangeType = true ;
			currentAction = ActionType.DIE ;
			return false ;
		}
		return true ;
	}
	
	public void generateEnergy(){
		forceNutrientReduction() ;
		energy += chemicalEnergyAmount ;
		subconsciousAction = ActionType.CHEMOSYNTHESIS ;
	}
	
	public void toxify(){
		forceNutrientReduction() ;
		toxicity += toxicityGenerateAmount ;
		currentAction = ActionType.TOXIFY ;
	}
	
	public void takeDamage(int damage, AdvancedOrganism instigator){
		healthPoints -= (damage - damageResistance) ;
		attacker = instigator ;
		attackMemoryCountdown = attackMemory ;
		
		if(bSpiky)
			instigator.takeDamage(1, this) ;
	}
	
	public void attackTarget(Organism target){
		if(target instanceof AdvancedOrganism){
			((AdvancedOrganism)target).takeDamage(attackDamage, this) ;
			lastVictim = (AdvancedOrganism)target ;
		}
		else
			target.bChangeType = true ;														//Basic organisms
		currentAction = ActionType.ATTACK ;
	}
	
	public void detox(){
		for(int i = 0; i < detoxCost; i++){
			useEnergy() ;
		}
		toxicity-- ;
		currentAction = ActionType.DETOX ;
	}
	
	public AdvancedOrganism newCopy(){														//Important for SpawnRandomizer
		return new AdvancedOrganism(xLocation, yLocation, zLocation, this) ;
	}
}
