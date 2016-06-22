package com.shane.chatbase.server;

import com.shane.chatbase.parser.Packet;
import com.shane.chatbase.parser.PacketListener;
import com.shane.chatbase.session.Session;
import com.shane.chatbase.session.SessionIndex;

/**
 * Created by SHANE on 2016/06/22.
 */
public class CloseStreamHandler implements PacketListener {
    private SessionIndex sessionIndex;

    public CloseStreamHandler(SessionIndex sessionIndex) {
        this.sessionIndex = sessionIndex;
    }

    public void notify(Packet packet) {
        try
        {
            packet.writeXML();

            Session session=packet.getSession();
            session.getSocket().close();

            sessionIndex.removeSession(session);

        }

        catch(Exception ex)
        {
            sessionIndex.removeSession(packet.getSession());
            ex.printStackTrace();
        }

    }
}
