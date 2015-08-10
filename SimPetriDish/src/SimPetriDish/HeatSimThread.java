/*
	HeatSimThread.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Calculates heat changes between entities in the world and exchanges with the boundaries of the simulation.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.util.concurrent.Callable;

public class HeatSimThread implements Callable<double[][][]> {
	
	int xBounds, yBounds, zBounds, heatFloor ;
	double heatTransferFactor, boundaryInsulationFactor ;
	double[][][] heats, ins ;
	int[] worldEdgeTemp ;
	
	HeatSimThread(int worldXBounds, int worldYBounds, int worldZBounds, double worldHeatTransferFactor, int worldHeatFloor, double worldBoundaryInsulationFactor, double[][][] worldHeats, double[][][] worldIns, int[] worldEdgeTemps){
		xBounds = worldXBounds ;
		yBounds = worldYBounds ;
		zBounds = worldZBounds ;
		heatTransferFactor = worldHeatTransferFactor ;
		heats = worldHeats ;
		ins = worldIns ;
		heatFloor = worldHeatFloor ;
		boundaryInsulationFactor = worldBoundaryInsulationFactor ;
		worldEdgeTemp = worldEdgeTemps ;
	}
	
	public double[][][] call() throws Exception{
		
		int modX, modY, modZ ;
		try{
			for(int x = 0; x < (xBounds / 2); x++){
				for(int y = 0; y < yBounds; y++){
					for(int z = 0; z < zBounds; z++){
						modX = x * 2 ;
						modY = y ;
						modZ = z ;
						if(y % 2 == 1 && z % 2 == 0)
							modX++ ;
						else if(z % 2 == 1 && y % 2 == 0)
							modX++ ;
						if(modX < xBounds)
							evalHeatAt(modX, modY, modZ) ;												//Each evaluation applies heat to itself and the 6 surrounding, so only execute on every other cell (makes a 3d checkerboard pattern)
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return heats ;
	}

	public void evalHeatAt(int xLoc, int yLoc, int zLoc){
		double tempEnergy, tempHeat, tempIns, tempHeatAdj, tempInsAdj ;
		double[] localHeats = getSurroundingHeats(xLoc, yLoc, zLoc) ;
		double[] localIns = getSurroundingIns(xLoc, yLoc, zLoc) ;
		
		for(int i = 0; i < 6; i++){																	//evaluate heat in one cell with surrounding 6
			tempHeat = heats[xLoc][yLoc][zLoc] ;
			tempIns = ins[xLoc][yLoc][zLoc] ;
			tempHeatAdj = localHeats[i] ;
			tempInsAdj = localIns[i] ;
			tempEnergy = (tempHeat - tempHeatAdj) / (tempIns + tempInsAdj) ;
			heats[xLoc][yLoc][zLoc] += ((tempEnergy * -1.0 * heatTransferFactor) / 32.0);
			localHeats[i] += ((tempEnergy * heatTransferFactor) / 32.0);
		}
		
		if(xLoc + 1 < xBounds)
			heats[xLoc + 1][yLoc][zLoc] = localHeats[0] ;											//Apply external boundary temperatures
		if(xLoc - 1 >= 0)
			heats[xLoc - 1][yLoc][zLoc] = localHeats[1] ;
		if(yLoc + 1 < yBounds)
			heats[xLoc][yLoc + 1][zLoc] = localHeats[2] ;
		if(yLoc - 1 >= 0)
			heats[xLoc][yLoc - 1][zLoc] = localHeats[3] ;
		if(zLoc + 1 < zBounds)
			heats[xLoc][yLoc][zLoc + 1] = localHeats[4] ;
		if(zLoc - 1 >= 0)
			heats[xLoc][yLoc][zLoc - 1] = localHeats[5] ;
	}
	
	public double[] getSurroundingHeats(int xCoord, int yCoord, int zCoord){
		double[] toReturn = new double[6] ;
		
		if(xCoord + 1 < xBounds)
			toReturn[0] = heats[xCoord+1][yCoord][zCoord] ;
		else
			toReturn[0] = worldEdgeTemp[0] ;
		if(xCoord - 1 >= 0)
			toReturn[1] = heats[xCoord-1][yCoord][zCoord] ;
		else
			toReturn[1] = worldEdgeTemp[1] ;
		
		if(yCoord + 1 < yBounds)
			toReturn[2] = heats[xCoord][yCoord+1][zCoord] ;
		else
			toReturn[2] = worldEdgeTemp[2] ;
		if(yCoord - 1 >= 0)
			toReturn[3] = heats[xCoord][yCoord-1][zCoord] ;
		else
			toReturn[3] = worldEdgeTemp[3] ;	
		
		if(zCoord + 1 < zBounds)
			toReturn[4] = heats[xCoord][yCoord][zCoord+1] ;
		else
			toReturn[4] = worldEdgeTemp[4] ;
		if(zCoord - 1 >= 0)
			toReturn[5] = heats[xCoord][yCoord][zCoord-1] ;
		else
			toReturn[5] = worldEdgeTemp[5] ;
		
		return toReturn ;
	}
	
	public double[] getSurroundingIns(int xCoord, int yCoord, int zCoord){
		double[] toReturn = new double[6] ;
		
		if(xCoord + 1 < xBounds)
			toReturn[0] = ins[xCoord+1][yCoord][zCoord] ;
		else
			toReturn[0] = boundaryInsulationFactor ;
		if(xCoord - 1 >= 0)
			toReturn[1] = ins[xCoord-1][yCoord][zCoord] ;
		else
			toReturn[1] = boundaryInsulationFactor ;
		
		if(yCoord + 1 < yBounds)
			toReturn[2] = ins[xCoord][yCoord+1][zCoord] ;
		else
			toReturn[2] = boundaryInsulationFactor ;
		if(yCoord - 1 >= 0)
			toReturn[3] = ins[xCoord][yCoord-1][zCoord] ;
		else
			toReturn[3] = boundaryInsulationFactor ;	
		
		if(zCoord + 1 < zBounds)
			toReturn[4] = ins[xCoord][yCoord][zCoord+1] ;
		else
			toReturn[4] = boundaryInsulationFactor ;
		if(zCoord - 1 >= 0)
			toReturn[5] = ins[xCoord][yCoord][zCoord-1] ;
		else
			toReturn[5] = boundaryInsulationFactor ;
		
		return toReturn ;
	}
}
