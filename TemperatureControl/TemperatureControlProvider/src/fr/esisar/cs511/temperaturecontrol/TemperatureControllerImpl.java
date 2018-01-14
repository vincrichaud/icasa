/**
 * 
 */
package fr.esisar.cs511.temperaturecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fr.esisar.cs511.temperaturecontrol.services.TemperatureConfiguration;
import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.temperature.Cooler;
import fr.liglab.adele.icasa.device.temperature.Heater;
import fr.liglab.adele.icasa.device.temperature.Thermometer;
import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;

/**
 * This class is a component that implements (in order to provide) the TemperatureConfiguration Service
 * It also  implements and provides the PeriodicRunnable Service
 * 
 * @author Vincent RICHAUD
 *
 */
public class TemperatureControllerImpl implements PeriodicRunnable, TemperatureConfiguration {

	/**
	 * The name of the TEMPERATURE property
	 */
	public static final String TEMPERATURE_PROPERTY_NAME = "thermometer.currentTemperature";

	/**
	 * The name of the LOCATION property
	 */
	public static final String LOCATION_PROPERTY_NAME = "Location";

	/**
	 * The name of the location for unknown value
	 */
	public static final String LOCATION_UNKNOWN = "unknown";

	/**
	 * The name of the location for the kitchen
	 */
	public static final String LOCATION_KITCHEN = "kitchen";

	/**
	 * The name of the location for the livingroom
	 */
	public static final String LOCATION_LIVINGROOM = "livingroom";

	/**
	 * The name of the location for the bedroom
	 */
	public static final String LOCATION_BEDROOM = "bedroom";

	/**
	 * The name of the location for the bathroom
	 */
	public static final String LOCATION_BATHROOM = "bathroom";

	/**
	 * the threshold for temperature control 
	 */
	private static final Double TEMPERATURE_THERSHOLD = 0.5d;

	/**
	 * The default temperature requirement
	 */
	private static final Double TEMPERATURE_DEFAULT_REQUIREMENT = 295.15d;

	/** Field for Heaters dependency */
	private Heater[] heaters;
	/** Field for Thermometers dependency */
	private Thermometer[] thermometers;
	/** Field for Coolers dependency */
	private Cooler[] coolers;

	/**
	 * The different temperature requirement
	 */
	private Double tempReqKit = 290d;
	private Double tempReqLiv = 295d;
	private Double tempReqBed = 300d;
	private Double tempReqBat = 305d;

	/** Bind Method for Coolers dependency */
	public void bindCooler(Cooler cooler, Map properties) {
		System.out.println("Cooler " + cooler.getSerialNumber() + " bound");
	}

	/** Unbind Method for Coolers dependency */
	public void unbindCooler(Cooler cooler, Map properties) {
		System.out.println("Cooler " + cooler.getSerialNumber() + " unbound");
	}

	/** Bind Method for Heaters dependency */
	public void bindHeater(Heater heater, Map properties) {
		System.out.println("Heater " + heater.getSerialNumber() + " bind");
	}

	/** Unbind Method for Heaters dependency */
	public void unbindHeater(Heater heater, Map properties) {
		System.out.println("Heater " + heater.getSerialNumber() + " bind");
	}

	/** Bind Method for Thermometers dependency */
	public void bindThermometer(Thermometer thermometer, Map properties) {
		System.out.println("Thermometer " + thermometer.getSerialNumber() + " bind");
	}

