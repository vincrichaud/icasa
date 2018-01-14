package fr.esisar.cs511.followme.service.spec;

/**
 * This enum describes the different energy goals associated with the
 * manager.
 */
public enum EnergyGoal {

	   LOW(100d), 
	   MEDIUM(200d), 
	   HALF(310d),
	   HIGH(1000d);
	 
    /**
     * The corresponding maximum energy in watt
     */
    private double maximumEnergyInRoom;
 
    /**
     * get the maximum energy consumption in each room
     * 
     * @return the energy in watt
     */
    public double getMaximumEnergyInRoom() {
        return maximumEnergyInRoom;
    }
 
    private EnergyGoal(double powerInWatt) {
        maximumEnergyInRoom = powerInWatt;
    }
    
    /**
     * This method finds energyGoal with the double associate corresponding to the max consumption allowed
     * @param numberOfConsumptionAllowed
     * 						is the max consumption allowed to find
     * @return
     */
    public static EnergyGoal getEnergyGoalByDouble(double numberOfConsumptionAllowed ) {
    	EnergyGoal energyGoal = null;
	    for (EnergyGoal ener : EnergyGoal.values()) {
	    	if(ener.getMaximumEnergyInRoom() == numberOfConsumptionAllowed) {
	    		energyGoal = ener;
	    	}
	    }
	    return energyGoal;
    }
	
    /**
     * This method finds energyGoal with the object name
     * @param nameOfEnergyGoal
     * 				is the name to find
     * @return
     */
	public static EnergyGoal getIEnergyGoallByName(String nameOfEnergyGoal ) {
		EnergyGoal energyGoal = null;
	    for (EnergyGoal ener : EnergyGoal.values()) {
	    	System.out.println("test de " + nameOfEnergyGoal + " avec " + ener.toString());
	    	if(ener.toString().equals(nameOfEnergyGoal)) {
	    		energyGoal = ener;
	    		break;
	    	}
	    }
	    return energyGoal;
    }
}
