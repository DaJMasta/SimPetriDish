/*
	World.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Contains the information that makes up the world - a three dimensional array of entities and a list of organism controllers - and handles running every-step updates for all elements
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class World{
	int heatFloor = 0 ;
	int xBounds = 50 ;
	int yBounds = 50 ;
	int zBounds = 1 ;
	double translucence = 3.0 ;
	double heatTransferFactor = 1.0 ;
	double boundaryInsulationFactor = 4.0 ;
	Entity[][][] allThings ;
	int radialLightResolution = 172 ;				// MUST BE DIVISIBLE BY 4
	int maxRaySteps = 100 ;
	int maxLightBrightness = 255 ;
	double lightContinueThreshold = 0.10 ;
	double lightStepIncrement = 0.49 ;
	double age = 1 ;
	int stepDeaths, stepConsumption, stepDecay, stepReproduction, totalLights, illuminationTotal, lastIlluminationTotal, lastTotalLights ;
	int stepKillCount = 0 ;
	int stepOldCount = 0 ;
	int stepStarveCount = 0 ;
	int stepFreezeCount = 0 ;
	int stepCookedCount = 0 ;
	int stepPoisonedCount = 0 ;
	int stepDeficiencyCount = 0 ;
	double driftVariance = 0.25 ;																					//Drift variance and speeds can only total 100, all values are in percent chance to move one tile in that direction
	double driftSpeedX = 0.0 ;
	double driftSpeedY = 0.0 ;
	double driftSpeedZ = 0.0 ;
	boolean bDrift = true ;
	int driftStrength = 2 ;
	int[] wallTemps, wallBrightness ;								//6 sides for the facings of the edges of the world, X+, X-, Y+, Y-, Z+, Z-
	
	ArrayList<OrganismEngine> controllers = new ArrayList<OrganismEngine>() ;
	List<Callable<double[][][]>> threadBlock ;
	ExecutorService threadPool ;
	
	World(int setX, int setY, int setZ, int setHeat, int setLight, double setTrans, double setHeatTrans){
		xBounds = setX ;
		yBounds = setY ;
		zBounds = setZ ;
		heatFloor = setHeat ;
		translucence = setTrans ;
		heatTransferFactor = setHeatTrans ;
		
		wallTemps = new int[6] ;
		wallBrightness = new int[6] ;
		for(int i = 0; i < 6; i++){
			wallTemps[i] = heatFloor ;
			wallBrightness[i] = setLight ;
		}
		
		
		initWorld() ;
	}
	
	public void initWorld(){																	//Fill the world with empty entities
		allThings = new Entity[xBounds][yBounds][zBounds] ;
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					allThings[x][y][z] = new Entity() ;
				}
			}
		}
		stepDeaths = 0 ;
		stepConsumption = 0 ;
		stepDecay = 0 ;
		totalLights = 0 ;
		lastTotalLights = 0 ;
		illuminationTotal = 0 ;
		lastIlluminationTotal = 0 ;
	
		threadPool = Executors.newCachedThreadPool();
		threadBlock = new ArrayList<Callable<double[][][]>>() ;
	}
	
	public void setDrift(boolean doesDrift, int driftWeightPush, double randomDrift, double flowX, double flowY, double flowZ){
		driftVariance = randomDrift ;
		driftSpeedX = flowX ;
		driftSpeedY = flowY ;
		driftSpeedZ = flowZ ;
		bDrift = doesDrift ;
		driftStrength = driftWeightPush ;
	}
	
	public void endWorldThreads(){
		threadPool.shutdown() ;
	}
	
	public void ageStep(){																				//Trigger every entity to age and trigger decay on any that require it
		OrganicEntity tempEntity ;
		Entity tempToxic ;
		Color tempHue ;
		int tempEnergy, tempMaxDecay, tempTox ;	
		double tempHeat, newlySpread ;
		OrganismEngine toDie ;
		int tempToRemove, tempCoordX, tempCoordY, tempCoordZ, randomChoice, plumeTargets ;
		Random rand = new Random() ;
		Entity[] surrounding ;
		boolean bValidSpread = false ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					allThings[x][y][z].age() ;
					
					if(allThings[x][y][z].entityType == EntityType.PLUME){
						newlySpread = ((ToxicPlume)allThings[x][y][z]).doSpread(driftVariance + Math.abs(driftSpeedX) + Math.abs(driftSpeedY) + Math.abs(driftSpeedZ)) ;
						if(newlySpread > 0){
							surrounding = getSurrounding(x, y, z) ;
							plumeTargets = 0 ;
							for(int i = 0; i < 6; i++){
								bValidSpread = surrounding[i].entityType == EntityType.NOTHING || surrounding[i].entityType == EntityType.ORGANISM || surrounding[i].entityType == EntityType.ORGANIC_OBJECT || surrounding[i].entityType == EntityType.PLUME ;
								if(bValidSpread){
									if((!(driftSpeedX < -0.25 && i == 0)) && (!(driftSpeedX > 0.25 && i == 1)) && (!(driftSpeedY < -0.25 && i == 2)) && (!(driftSpeedY > 0.25 && i == 3)) && (!(driftSpeedZ < -0.25 && i == 4)) && (!(driftSpeedZ > 0.25 && i == 5)))
										plumeTargets++ ;
									else
										surrounding[i] = new Entity(boundaryInsulationFactor, heatFloor) ;
								}
							}
							
							if(plumeTargets > 0){
								randomChoice = rand.nextInt(plumeTargets) ;
								
								for(int i = 0; i < 6; i++){
									bValidSpread = surrounding[i].entityType == EntityType.NOTHING || surrounding[i].entityType == EntityType.ORGANISM || surrounding[i].entityType == EntityType.ORGANIC_OBJECT || surrounding[i].entityType == EntityType.PLUME ;
									if(randomChoice == 0 && bValidSpread){
										tempCoordX = x ;
										tempCoordY = y ;
										tempCoordZ = z ;
										
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
										if(allThings[tempCoordX][tempCoordY][tempCoordZ].entityType == EntityType.NOTHING){
											tempHeat = allThings[tempCoordX][tempCoordY][tempCoordZ].heat ;
											setEntity(tempCoordX, tempCoordY, tempCoordZ, new ToxicPlume(newlySpread, tempHeat)) ;
											break ;
										}
										else if(allThings[tempCoordX][tempCoordY][tempCoordZ].entityType == EntityType.ORGANISM || allThings[tempCoordX][tempCoordY][tempCoordZ].entityType == EntityType.ORGANIC_OBJECT){
											((OrganicEntity)allThings[tempCoordX][tempCoordY][tempCoordZ]).toxicity += newlySpread ;
											break ;
										}
										else if(allThings[tempCoordX][tempCoordY][tempCoordZ].entityType == EntityType.PLUME){
											((ToxicPlume)allThings[tempCoordX][tempCoordY][tempCoordZ]).toxicity += newlySpread ;
											break ;
										}
									}
									else if(bValidSpread)
										randomChoice-- ;
								}
							}
						}
					}
					else if(allThings[x][y][z].entityType == EntityType.ORGANISM || allThings[x][y][z].entityType == EntityType.ORGANIC_OBJECT) {
						tempEntity = (OrganicEntity)allThings[x][y][z] ;
						tempToRemove = -1 ;
						if(tempEntity.bChangeType){
							
							if(tempEntity instanceof Organism){
								if(tempEntity instanceof AdvancedOrganism){
									switch(((AdvancedOrganism)tempEntity).deathState){
									case KILLED:	stepKillCount++ ;
													break ;
									case OLD_AGE:	stepOldCount++ ;
													break ;
									case STARVATION:stepStarveCount++ ;
													break ;
									case COLD:		stepFreezeCount++ ;
													break ;
									case HEAT:		stepCookedCount++ ;
													break ;
									case TOXICITY:	stepPoisonedCount++ ;
													break ;
									case NUTRITION: stepDeficiencyCount++ ;
													break ;
									default:		System.out.println("Living dead organism") ;
													break ;
									}
								}
								tempHue = tempEntity.nutrientHue ;
								tempEnergy = tempEntity.energy ;
								tempTox = tempEntity.toxicity ;
								tempMaxDecay = tempEntity.maxDecayLifetime ;
								tempHeat = tempEntity.heat ;
								allThings[x][y][z] = new OrganicEntity(tempEnergy, tempHue.getRed(), tempHue.getGreen(), tempHue.getBlue(), tempMaxDecay, tempTox, tempHeat) ;
								stepDeaths++ ;
								for(int i = 0; i < controllers.size(); i++){
									toDie = controllers.get(i) ;
									if(toDie != null && toDie.host == tempEntity){
										tempToRemove = i ;
									}
								}
								if(tempToRemove >= 0)
									controllers.remove(tempToRemove) ;
							}
							else {
								allThings[x][y][z] = new Entity(tempEntity.heat) ;
								stepDecay++ ;
							} 
						}
					}
					
					if(allThings[x][y][z].entityType == EntityType.PLUME){
						if(((ToxicPlume)allThings[x][y][z]).toxicity < 1){
							tempToxic = allThings[x][y][z] ;
							allThings[x][y][z] = new Entity(tempToxic.heat) ;
						}
					}

				}
			}
		}
		age++ ;
	}
	
	public void worldStep(){													//World step order of operations
		counterReset() ;
		driftStep() ;
		actorStep() ;
		ageStep() ;
		
		heatStep() ;
		lightStep() ;
		threadIntegration() ;
	}
	
	public void counterReset(){
		stepDeaths = 0 ;
		stepDecay = 0 ;
		stepConsumption = 0 ;
		stepReproduction = 0 ;
		illuminationTotal = 0 ;
		stepKillCount = 0 ;
		stepOldCount = 0 ;
		stepStarveCount = 0 ;
		stepFreezeCount = 0 ;
		stepCookedCount = 0 ;
		stepPoisonedCount = 0 ;
		stepDeficiencyCount = 0 ;
	}
	
	public void actorStep(){													//Goes through the list of controllers (OrganismEngines) and tells each to act
		for(int i = 0; i < controllers.size(); i++){
			controllers.get(i).act() ;
		}
	}
	
	public void threadIntegration(){											//Waits for light and heat threads to finish and integrates their output back into the world.
		ArrayList<double[][][]> threadData = new ArrayList<double[][][]>() ;
		List<Future<double[][][]>> threadOutputs ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					allThings[x][y][z].illumination = 0 ;
				}
			}
		}
		
		try{
			threadOutputs = threadPool.invokeAll(threadBlock) ;
			
			threadPool.shutdown() ;
			
			for(int i = 0; i < threadOutputs.size(); i++){
				threadData.add(threadOutputs.get(i).get()) ;
			}
		}catch(Exception e){
		      e.printStackTrace();
		}
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					for(int i = 0; i < threadData.size(); i++){
						if(i == 0)
							allThings[x][y][z].heat = threadData.get(i)[x][y][z] ;
						else
							allThings[x][y][z].illumination += threadData.get(i)[x][y][z] ;
					}
				}
			}
		}	
		
		threadPool = Executors.newCachedThreadPool();
		threadBlock.clear() ;
	}
	
	public void driftStep(){													//Let entities drift according to random variance and world currents
		Random rand = new Random() ;
		Entity temp, tempTarget ;
		int randX, randY, randZ, tempInt ;
		
		if(!bDrift)
			return ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					temp = allThings[x][y][z] ;
					if(temp.entityType != EntityType.NOTHING){
						if(driftStrength >= temp.weight){
							randX = 0 ;
							randY = 0 ;
							randZ = 0 ;
							tempInt = rand.nextInt(10000) ;
							if(tempInt < driftVariance * 300 + Math.abs(driftSpeedX) * 100 + Math.abs(driftSpeedY) * 100 + Math.abs(driftSpeedZ) * 100){
								if(tempInt < driftVariance * 100){
									if(rand.nextBoolean())
										randX++ ;
									else
										randX-- ;
								}
								else if(tempInt < Math.abs(driftSpeedX) * 100 + driftVariance * 100){
									if(driftSpeedX > 0)
										randX++ ;
									else
										randX-- ;
								}
								else if(tempInt < driftVariance * 200 + Math.abs(driftSpeedX) * 100){
									if(rand.nextBoolean())
										randY++ ;
									else
										randY-- ;
								}
								else if(tempInt < Math.abs(driftSpeedY) * 100 + driftVariance * 200 + Math.abs(driftSpeedX) * 100){
									if(driftSpeedY > 0)
										randY++ ;
									else
										randY-- ;
								}
								else if(tempInt < driftVariance * 300 + Math.abs(driftSpeedY) * 100 + Math.abs(driftSpeedX) * 100){
									if(rand.nextBoolean())
										randZ++ ;
									else
										randZ-- ;
								}
								else if(tempInt < Math.abs(driftSpeedZ) * 100 + driftVariance * 300 + Math.abs(driftSpeedY) * 100 + Math.abs(driftSpeedX) * 100){
									if(driftSpeedZ > 0)
										randZ++ ;
									else
										randZ-- ;
								}
								
								if((x + randX) >= 0 && (x + randX) < xBounds && (y + randY) >= 0 && (y + randY) < yBounds && (z + randZ) >= 0 && (z + randZ) < zBounds){
									if(x != randX || y != randY || z != randZ){
										tempTarget = allThings[x+randX][y+randY][z+randZ] ;
										if(tempTarget.entityType == EntityType.NOTHING){
											if(temp.bTrackLocation){
												temp.updateLocation(x+randX, y+randY, z+randZ) ;
											}
											allThings[x+randX][y+randY][z+randZ] = temp ;
											if(tempTarget.bTrackLocation){
												tempTarget.updateLocation(x, y, z) ;
											}
											allThings[x][y][z] = tempTarget ;
										}
									}
								}
							}
						}
					}
				}
			}
		}	
	}
	
	public void heatStep(){																			//Generate a heat thread
		double[][][] heats = new double[xBounds][yBounds][zBounds] ;
		double[][][] insulations = new double[xBounds][yBounds][zBounds] ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					heats[x][y][z] += allThings[x][y][z].heat;
					insulations[x][y][z] = allThings[x][y][z].insulationFactor;
				}
			}
		}
		
		threadBlock.add(0, new HeatSimThread(xBounds, yBounds, zBounds, heatTransferFactor, heatFloor, boundaryInsulationFactor, heats, insulations, wallTemps)) ;
	}	
	
	public void lightStep(){																		//Generate a light thread for each light source (and one for all wall lighting)
		double[][][] translucenceMap = new double[xBounds][yBounds][zBounds] ;
		double[] fullTranslucentFacings = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0} ;
		Callable<double[][][]> lightThread ;
		
		lastTotalLights = totalLights ;
		totalLights = 0 ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					translucenceMap[x][y][z] = allThings[x][y][z].translucence ;
				}
			}
		}
		
		for(int i = 0; i < 6; i++){
			if(wallBrightness[i] > maxLightBrightness)
				wallBrightness[i] = maxLightBrightness ;
			else if(wallBrightness[i] < 0)
				wallBrightness[i] = 0 ;
			
			if(wallBrightness[i] > 0){
					lightThread = new ExternalLightThread(wallBrightness[i], i, xBounds, yBounds, zBounds, maxLightBrightness, lightStepIncrement, lightContinueThreshold, maxRaySteps, translucence, translucenceMap) ;
				threadBlock.add(lightThread) ;
			}
		}
		
		illuminationTotal += ((wallBrightness[0] + wallBrightness[1]) * (yBounds * zBounds) / 9) ;
		illuminationTotal += ((wallBrightness[2] + wallBrightness[3]) * (xBounds * zBounds) / 9) ;
		illuminationTotal += ((wallBrightness[4] + wallBrightness[5]) * (xBounds * yBounds) / 9) ;						//16 chosen to quantify wall illumination, since the models are so different, it's just something I picked
		
		for(int i = 0; i < 6; i++)
			if(wallBrightness[i] > 0)
				totalLights++ ;
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					if(allThings[x][y][z].bLightSource){
						if(allThings[x][y][z].lightEmission > maxLightBrightness)
							allThings[x][y][z].lightEmission = maxLightBrightness ;
						if(allThings[x][y][z] instanceof LightSource){
							lightThread = new LightTraceThread(allThings[x][y][z].lightEmission, x, y, z, xBounds, yBounds, zBounds, 
																						maxLightBrightness, radialLightResolution, lightStepIncrement, 
																						lightContinueThreshold, maxRaySteps, translucence, translucenceMap, ((LightSource)allThings[x][y][z]).facingTranslucence) ;
						}
						else {
							lightThread = new LightTraceThread(allThings[x][y][z].lightEmission, x, y, z, xBounds, yBounds, zBounds, 
									maxLightBrightness, radialLightResolution, lightStepIncrement, 
									lightContinueThreshold, maxRaySteps, translucence, translucenceMap, fullTranslucentFacings) ;
						}
							
						threadBlock.add(lightThread) ;
						illuminationTotal += allThings[x][y][z].lightEmission * translucence ;
						totalLights++ ;
					}
				}
			}
		}	
		lastIlluminationTotal = illuminationTotal ;
	}
	
	// East, West, North, South, Top, Bottom  (X+, X-, Y+, Y-, Z+, Z-)
	public Entity[] getSurrounding(int xCoord, int yCoord, int zCoord){
		Entity[] toReturn = new Entity[6] ;
		
		if(xCoord + 1 < xBounds)
			toReturn[0] = allThings[xCoord+1][yCoord][zCoord] ;
		else
			toReturn[0] = new Entity(boundaryInsulationFactor, wallTemps[0]) ;
		if(xCoord - 1 >= 0)
			toReturn[1] = allThings[xCoord-1][yCoord][zCoord] ;
		else
			toReturn[1] = new Entity(boundaryInsulationFactor, wallTemps[1]) ;
		
		if(yCoord + 1 < yBounds)
			toReturn[2] = allThings[xCoord][yCoord+1][zCoord] ;
		else
			toReturn[2] = new Entity(boundaryInsulationFactor, wallTemps[2]) ;
		if(yCoord - 1 >= 0)
			toReturn[3] = allThings[xCoord][yCoord-1][zCoord] ;
		else
			toReturn[3] = new Entity(boundaryInsulationFactor, wallTemps[3]) ;	
		
		if(zCoord + 1 < zBounds)
			toReturn[4] = allThings[xCoord][yCoord][zCoord+1] ;
		else
			toReturn[4] = new Entity(boundaryInsulationFactor, wallTemps[4]) ;
		if(zCoord - 1 >= 0)
			toReturn[5] = allThings[xCoord][yCoord][zCoord-1] ;
		else
			toReturn[5] = new Entity(boundaryInsulationFactor, wallTemps[5]) ;
		
		return toReturn ;
	}
	
	public void incrementConsumption(){
		stepConsumption++ ;
	}
	
	public void incrementReproduction(){
		stepReproduction++ ;
	}
	
	public Entity getEntity(int xCoord, int yCoord, int zCoord){
		return allThings[xCoord][yCoord][zCoord] ;
	}
	
	public boolean isOccupied(int xCoord, int yCoord, int zCoord){																									//Plumes do not count as blocking a cell
		if(xCoord < 0 || xCoord >= xBounds || yCoord < 0 || yCoord >= yBounds || zCoord < 0 || zCoord >= zBounds)
			return true ;
		if(allThings[xCoord][yCoord][zCoord].entityType != EntityType.NOTHING && allThings[xCoord][yCoord][zCoord].entityType != EntityType.PLUME)
			return true ;
		return false ;
	}
	
	public void setEntity(int xCoord, int yCoord, int zCoord, Entity setTo){
		if(setTo == null)
			return ;
		allThings[xCoord][yCoord][zCoord] = setTo ;
	}
	
	public int readIllumination(int xCoord, int yCoord, int zCoord){
		return allThings[xCoord][yCoord][zCoord].illumination ;
	}
	
	public void addController(OrganismEngine newController){
		if(newController != null)
			controllers.add(newController) ;
	}	
	
	public boolean takeControl(AdvancedOrganismEngine newController, AdvancedOrganism host){
		for(OrganismEngine c : controllers){
			if(c.host == host){
				controllers.remove(c) ;
				controllers.add(newController) ;
				return true ;
			}
		}
		return false ;
	}
}
