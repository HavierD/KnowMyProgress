package tech.havier;

public class GlobalStateManager {

    private boolean databaseNeedsRefresh = false;
    private boolean hasImportedTxtAtLeast1Time = false;

    private static GlobalStateManager globalStateManager;

    public static GlobalStateManager getInstance() {
        if (globalStateManager == null) {
            globalStateManager = new GlobalStateManager();
        }
        return globalStateManager;
    }

    private GlobalStateManager() {
    }

    public boolean hasImportedTxtAtLeast1Time() {
        return hasImportedTxtAtLeast1Time;
    }

    public void importedTxt() {
        if(hasImportedTxtAtLeast1Time()){
            databaseNeedsRefresh = true;
            return;
        }
        this.hasImportedTxtAtLeast1Time = true;
    }

    public boolean databaseNeedsRefresh() {
        return databaseNeedsRefresh;
    }
}
