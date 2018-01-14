package fr.esisar.cs511.followme.impl;

import fr.esisar.cs511.followme.api.BinaryLightActions;
import fr.esisar.cs511.followme.api.DimmerLightActions;
import fr.esisar.cs511.followme.service.spec.FollowMeConfiguration;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import fr.liglab.adele.icasa.device.light.DimmerLight;
import fr.liglab.adele.icasa.device.light.Photometer;
import fr.liglab.adele.icasa.service.zone.size.calculator.ZoneSizeCalculator;

public class BinaryFollowMeImpl implements DeviceListener, FollowMeConfiguration {

	/** Field for binaryLights dependency */
	private BinaryLight[] binaryLights;
	/** Field for presenceSensors dependency */
	private PresenceSensor[] presenceSensors;
	/** Field for dimmerLight dependency */
	private DimmerLight[] dimmerLights;
	/** Field for photoMeters dependency */
	private Photometer[] photoMeters;
	/** Field for zoneSizeCalculator dependency */
	private ZoneSizeCalculator zoneSizeCalculator;

	/**
	 * The name of the LOCATION property
	 */
	public static final String LOCATION_PROPERTY_NAME = "Location";

	/**
	 * The name of the location for unknown value
	 */
	public static final String LOCATION_UNKNOWN = "unknown";

	/**
	 * The power consumption of one binary light
	 */
	public static final double ONE_LIGHT_POWER_CONSUMPTION = 100d;

	/**
	 * Watt to lumens conversion factor
	 * It has been considered that: 1 Watt=680.0 lumens at 555nm.
	 */
	public final static double ONE_WATT_TO_ONE_LUMEN = 680.0d;

	/** 
	 * The maximum energy consumption allowed in a room in Watt 
	 * */
	private double maximumEnergyConsumptionAllowedInARoom = 100.0d;

	/** 
	 * The maximum number of lights to turn on when a user enters the room 
	 */
	private int maxLightsToTurnOnPerRoom = 1;

	/** 
	 * The number of power that rest when allowed lights maximum are on at 100% 
	 */
	private double maxLightsToTurnOnRest = 0d;

	/** 
	 * The maximum number of lights to turn on when the user comes in the room to target 
	 * illuminance settings
	 * It is limited by maxLightsToTurnOnPerRoom
	 */
	private int maxLightsToTurnOnToTarget = 1;

	/** 
	 * The number of power that rest to target illuminance setting when allowed lights 
	 * maximum are on at 100% 
	 */
	private double maxLightsToTurnOnRestToTarget = 0d;

	/** 
	 * The targeted illuminance in each room 
	 */
	private double targetedIlluminancePerRoom = 4000.0d;



//-----------------------------------------BIND / UNBIND METHOD---------------------------------
	/**
	 * Bind Method for binaryLights dependency.
	 * This method will be used to manage device listener.
	 */
	public void bindBinaryLight(BinaryLight binaryLight, Map properties) {
		System.out.println("bind binary light " + binaryLight.getSerialNumber());
		binaryLight.addListener(this); //..
	}

	/** 
	 * Unbind Method for binaryLights dependency 
	 * This method will be used to manage device listener.
	 */
	public void unbindBinaryLight(BinaryLight binaryLight, Map properties) {
		System.out.println("unbind binary light " + binaryLight.getSerialNumber());
		binaryLight.removeListener(this);
	}

