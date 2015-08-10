/*
	ChemicalSignal.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	Bundles information from a signal between AdvancedOrganisms - built in degradation in the repeating copy-constructor.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package WorldSim;

public class ChemicalSignal {

	int strength = 3 ;
	char message = ' ' ;
	int quantity = 0 ;
	String sender = "" ;
	int senderId = -1 ;
	int senderGeneration = -1 ;
	int degradeStrength = 1 ;
	
	ChemicalSignal(){}
	
	ChemicalSignal(char newMessage, int messageQty, int startStrength, int degradePerStep, String senderName, int id, int generation){
		message = newMessage ;
		quantity = messageQty ;
		strength = startStrength ;
		degradeStrength = degradePerStep ;
		sender = senderName ;
		senderId = id ;
		senderGeneration = generation ;
	}
	
	ChemicalSignal(ChemicalSignal toRepeat){											//Copy constructor for passing signals along, degradation included
		message = toRepeat.message ;
		quantity = toRepeat.quantity ;
		strength = toRepeat.strength ;
		degradeStrength = toRepeat.degradeStrength ;
		sender = toRepeat.sender ;
		senderId = toRepeat.senderId ;
		senderGeneration = toRepeat.senderGeneration ;
		
		strength -= degradeStrength ;
	}
	
	public boolean degrade(){															//For degrading over time without being passed on
		strength -= degradeStrength ;
		
		return strength > 0 ;
	}
}
