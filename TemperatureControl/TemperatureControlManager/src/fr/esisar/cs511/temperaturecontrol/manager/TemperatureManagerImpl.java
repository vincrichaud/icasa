package fr.esisar.cs511.temperaturecontrol.manager;

import fr.esisar.cs511.temperaturecontrol.services.TemperatureAdministration;
import fr.esisar.cs511.temperaturecontrol.services.TemperatureConfiguration;

/**
 * This class is a component that uses the TemperatureConfiguration service
 * to implements and provide the TemperatureAdminitration Service
 * 
 * @author Vincent RICHAUD
 *
 *
 */
public class TemperatureManagerImpl implements TemperatureAdministration {

	/** Field for temperatureConfiguration dependency */
	private TemperatureConfiguration temperatureConfiguration;

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("_stop manager bundle");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("_Start Manager bundle");
		// TODO: Add your implementation code here
	}

	@Override
	public void temperatureIsTooHigh(String roomName) {
		float currentTargetedTemperature = temperatureConfiguration.getTargetedTemperature(roomName);
		float newTargetedTemperature = currentTargetedTemperature - 2;
		temperatureConfiguration.setTargetedTemperature(roomName, newTargetedTemperature);
	}

	@Override
	public void temperatureIsTooLow(String roomName) {
		float currentTargetedTemperature = temperatureConfiguration.getTargetedTemperature(roomName);
		float newTargetedTemperature = currentTargetedTemperature + 2;
		temperatureConfiguration.setTargetedTemperature(roomName, newTargetedTemperature);
	}

}
