/*
	ControllableOrganismEngine.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	A class that has the handles for external code to give an organism directions.  Not implemented in the 1.1 release (coming in 1.2).

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class ControllableOrganismEngine extends AdvancedOrganismEngine {

	boolean bChemosynthToTarget = true ;
	boolean bAutoRegenerate = true ;
	boolean bResendMessages = true ;
	boolean bSignalOnAttacked = true ;
	boolean bSignalOnTemp = true ;
	int chemosynthesisTarget = 80 ;
	String errorMessage = "" ;
	
	ActionType plannedAction = ActionType.IDLE ;
	int target = -1 ;
	ChemicalSignal toSignal = new ChemicalSignal() ;
	boolean bDoAction = false ;
	
																							//nutSeekMin, foodSeekMin, chemosynthesisNutMin
	
	public boolean act(){
		Entity tempTarget ;
		int foodTargets = 0 ;
		int reproduceMoveTargets = 0 ;
		int signalTargets = 0 ;
		ChemicalSignal toSend ;
		targetX = host.xLocation ;
		targetY = host.yLocation ;
		targetZ = host.zLocation ;
		
		bIsHungry = false ;
		bIsStarving = false ;
		host.currentAction = ActionType.IDLE ;
		
		Entity[] surrounding = habitat.getSurrounding(host.xLocation,  host.yLocation,  host.zLocation) ;
		
		for(int i = 0; i < 6 ; i++){
			tempTarget = surrounding[i] ;
			if(tempTarget.entityType == EntityType.ORGANIC_OBJECT)
				foodTargets++ ;
			else if(tempTarget.entityType == EntityType.NOTHING)
				reproduceMoveTargets++ ;
			if(surrounding[i] instanceof AdvancedOrganism)
				signalTargets++ ;
		}
		
		((AdvancedOrganism)host).bDoRegen = bAutoRegenerate ;																					//Subconscious Controls
		((AdvancedOrganism)host).bRepeatSignal = bResendMessages ;
		
		//Are they hungry
		if(host.energy <= foodSeekMin || host.nutrientHue.getRed() <= nutSeekMin ||  host.nutrientHue.getGreen() <= nutSeekMin ||  host.nutrientHue.getBlue() <= nutSeekMin)
			bIsStarving = true ;
		if(host.energy < foodSeekMax || host.nutrientHue.getRed() < nutSeekMax ||  host.nutrientHue.getGreen() < nutSeekMax ||  host.nutrientHue.getBlue() < nutSeekMax)
			bIsHungry = true ;
		
		//Chemosynthesis allowed
		if(bChemosynthToTarget){
			if(((AdvancedOrganism)host).bChemotrophic){
				if(host.energy < chemosynthesisTarget && (host.nutrientHue.getRed() >= chemosynthesisNutMin && host.nutrientHue.getGreen() >= chemosynthesisNutMin && host.nutrientHue.getBlue() >= chemosynthesisNutMin))
					((AdvancedOrganism)host).bDoChemosynthesis = true ;
				else
					((AdvancedOrganism)host).bDoChemosynthesis = false ;
			}
		}
		
		//Reactionary signaling				-- subconscious, but evaluated here
		if(((AdvancedOrganism)host).bCanSignal && signalTargets > 0){
			if(((AdvancedOrganism)host).attacker != null && ((AdvancedOrganism)host).lastSignalSent.message != 'E' && bSignalOnAttacked){
				toSend = new ChemicalSignal('E', 100, 4, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
			else if(((AdvancedOrganism)host).heat > host.heatTolerance * (heatRunFactor + 0.05) && ((AdvancedOrganism)host).lastSignalSent.message != 'H' && bSignalOnTemp){
				toSend = new ChemicalSignal('H', 80, 4, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
			else if(((AdvancedOrganism)host).heat < host.heatTolerance * (coldRunFactor + 0.05) && ((AdvancedOrganism)host).lastSignalSent.message != 'C' && bSignalOnTemp){
				toSend = new ChemicalSignal('C', 80, 4, 1, ((AdvancedOrganism)host).name, ((AdvancedOrganism)host).id, ((AdvancedOrganism)host).generation) ;
				if(((AdvancedOrganism)host).signal(toSend))
					host.subconsciousAction = ActionType.SIGNAL ;
			}
		}
		
																																				//Exit method if not yet time to act again
		
		if(((lastActionStep + host.actionDelay) * 2 > host.lifetime  && ((AdvancedOrganism)host).bVeryCold) || ((lastActionStep + host.actionDelay) * 1.25 > host.lifetime  && ((AdvancedOrganism)host).bCold) || (lastActionStep + host.actionDelay > host.lifetime)){
			return false ;
		}
		
		if(!bDoAction)
			return false ;
		
		//Degrade memory
		
		if(!((AdvancedOrganism)host).recievedSignal.degrade()){
			((AdvancedOrganism)host).bSignaled = false ;
			((AdvancedOrganism)host).recievedSignal = new ChemicalSignal() ;
			((AdvancedOrganism)host).recievedSignalSender = null ;
		}
		if(((AdvancedOrganism)host).attackMemoryCountdown > 0)
			((AdvancedOrganism)host).attackMemoryCountdown-- ;
		
																																				//Execute action that has been given
		
		switch(plannedAction){
			case EAT:		if(target > -1 && target < 6){
								target(target) ;
								if(habitat.getEntity(targetX, targetY, targetZ) instanceof OrganicEntity)
									host.eat((OrganicEntity)surrounding[target]) ;
								else 
									errorMessage = "No food at target" ;					
							}
							else 
								errorMessage = "Invalid eat location" ;							
							break ;
							
			case REPRODUCE:	if(host.checkReproduce()){
								target(target) ;
								if(habitat.getEntity(targetX, targetY, targetZ).entityType == EntityType.NOTHING)
									host.reproduce(targetX, targetY, targetZ) ;
							}	
							else
								errorMessage = "Unable to reproduce" ;
							break ;
							
			case MOVE:		if(target > -1 && target < 6){
								target(target) ;
								if(habitat.getEntity(targetX, targetY, targetZ).entityType == EntityType.NOTHING){
									habitat.setEntity(targetX,  targetY,  targetZ, host) ;
									habitat.setEntity(host.xLocation,  host.yLocation,  host.zLocation, new Entity(habitat.getEntity(targetX,  targetY,  targetZ).heat)) ;
									host.xLocation = targetX ;
									host.yLocation = targetY ;
									host.zLocation = targetZ ;
									
									host.currentAction = ActionType.MOVE ;
								}
								else
									errorMessage = "Move location occupied" ;
							}
							else 
								errorMessage = "Invalid move location" ;
							break ;
							
			case ATTACK:	if(target > -1 && target < 6){
								target(target) ;
								if(habitat.getEntity(targetX, targetY, targetZ).entityType == EntityType.ORGANISM)
									((AdvancedOrganism)host).attackTarget((Organism)habitat.getEntity(targetX, targetY, targetZ)) ;
								else 
									errorMessage = "Invalid attack target" ;
							}
							else 
								errorMessage = "Invalid attack location" ;
							break ;
							
			case SIGNAL:	if(signalTargets > 0){
								if(toSignal.message != ' ')
									((AdvancedOrganism)host).signal(toSignal) ;
								else 
									errorMessage = "No message to send" ;
							}
							else 
								errorMessage = "No organism to signal" ;
							break ;
							
			case DETOX:		if(host.toxicity > 0)
								((AdvancedOrganism)host).detox();
							else 
								errorMessage = "No toxicity to remove" ;
							break ;
							
			case TOXIFY:	if(((AdvancedOrganism)host).bGenerateToxins)
								((AdvancedOrganism)host).toxify();
							else 
								errorMessage = "Cannot generate toxins" ;
							break ;
			case EMIT_TOXINS:if(foodTargets > 0 || reproduceMoveTargets > 0 || signalTargets > 0)
								((AdvancedOrganism)host).emitToxins(target) ;
							else
								errorMessage = "No space to emit toxins" ;
		}
		plannedAction = ActionType.NO_ACTION ;
		bDoAction = false ;
		return true ;
	}
	
	public void setNextAction(ActionType newAction, int newTarget, ChemicalSignal newToSend){
		plannedAction = newAction ;
		target = newTarget ;
		toSignal = newToSend ;
		bDoAction = true ;
		errorMessage = "" ;
	}
	
	public void setChemoTarget(int newChemoTarget, boolean bNewChemoToTarget){
		chemosynthesisTarget = newChemoTarget ;
		bChemosynthToTarget = bNewChemoToTarget ;
	}
	
	public void setSubconscious(boolean bAutoRegen, boolean bResend, boolean bSignalAttack, boolean bSignalTemp){
		bAutoRegenerate = bAutoRegen ;
		bResendMessages = bResend ;
		bSignalOnAttacked = bSignalAttack ;
		bSignalOnTemp = bSignalTemp ;
	}
}
