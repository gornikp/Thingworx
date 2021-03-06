import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.annotations.*;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.BooleanPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@ThingworxPropertyDefinitions(properties = {
        @ThingworxPropertyDefinition(name="temperature", description="", baseType="NUMBER",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:21"}),
        @ThingworxPropertyDefinition(name="humidity", description="", baseType="NUMBER",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:0"}),
        @ThingworxPropertyDefinition(name="occupancy", description="", baseType="BOOLEAN",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:false"}),
        @ThingworxPropertyDefinition(name="lighting", description="", baseType="BOOLEAN",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:false"}),
        @ThingworxPropertyDefinition(name="airConditioning", description="", baseType="BOOLEAN",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:false"}),
        @ThingworxPropertyDefinition(name="heating", description="", baseType="BOOLEAN",
                aspects={"dataChangeType:VALUE",
                        "dataChangeThreshold:0",
                        "cacheTime:0",
                        "isPersistent:TRUE",
                        "isReadOnly:FALSE",
                        "pushType:ALWAYS",
                        "isFolded:FALSE",
                        "defaultValue:false"})
})



public class RoomTemplate extends VirtualThing implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(RoomTemplate.class);

    private ArrayList<String> roomNames = null;
    private final static String TEMPERATURE_FIELD = "temperature";
    private final static String HUMIDITY_FIELD = "humidity";
    private final static String OCCUPANCY_FIELD = "occupancy";
    private final static String LIGHTING_FIELD = "lighting";
    private final static String AIR_CONDITIONING_FIELD = "airConditioning";
    private final static String HEATING_FIELD = "heating";
    private Double temperature;
    private Double humidity;
    private Boolean occupancy;
    private Boolean lighting;
    private Boolean airConditioning;
    private Boolean heating;


    private String thingName = null;

    public RoomTemplate(String name, String description, ConnectedThingClient client) {
        super(name, description, client);
        thingName = name;
        // Populate the thing shape with the properties, services, and events that are annotated in this code
        super.initializeFromAnnotations();
        this.init();
    }

    // From the VirtualThing class
    // This method will get called when a connect or reconnect happens
    // Need to send the values when this happens
    // This is more important for a solution that does not send its properties on a regular basis
    public void synchronizeState() {
        // Be sure to call the base class
        super.synchronizeState();
        // Send the property values to ThingWorx when a synchronization is required
        super.syncProperties();
    }


    private void init() {

        //get
        try {
            heating = getHeating();
        } catch(Exception ex) {
            heating = false;

        }

        if(heating == null){
            heating = false;
        }

        try {
            getHeating();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }



        //get
        try {
            airConditioning = getAirConditioning();
        } catch(Exception ex) {
            airConditioning = false;

        }

        if(airConditioning == null){
            airConditioning = false;
        }

        try {
            setAirConditioning();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }


        //get
        try {
            lighting = getLighting();
        } catch(Exception ex) {
            lighting = false;

        }

        //get
        try {
            lighting = getLighting();
        } catch(Exception ex) {
            lighting = false;

        }

        if(lighting == null){
            lighting = false;
        }

        try {
            setLighting();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }


        //get
        try {
            lighting = getLighting();
        } catch(Exception ex) {
            lighting = false;

        }

        if(occupancy == null){
            occupancy = false;
        }

        try {
            setOccupancy();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

        //get
        try {
            humidity = getHumidity();
        } catch(Exception ex) {
            humidity = new Double(0);

        }

        if(humidity == null){
            humidity = new Double(0);
        }

        try {
            setHumidity();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

        try {
            temperature = getTemperature();
        } catch(Exception ex) {
            temperature = new Double(0);

        }

        if(temperature == null){
            temperature = new Double(0);
        }

        try {
            setTemperature();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

        try {
            this.setPropertyValue("humidity", new NumberPrimitive(100));
        } catch (Exception ex) {
            LOG.error("Could not set default value for humidity", ex);
        }
    }


    public void run() {
        try {
            // Delay for a period to verify that the Shutdown service will return
            Thread.sleep(1000);
            // Shutdown the client
            this.getClient().shutdown();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

    }

    @Override
    public void processScanRequest() throws Exception { //TODO LOGIKA
        //get
        try{
            temperature = getTemperature();
            humidity = getHumidity();
            occupancy = getOccupancy();
            lighting = getLighting();
            airConditioning = getAirConditioning();
            heating = getHeating();

        }catch(Exception ex){
            LOG.error("Error " + thingName, ex);
        }
        if(temperature == null){
            temperature = new Double(0);
        }
        if(humidity == null){
            humidity = new Double(0);
        }
        if(occupancy == null){
            occupancy = false;
        }
        if(lighting == null){
            lighting = false;
        }
        if(airConditioning == null){
            airConditioning = false;
        }
        if(heating == null){
            heating = false;
        }
        Random r = new Random(5564646);
        temperature+=r.nextDouble();
        humidity += r.nextDouble() / 2;

        if (temperature < 17) {
            heating = true;
            airConditioning = false;
        }

        else  if (temperature > 23){
            heating = false;
            airConditioning = true;
        }
        if (humidity > 70)
        {
            airConditioning = true;
        }


        if (!occupancy){
            airConditioning = false;
            lighting = false;
            heating = false;
        }
        LocalTime localTime = LocalTime.now();
        if (occupancy && localTime.getHourOfDay() > 13){
            lighting = true;
        }


        LOG.info("Sending " + thingName + " double " + temperature.intValue());
        LOG.info("Sending " + thingName + " double " + humidity.intValue());
        LOG.info("Sending " + thingName + " bool " + airConditioning.booleanValue());
        LOG.info("Sending " + thingName + " bool " + heating.booleanValue());
        LOG.info("Sending " + thingName + " bool " + occupancy.booleanValue());
        LOG.info("Sending " + thingName + " bool " + lighting.booleanValue());
        try {
            setTemperature();
            setHumidity();
            setAirConditioning();
            setHeating();
            setOccupancy();
            setLighting();
        } catch (Exception ex) {
            LOG.error("Error " + thingName, ex);
        }

        this.updateSubscribedProperties(5000);


    }


    public void setTemperature() throws Exception {
        setProperty(TEMPERATURE_FIELD, new NumberPrimitive(this.temperature));
    }

    public void setHumidity() throws Exception {
        setProperty(HUMIDITY_FIELD, new NumberPrimitive(this.humidity));
    }

    public Boolean getOccupancy() {
        return (Boolean) getProperty(OCCUPANCY_FIELD).getValue().getValue();
    }

    public void setOccupancy() throws Exception {
        setProperty(OCCUPANCY_FIELD, new BooleanPrimitive(this.occupancy));
    }

    public Boolean getLighting() {
        return (Boolean) getProperty(LIGHTING_FIELD).getValue().getValue();
    }

    public void setLighting() throws Exception {
        setProperty(LIGHTING_FIELD, new BooleanPrimitive(this.lighting));
    }

    public Boolean getAirConditioning() {
        return (Boolean) getProperty(AIR_CONDITIONING_FIELD).getValue().getValue();
    }

    public void setAirConditioning() throws Exception {
        setProperty(AIR_CONDITIONING_FIELD, new BooleanPrimitive(this.airConditioning));
    }

    public Boolean getHeating() {
        return (Boolean) getProperty(HEATING_FIELD).getValue().getValue();
    }

    public void setHeating() throws Exception {
        setProperty(HEATING_FIELD, new BooleanPrimitive(this.heating));
    }

    @ThingworxServiceDefinition(name="getHumidity", description="")
    @ThingworxServiceResult(name="result", description="Result", baseType="NUMBER")
    public Double getHumidity () throws Exception {
        LOG.info("Get humidity: " + humidity.intValue());

        return humidity;
    }

    @ThingworxServiceDefinition(name="getTemperature", description="")
    @ThingworxServiceResult(name="result", description="Result", baseType="NUMBER")
    public Double getTemperature () throws Exception {
            LOG.info("Get tempetature: " + temperature.intValue());

            return temperature;
        }
}