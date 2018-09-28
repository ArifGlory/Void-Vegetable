package Module;

import java.util.List;

/**
 * Created by Mai Thanh Hiep on 4/3/2016.
 */
public interface DirectionKobalListener {
    void onDirectionFinderStart();

    void onDirectionKobalStart();
    void onDirectionKobalSuccess(List<Route> route);

}
