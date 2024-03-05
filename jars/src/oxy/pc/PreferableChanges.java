package oxy.pc;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.SkillSpecAPI;
import com.fs.starfarer.launcher.ModManager;
import com.fs.starfarer.launcher.ModManager.ModSpec;
import com.fs.starfarer.api.SettingsAPI;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


public class PreferableChanges extends BaseModPlugin
{
    public static final String ID = "oxy_pc";
    public static final Logger LOG = Global.getLogger(PreferableChanges.class);
    public static final String CONFIG_FILE = "Config.json";

    public static final String VALUES_FILE = "data/config/settings.json";
    public static final String SKILLS_FILE = "data/skill_data.csv";

    public static final String[] LIST_SKILLS_FILE = new String[] {"data/characters/skills/special_modifications.skill"};

    
    public static boolean oxy_pc_hasSkillsChanged;
    public static boolean oxy_pc_hasSkillSpecial;

	@Override
	public void onApplicationLoad() {
        try {
            SettingsAPI settings = Global.getSettings();
            if (settings.getModManager().isModEnabled("lunalib")) {
                LunaLibImplementer.init();
            }
            else {
                JSONObject configSettings = Global.getSettings().loadJSON(CONFIG_FILE);
                oxy_pc_hasSkillsChanged = configSettings.optBoolean("oxy_pc_hasSkillsChanged");
                oxy_pc_hasSkillSpecial = configSettings.optBoolean("oxy_pc_hasSkillSpecial");
            }
            if (oxy_pc_hasSkillsChanged) {
                JSONArray file = Global.getSettings().loadCSV(SKILLS_FILE);
                for (int i = 0; i < file.length(); i++) {
                    JSONObject object = file.getJSONObject(i);
                    if (object != null) {
                        String id = object.optString("id");
                        if (! id.isEmpty() && ! id.startsWith("#") && ! id.equals("id")) {
                            SkillSpecAPI api = Global.getSettings().getSkillSpec(id);
                            if (api != null && api.getName() != null && !api.getName().isEmpty()) {
                                if (!id.equals("special_modifications") || oxy_pc_hasSkillSpecial) {
                                    api.setOrder(object.optInt("order"));
                                    api.setTier(object.optInt("tier"));
                                    api.setReqPoints(object.optInt("reqPoints"));
                                    api.setReqPointsPer(object.optInt("reqPointsPerExtraSkill"));
                                    api.getTags().clear();
                                    Object tags = object.opt("tags");
                                    if (tags != null && ! ((String)tags).isEmpty()) {
                                        String[] array = ((String)tags).split(",");
                                        for (int e = 0; e < array.length; e++) {
                                            String tag = array[e].trim();
                                            if (! tag.isEmpty()) {
                                                api.getTags().add(tag);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (oxy_pc_hasSkillSpecial) {
                    for (String f : LIST_SKILLS_FILE) {
                        for (ModSpec m : ModManager.getInstance().getEnabledMods()) {
                            if (m.getId().equals(ID)) {
                                m.getFullOverrides().add("data/characters/skills/" + Global.getSettings().loadJSON(f).getString("id") + ".skill");
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            LOG.error("PreferableChanges: Error in onApplicationLoad().", e);
        }
	}
}