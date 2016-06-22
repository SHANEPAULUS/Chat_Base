package com.shane.chatbase.server;

import com.shane.chatbase.parser.JabberInputHandler;
import com.shane.chatbase.parser.PacketQueue;
import com.shane.chatbase.session.Session;

/**
 * Created by SHANE on 2016/06/22.
 */
public class ProcessThread extends Thread {
    private Session session;
    private PacketQueue packetQueue;

    public ProcessThread(PacketQueue packetQueue, Session session)
    {
        this.packetQueue=packetQueue;
        this.session=session;
    }

    public void run()
    {
        try
        {
            JabberInputHandler handler=new JabberInputHandler(packetQueue);
            handler.process(session);
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
