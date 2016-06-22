package com.shane.chatbase.parser;



import com.shane.chatbase.session.Session;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;


/**
 * Created by SHANE on 2016/06/21.
 */
public class JabberInputHandler extends DefaultHandler {
    private PacketQueue packetQueue;
    private Session session;
    private Packet packet;
    private int depth=0;

    public JabberInputHandler(PacketQueue packetQueue) {
        this.packetQueue = packetQueue;
    }

    public void process(Session session) throws IOException, SAXException
    {
        SAXParser parser=new SAXParser();

        parser.setContentHandler((ContentHandler)this);

        parser.setReaderFactory(new StreamingCharFactory());
        this.session=session;

        parser.parse(new InputSource(session.getInput()));
    }

    public void startElement(String namespaceURL,String localName,String qName,Attributes atts) throws SAXException
    {
        switch(depth++)
        {
            case 0:
                if(qName.equals("stream:stream"))
                {
                    Packet openPacket=new Packet(null,qName,namespaceURL,atts);
                    openPacket.setSession(session);
                    packetQueue.push(openPacket);
                    return;
                }
                throw new SAXException("Root Element must be <stream:stream>");
            case 1:
                packet=new Packet(null,qName,namespaceURL,atts);
                packet.setSession(session);
                break;

            default:
                Packet child =new Packet(packet,qName,namespaceURL,atts);
                packet=child;
        }
    }

    @Override
    public void characters(char[] chars, int start, int length) throws jdk.internal.org.xml.sax.SAXException {
        //super.characters(chars, i, i1);
        if(depth>1)
        {
            packet.getChildren().add(new String(chars,start,length));
        }
    }

    public void endElement(String uri,String localName, String qName) throws jdk.internal.org.xml.sax.SAXException
    {
        switch (--depth)
        {
            case 0:
                Packet closePacket=new Packet("/stream:stream");
                closePacket.setSession(session);
                packetQueue.push(closePacket);
                break;

            case 1:
                packetQueue.push(packet);
                break;

            default:
                packet=packet.getParent();
        }
    }
}
