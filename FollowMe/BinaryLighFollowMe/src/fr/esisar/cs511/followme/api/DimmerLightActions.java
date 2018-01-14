package fr.esisar.cs511.followme.api;

import java.util.List;

import fr.liglab.adele.icasa.device.light.DimmerLight;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;

public class DimmerLightActions {

	/** value for dimmer light **/
	public static double ON = 1;
	public static double OFF = 0;
	public static boolean isRest = true;

	public static boolean getIsRest() {
		return isRest;
	}

	public static void setisRest(boolean isRest) {
		DimmerLightActions.isRest = isRest;
	}

	/**
	 * This method changes max dimmer light according to presence sensors sensed in the room 
	 * and the setting limitation
	 * @param presenceSensor
	 * 				is PresenceSensor in the room
	 * @param dimmerLightList
	 * 				is list of all light in the room
	 * @param maxLightsToTurnOnPerRoom
	 * 				is max light to turn on allowed in the room
	 * @param maxLightToTurnOnRest
	 * 				is rest value of light to turn on
	 * @param lightOn
	 * 				is the number of light already turned on 				
	 * @return the number of current light turned on
	 */
	public static int changeMaxDimmerLight(PresenceSensor presenceSensor, 
			List<DimmerLight> dimmerLightList, int maxLightsToTurnOnPerRoom,double maxLightToTurnOnRest,
			int lightOn) {
		int lightCounter = lightOn;
		isRest = true;
		System.out.println("NB LAMPE ON au debut de max dimmer: "+ lightCounter);
		
		for (DimmerLight dimmerLight : dimmerLightList) {
			// and switch them on/off depending on the sensed presence
			lightCounter = changeOneDimmerLightUntilMax( presenceSensor, dimmerLight, 
					maxLightsToTurnOnPerRoom, maxLightToTurnOnRest, lightCounter);
		}

		System.out.println("NB LAMPE ON à la fin de max dimmer: "+ lightCounter);
		return lightCounter;
	}

	
	/**
	 * This method changes one dimmer light according to the number of light already 
	 * turned on and the max number allowed
	 * @param presenceSensor
	 * 					is PresenceSensor in the room
	 * @param dimmerLight
	 * 					is the light to change 
	 * @param maxLightsToTurnOnPerRoom
	 * 					is max light allowed to be turn on
	 * @param maxLightToTurnOnRest
	 * 					is rest value of light to turn on
	 * @param lightCounter
	 * 					is the number of light already turned on 	
	 * @return
	 */
	public static int changeOneDimmerLightUntilMax(PresenceSensor presenceSensor, 
			DimmerLight dimmerLight,  int maxLightsToTurnOnPerRoom, double maxLightToTurnOnRest, 
			int lightCounter) {
		if (presenceSensor.getSensedPresence()) {
			System.out.println("LAMPE DIMMER N°" + lightCounter+"/"+maxLightsToTurnOnPerRoom+
					" est à : "+ dimmerLight.getPowerLevel());
			if (lightCounter < maxLightsToTurnOnPerRoom) {
				if(dimmerLight.getPowerLevel() == OFF) {
					dimmerLight.setPowerLevel(ON);
					System.out.println("changé à" +dimmerLight.getPowerLevel());
					lightCounter ++;
				}
			}else if(lightCounter > maxLightsToTurnOnPerRoom) {
				if(dimmerLight.getPowerLevel() == ON) {
					dimmerLight.setPowerLevel(OFF);
					System.out.println("changé à" +dimmerLight.getPowerLevel());
					lightCounter --;
				}
			}else {
				if (isRest) {
					System.out.println("LAMPE DIMMER EGAAAAAAAAAAAAAAAAAAL");
					dimmerLight.setPowerLevel(maxLightToTurnOnRest);
					System.out.println("changé à" +dimmerLight.getPowerLevel());
					isRest = false;
				}
			}
		} else {
			dimmerLight.setPowerLevel(OFF);
		}
		return lightCounter;
	}

	/**
	 * This method counts the number of dimmer light turned on
	 * @param lightCounter 
	 * 					is the initial value of counter
	 * @param dimmerLightList
	 * 					is the list of binary light to check
	 * @return the number of light turned on among binary light
	 */
	public static int checkNumberDimmerLightOn(int lightCounter, List<DimmerLight> dimmerLightList) {
		for (DimmerLight dimmerlight : dimmerLightList) {
			if(dimmerlight.getPowerLevel() != 0) {
				lightCounter++;
			}
		}
		return lightCounter;
	}
	
	/**
	 * This method return the ON value
	 * @return
	 */
	public static double getON() {
		return ON;
	}

	/**
	 * This method changes the ON value 
	 * @param oN
	 */
	public static void setON(double oN) {
		ON = oN;
	}

	/**
	 * This method return OFF value
	 * @return
	 */
	public static double getOFF() {
		return OFF;
	}

	/**
	 * This method changes the OFF value
	 * @param oFF
	 */
	public static void setOFF(double oFF) {
		OFF = oFF;
	}

}
