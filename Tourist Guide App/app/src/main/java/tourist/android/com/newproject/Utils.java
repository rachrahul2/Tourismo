package tourist.android.com.newproject;

import android.content.res.Resources;
import android.util.TypedValue;

public class Utils {
    public static int convertDipToPixcel(Resources res, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, res.getDisplayMetrics());
    }
}
