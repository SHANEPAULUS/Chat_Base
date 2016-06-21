package com.shane.chatbase.session;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * Created by SHANE on 2016/06/21.
 */
public class SessionIndex {
    private Hashtable userIndex=new Hashtable();
    private Hashtable jidIndex=new Hashtable();

    public Session getSession(String jabberID)
    {
        return getSession(new JabberID(jabberID));
    }

    public Session getSession(JabberID jabberID)
    {
        String jidString=jabberID.toString();
        Session session=(Session)jidIndex.get(jidString);
        if(session==null)
        {
            LinkedList resources=(LinkedList)userIndex.get(jabberID.getUser());
            if(resources==null)
            {
                return null;
            }
            session=(Session)resources.getFirst();
        }

        return session;
    }

    public void removeSession(Session session)
    {
        String jidString=session.getJabberID().toString();
        String user=session.getJabberID().getUser();

        if(jidIndex.containsKey(jidString))
        {
            jidIndex.remove(jidString);
        }

        LinkedList resources=(LinkedList)userIndex.get(user);
        if(resources==null)
        {
            return;
        }
        if(resources.size()<=1)
        {
            userIndex.remove(user);
            return;
        }

        resources.remove(session);
    }

    public void addSession(Session session)
    {
        jidIndex.put(session.getJabberID().toString(),session);
        String user=session.getJabberID().getUser();
        LinkedList resources=(LinkedList)userIndex.get(user);
        if(resources==null)
        {
            resources=new LinkedList();
            userIndex.put(user,resources);
        }

        resources.addLast(session);
    }


}
