package fr.esisar.cs511.followme.manager.impl;

import fr.esisar.cs511.followme.service.spec.EnergyGoal;
import fr.esisar.cs511.followme.service.spec.FollowMeAdministration;
import fr.esisar.cs511.followme.service.spec.FollowMeConfiguration;
import fr.esisar.cs511.followme.service.spec.IlluminanceGoal;

import java.util.Map;

public class FollowMeManagerImpl implements FollowMeAdministration {

	/** Field for maxLightConfiguration dependency */
	private FollowMeConfiguration maxLightConfiguration;

	/** Bind Method for maxLightConfiguration dependency */
	public void binConfiguration(FollowMeConfiguration followMeConfiguration, Map properties) {
		// TODO: Add your implementation code here
	}
	/** Unbind Method for maxLightConfiguration dependency */
	public void unbindConfiguration(FollowMeConfiguration followMeConfiguration, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Component Lifecycle Method */
	public void stop() {
		// TODO: Add your implementation code here
		//System.out.println("followme configuration service stoping");
	}

	/** Component Lifecycle Method */
	public void start() {
		// TODO: Add your implementation code here
		System.out.println("component manager is starting ...");
	}

	/**
	 * This method returns the current max light allowed to be turn on
	 * @return
	 */
	private int askProviderCurrentMaxLight() {
		int lastValue = 0;
		lastValue = maxLightConfiguration.getMaximumNumberOfLightsToTurnOn();
		System.out.println("max lampe actuelle " + lastValue);
		return lastValue;
	}

	/**
	 * This method changes the current max light allowed to be turn on
	 * @param newValue
	 * 				is the new value of max light allowed to be turn on
	 */
	private void askProviderToChangeMaxLight(int newValue) {
		maxLightConfiguration.setMaximumNumberOfLightsToTurnOn(newValue);
		System.out.println( " valeur set");
	}

	/**
	 * This method returns the current target illuminance
	 * @return
	 */
	private double askProviderCurrentTargetIlluminance() {
		double CurrentValue = 0;
		CurrentValue = maxLightConfiguration.getTargetedIlluminance();
		System.out.println("max lampe actuelle " + CurrentValue);
		return CurrentValue;
	}

	/**
	 * This method changes the target illuminance value
	 * @param newValue
	 * 				is the new value of the target illuminance
	 */
	private void askProviderToChangeTargetIlluminance(double newValue) {
		maxLightConfiguration.setTargetedIlluminance(newValue);
		System.out.println( " valeur set");
	}

	/**
	 * This method returns the current max consumption value
	 * @return
	 */
	private double askProviderCurrentlyMaxConsumption() {
		double lastValue = 0;
		lastValue = maxLightConfiguration.getMaximumAllowedEnergyInRoom();
		System.out.println("max energie actuelle " + lastValue);
		return lastValue;
	}

	/**
	 * This method changes the current max consumption value
	 * @param newMaxValue
	 * 				is the new the max consumption value
	 */
	private void askProviderToChangeMaxConsumption(double newMaxValue) {
		maxLightConfiguration.setMaximumAllowedEnergyInRoom(newMaxValue);
		System.out.println(" valeur set");
	}

	@Override
	public void setIlluminancePreference(IlluminanceGoal illuminanceGoal) {
		askProviderToChangeMaxLight(illuminanceGoal.getNumberOfLightsToTurnOn());
		askProviderToChangeTargetIlluminance(illuminanceGoal.getTargetIlluminance());
	}

	@Override
	public IlluminanceGoal getIlluminancePreference() {
		IlluminanceGoal illuminanceGoalByInt = IlluminanceGoal.getIlluminanceGoalByInt(askProviderCurrentMaxLight());
		IlluminanceGoal illuminanceGoalByDouble = IlluminanceGoal.getIlluminanceGoalByDouble(askProviderCurrentTargetIlluminance());
		
		if(illuminanceGoalByDouble != illuminanceGoalByInt) {
			System.out.println("WARNING : illuminance goal is not the same by int by double");
		}
			
		if (illuminanceGoalByInt == null) {
			System.out.println("ERROR : illuminance goal is null");
		}
		
		return illuminanceGoalByInt;
	}

	@Override
	public void setEnergySavingGoal(EnergyGoal energyGoal) {
		askProviderToChangeMaxConsumption(energyGoal.getMaximumEnergyInRoom());
	}

	@Override
	public EnergyGoal getEnergyGoal() {
		EnergyGoal energyGoal = EnergyGoal.getEnergyGoalByDouble(askProviderCurrentlyMaxConsumption());
		if(energyGoal == null) {
			System.out.println("ERROR : energy goal is null");
		}
		return energyGoal;
	}

}
