package oxy.pc;

import lunalib.lunaSettings.LunaSettings;
import lunalib.lunaSettings.LunaSettingsListener;

public class LunaLibImplementer implements LunaSettingsListener {
	
	public static final String ID = "oxy_pc";

    public static void init() {
        PreferableChanges.oxy_pc_hasSkillsChanged = getSetting("oxy_pc_hasSkillsChanged");
        PreferableChanges.oxy_pc_hasSkillSpecial = getSetting("oxy_pc_hasSkillSpecial");
    }

    public static boolean getSetting(String key) {
        return (boolean)LunaSettings.getBoolean(ID, key);
    }

    @Override
    public void settingsChanged(String modId) {
        if (ID.equals(modId)) {
            init();
        }
    }
}