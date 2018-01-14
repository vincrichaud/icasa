package fr.esisar.cs511.followme.service.spec;

		 
/**
 * The FollowMeConfiguration service allows one to configure the Follow Me
 * application.
 */
public interface FollowMeConfiguration {
	
		//public static final String PROP_LUMINANCE= "luminance";
	 
	    /**
	     * Gets the maximum number of lights to turn on each time an user is
	     * entering a room.
	     * 
	     * @return the maximum number of lights to turn on
	     */
	    public int getMaximumNumberOfLightsToTurnOn();
	 
	    /**
	     * Sets the maximum number of lights to turn on each time an user is
	     * entering a room.
	     * 
	     * @param maximumNumberOfLightsToTurnOn
	     *            the new maximum number of lights to turn on
	     */
	    public void setMaximumNumberOfLightsToTurnOn(int maximumNumberOfLightsToTurnOn);
	    
	    /**
	     * Gets the maximum allowed energy consumption in Watts in each room
	     * 
	     * @return the maximum allowed energy consumption in Watts/hours
	     */
	    public double getMaximumAllowedEnergyInRoom();
	 
	    /**
	     * Sets the maximum allowed energy consumption in Watts in each room
	     * 
	     * @param maximumEnergy
	     *            the maximum allowed energy consumption in Watts/hours in each room
	     */
	    public void setMaximumAllowedEnergyInRoom(double maximumEnergy);
	    
	    /**
	     * Gets the targeted illuminance for each room
	     * 
	     * @return the targeted illuminance in lumens
	     */
	    public double getTargetedIlluminance();
	 
	    /**
	     * Sets the targeted illuminance for each room
	     * 
	     * @param illuminance
	     *       the targeted illuminance in lumens for each room
	     */
	    public void setTargetedIlluminance(double illuminance);
}

