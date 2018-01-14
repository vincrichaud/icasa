package fr.esisar.cs511.followme.api;

import java.util.List;

import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;

public class BinaryLightActions {
	
	/**
	 * This method changes max binary light according to presence sensors sensed in the room 
	 * and the setting limitation
	 * @param presenceSensor
	 * 				is PresenceSensor in the room
	 * @param binaryLightList
	 * 				is list of all light in the room
	 * @param maxLightsToTurnOnPerRoom
	 * 				is max light to turn on allowed in the room
	 * @param lightOn
	 * 				is the number of light already turned on 
	 * @return the number of current light turned on
	 */
	public static int changeMaxBinaryLight(PresenceSensor presenceSensor,
			List<BinaryLight> binaryLightList, int maxLightsToTurnOnPerRoom, int lightOn) {
		
		int lightCounter = lightOn;
		System.out.println("NB LAMPE ON au debut de max binary: "+ lightCounter);
		for (BinaryLight binaryLight : binaryLightList) {
	        // switch light to on/off depending on the sensed presence
			lightCounter = changeOneBinaryLightUntilMax( presenceSensor, binaryLight, 
					maxLightsToTurnOnPerRoom, lightCounter);
		}

		System.out.println("NB LAMPE ON à la fin de max binary: "+ lightCounter);
		return lightCounter;
	}
	
	/**
	 * This method changes one binary light according to the number of light already 
	 * turned on and the max number allowed
	 * @param presenceSensor 
	 * 					is sensor in the same place than binary light
	 * @param binaryLight 
	 * 					is light to change
	 * @param maxLightsToTurnOnPerRoom 
	 * 					is max light allowed to be turn on
	 * @param lightCounter 
	 * 					is current light turned on number 
	 * @return the number of current light turned on
	 */
	public static int changeOneBinaryLightUntilMax(PresenceSensor presenceSensor, 
			BinaryLight binaryLight, int maxLightsToTurnOnPerRoom, int lightCounter) {
		  	   
		   if(presenceSensor.getSensedPresence()){
			  System.out.println("LAMPE BINARY N°" + lightCounter +"/"+ maxLightsToTurnOnPerRoom+" est à: "+ binaryLight.getPowerStatus());
	    	   if(lightCounter<maxLightsToTurnOnPerRoom) {
	    		   if(!binaryLight.getPowerStatus()) {
	    			   binaryLight.turnOn();
						System.out.println("changé à" +binaryLight.getPowerStatus());
	    			   lightCounter ++;
	    		   }  
	    	   }else if(lightCounter>maxLightsToTurnOnPerRoom){
	    		   if(binaryLight.getPowerStatus()) {
	    			   binaryLight.turnOff();
						System.out.println("changé à" +binaryLight.getPowerStatus());
	    			   lightCounter --;
	    		   }  
	    	   }
	       }else{
	           binaryLight.turnOff();
	       }
		   return lightCounter; 
	}
	
	/**
	 * This method counts the number of binary light turned on
	 * @param lightCounter 
	 * 					is the initial value of counter
	 * @param binaryLightList
	 * 					is the list of binary light to check
	 * @return the number of light turned on among binary light
	 */
	public static int checkNumberBinaryLightOn(int lightCounter, List<BinaryLight> binaryLightList ) {
		for (BinaryLight binaryLight : binaryLightList) {
			if (binaryLight.getPowerStatus()) {
				lightCounter++;
			}
		}	
		return lightCounter;
	}
	
}
