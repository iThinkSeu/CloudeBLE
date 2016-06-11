package util;

import android.content.Context;
import android.util.DisplayMetrics;

public final class DimensionUtils {
	
	  public static DisplayMetrics getDisplay(Context context){
	        return context.getResources().getDisplayMetrics();
	    }
 
    }