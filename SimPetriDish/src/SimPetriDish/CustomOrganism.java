/*
	CustomOrganism.class				Petri Dish Sim 1.1 release
	By: Jonathan Zepp - @DaJMasta
	
	The basics for a customizable organism, but not implemented in the 1.1 release beyond this stub.

	August 9, 2015
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

import java.awt.Color;

public class CustomOrganism extends AdvancedOrganism {
	
	Color displayColor ;
	
	CustomOrganism(){
		super() ;
		name = "CUSTOM" ;
		displayColor = new Color(200, 220, 255) ;
	}
	
	CustomOrganism(int xLoc, int yLoc, int zLoc, CustomOrganism parentOrganism){
		super(xLoc, yLoc, zLoc, parentOrganism) ;
		displayColor = parentOrganism.displayColor ;
	}
	
	public CustomOrganism newCopy(){														//Important for SpawnRandomizer
		return new CustomOrganism(xLocation, yLocation, zLocation, this) ;
	}
}
