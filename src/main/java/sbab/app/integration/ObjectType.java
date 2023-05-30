package sbab.app.integration;

import sbab.app.integration.journeypattern.JourneyPatternResponse;
import sbab.app.integration.line.LineResponse;
import sbab.app.integration.stoppoint.StopPointResponse;

public enum ObjectType {
    JOURNEY_PATTERN("jour", JourneyPatternResponse.class),
    STOP_POINT("stop", StopPointResponse.class),
    LINE("line", LineResponse.class);

    public final String model;
    public final Class responseClass;

    ObjectType(String model, Class responseClass){
        this.model = model;
        this.responseClass = responseClass;
    }

}
