import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;

import java.util.ArrayList;

public class Client extends ConnectedThingClient{


    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private static final String KEY = "dd22274d-abc3-47b0-86d8-8e363ba2b8fc";
    private static final String LINK = "ws://127.0.0.1:8080/Thingworx/WS";
    private static ArrayList<String> roomNames = null;


    public Client(ClientConfigurator config) throws Exception {
        super(config);

        this.roomNames = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {

            roomNames.add("Room" + i);
        }
    }

    public static void startConnection(){
        ClientConfigurator config = new ClientConfigurator();

        config.setUri(LINK);
        config.setAppKey(KEY);
        config.ignoreSSLErrors(true);
        try {
            Client client = new Client(config);


            client.start();

            while(!client.getEndpoint().isConnected()) {
                Thread.sleep(1000);
            }

            for(String roomName : roomNames) {
                ValueCollection params = new ValueCollection();
                params.SetStringValue("RoomName", roomName);
                client.invokeService(ThingworxEntityTypes.Things, "RoomTemplate", "CreateRoom", params, 5000);
            }

            for(String roomName : roomNames) {
                RoomTemplate thing = new RoomTemplate(roomName, "", client);
                  client.bindThing(thing);
            }


            while(!client.isShutdown()) {
                // Loop over all the Virtual Things and process them
                if(client.isConnected()) {
                    for(VirtualThing thing : client.getThings().values()) {
                        try {
                            thing.processScanRequest();
                        }
                        catch(Exception eProcessing) {
                            System.out.println("Error Processing Scan Request for [" + thing.getName() + "] : " + eProcessing.getMessage());
                        }
                    }
                    Thread.sleep(5000);
                }
            }
        } catch (Exception e) {
            LOG.info("ERROR");
            e.printStackTrace();
        }
    }


}