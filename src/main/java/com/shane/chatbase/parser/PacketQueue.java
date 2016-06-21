package com.shane.chatbase.parser;


import java.util.LinkedList;

/**
 * Created by SHANE on 2016/06/21.
 */
public class PacketQueue {
    LinkedList queue=new LinkedList();

    public synchronized void push(Packet packet)
    {
        queue.add(packet);
        notifyAll();
    }

    public synchronized Packet pull()
    {
        try
        {
            while (queue.isEmpty())
            {
                wait();
            }
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
        return (Packet)queue.remove(0);
    }
}
