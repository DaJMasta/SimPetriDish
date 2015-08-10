/*
	ActionType.class				Petri Dish Sim 1.0 release
	By: Jonathan Zepp - @DaJMasta
	
	An enum to keep track of actions organisms can perform.
	
	July 30, 2015
	August 9, 2015 - Updated for 1.1 release
	
	This code and program can be used according the the GPL v3 found here: http://www.gnu.org/licenses/gpl-3.0.en.html
	Free to modify and use in your own projects, but not for use in any commercial product.  If you use it in your project, I'd appreciate a credit as a source!
*/

package SimPetriDish;

public enum ActionType {
	NO_ACTION, IDLE, EAT, REPRODUCE, DIE, MOVE, SIGNAL, CREATED, ATTACK, PHOTOSYNTHESIS, CHEMOSYNTHESIS, REGENERATE, DETOX, TOXIFY, EMIT_TOXINS
}
