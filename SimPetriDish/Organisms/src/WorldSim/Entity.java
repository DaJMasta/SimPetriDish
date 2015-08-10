/*
	Entity.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	The basic object that populates a world, supports an illumination model, a heat model, has stubs for actions called from the world.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class Entity {
	double heat = 0.0 ;
	double insulationFactor = 1.0 ;
	double translucence = 1.0 ;
	boolean bLightSource = false ;
	int lightEmission = 0 ;
	double lifetime = 0 ;
	int illumination = 0 ;
	double illuminationPerHeat = 230 ;
	EntityType entityType = EntityType.NOTHING ;
	int weight = 0 ;
	boolean bTrackLocation = false ;
	int xLocation = -1 ;
	int yLocation = -1 ;
	int zLocation = -1 ;
	
	Entity(){}
	
	Entity(double boundaryInsulationFactor, int heatFloor){
		entityType = EntityType.BOUNDARY ;
		heat = heatFloor ;
		insulationFactor = boundaryInsulationFactor ;
	}
	
	Entity(double newHeat){
		heat = newHeat ;
	}
	
	Entity(double setInsulation, double setTrans, boolean lightSource, int setLight, EntityType setType) {
		insulationFactor = setInsulation ;
		translucence = setTrans ;
		bLightSource = lightSource ;
		lightEmission = setLight ;
		entityType = setType ;
	}
	
	public void age(){
		lifetime++ ;
		heat += (illumination / illuminationPerHeat) * (1.0 - translucence) ;
	}
	
	public void addHeat(double toAdd){
		heat += toAdd ;
	}
	
	public void setTranslucence(double setTrans){
		if(setTrans >= 0.0)
			translucence = setTrans ;
	}
	
	public void setLighting(int setLight, boolean lightSource){
		if(setLight >= 0)
			lightEmission = setLight ;
		else
			lightEmission = 0 ;
		bLightSource = lightSource ;
	}
	
	public Entity newCopy(){
		return new Entity(insulationFactor, translucence, bLightSource, lightEmission, entityType) ;
	}
	
	public void updateLocation(int xLoc, int yLoc, int zLoc){
		xLocation = xLoc ;
		yLocation = yLoc ;
		zLocation = zLoc ;
	}
}
