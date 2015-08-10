/*
	WorldEngine.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Spurs the world class to act and keeps track of various simulation total and step total statistics.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.ArrayList;
import java.util.Date;

public class WorldEngine {
	
	World dominion = null;
	int totalDeaths = 0 ;
	int organicCount = 0 ;
	int organismCount = 0 ;
	int totalConsumption = 0 ;
	int totalDecay = 0 ;
	double stepMaxHeat = 0.0 ;
	double stepMinHeat = 999999.0 ;
	double totalHeat = 0.0 ;
	int totalToxicity = 0 ;
	int totalEnergy = 0 ;
	int totalRedNut = 0 ;
	int totalGreenNut = 0 ;
	int totalBlueNut = 0 ;
	int oldestOrganism = 0 ;
	int youngestOrganism = 999999 ;
	long stepAgeTotal = 0 ;
	double averageAge = 0.0 ;
	int totalReproduction = 0 ;
	int lastStepReproduction = 0 ;
	int lastStepDeaths = 0 ;
	int lastStepConsumption = 0 ;
	int lastStepDecay = 0 ;
	int totalKilled = 0 ;
	int totalNaturalCauses = 0 ;
	int totalStarved = 0 ;
	int totalFrozen = 0 ;
	int totalCooked = 0 ;
	int totalPoisoned = 0 ;
	int totalMalnutrition = 0 ;
	int lastStepKilled = 0 ;
	int lastStepNaturalCauses = 0 ;
	int lastStepStarved = 0 ;
	int lastStepFrozen = 0 ;
	int lastStepCooked = 0 ;
	int lastStepPoisoned = 0 ;
	int lastStepMalnutrition = 0 ;
	int maxStepIllumination = 0 ;
	int minStepIllumination = 999999 ;
	int stepTotalSpawned = 0 ;
	int stepFoodSpawned = 0 ;
	int stepNutrientsSpawned = 0 ;
	int stepOrganismsSpawned = 0 ;
	int lastStepTotalSpawned = 0 ;
	int lastStepFoodSpawned = 0 ;
	int lastStepNutrientsSpawned = 0 ;
	int lastStepOrganismsSpawned = 0 ;
	
	double stepMaxLight = 0.0 ;
	double stepMinLight = 9999.0 ;
	long lastWorldFrame, worldFrameStart, fpsTime ;
	int fpsCount = 0 ;
	int stepCount = 0 ;
	int stepIdleCount = 0 ;
	int stepMoveCount =  0 ;
	int stepSignalCount = 0 ;
	int stepCreatedCount = 0 ;
	int stepAttackCount = 0 ;
	int stepPhotoCount = 0 ;
	int stepChemoCount = 0 ;
	int stepRegenCount = 0 ;
	int stepDetoxCount = 0 ;
	int stepToxCount = 0 ;
	int stepEmitToxCount = 0 ;
	int stepEatCount = 0 ;
	int stepSubconsciousSignalCount = 0 ;
	int lastStepIdleCount = 0 ;
	int lastStepMoveCount =  0 ;
	int lastStepSignalCount = 0 ;
	int lastStepCreatedCount = 0 ;
	int lastStepAttackCount = 0 ;
	int lastStepPhotoCount = 0 ;
	int lastStepChemoCount = 0 ;
	int lastStepRegenCount = 0 ;
	int lastStepDetoxCount = 0 ;
	int lastStepSubconsciousSignalCount = 0 ;
	int lastStepToxCount = 0 ;
	int lastStepEmitToxCount = 0 ;
	int lastStepEatCount = 0 ;
	
	int basicOrganismCount = 0 ;
	int advancedOrganismCount = 0 ;
	int cyanobacteriaCount = 0 ;
	int nitrogenFixerCount = 0 ;
	int phytoplanktonCount = 0 ;
	int superOrganismCount = 0 ;
	int zooplanktonCount = 0 ;
	int plumeCount = 0 ;
	int streptomycesCount = 0 ;
	int dinoflagellateCount = 0 ;
	int diatomCount = 0 ;
	int coccolithophoreCount = 0 ;
	int yeastCount = 0 ;
	int customCount = 0 ;
	int lastBasicOrganismCount = 0 ;
	int lastAdvancedOrganismCount = 0 ;
	int lastCyanobacteriaCount = 0 ;
	int lastNitrogenFixerCount = 0 ;
	int lastPhytoplanktonCount = 0 ;
	int lastSuperOrganismCount = 0 ;
	int lastZooplanktonCount = 0 ;
	int lastPlumeCount = 0 ;
	int lastStreptomycesCount = 0 ;
	int lastDinoflagellateCount = 0 ;
	int lastDiatomCount = 0 ;
	int lastCoccolithophoreCount = 0 ;
	int lastYeastCount = 0 ;
	int lastCustomCount = 0 ;
	
	ArrayList<SpawnRandomizer> spawners = new ArrayList<SpawnRandomizer>() ;
	boolean bSpawn = true ;
	
	String mostVirile = "None" ;
	String newMostVirile = "None" ;
	
	WorldEngine(){}
	
	WorldEngine(World newWorld){
		dominion = newWorld ;
		fpsTime = new Date().getTime() ;
	}
	
	public void worldStep(){											//Everything that happens in a simulation step for the world
		worldFrameStart = new Date().getTime() ;
		if(bSpawn)
			for(SpawnRandomizer sr : spawners)
				sr.stepAddRandom() ;
		dominion.worldStep() ;
		statsStep() ;
		lastWorldFrame = new Date().getTime() - worldFrameStart ;
		
		if(fpsTime + 1000 <= new Date().getTime()){
			fpsCount = stepCount ;
			stepCount = 0 ;
			fpsTime = new Date().getTime() ;
		}
		else
			stepCount++ ;
	}
	
	public void addSpawner(SpawnRandomizer toAdd){
		spawners.add(toAdd) ;
	}
	
	public SpawnRandomizer getSpawner(Entity type){						//For modifying the already created spawners
		for(SpawnRandomizer i : spawners){
			if(i.toAdd.getClass().equals(type.getClass()))
				return i ;
		}	
		return null ;
	}
	
	public boolean removeSpawnOf(Entity toRemoveSpawnerOf){
		boolean bToReturn = false ;
		for(SpawnRandomizer i : spawners){
			if(i.toAdd.getClass().equals(toRemoveSpawnerOf.getClass())){
				spawners.remove(i) ;
				bToReturn = true ;
			}
		}
		return bToReturn ;
	}
	
	public void clearSpawners(){
		spawners.clear() ;
	}
	
	public boolean toggleSpawners(){								
		bSpawn = !bSpawn ;
		return bSpawn ;
	}

	public void statsStep(){																//Totaling and storing variables - reading them from the current frame without having a second layer of storage has problems because of using many threads and reading at any given time
		double temp ;
		int topProgeny, topNutrients, tempNutrients ;
		totalDeaths += dominion.stepDeaths ;
		lastStepDeaths = dominion.stepDeaths ;
		totalDecay += dominion.stepDecay ;
		lastStepDecay = dominion.stepDecay ;
		totalConsumption += dominion.stepConsumption ;
		lastStepConsumption = dominion.stepConsumption ;
		totalReproduction += dominion.stepReproduction ;
		lastStepReproduction = dominion.stepReproduction ;
		totalKilled += dominion.stepKillCount ;
		lastStepKilled = dominion.stepKillCount ;
		totalNaturalCauses += dominion.stepOldCount ;
		lastStepNaturalCauses = dominion.stepOldCount ;
		totalStarved += dominion.stepStarveCount ;
		lastStepStarved = dominion.stepStarveCount ;
		totalFrozen += dominion.stepFreezeCount ;
		lastStepFrozen = dominion.stepFreezeCount ;
		totalCooked += dominion.stepCookedCount ;
		lastStepCooked = dominion.stepCookedCount ;
		totalPoisoned += dominion.stepPoisonedCount ;
		lastStepPoisoned = dominion.stepPoisonedCount ;
		totalMalnutrition += dominion.stepDeficiencyCount ;
		lastStepMalnutrition = dominion.stepDeficiencyCount ;
		lastStepTotalSpawned = stepTotalSpawned ;
		lastStepFoodSpawned = stepFoodSpawned ;
		lastStepNutrientsSpawned = stepNutrientsSpawned ;
		lastStepOrganismsSpawned = stepOrganismsSpawned ;
		lastStepIdleCount = stepIdleCount ;
		lastStepMoveCount = stepMoveCount ;
		lastStepSignalCount = stepSignalCount ;
		lastStepCreatedCount = stepCreatedCount ;
		lastStepAttackCount = stepAttackCount ;
		lastStepPhotoCount = stepPhotoCount ;
		lastStepChemoCount = stepChemoCount ;
		lastStepRegenCount = stepRegenCount ;
		lastStepDetoxCount = stepDetoxCount ;
		lastStepSubconsciousSignalCount = stepSubconsciousSignalCount ;
		lastBasicOrganismCount = basicOrganismCount ;
		lastAdvancedOrganismCount = advancedOrganismCount ;
		lastCyanobacteriaCount = cyanobacteriaCount ;
		lastNitrogenFixerCount = nitrogenFixerCount ;
		lastPhytoplanktonCount = phytoplanktonCount ;
		lastSuperOrganismCount = superOrganismCount ;
		lastZooplanktonCount = zooplanktonCount ;
		lastPlumeCount = plumeCount ;
		lastStreptomycesCount = streptomycesCount ;
		lastDinoflagellateCount = dinoflagellateCount ;
		lastDiatomCount = diatomCount ;
		lastCoccolithophoreCount = coccolithophoreCount ;
		lastYeastCount = yeastCount ;
		lastStepToxCount = stepToxCount ;
		lastStepEmitToxCount = stepEmitToxCount ;
		lastStepEatCount = stepEatCount;
		lastCustomCount = customCount ;
		
		
		mostVirile = newMostVirile ;
		newMostVirile = "None" ;
		
		if(maxStepIllumination < dominion.illuminationTotal)
			maxStepIllumination = dominion.illuminationTotal ;
		if(minStepIllumination > dominion.illuminationTotal)
			minStepIllumination = dominion.illuminationTotal ;
		
		totalToxicity = 0 ;
		totalEnergy = 0 ;
		totalRedNut = 0 ;
		totalGreenNut = 0 ;
		totalBlueNut = 0 ;
		organicCount = 0 ;
		organismCount = 0 ;
		oldestOrganism = 0 ;
		youngestOrganism = 999999 ;
		stepMaxLight = 0 ;
		stepMinLight = 999999 ;
		stepMinHeat = 999999 ;
		stepMaxHeat = 0 ;
		totalHeat = 0 ;
		stepTotalSpawned = 0 ;
		stepFoodSpawned = 0 ;
		stepNutrientsSpawned = 0 ;
		stepOrganismsSpawned = 0 ;
		topNutrients = 0 ;
		topProgeny = 0 ;
		stepIdleCount = 0 ;
		stepMoveCount =  0 ;
		stepSignalCount = 0 ;
		stepCreatedCount = 0 ;
		stepAttackCount = 0 ;
		stepPhotoCount = 0 ;
		stepChemoCount = 0 ;
		stepRegenCount = 0 ;
		stepDetoxCount = 0 ;
		stepSubconsciousSignalCount = 0 ;
		basicOrganismCount = 0 ;
		advancedOrganismCount = 0 ;
		cyanobacteriaCount = 0 ;
		nitrogenFixerCount = 0 ;
		phytoplanktonCount = 0 ;
		superOrganismCount = 0 ;
		zooplanktonCount = 0 ;
		plumeCount = 0 ;
		streptomycesCount = 0 ;
		dinoflagellateCount = 0 ;
		diatomCount = 0 ;
		coccolithophoreCount = 0 ;
		yeastCount = 0 ;
		stepToxCount = 0 ;
		stepEmitToxCount = 0 ;
		stepEatCount = 0 ;
		customCount = 0 ;
		
		for(int x = 0; x < dominion.xBounds; x++){
			for(int y = 0; y < dominion.yBounds; y++){
				for(int z = 0; z < dominion.zBounds; z++){
					temp = dominion.allThings[x][y][z].illumination ;
					if(temp > stepMaxLight)
						stepMaxLight = temp ;
					else if(temp < stepMinLight)
						stepMinLight = temp ;
					
					temp = dominion.allThings[x][y][z].heat ;
					totalHeat += temp ;
					if(temp > stepMaxHeat)
						stepMaxHeat = temp ;
					else if(temp < stepMinHeat)
						stepMinHeat = temp ;
					
					if(dominion.allThings[x][y][z] instanceof ToxicPlume){
						temp = ((ToxicPlume)dominion.allThings[x][y][z]).toxicity ;
						totalToxicity += temp ;
						plumeCount++ ;
					}
					else if(dominion.allThings[x][y][z].entityType == EntityType.ORGANIC_OBJECT || dominion.allThings[x][y][z].entityType == EntityType.ORGANISM){
						if(dominion.allThings[x][y][z].entityType == EntityType.ORGANIC_OBJECT)
							organicCount++ ;
						temp = ((OrganicEntity)dominion.allThings[x][y][z]).energy ;
						totalEnergy += temp ;
						temp = ((OrganicEntity)dominion.allThings[x][y][z]).nutrientHue.getRed() ;
						totalRedNut += temp ;
						temp = ((OrganicEntity)dominion.allThings[x][y][z]).nutrientHue.getGreen() ;
						totalGreenNut += temp ;
						temp = ((OrganicEntity)dominion.allThings[x][y][z]).nutrientHue.getBlue() ;
						totalBlueNut += temp ;						
						temp = ((OrganicEntity)dominion.allThings[x][y][z]).toxicity ;
						totalToxicity += temp ;
						
						if(dominion.allThings[x][y][z].entityType == EntityType.ORGANISM){
							organismCount++ ;
							switch(((Organism)dominion.allThings[x][y][z]).currentAction){
							case IDLE:	stepIdleCount++ ;
										break ;
							case MOVE:	stepMoveCount++ ;
										break ;
							case SIGNAL:stepSignalCount++ ;
										break ;
							case CREATED:stepCreatedCount++ ;
										break ;
							case ATTACK:stepAttackCount++ ;
										break ;
							case DETOX	:stepDetoxCount++ ;
										break ;
							case TOXIFY: stepToxCount++ ;
										break ;
							case EMIT_TOXINS: stepEmitToxCount++ ;
										break ;
							case EAT:	stepEatCount++ ;
										break ;
							default:	break ;
							}
							
							temp = ((Organism)dominion.allThings[x][y][z]).lifetime ;
							if(temp > oldestOrganism)
								oldestOrganism = (int)temp ;
							if(temp < youngestOrganism)
								youngestOrganism = (int)temp ;
							tempNutrients = ((Organism)dominion.allThings[x][y][z]).energy + ((Organism)dominion.allThings[x][y][z]).nutrientHue.getRed() + ((Organism)dominion.allThings[x][y][z]).nutrientHue.getGreen() + ((Organism)dominion.allThings[x][y][z]).nutrientHue.getBlue() ;
							if(dominion.allThings[x][y][z] instanceof AdvancedOrganism){
								switch(((AdvancedOrganism)dominion.allThings[x][y][z]).subconsciousAction){
								case PHOTOSYNTHESIS:stepPhotoCount++ ;
													break ;
								case SIGNAL:		lastStepSubconsciousSignalCount++ ;
													break ;
								case CHEMOSYNTHESIS:stepChemoCount++ ;
													break ;
								case REGENERATE:	stepRegenCount++ ;
													break ;
								default:			break ;
								}
								
								if(((AdvancedOrganism)dominion.allThings[x][y][z]).progeny > topProgeny){
									topProgeny = ((AdvancedOrganism)dominion.allThings[x][y][z]).progeny ;
									newMostVirile = ((AdvancedOrganism)dominion.allThings[x][y][z]).name + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).id + "." + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).generation + ", age " + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).lifetime + " with " + topProgeny + " children" ;
								}
								else if(((AdvancedOrganism)dominion.allThings[x][y][z]).progeny == topProgeny && tempNutrients > topNutrients){
									newMostVirile = ((AdvancedOrganism)dominion.allThings[x][y][z]).name + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).id + "." + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).generation + ", age " + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).lifetime + " with " + topProgeny + " children";
									topNutrients = tempNutrients ;
								}
								else if(topProgeny == 0 && tempNutrients > topNutrients){
									newMostVirile = ((AdvancedOrganism)dominion.allThings[x][y][z]).name + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).id + "." + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).generation + ", age " + (int)((AdvancedOrganism)dominion.allThings[x][y][z]).lifetime ;
									topNutrients = tempNutrients ;
								}
							
								if(dominion.allThings[x][y][z] instanceof Cyanobacteria)
									cyanobacteriaCount++ ;
								else if(dominion.allThings[x][y][z] instanceof NitrogenFixer)
									nitrogenFixerCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Phytoplankton)
									phytoplanktonCount++ ;
								else if(dominion.allThings[x][y][z] instanceof SuperOrganism)
									superOrganismCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Zooplankton)
									zooplanktonCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Streptomyces)
									streptomycesCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Dinoflagellate)
									dinoflagellateCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Diatom)
									diatomCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Coccolithophore)
									coccolithophoreCount++ ;
								else if(dominion.allThings[x][y][z] instanceof Yeast)
									yeastCount++ ;
								else if(dominion.allThings[x][y][z] instanceof CustomOrganism)
									customCount++ ;
								else if(dominion.allThings[x][y][z] instanceof AdvancedOrganism)
									advancedOrganismCount++ ;
							}
							else{
								if(tempNutrients > topNutrients && topProgeny < 1){
									newMostVirile = "Basic Organism, age " + (int)dominion.allThings[x][y][z].lifetime ;
									topNutrients = tempNutrients ;
								}
								basicOrganismCount++ ;
							}
						}
					}
				}
			}
		}
	}
}
