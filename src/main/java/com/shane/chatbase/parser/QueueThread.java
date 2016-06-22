package com.shane.chatbase.parser;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by SHANE on 2016/06/22.
 */
public class QueueThread extends Thread {
    private PacketQueue packetQueue;
    private HashMap packetListeners=new HashMap();

    public QueueThread(PacketQueue packetQueue)
    {
        this.packetQueue=packetQueue;
    }

    public boolean addPacketListener(PacketListener listener,String element)
    {
        if(listener==null||element==null)
        {
            return false;
        }
        packetListeners.put(listener,element);
        return true;
    }

    public boolean removePacketListener(PacketListener listener)
    {
        packetListeners.remove(listener);
        return true;
    }

    public void run()
    {
        for(Packet packet=packetQueue.pull();packet!=null;packet=packetQueue.pull())
        {
            try
            {
                synchronized (packetListeners)
                {
                    Iterator iterator=packetListeners.keySet().iterator();
                    while (iterator.hasNext())
                    {
                        PacketListener listener=(PacketListener)iterator.next();
                        String element=(String)packetListeners.get(listener);

                        if(element.equals(packet.getElement())||element.length()==0)
                        {
                            listener.notify(packet);
                        }
                    }
                }
            }

            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

}
