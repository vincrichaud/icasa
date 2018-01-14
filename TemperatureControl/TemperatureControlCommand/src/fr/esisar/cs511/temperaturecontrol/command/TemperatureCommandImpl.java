package fr.esisar.cs511.temperaturecontrol.command;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import fr.esisar.cs511.temperaturecontrol.services.TemperatureAdministration;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;

/**
 * This class is a component that uses the TemperatureAdministration service.
 * It implments and provides a command service thet allow user to regulate the temperature 
 * by calling two commands : tooLow and tooHigh.
 * 
 * @author Vincent RICHAUD
 *
 */
@Component
@Instantiate(name = "temperature.administration.command")
@CommandProvider(namespace = "temperature")
public class TemperatureCommandImpl {

	/** Field for temperatureAdministration dependency */
	@Requires
	private TemperatureAdministration temperatureAdministration;
	
	@Command
	public void tooHigh(String room) {
		temperatureAdministration.temperatureIsTooHigh(room);
	}
	
	@Command
	public void tooLow(String room) {
		temperatureAdministration.temperatureIsTooLow(room);
	}

	/** Component Lifecycle Method */
	@Invalidate
	public void stop() {
		System.out.println("_stop command bunlde");
	}

	/** Component Lifecycle Method */
	@Validate
	public void start() {
		System.out.println("_Start command bundle");
	}

}
