package fr.esisar.cs511.followme.commande.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;

import fr.esisar.cs511.followme.service.spec.EnergyGoal;
import fr.esisar.cs511.followme.service.spec.FollowMeAdministration;
import fr.esisar.cs511.followme.service.spec.IlluminanceGoal;
import fr.liglab.adele.icasa.command.handler.Command;
import fr.liglab.adele.icasa.command.handler.CommandProvider;


//Define this class as an implementation of a component :
@Component
//Create an instance of the component
@Instantiate(name = "followme.commande.impl")
//Use the handler command and declare the command as a command provider. The
//namespace is used to prevent name collision.
@CommandProvider(namespace = "followme")
public class FollowMeCommandImpl {

	// Declare a dependency to a FollowMeAdministration service
    @Requires
	private FollowMeAdministration administrationService;
	
	/** Component Lifecycle Method */
	@Invalidate
    public void stop() {
	}

	/** Component Lifecycle Method */
	@Validate
	public void start() {
		System.out.println("component command is starting ...");
	}
	
	private IlluminanceGoal askProviderCurrentlyIlluminanceGoal() {
		return administrationService.getIlluminancePreference();
	}

	private void askProviderToChangeIlluminanceGoal(IlluminanceGoal illuminanceGoal) {
		administrationService.setIlluminancePreference(illuminanceGoal);
	}
	
	private EnergyGoal askProviderCurrentlyEnergyGoal() {
		return administrationService.getEnergyGoal(); 
	}
	
	private void askProviderToChangeEnergyGoal(EnergyGoal energyGoal) {
		administrationService.setEnergySavingGoal(energyGoal);
	}
	
	
	 /**
     * Felix shell command implementation to sets the illuminance preference.
     *
     * @param goal the new illuminance preference ("SOFT", "MEDIUM", "FULL")
     */
 
    // Each command should start with a @Command annotation
	//setIlluminancePreference FULL
	//setIlluminancePreference SOFT
    @Command
    public void setIlluminancePreference(String goal) {
    	IlluminanceGoal illuminanceGoal = IlluminanceGoal.getIlluminanceGoalByName(goal);
        if(illuminanceGoal == null) {
        	System.out.println("ERREUR cette config n'existe pas : " + goal);
        }else {
            askProviderToChangeIlluminanceGoal(illuminanceGoal);
        }
    }
    

   // setEnergyPreference HIGH 
    //setEnergyPreference LOW
    @Command 
    public void setEnergyPreference(String goal) {
    	EnergyGoal energyGoal = EnergyGoal.getIEnergyGoallByName(goal);
    	if(energyGoal == null) {
    		System.out.println("ERREUR cette config n'existe pas : " + goal);
    	}else {
    		askProviderToChangeEnergyGoal(energyGoal);
    	}
    }
 
    //getIlluminancePreference
    @Command
    public void getIlluminancePreference(){
    	
        System.out.println("The illuminance goal is " + askProviderCurrentlyIlluminanceGoal().toString());
    }
    
    //getEnergyPreference
    @Command
    public void getEnergyPreference(){
        System.out.println("The illuminance goal is " + askProviderCurrentlyEnergyGoal().toString());
    }

}
