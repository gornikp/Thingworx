import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;

public class Client extends ConnectedThingClient{


    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private static final String KEY = "dd22274d-abc3-47b0-86d8-8e363ba2b8fc";
    private static final String LINK = "ws://127.0.0.1:8080/Thingworx/WS";


    public Client(ClientConfigurator config) throws Exception {
        super(config);
    }

    public static void startConnection(){
        ClientConfigurator config = new ClientConfigurator();

        LOG.info("START");
        config.setUri(LINK);
        config.setAppKey(KEY);
        config.ignoreSSLErrors(true);
        try {
            Client client = new Client(config);


            client.start();

            while(!client.getEndpoint().isConnected()) {
                Thread.sleep(1000);
                LOG.info("WAIT");
            }
            ValueCollection params = new ValueCollection();

           // client.invokeService(ThingworxEntityTypes.Things, "Room1", "getTemerature", params, 5000);

            RoomTemplate room1 = new RoomTemplate("Room1", "", client);
           /* RoomTemplate thing2 = new RoomTemplate("TestR2", "test conbection r2", client);
            RoomTemplate thing3 = new RoomTemplate("TestR3", "test conbection r3", client);*/
            client.bindThing(room1);
          /*  client.bindThing(thing2);
            client.bindThing(thing3);*/

            while(!client.isShutdown()) {
                // Loop over all the Virtual Things and process them
                if(client.isConnected()) {
                    LOG.info("SEND");
                    for(VirtualThing thing : client.getThings().values()) {
                        try {
                            thing.processScanRequest();
                        }
                        catch(Exception eProcessing) {
                            System.out.println("Error Processing Scan Request for [" + thing.getName() + "] : " + eProcessing.getMessage());
                        }
                    }
                    LOG.info("SLEEP");
                    Thread.sleep(5000);
                }
            }
            LOG.info("END");


        } catch (Exception e) {
            LOG.info("ERROR");
            e.printStackTrace();
        }

    }


}