	/** 
	 * Bind Method for PresenceSensor dependency 
	 * This method will be used to manage device listener.
	 */
	public synchronized void bindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("bind presence sensor " + presenceSensor.getSerialNumber());
		presenceSensor.addListener(this); //..
	}

	/** 
	 * Unbind Method for PresenceSensor dependency 
	 * This method will be used to manage device listener.
	 */
	public void unbindPresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("unbind presence sensor " + presenceSensor.getSerialNumber());
		presenceSensor.removeListener(this); //..
	}

	/** 
	 * Bind Method for dimmerLight dependency
	 * This method will be used to manage device listener.
	 */
	public void bindDimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("bind dimmer light " + dimmerLight.getSerialNumber());
		dimmerLight.addListener(this);
	}

	/** 
	 * Unbind Method for dimmerLight dependency
	 * This method will be used to manage device listener.
	 */
	public void unbindDimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("unbind dimmer light " + dimmerLight.getSerialNumber());
		dimmerLight.removeListener(this); //..
	}

	/** 
	 * Bind Method for photoMeters dependency
	 * This method will be used to manage device listener.
	 */
	public void bindPhotoMeter(Photometer photometer, Map properties) {
		System.out.println("bind photometer " + photometer.getSerialNumber());
		photometer.addListener(this);
	}

	/** 
	 * Unbind Method for photoMeters dependency
	 * This method will be used to manage device listener.
	 */
	public void unbindPhotoMeter(Photometer photometer, Map properties) {
		System.out.println("bind photometer " + photometer.getSerialNumber());
		photometer.removeListener(this);
	}




