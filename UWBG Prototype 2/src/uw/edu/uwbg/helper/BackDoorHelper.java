package uw.edu.uwbg.helper;


/**
 * A Singleton class to control which activities are executed
 * 
 * @author Brett Schormann
 * @version 0.1 10/2014
 * 			0.2 10/30/2014
 * 			Changed so that production Trails code is executed. (BS)
 * 			0.3 11/12/2014
 * 			Changed so that Capstone project format is used. (BS)
 * @since 0.2
 */
public class BackDoorHelper {
	// TODO Use an adapter
	private static BackDoorHelper singleton;
	
	private boolean useProductionMap = true;
	private boolean useProductionHelp = true;
	private boolean useProductionVisitorInformation = true;
	private boolean useProductionTrails = true;
	private boolean useProductionFeaturedGardens = true;
	private boolean useProductionBookmarks = true;
	private boolean useProductionParkHistory = true;
	private boolean useProductionArboretumEvents = true;
	private boolean useProductionPlantLookup = true;
	private boolean useCapstoneProject = false;

	public boolean isUseProductionPlantLookup() {
		return useProductionPlantLookup;
	}

	public void setUseProductionPlantLookup(boolean useProductionPlantLookup) {
		this.useProductionPlantLookup = useProductionPlantLookup;
	}

	public boolean isUseProductionTrails() {
		return useProductionTrails;
	}

	public void setUseProductionTrails(boolean useProductionTrails) {
		this.useProductionTrails = useProductionTrails;
	}

	public boolean isUseProductionFeaturedGardens() {
		return useProductionFeaturedGardens;
	}

	public void setUseProductionFeaturedGardens(boolean useProductionFeaturedGardens) {
		this.useProductionFeaturedGardens = useProductionFeaturedGardens;
	}

	public boolean isUseCapstoneProject() {
		return useCapstoneProject;
	}

	public void setUseCapstoneProject(boolean useCapstoneProject) {
		this.useCapstoneProject = useCapstoneProject;
	}

	public boolean isUseProductionBookmarks() {
		return useProductionBookmarks;
	}

	public void setUseProductionBookmarks(boolean useProductionBookmarks) {
		this.useProductionBookmarks = useProductionBookmarks;
	}

	public boolean isUseProductionParkHistory() {
		return useProductionParkHistory;
	}

	public void setUseProductionParkHistory(boolean useProductionParkHistory) {
		this.useProductionParkHistory = useProductionParkHistory;
	}

	public boolean isUseProductionArboretumEvents() {
		return useProductionArboretumEvents;
	}

	public void setUseProductionArboretumEvents(boolean useProductionArboretumEvents) {
		this.useProductionArboretumEvents = useProductionArboretumEvents;
	}

	public boolean isUseProductionVisitorInformation() {
		return useProductionVisitorInformation;
	}

	public void setUseProductionVisitorInformation(
			boolean useProductionVisitorInformation) {
		this.useProductionVisitorInformation = useProductionVisitorInformation;
	}

	private BackDoorHelper() {}
	
	public static BackDoorHelper getBackDoorHelper() {
		if (singleton == null){
			singleton = new BackDoorHelper(); //This only executes if singleton does not exist
		}
		return singleton;
	}

	public boolean isUseProductionMap() {
		return useProductionMap;
	}

	public void setUseProductionMap(boolean useProductionMap) {
		this.useProductionMap = useProductionMap;
	}

	public boolean isUseProductionHelp() {
		return useProductionHelp;
	}

	public void setUseProductionHelp(boolean useProductionHelp) {
		this.useProductionHelp = useProductionHelp;
	}
	
	
}
