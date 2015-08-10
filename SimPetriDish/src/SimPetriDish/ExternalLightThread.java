/*
	ExternalLightThread.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Calculates the lighting for external light sources lighting the external facing walls of the simulation area.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.concurrent.Callable;

public class ExternalLightThread implements Callable<double[][][]>{
	int maxLightBrightness, xBounds, yBounds, zBounds, maxRaySteps ;
	double translucence, lightStepIncrement, lightContinueThreshold ;
	double[][][] translucenceMap, wallIlluminationModel, cumulativeIlluminationModel ;
	int wallEmission, toCalculate ;
	int[][][] rayAddCount ;
	
	ExternalLightThread(int wallBrightness, int wall, int worldX, int worldY, int worldZ, int worldMaxBrightness, double worldLightStepIncrement, double worldLightContinueThreshold, int worldMaxRaySteps, double worldTranslucence, double[][][] worldTranslucenceMap){
		maxLightBrightness = worldMaxBrightness ;
		maxRaySteps = worldMaxRaySteps ;
		translucence = worldTranslucence ;
		lightStepIncrement = worldLightStepIncrement ;
		lightContinueThreshold = worldLightContinueThreshold ;
		translucenceMap = worldTranslucenceMap ;
		xBounds = worldX ;
		yBounds = worldY ;
		zBounds = worldZ ;
		wallEmission = wallBrightness ;
		toCalculate = wall ;
	}
	
	public double[][][] call() throws Exception{
		try{
			return rayTrace() ;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public double[][][] rayTrace(){
		rayAddCount = new int[xBounds][yBounds][zBounds] ;
		wallIlluminationModel = new double[xBounds][yBounds][zBounds] ;
		cumulativeIlluminationModel = new double[xBounds][yBounds][zBounds] ;
		double temp ;
		
		double rayAngle = (Math.PI / 18.0) ;								//5 degree side facing ray for slight soft shadows
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					wallIlluminationModel[x][y][z] = 0 ;
					rayAddCount[x][y][z] = 0 ;
					cumulativeIlluminationModel[x][y][z] = 0 ;
				}
			}
		}
		if(wallEmission <= 0)
			return cumulativeIlluminationModel ;
		
		switch(toCalculate){
		case 0:		for(int y = 0; y < yBounds; y++){													//5 rays per lit boundary tile    - light at X bound
						for(int z = 0; z < zBounds; z++){
							singleRay(wallEmission, -1 * lightStepIncrement, 0, 0, xBounds - 1, y, z) ;
							singleRay(wallEmission, -1 * Math.cos(rayAngle) * lightStepIncrement, Math.sin(rayAngle) * lightStepIncrement, 0, xBounds - 1, y, z) ;
							singleRay(wallEmission, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, Math.sin(-1 * rayAngle) * lightStepIncrement, 0, xBounds - 1, y, z) ;
							singleRay(wallEmission, -1 * Math.cos(rayAngle) * lightStepIncrement, 0, Math.sin(rayAngle) * lightStepIncrement, xBounds - 1, y, z) ;
							singleRay(wallEmission, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, 0, Math.sin(-1 * rayAngle) * lightStepIncrement, xBounds - 1, y, z) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		case 1:		for(int y = 0; y < yBounds; y++){																					//light at X = -1
						for(int z = 0; z < zBounds; z++){
							singleRay(wallEmission, lightStepIncrement, 0, 0, -1, y, z) ;
							singleRay(wallEmission, Math.cos(rayAngle) * lightStepIncrement, Math.sin(rayAngle) * lightStepIncrement, 0, - 1, y, z) ;
							singleRay(wallEmission, Math.cos(-1 * rayAngle) * lightStepIncrement, Math.sin(-1 * rayAngle) * lightStepIncrement, 0, - 1, y, z) ;
							singleRay(wallEmission, Math.cos(rayAngle) * lightStepIncrement, 0, Math.sin(rayAngle) * lightStepIncrement, - 1, y, z) ;
							singleRay(wallEmission, Math.cos(-1 * rayAngle) * lightStepIncrement, 0, Math.sin(-1 * rayAngle) * lightStepIncrement, - 1, y, z) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		case 2:		for(int x = 0; x < xBounds; x++){																						//light at Y bound
						for(int z = 0; z < zBounds; z++){
							singleRay(wallEmission, 0, -1 * lightStepIncrement, 0, x, yBounds - 1, z) ;
							singleRay(wallEmission, Math.sin(rayAngle) * lightStepIncrement, -1 * Math.cos(rayAngle) * lightStepIncrement, 0, x, yBounds - 1, z) ;
							singleRay(wallEmission, Math.sin(-1 * rayAngle) * lightStepIncrement, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, 0, x, yBounds - 1, z) ;
							singleRay(wallEmission, 0, -1 * Math.cos(rayAngle) * lightStepIncrement, Math.sin(rayAngle) * lightStepIncrement, x, yBounds - 1, z) ;
							singleRay(wallEmission, 0, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, Math.sin(-1 * rayAngle) * lightStepIncrement, x, yBounds - 1, z) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		case 3:		for(int x = 0; x < xBounds; x++){																					//light at y = -1
						for(int z = 0; z < zBounds; z++){
							singleRay(wallEmission, 0, lightStepIncrement, 0, x, -1, z) ;
							singleRay(wallEmission, Math.sin(rayAngle) * lightStepIncrement, Math.cos(rayAngle) * lightStepIncrement, 0, x, - 1, z) ;
							singleRay(wallEmission, Math.sin(-1 * rayAngle) * lightStepIncrement, Math.cos(-1 * rayAngle) * lightStepIncrement, 0, x, - 1, z) ;
							singleRay(wallEmission, 0, Math.cos(rayAngle) * lightStepIncrement, Math.sin(rayAngle) * lightStepIncrement, x, - 1, z) ;
							singleRay(wallEmission, 0, Math.cos(-1 * rayAngle) * lightStepIncrement, Math.sin(-1 * rayAngle) * lightStepIncrement, x, - 1, z) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		case 4:		for(int x = 0; x < xBounds; x++){																						//light at Z bound
						for(int y = 0; y < yBounds; y++){
							singleRay(wallEmission, 0, 0, -1 * lightStepIncrement, x, y, zBounds - 1) ;
							singleRay(wallEmission, Math.sin(rayAngle) * lightStepIncrement, 0, -1 * Math.cos(rayAngle) * lightStepIncrement, x, y, zBounds - 1) ;
							singleRay(wallEmission, Math.sin(-1 * rayAngle) * lightStepIncrement, 0, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, x, y, zBounds - 1) ;
							singleRay(wallEmission, 0, Math.sin(rayAngle) * lightStepIncrement, -1 * Math.cos(rayAngle) * lightStepIncrement, x, y, zBounds - 1) ;
							singleRay(wallEmission, 0, Math.sin(-1 * rayAngle) * lightStepIncrement, -1 * Math.cos(-1 * rayAngle) * lightStepIncrement, x, y, zBounds - 1) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		case 5:		for(int x = 0; x < xBounds; x++){																					//light at z = -1
						for(int y = 0; y < yBounds; y++){
							singleRay(wallEmission, 0, 0, lightStepIncrement, x, y, -1) ;
							singleRay(wallEmission, Math.sin(rayAngle) * lightStepIncrement, 0, Math.cos(rayAngle) * lightStepIncrement, x, y, -1) ;
							singleRay(wallEmission, Math.sin(-1 * rayAngle) * lightStepIncrement, 0, Math.cos(-1 * rayAngle) * lightStepIncrement, x, y, -1) ;
							singleRay(wallEmission, 0, Math.sin(rayAngle) * lightStepIncrement, Math.cos(rayAngle) * lightStepIncrement, x, y, -1) ;
							singleRay(wallEmission, 0, Math.sin(-1 * rayAngle) * lightStepIncrement, Math.cos(-1 * rayAngle) * lightStepIncrement, x, y, -1) ;
						}
					}
					for(int x = 0; x < xBounds; x++){
						for(int y = 0; y < yBounds; y++){
							for(int z = 0; z < zBounds; z++){
								if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
									temp = (wallIlluminationModel[x][y][z] / rayAddCount[x][y][z]);
									cumulativeIlluminationModel[x][y][z] += temp ;
								}	
							}
						}
					}
					break ;
		}
		return cumulativeIlluminationModel ;
	}
	
	private void singleRay(int wallLight, double rayXStep, double rayYStep, double rayZStep, int xLoc, int yLoc, int zLoc){
		double curRayIntensity, rayLength, rayX, rayY, rayZ, temp, modTranslucence ;
		int stepCounter, lastLitX, lastLitY, lastLitZ, curLitX, curLitY, curLitZ ;
		
		modTranslucence = translucence ;
		curRayIntensity = wallLight * modTranslucence ;
		stepCounter = 0 ;
		rayLength = 0.0 ;
		rayX = 0 ;
		rayY = 0 ;
		rayZ = 0 ;
		lastLitX = -1 ;
		lastLitY = -1 ;
		lastLitZ = -1 ;
		wallIlluminationModel[(int)(xLoc + 0.5)][(int)(yLoc + 0.5)][(int)(zLoc + 0.5)] += curRayIntensity ;				//Starting position light
		rayAddCount[(int)(xLoc + 0.5)][(int)(yLoc + 0.5)][(int)(zLoc + 0.5)]++ ;
			
		while(curRayIntensity >= lightContinueThreshold && stepCounter < maxRaySteps){			//One ray  
			rayX += rayXStep ;
			rayY += rayYStep ;
			rayZ += rayZStep ;
			rayLength += lightStepIncrement ;

			curLitX = (int)(xLoc + rayX + 0.5) ;																//Measure from the center of the light box
			curLitY = (int)(yLoc + rayY + 0.5) ;
			curLitZ = (int)(zLoc + rayZ + 0.5) ;		
			
			if(curLitX < 0 || curLitX >= xBounds || curLitY < 0 || curLitY >= yBounds || curLitZ < 0 || curLitZ >= zBounds)
				break ;
			
			if(curLitX != lastLitX || curLitY != lastLitY || curLitZ != lastLitZ){								//If the step increment lands in a new location
				wallIlluminationModel[curLitX][curLitY][curLitZ] += curRayIntensity ;
				rayAddCount[curLitX][curLitY][curLitZ]++ ;
				temp = modTranslucence * translucenceMap[curLitX][curLitY][curLitZ] ;
				modTranslucence = temp ;
				curRayIntensity = (wallLight / (rayLength * rayLength)) * modTranslucence ;
				lastLitX = curLitX ;
				lastLitY = curLitY ;
				lastLitZ = curLitZ ;
			}
			stepCounter++ ;
		}
	}
}