//--------------------------------LIFECYCLE METHOD-------------------------------
	/** 
	 * Component Lifecycle Method 
	 * This method will be used to manage device listener.
	 */
	public synchronized void stop() {
		System.out.println("component is stopping ...");
		for (PresenceSensor sensor : presenceSensors) {
			sensor.removeListener(this);
		}
	}

	/** 
	 * Component Lifecycle Method 
	 * This method begins the component 
	 */
	public void start() {
		System.out.println("component binary is starting ...");
	}
 
	@Override
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * This method is part of the DeviceListener interface and is called when a
	 * subscribed device property is modified.
	 * 
	 * @param device
	 *            is the device whose property has been modified.
	 * @param propertyName
	 *            is the name of the modified property.
	 * @param oldValue
	 *            is the old value of the property
	 * @param newValue
	 *            is the new value of the property
	 */
	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, 
			Object oldValue, Object newValue) {
		//PRESENCESENSOR
		//check type device modified is prensenceSensor
		if (device instanceof PresenceSensor) {
			System.out.println("le device changé est un CAPTEUR");
			PresenceSensor changingSensor = (PresenceSensor) device;

			// check the change is related to presence sensing
			if (propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {
				// get the location of the changing sensor:
				String detectorLocation = (String) changingSensor.getPropertyValue(LOCATION_PROPERTY_NAME);
				//System.out.println("This sensor is in the room :" + detectorLocation);

				if (!detectorLocation.equals(LOCATION_UNKNOWN)) {
					updateLightByLocation(detectorLocation);
				}
			}else if (propertyName.equals(LOCATION_PROPERTY_NAME)) {
				System.out.println("le device changé est un CAPTEUR");
				String newValueString = (String) newValue;
				String oldValueString = (String) oldValue;
				//System.out.println("This lamp is now in the room :" + newValue);

				if (!newValueString.equals(LOCATION_UNKNOWN)) {
					updateLightByLocation(newValueString);
					if(!oldValueString.equals(LOCATION_UNKNOWN)) {
						updateLightByLocation(oldValueString);
					}
				}
			}
		}

		//BINARYLIGHT
		//check type device modified is binarylight
		if (device instanceof BinaryLight) {
			BinaryLight changingLight = (BinaryLight) device;

			// check the change is related to lamp location changing
			if (propertyName.equals(LOCATION_PROPERTY_NAME)) {
				System.out.println("le device changé est une LAMPE");
				String newValueString = (String) newValue;
				String oldValueString = (String) oldValue;
				//System.out.println("This lamp is now in the room :" + newValue);

				if (!newValueString.equals(LOCATION_UNKNOWN)) {
					updateLightByLocation(newValueString);
					if(!oldValueString.equals(LOCATION_UNKNOWN)) {
						updateLightByLocation(oldValueString);
					}
				}else {
					changingLight.turnOff();
				}

			}
		}

		//DIMMERLIGHT
		//check type device modified is dimmerlight
		if (device instanceof DimmerLight) {

			DimmerLight changingLight = (DimmerLight) device;

			// check the change is related to lamp location changing
			if (propertyName.equals(LOCATION_PROPERTY_NAME)) {
				System.out.println("le device changé est une LAMPE A VARIATION");
				String newValueString = (String) newValue;
				String oldValueString = (String) oldValue;
				//System.out.println("This lamp is now in the room :" + newValue);

				if (!newValueString.equals(LOCATION_UNKNOWN)) {
					updateLightByLocation(newValueString);
					if(!oldValueString.equals(LOCATION_UNKNOWN)) {
						updateLightByLocation(oldValueString);
					}
				}else {
					changingLight.setPowerLevel(DimmerLightActions.OFF);
				}
			}
		}

	}


//------------------------------------ACTIONS METHOD-------------------------------
	/**
	 * This method actualize lights according to the situation in the room 
	 * @param location
	 *            : the given location
	 */
	private synchronized void updateLightByLocation(String location) {
		turnOffAllLightsOfLocation(location);
		//System.out.println("UPDATE NEW BINARY");
		//Parametre
		int lightCounter = checkMaxLightTurnedOn(location);
		PresenceSensor presenceSensor = getPresenceSensorFromLocation(location).get(0);
		List<BinaryLight> binaryLights = getBinaryLightFromLocation(location);
		List<DimmerLight> dimmerLights = getDimmerLightFromLocation(location);
		//change light state

		if(presenceSensor != null) {
			//update by maxLightsToTurnOnPerRoom or max Energy consumption
			lightCounter = BinaryLightActions.changeMaxBinaryLight(presenceSensor, binaryLights,
					maxLightsToTurnOnPerRoom, lightCounter);
			lightCounter = DimmerLightActions.changeMaxDimmerLight(presenceSensor, dimmerLights, 
					maxLightsToTurnOnPerRoom, maxLightsToTurnOnRest, lightCounter);
			 
//			//update by target lluminance
//			if(targetedIlluminancePerRoom != getPhotoMeterFromLocation(location).get(0).getIlluminance()) {
//				System.out.println("luminosité actuelle :" +  getPhotoMeterFromLocation(location).get(0).getIlluminance());
//				calculateParameterToTarget(location);
//				lightCounter = BinaryLightActions.changeMaxBinaryLight(presenceSensor, binaryLights, 
//						maxLightsToTurnOnToTarget, lightCounter);
//				lightCounter = DimmerLightActions.changeMaxDimmerLight(presenceSensor, dimmerLights, 
//						maxLightsToTurnOnToTarget, maxLightsToTurnOnRestToTarget, lightCounter);
//				System.out.println("luminosité apres traitement :" + getPhotoMeterFromLocation(location).get(0).getIlluminance());
//			}
		}else {
			turnOffAllLightsOfLocation(location);
		}
	}

	/**
	 * This method counts all light turn on in the room 
	 * @param location
	 *            : the given location
	 * @return light turn on number
	 */
	private synchronized int checkMaxLightTurnedOn(String location) {
		int lightCounter = 0;
		//System.out.println("on compte les lampes à ON init : " + lightCounter);
		//among binarylight
		List<BinaryLight> sameLocationLigths = getBinaryLightFromLocation(location);
		lightCounter = BinaryLightActions.checkNumberBinaryLightOn(lightCounter, sameLocationLigths);
		//System.out.println("on compte les lampes ON parmi biniary : " + lightCounter);
		//among dimmerlight (added to binary light)
		List<DimmerLight> sameLocationLights = getDimmerLightFromLocation(location);
		lightCounter = DimmerLightActions.checkNumberDimmerLightOn(lightCounter, sameLocationLights);
		//System.out.println("on ajoute les lampes à ON parmi dimmer : " + lightCounter);
		return lightCounter;
	}
	
	/**
	 * This method turn off all light in the room
	 * @param location 
	 *            : the given location
	 */
	private synchronized void turnOffAllLightsOfLocation(String location) {
		for (BinaryLight bin : binaryLights) {
			if (bin.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				bin.turnOff();
			}
		}
		for (DimmerLight dim : dimmerLights) {
			if (dim.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				dim.setPowerLevel(DimmerLightActions.getOFF());
			}
		}
	}

	
	
//------------------------------------GET METHOD---------------------------------------------
	/**
	 * This method return all BinaryLight from the given location
	 * 
	 * @param location
	 *            : the given location
	 * @return list of matching BinaryLights
	 */
	private synchronized List<BinaryLight> getBinaryLightFromLocation(String location) {
		List<BinaryLight> binaryLightsLocation = new ArrayList<BinaryLight>();
		for (BinaryLight binLight : binaryLights) {
			if (binLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				binaryLightsLocation.add(binLight);
			}
		}
		return binaryLightsLocation;
	}

	/**
	 * This method return all DimmerLight from the given location
	 * 
	 * @param location
	 *            : the given location
	 * @return list of matching DimmerLight
	 */
	private synchronized List<DimmerLight> getDimmerLightFromLocation(String location) {
		List<DimmerLight> dimmerLightsLocation = new ArrayList<DimmerLight>();
		for (DimmerLight dimmerLight : dimmerLights) {
			if (dimmerLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				dimmerLightsLocation.add(dimmerLight);
			}
		}
		return dimmerLightsLocation;
	}

	/**
	 * This method return all PresenceSensors from the given location
	 * 
	 * @param location
	 *            : the given location
	 * @return list of matching PresenceSensors
	 */
	private synchronized List<PresenceSensor> getPresenceSensorFromLocation(String location) {
		List<PresenceSensor> presenceSensorsLocation = new ArrayList<PresenceSensor>();
		for (PresenceSensor presSensor : presenceSensors) {
			if (presSensor.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				presenceSensorsLocation.add(presSensor);
			}
		}
		return presenceSensorsLocation;
	}

	/**
	 * This method return all PhotoMeters from the given location
	 * 
	 * @param location
	 *            : the given location
	 * @return list of matching PhotoMeters 
	 */
	private synchronized List<Photometer> getPhotoMeterFromLocation(String location){
		List<Photometer> photometerLocation = new ArrayList<Photometer>();
		for (Photometer photometer : photoMeters) {
			if (photometer.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				photometerLocation.add(photometer);
			}
		}
		return photometerLocation;
	}

	/**
	 * This method return the surface of the given location
	 * 
	 * @param location
	 *            : the given location
	 * @return surface of location in square meters
	 */
	private synchronized double getSurfaceFromLocation(String location) {
		double area = zoneSizeCalculator.getSurfaceInMeterSquare(location);
		System.out.println("surface de la piece : " + area);
		return area;
	}

//------------------------------------SERVICE PROVIDED METHOD ---------------------------------
	
	@Override
	public int getMaximumNumberOfLightsToTurnOn() {
		System.out.println("Max lampe actuel" + maxLightsToTurnOnPerRoom);
		return maxLightsToTurnOnPerRoom;
	}

	@Override
	public void setMaximumNumberOfLightsToTurnOn(int maximumNumberOfLightsToTurnOn) {
		maxLightsToTurnOnPerRoom = maximumNumberOfLightsToTurnOn;
	}

	@Override
	public double getMaximumAllowedEnergyInRoom() {
		System.out.println("Max consomation actuel : " + maximumEnergyConsumptionAllowedInARoom);
		System.out.println("Max lampe actuel : " + maxLightsToTurnOnPerRoom);
		return maximumEnergyConsumptionAllowedInARoom;
	}

	@Override
	public void setMaximumAllowedEnergyInRoom(double maximumEnergy) {
		//Calculate new values with maxmimEnergy
		maximumEnergyConsumptionAllowedInARoom = maximumEnergy;
		maxLightsToTurnOnPerRoom = (int) (maximumEnergyConsumptionAllowedInARoom / ONE_LIGHT_POWER_CONSUMPTION);
		maxLightsToTurnOnRest = (maximumEnergyConsumptionAllowedInARoom % ONE_LIGHT_POWER_CONSUMPTION) / 100;
		System.out.println("Max cosomation nouveau : " + maximumEnergyConsumptionAllowedInARoom);
		System.out.println("Max Lampe nouveau :" + maxLightsToTurnOnPerRoom);
		System.out.println("Valeur reste lampe  :" + maxLightsToTurnOnRest);

		for (PresenceSensor sensor : presenceSensors) {
			if (sensor.getSensedPresence()) {
				
				turnOffAllLightsOfLocation(sensor.getPropertyValue(LOCATION_PROPERTY_NAME).toString());
				
				//parameters
				String location = sensor.getPropertyValue(LOCATION_PROPERTY_NAME).toString();
				List<BinaryLight> sameLocationBinatyLigths = getBinaryLightFromLocation(location);
				List<DimmerLight> sameLocationDimmerLigths = getDimmerLightFromLocation(location);
				int lightCounter = checkMaxLightTurnedOn(location);
				
				//change state light
				lightCounter = BinaryLightActions.changeMaxBinaryLight(sensor, sameLocationBinatyLigths,
						maxLightsToTurnOnPerRoom, lightCounter);
				lightCounter = DimmerLightActions.changeMaxDimmerLight(sensor, sameLocationDimmerLigths,
						maxLightsToTurnOnPerRoom, maxLightsToTurnOnPerRoom, lightCounter);
			}
		}
	}

	@Override
	public double getTargetedIlluminance() {
		System.out.println("Luminosité ciblée actuelle : " + targetedIlluminancePerRoom);
		System.out.println("Max lampe actuel : " + maxLightsToTurnOnPerRoom);
		return targetedIlluminancePerRoom;
	}

	@Override
	public void setTargetedIlluminance(double illuminance) {
		targetedIlluminancePerRoom = illuminance;
		for (PresenceSensor sensor : presenceSensors) {
			if (sensor.getSensedPresence()) {
				//parameters
				String location = sensor.getPropertyValue(LOCATION_PROPERTY_NAME).toString();
				List<BinaryLight> sameLocationBinatyLigths = getBinaryLightFromLocation(location);
				List<DimmerLight> sameLocationDimmerLigths = getDimmerLightFromLocation(location);
				int lightCounter = checkMaxLightTurnedOn(location);
				
				//Calculate new values with illuminance
				calculateParameterToTarget(location);
				
				//change state light
				lightCounter = BinaryLightActions.changeMaxBinaryLight(sensor, sameLocationBinatyLigths,
						maxLightsToTurnOnToTarget, lightCounter);
				lightCounter = DimmerLightActions.changeMaxDimmerLight(sensor, sameLocationDimmerLigths,
						maxLightsToTurnOnToTarget, maxLightsToTurnOnRestToTarget, lightCounter);
			}
		}
	}

	/**
	 * This method calculate parameters to set to target illuminance
	 * @param location
	 * 				: the given location
	 */
	public synchronized void calculateParameterToTarget(String location) {
		//Calculate the sum of all light configuration (1 binary = 1, 1 dimmer € [0;1])
		double sumLightConfiguration = (targetedIlluminancePerRoom * getSurfaceFromLocation(location))
				/ (ONE_WATT_TO_ONE_LUMEN * ONE_LIGHT_POWER_CONSUMPTION);
		//calculate the number on light to turn on to their maximum
		maxLightsToTurnOnToTarget = (int) (sumLightConfiguration / (ONE_LIGHT_POWER_CONSUMPTION/100));
		//calculate the rest of configuration €]0;1[
		maxLightsToTurnOnRestToTarget = sumLightConfiguration % (ONE_LIGHT_POWER_CONSUMPTION/100);
		System.out.println("target illuminence : "+ targetedIlluminancePerRoom);
		System.out.println("sum of luminosity : " + sumLightConfiguration);
		System.out.println("max light to turn on to target : " + maxLightsToTurnOnToTarget);
		System.out.println("rest to target : " + maxLightsToTurnOnRestToTarget);

		//Limited illuminance by allowed parameters
		if (maxLightsToTurnOnPerRoom <= maxLightsToTurnOnToTarget) {
			//light number to turn on is limited by max lights to turn on allowed
			maxLightsToTurnOnToTarget = maxLightsToTurnOnPerRoom;
			System.out.println("max light to turn on to target : " + maxLightsToTurnOnToTarget);
			if(maxLightsToTurnOnRest <= maxLightsToTurnOnRestToTarget) {
				//light rest to turn on is limited by rest lights to turn on allowed
				maxLightsToTurnOnRestToTarget = maxLightsToTurnOnRest;
				System.out.println("rest to target : " + maxLightsToTurnOnRestToTarget);
			}
		}
		
	}

}
