package fr.esisar.cs511.followme.service.spec;

/**
 * This enum describes the different illuminance goals associated with the
 * manager.
 */
public enum IlluminanceGoal {

	/** The goal associated with soft illuminance. */
    SOFT(1, 500d),
    /** The goal associated with medium illuminance. */
    MEDIUM(2, 2750d),
    /** The goal associated with full illuminance. */
    FULL(3, 4000d),
    
    //added for test
	/** The goal associated with fsoft illuminance. */
	FSOFT(1, 10000d),
	/** The goal associated with fmedium illuminance. */
	FMEDIUM(2, 12160d),
	/** The goal associated with ffull illuminance. */
	FFULL(3, 50000d);
	
	/** The number of lights to turn on. */
	private int numberOfLightsToTurnOn;
	/**The illuminance value to target	 */
	private double targetIlluminance;

	/**
	 * Instantiates a new illuminance goal.
	 * 
	 * @param numberOfLightsToTurnOn
	 *                 is the number of lights to turn on.
	 * @param targetIlluminance 
	 * 					is the target illuminance
	 */
	private IlluminanceGoal(int numberOfLightsToTurnOn, double targetIlluminance) {
		this.numberOfLightsToTurnOn = numberOfLightsToTurnOn;
		this.targetIlluminance = targetIlluminance;
	}

	/**
	 * Gets the illuminance value to target.
	 * 
	 * @return the number of lights to turn On.
	 */
	public int getNumberOfLightsToTurnOn() {
		return numberOfLightsToTurnOn;
	}

	/**
	 * Gets the number of lights to turn On.
	 * 
	 * @return the number of lights to turn On.
	 */
	public double getTargetIlluminance() {
		return targetIlluminance;
	}

	/**
	 * This method finds illuminancGoal with the number of light to turn on allowed
	 * @param numberOfLightsToTurnOn
	 * 					is the number of light to turn on allowed to find
	 * @return
	 */
	public static IlluminanceGoal getIlluminanceGoalByInt(int numberOfLightsToTurnOn ) {
		IlluminanceGoal illuminanceGoal = null;
		for (IlluminanceGoal ill : IlluminanceGoal.values()) {
			if(ill.getNumberOfLightsToTurnOn() == numberOfLightsToTurnOn) {
				illuminanceGoal = ill;
			}
		}
		return illuminanceGoal;
	}
	
	/**
	 * This method finds illuminanceGoal with the illuminance target
	 * @param targetIlluminance
	 * 					is the target illuminance to find
	 * @return
	 */
	public static IlluminanceGoal getIlluminanceGoalByDouble(double targetIlluminance ) {
		IlluminanceGoal illuminanceGoal = null;
		for (IlluminanceGoal ill : IlluminanceGoal.values()) {
			if(ill.getTargetIlluminance() == targetIlluminance) {
				illuminanceGoal = ill;
			}
		}
		return illuminanceGoal;
	}

	/**
	 * This method finds with the name of Object
	 * @param nameOfIlluminanceGoal
	 * 					is the name to find
	 * @return
	 */
	public static IlluminanceGoal getIlluminanceGoalByName(String nameOfIlluminanceGoal ) {
		IlluminanceGoal illuminanceGoal = null;
		for (IlluminanceGoal ill : IlluminanceGoal.values()) {
			//System.out.println("test de " + nameOfIlluminanceGoal + " avec " + ill.toString());
			if(ill.toString().equals(nameOfIlluminanceGoal)) {
				illuminanceGoal = ill;
				break;
			}
		}
		return illuminanceGoal;
	}



}
