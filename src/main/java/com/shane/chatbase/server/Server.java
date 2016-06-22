package com.shane.chatbase.server;

import com.shane.chatbase.parser.PacketQueue;
import com.shane.chatbase.parser.QueueThread;
import com.shane.chatbase.session.Session;
import com.shane.chatbase.session.SessionIndex;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by SHANE on 2016/06/22.
 */

public class Server {
    final static public int JABBER_PORT=5222;
    final static public String SERVER_NAME="127.0.0.1";


    public static void main(String[] args)
    {
        System.out.println("Jabber Server -- "+JABBER_PORT);

        // The shared packed queue
        PacketQueue packetQueue=new PacketQueue();
        SessionIndex sessionIndex=new SessionIndex();

        // Create and start QueueThread
        QueueThread queueThread=new QueueThread(packetQueue);
        queueThread.setDaemon(true);

        queueThread.addPacketListener(new OpenStreamHandler(sessionIndex), "stream:stream");
        queueThread.addPacketListener(new CloseStreamHandler(sessionIndex), "/stream:stream");
        queueThread.addPacketListener(new DeliveryHandler(sessionIndex), "");

        queueThread.start();

        ServerSocket serverSocket;

        try
        {
            serverSocket=new ServerSocket(JABBER_PORT);
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
            return;
        }

        while(true)
        {
            try
            {
                Socket newSocket=serverSocket.accept();
                Session session=new Session(newSocket);

                ProcessThread processor=new ProcessThread(packetQueue,session);
                processor.start();
            }

            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
