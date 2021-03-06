package net.osmand.plus.quickaction;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.osmand.plus.OsmandApplication;
import net.osmand.plus.activities.MapActivity;

import java.util.HashMap;
import java.util.List;

public class QuickAction {

    public interface QuickActionSelectionListener {

        void onActionSelected(QuickAction action);
    }

    protected int type;
    protected long id;

    private @StringRes int nameRes;
    private @DrawableRes int iconRes;
    private boolean isActionEditable;

    private String name;
    private HashMap<String, String> params;

    protected QuickAction() {
        this.id = System.currentTimeMillis();
    }

    protected QuickAction(int type, int nameRes) {
        this.id = System.currentTimeMillis();
        this.nameRes = nameRes;
        this.type = type;
    }

    public QuickAction(int type) {
        this.id = System.currentTimeMillis();
        this.type = type;
        this.nameRes = QuickActionFactory.getActionName(type);
        this.iconRes = QuickActionFactory.getActionIcon(type);
        this.isActionEditable = QuickActionFactory.isActionEditable(type);
    }

    public QuickAction(QuickAction quickAction) {
        this.type = quickAction.type;
        this.id = quickAction.id;
        this.name = quickAction.name;
        this.params = quickAction.params;

        this.nameRes = QuickActionFactory.getActionName(type);
        this.iconRes = QuickActionFactory.getActionIcon(type);
        this.isActionEditable = QuickActionFactory.isActionEditable(type);
    }

    public int getNameRes() {
        return nameRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getIconRes(Context context) {
        return iconRes;
    }

    public long getId() {
        return id;
    }

	public long getType() {
		return type;
	}

    public boolean isActionEditable() {
        return isActionEditable;
    }

    public boolean isActionEnable(OsmandApplication app) {
        return true;
    }

    public String getName(Context context) {
        return name == null || name.isEmpty() ? nameRes > 0 ? context.getString(nameRes) : "" : name;
    }

    public HashMap<String, String> getParams() {

        if (params == null) params = new HashMap<>();

        return params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public boolean isActionWithSlash(OsmandApplication application){
        return false;
    }

    public String getActionText(OsmandApplication application){
        return getName(application);
    }

    public void setAutoGeneratedTitle(EditText title){
    }

    public void execute(MapActivity activity){};
    public void drawUI(ViewGroup parent, MapActivity activity){};
    public boolean fillParams(View root, MapActivity activity){ return true; };

    public boolean hasInstanceInList(List<QuickAction> active){

        for (QuickAction action: active){
            if (action.type == type) return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof QuickAction) {

            QuickAction action = (QuickAction) o;

            if (type != action.type) return false;
            if (id != action.id) return false;

            return true;

        } else return false;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + nameRes;
        result = 31 * result + iconRes;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public boolean hasCustomName(Context context) {
        return !getName(context).equals(context.getString(nameRes));
    }
}
