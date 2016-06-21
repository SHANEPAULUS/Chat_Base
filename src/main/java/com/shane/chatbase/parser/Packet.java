package com.shane.chatbase.parser;

import com.shane.chatbase.session.Session;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Created by SHANE on 2016/06/21.
 */
public class Packet {
    //This packet/parser needs some serious refactoring
    //it is very, very in-effecient.
    //Controller is needed with a good design pattern

    private Session session;
    private String namespace;
    private String element;
    private Packet parent;
    private LinkedList children=new LinkedList();
    private Hashtable attributes;

    public Packet(String element) {
        setElement(element);
    }

    public Packet(String element, String value) {
        setElement(element);
        children.add(value);
    }

    public Packet(Packet parent,String element, String namespace,Attributes atts) {
        setElement(element);
        setNamespace(namespace);
        setParent(parent);

        //Copying these attributes into hashtable
        for(int x=0;x<atts.getLength();x++)
        {
            attributes.put(atts.getQName(x),atts.getValue(x));
        }
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public void setParent(Packet parent) {
        if(parent!=null)
        {
            parent.children.add(this);
        }
    }

    public Packet getFirstChild(String subElement)
    {
        Iterator childIterator=children.iterator();
        while(childIterator.hasNext())
        {
            Object child=childIterator.next();
            if(child instanceof Packet)
            {
                Packet childPacket=(Packet)child;
                if(childPacket.getElement().equals(subElement))
                {
                    return childPacket;
                }
            }
        }
        return null;
    }

    public String getValue()
    {
        StringBuffer value=new StringBuffer();
        Iterator childIterator=children.iterator();
        while(childIterator.hasNext())
        {
            Object valueChild=childIterator.next();
            if(valueChild instanceof String)
            {
                value.append((String)valueChild);
            }
        }
        return value.toString();
    }

    public Session getSession() {
        if(session!=null)
        {
            return session;
        }
        if(parent!=null)
        {
            return parent.getSession();
        }
        return null;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getChildValue(String subElement)
    {
        Packet child=getFirstChild(subElement);
        if(child==null)
        {
            return null;
        }
        return child.getValue();
    }

    public String getAttributes(String attribute) {
        return (String)attributes.get(attribute);
    }

    public void setAttributes(String attribute,String value) {
        if(value==null)
        {
            removeAttribute(attribute);
        }
        else
        {
            attributes.put(attribute,value);
        }
    }

    public void removeAttribute(String attribute)
    {
        attributes.remove(attribute);
    }

    public void clearAttributes()
    {
        attributes.clear();
    }

    public String getTo(){return (String)attributes.get("to");}
    public void setTo(String recipient){setAttributes("to",recipient);}

    public String getFrom(){return (String)attributes.get("from");}
    public void setFrom(String sender){setAttributes("from",sender);}

    public String getType(){return (String)attributes.get("type");}
    public void setType(String type){setAttributes("type",type);}

    public String getID(){return (String)attributes.get("id");}
    public void setID(String ID){setAttributes("id",ID);}

    public String getNamespace() {
        return namespace;
    }

    public String getElement() {
        return element;
    }

    public Packet getParent() {
        return parent;
    }

    public LinkedList getChildren() {
        return children;
    }

    public void writeXML() throws IOException
    {
        writeXML(session.getOutput());
    }

    public void writeXML(Writer out) throws IOException
    {
        out.write("<");
        out.write(element);

        // Displaying the attributes for the element...
        Enumeration keys=attributes.keys();
        while(keys.hasMoreElements())
        {
            String key=(String)keys.nextElement();
            out.write(" ");
            out.write(key);
            out.write("='");
            out.write((String)attributes.get(key));
            out.write("'");
        }

        // Empty element
        if(children.size()==0)
        {
            out.write("/>");
            out.flush();
            return;
        }

        out.write(">");

        // Iterate over each child
        Iterator childIterator=children.iterator();
        while(childIterator.hasNext())
        {
            Object child=childIterator.next();
            // Send value to the writer
            if(child instanceof String)
            {
                out.write((String)child);
                // Or recursively write children's XML
            }

            else
            {
                ((Packet)child).writeXML(out);
            }
        }
        out.write("</");
        out.write(element);
        out.write(">");
        out.flush();
    }

    @Override
    public String toString()
    {
        try
        {
            StringWriter reply=new StringWriter();
            writeXML(reply);
            return reply.toString();
        }

        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "<"+element+">";
    }
}