	/** Unbind Method for Thermometers dependency */
	public void unbindThermometer(Thermometer thermometer, Map properties) {
		System.out.println("Thermometer " + thermometer.getSerialNumber() + " bind");
	}

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Component TemperatureControllerImpl stopped");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Component TemperatureControllerImpl started");
	}

	/**
	 * Get the list of coolers at the given location
	 * @param location
	 * @return
	 */
	private List<Cooler> getSameLocationCoolers(String location) {
		List<Cooler> sameLocationCoolers = new ArrayList<Cooler>();
		for (Cooler c : coolers) {
			if (c.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				sameLocationCoolers.add(c);
			}
		}
		return sameLocationCoolers;
	}

	/**
	 * Get the list of heaters at the given location
	 * @param location
	 * @return
	 */
	private List<Heater> getSameLlocationHeaters(String location) {
		List<Heater> sameLocationHeaters = new ArrayList<Heater>();
		for (Heater h : heaters) {
			if (h.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				sameLocationHeaters.add(h);
			}
		}
		return sameLocationHeaters;
	}

	/**
	 * Get the temperature requirement for the given location
	 * @param location
	 * @return
	 */
	private Double getTemperatureRequiredFromLocation(String location) {
		if (!location.equals(LOCATION_UNKNOWN)) {
			switch (location) {
			case LOCATION_KITCHEN:
				return tempReqKit;
			case LOCATION_LIVINGROOM:
				return tempReqLiv;
			case LOCATION_BATHROOM:
				return tempReqBat;
			case LOCATION_BEDROOM:
				return tempReqBed;
			default:
				return TEMPERATURE_DEFAULT_REQUIREMENT;
			}
		} else {
			return TEMPERATURE_DEFAULT_REQUIREMENT;
		}
	}

	
	@Override
	public void run() {
		//Check every thermometers
		for (Thermometer t : thermometers) {
			//for each get the location
			String location = t.getPropertyValue(LOCATION_PROPERTY_NAME).toString();
			//if the location is known
			if (!location.equals(LOCATION_UNKNOWN)) {
				//get the current temperature, and the one required
				Double currentTemperature = t.getTemperature();
				Double temperatureRequired = getTemperatureRequiredFromLocation(location);
				//get the devices at the thermometer's location
				List<Cooler> sameLocationCoolers = getSameLocationCoolers(location);
				List<Heater> sameLocationHeaters = getSameLlocationHeaters(location);
				//if too cold
				if (currentTemperature < (temperatureRequired - TEMPERATURE_THERSHOLD)) {
					//stop coolers
					for (Cooler c : sameLocationCoolers) {
						c.setPowerLevel(0);
					}
					//start heaters
					for (Heater h : sameLocationHeaters) {
						h.setPowerLevel(1);
					}
					//if too hot
				} else if (currentTemperature > (temperatureRequired + TEMPERATURE_THERSHOLD)) {
					//start coolers
					for (Cooler c : sameLocationCoolers) {
						c.setPowerLevel(1);
					}
					//stop heaters
					for (Heater h : sameLocationHeaters) {
						h.setPowerLevel(0);
					}
					//if correct
				} else {
					//stop all devices
					for (Cooler c : sameLocationCoolers) {
						c.setPowerLevel(0);
					}
					for (Heater h : sameLocationHeaters) {
						h.setPowerLevel(0);
					}
				}

			}
		}

	}

	/**
	 * Get the period between to call of the method run()
	 */
	@Override
	public long getPeriod() {
		return 10;
	}

	/**
	 * Get the Unit of the period given by getPeriod
	 */
	@Override
	public TimeUnit getUnit() {
		return TimeUnit.SECONDS;
	}


	@Override
	public void setTargetedTemperature(String targetedRoom, float temperature) {
		switch(targetedRoom) {
		case LOCATION_KITCHEN:
			tempReqKit = Double.valueOf(temperature);
			break;
		case LOCATION_LIVINGROOM:
			tempReqLiv = Double.valueOf(temperature);
			break;
		case LOCATION_BATHROOM:
			tempReqBat = Double.valueOf(temperature);
			break;
		case LOCATION_BEDROOM:
			tempReqBed = Double.valueOf(temperature);
			break;
		default:
			System.out.println("_unknown room " + targetedRoom + " temperature not set");
			return;
		}
		System.out.println("_new targeted temperature " + temperature + " in room " + targetedRoom);
	}

	@Override
	public float getTargetedTemperature(String room) {
		Double ret = TEMPERATURE_DEFAULT_REQUIREMENT;
		switch (room) {
		case LOCATION_KITCHEN:
			ret = tempReqKit;
			break;
		case LOCATION_LIVINGROOM:
			ret = tempReqLiv;
			break;
		case LOCATION_BATHROOM:
			ret = tempReqBat;
			break;
		case LOCATION_BEDROOM:
			ret = tempReqBed;
			break;
		}
		return Float.valueOf(ret.toString());
	}
}
