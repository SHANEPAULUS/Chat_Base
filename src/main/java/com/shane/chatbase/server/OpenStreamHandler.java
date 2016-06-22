package com.shane.chatbase.server;

import com.shane.chatbase.parser.Packet;
import com.shane.chatbase.parser.PacketListener;
import com.shane.chatbase.session.JabberID;
import com.shane.chatbase.session.Session;
import com.shane.chatbase.session.SessionIndex;

/**
 * Created by SHANE on 2016/06/22.
 */
public class OpenStreamHandler implements PacketListener {
    private static int streamID=0;
    private SessionIndex sessionIndex;

    public OpenStreamHandler(SessionIndex sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public void notify(Packet packet) {
        try
        {
            Session session=packet.getSession();
            String from=packet.getFrom();
            if(from==null)
            {
                session.getSocket().close();
                return;
            }

            session.setJabberID(new JabberID(from));
            session.setStatus(Session.STREAMING);
            session.setStreamID(Integer.toHexString(streamID++));
            sessionIndex.addSession(session);

            packet.setTo(packet.getFrom());
            packet.setFrom(Server.SERVER_NAME);
            packet.setID(session.getStreamID());
            packet.writeXML();
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
