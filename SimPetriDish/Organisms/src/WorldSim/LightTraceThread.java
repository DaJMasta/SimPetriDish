/*
	LightTraceThread.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Calculates lighting for a single point light source and returns an illumination map for the light source.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/


package WorldSim;

import java.util.concurrent.Callable;

public class LightTraceThread implements Callable<double[][][]>{
	
	int maxLightBrightness, radialLightResolution, xBounds, yBounds, zBounds, maxRaySteps, intensity, xLoc, yLoc, zLoc ;
	double translucence, lightStepIncrement, lightContinueThreshold ;
	double[][][] translucenceMap ;
	double[] lightFacingTranslucenceMap ;
	
	LightTraceThread(int lightIntensity, int lightXLoc, int lightYLoc, int lightZLoc, int worldX, int worldY, int worldZ, int worldMaxBrightness, int worldRadialResolution, double worldLightStepIncrement, double worldLightContinueThreshold, int worldMaxRaySteps, double worldTranslucence, double[][][] worldTranslucenceMap, double[] lightFacingTransMap){
		maxLightBrightness = worldMaxBrightness ;
		radialLightResolution = worldRadialResolution ;
		maxRaySteps = worldMaxRaySteps ;
		translucence = worldTranslucence ;
		lightStepIncrement = worldLightStepIncrement ;
		lightContinueThreshold = worldLightContinueThreshold ;
		translucenceMap = worldTranslucenceMap ;
		xBounds = worldX ;
		yBounds = worldY ;
		zBounds = worldZ ;
		intensity = lightIntensity ;
		xLoc = lightXLoc ;
		yLoc = lightYLoc ;
		zLoc = lightZLoc ;
		lightFacingTranslucenceMap = lightFacingTransMap ;
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
		double curRayIntensity, rayLength, rayX, rayY, rayZ, iAngle, jAngle, rayXStep, rayYStep, rayZStep, temp, modTranslucence, facingTrans ;
		int stepCounter, lastLitX, lastLitY, lastLitZ, curLitX, curLitY, curLitZ, modLightResolution, facingTransCount ;
		int[][][] rayAddCount = new int[xBounds][yBounds][zBounds] ;
		double[][][] singleSourceModel = new double[xBounds][yBounds][zBounds] ;
		
		if(intensity * translucence > 270)												//Performance optimization for dim lights
			modLightResolution = radialLightResolution ;
		else if(intensity * translucence > 120)
			modLightResolution = (int)(radialLightResolution * 0.75) ;
		else if(intensity * translucence > 46)
			modLightResolution = (int)(radialLightResolution * 0.5) ;
		else
			modLightResolution = (int)(radialLightResolution * 0.25) ;
		
		double stepIncrement = (2 * Math.PI) / modLightResolution ;								//Radians
		
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					singleSourceModel[x][y][z] = 0 ;
					rayAddCount[x][y][z] = 0 ;
				}
			}
		}
		
		singleSourceModel[xLoc][yLoc][zLoc] += intensity * translucence ;
		rayAddCount[xLoc][yLoc][zLoc]++ ;
		
		iAngle = 0.0 ;
		jAngle = 0.0 ;
		
		for(int i = 0; i < (modLightResolution / 2) ; i++){												//Create a disc of rays from a central point, then rotate the disc 180 degrees to make a full ball of rays
			iAngle = i * stepIncrement ;
			for(int j = 0; j < modLightResolution ; j++){
				jAngle = j * stepIncrement ;
				curRayIntensity = intensity ;
				modTranslucence = translucence ;
				stepCounter = 0 ;
				rayLength = 0.0 ;
				rayXStep = (Math.sin(iAngle) * Math.cos(jAngle)) * lightStepIncrement ;									//The direction the current ray goes (unit vector * lightStepIncrement)
				rayYStep = (Math.cos(iAngle) * Math.cos(jAngle)) * lightStepIncrement ;									//these values are added each loop iteration to check if they light a new cell
				rayZStep = Math.sin(jAngle) * lightStepIncrement ;
				rayX = 0 ;
				rayY = 0 ;
				rayZ = 0 ;
				lastLitX = -1 ;
				lastLitY = -1 ;
				lastLitZ = -1 ;
				
				singleSourceModel[(int)(xLoc + 0.5)][(int)(yLoc + 0.5)][(int)(zLoc + 0.5)] += intensity * translucence ;
				rayAddCount[(int)(xLoc + 0.5)][(int)(yLoc + 0.5)][(int)(zLoc + 0.5)]++ ;
					
				while(curRayIntensity >= lightContinueThreshold && stepCounter < maxRaySteps){			//One ray  
					facingTrans = 0 ;
					facingTransCount = 0 ;
					rayX += rayXStep ;
					rayY += rayYStep ;
					rayZ += rayZStep ;
					rayLength += lightStepIncrement ;
	
					curLitX = (int)(xLoc + rayX + 0.5) ;																//Measure from the center of the light box
					curLitY = (int)(yLoc + rayY + 0.5) ;
					curLitZ = (int)(zLoc + rayZ + 0.5) ;
						
					
					if(curLitX == xLoc + 1 && Math.abs(curLitY - yLoc) <= 1 && Math.abs(curLitZ - zLoc) <= 1){			//Non-translucent facings
						facingTrans += lightFacingTranslucenceMap[0] ;
						facingTransCount++ ;
					}
					else if(curLitX == xLoc - 1 && Math.abs(curLitY - yLoc) <= 1 && Math.abs(curLitZ - zLoc) <= 1){
						facingTrans += lightFacingTranslucenceMap[1] ;
						facingTransCount++ ;
					}
					if(curLitY == yLoc + 1 && Math.abs(curLitX - xLoc) <= 1 && Math.abs(curLitZ - zLoc) <= 1){
						facingTrans += lightFacingTranslucenceMap[2] ;
						facingTransCount++ ;
					}
					else if(curLitY == yLoc - 1 && Math.abs(curLitX - xLoc) <= 1 && Math.abs(curLitZ - zLoc) <= 1){
						facingTrans += lightFacingTranslucenceMap[3] ;
						facingTransCount++ ;
					}
					if(curLitZ == zLoc + 1 && Math.abs(curLitY - yLoc) <= 1 && Math.abs(curLitX - xLoc) <= 1){
						facingTrans += lightFacingTranslucenceMap[4] ;
						facingTransCount++ ;
					}
					else if(curLitZ == zLoc - 1 && Math.abs(curLitY - yLoc) <= 1 && Math.abs(curLitX - xLoc) <= 1){
						facingTrans += lightFacingTranslucenceMap[5] ;
						facingTransCount++ ;
					}
						
					if(facingTransCount > 0){
						modTranslucence *= (facingTrans / facingTransCount) ;											//Average facing translucence if on an edge
					}
					
					
					if(curLitX < 0 || curLitX >= xBounds || curLitY < 0 || curLitY >= yBounds || curLitZ < 0 || curLitZ >= zBounds)
						break ;
					
					if(curLitX != lastLitX || curLitY != lastLitY || curLitZ != lastLitZ){								//If the step increment lands in a new location
						singleSourceModel[curLitX][curLitY][curLitZ] += curRayIntensity ;
						rayAddCount[curLitX][curLitY][curLitZ]++ ;
						temp = modTranslucence * translucenceMap[curLitX][curLitY][curLitZ] ;
						modTranslucence = temp ;
						curRayIntensity = (intensity / (rayLength * rayLength)) * modTranslucence ;
						lastLitX = curLitX ;
						lastLitY = curLitY ;
						lastLitZ = curLitZ ;
					}
					stepCounter++ ;
				}
			}
		}
		for(int x = 0; x < xBounds; x++){
			for(int y = 0; y < yBounds; y++){
				for(int z = 0; z < zBounds; z++){
					if(rayAddCount[x][y][z] != 0) {														//Average multiple rays that pass through the same location
						temp = (singleSourceModel[x][y][z] / rayAddCount[x][y][z]);
						singleSourceModel[x][y][z] = temp ;
					}
					else
						singleSourceModel[x][y][z] = 0 ;
				}
			}
		}
		return singleSourceModel ;
	}
}
