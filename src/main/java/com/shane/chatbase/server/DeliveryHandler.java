package com.shane.chatbase.server;

import com.shane.chatbase.parser.Packet;
import com.shane.chatbase.parser.PacketListener;
import com.shane.chatbase.session.Session;
import com.shane.chatbase.session.SessionIndex;

/**
 * Created by SHANE on 2016/06/22.
 */
public class DeliveryHandler implements PacketListener {
    private SessionIndex sessionIndex;

    public DeliveryHandler(SessionIndex sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public void notify(Packet packet) {
        String recipient=packet.getTo();

        if(recipient.equalsIgnoreCase(Server.SERVER_NAME))
        {
            return;
        }

        try
        {
            Session session=sessionIndex.getSession(recipient);

            if(session==null)
            {
                packet.writeXML(session.getOutput());

            }
            else
            {
                return;
            }
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
