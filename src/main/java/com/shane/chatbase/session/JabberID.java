package com.shane.chatbase.session;

/**
 * Created by SHANE on 2016/06/21.
 */
public class JabberID {
    //Still needs major refactoring
    private String user;
    private String domain;
    private String resource;

    public JabberID(String user, String domain, String resource) {
        this.user = user;
        this.domain = domain;
        this.resource = resource;
        //s
    }

    public JabberID(String jid) {
        setJID(jid);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public String getServer(){return domain;}

    public void setServer(String server){this.domain=server;}

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Boolean equalsDomain(String domain)
    {
        if(this.domain==null^domain==null)
        {
            return false;
        }
        return this.domain.equalsIgnoreCase(domain);
    }

    public Boolean equalsDomain(JabberID jabberID)
    {
        return equalsDomain(jabberID.domain);
    }

    public Boolean equalsUser(String user)
    {
        if(this.user==null^user==null)
        {
            return false;
        }
        return this.user.equalsIgnoreCase(user);
    }

    public Boolean equalsUser(JabberID jabberID)
    {
        return equalsUser(jabberID.user);
    }

    public Boolean equalsResource(String resource)
    {
        if(this.resource==null^resource==null)
        {
            return false;
        }

        return this.resource.equalsIgnoreCase(resource);
    }

    public Boolean equalsResource(JabberID jabberID)
    {
        return equalsResource(jabberID.resource);
    }

    public Boolean equalsUser(String user, String resource)
    {
        return equalsUser(user)&&resource.equals(resource);
    }

    public Boolean equals(JabberID jabberID)
    {
        return equalsUser(jabberID)&&equalsDomain(jabberID)&&equalsResource(jabberID);
    }

    public Boolean equals(String jabberID)
    {
        return equals(new JabberID(jabberID));
    }

    public void setJID(String jid)
    {
        if(jid==null)
        {
            this.user=null;
            this.domain=null;
            this.resource=null;
            return;
        }

        int atLoc=jid.indexOf("@");
        if(atLoc==-1)
        {
            user=null;
        }
        else
        {
            user=jid.substring(0,atLoc).toLowerCase();
            jid=jid.substring(atLoc + 1);
        }

        atLoc=jid.indexOf("/");
        if(atLoc==-1)
        {
            resource=null;
            domain=jid.toLowerCase();
        }
        else
        {
            domain=jid.substring(0,atLoc).toLowerCase();
            resource=jid.substring(atLoc+1).toLowerCase();
        }
    }

    @Override
    public String toString() {
        StringBuffer jid=new StringBuffer();
        if(this.user!=null)
        {
            jid.append(this.user);
            jid.append("@");
        }
        jid.append(this.domain);
        if(this.resource!=null)
        {
            jid.append("/");
            jid.append(this.resource);
        }

        return jid.toString();
    }
}
