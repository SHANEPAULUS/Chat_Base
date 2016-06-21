package com.shane.chatbase.parser;

import com.shane.chatbase.session.Session;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by SHANE on 2016/06/21.
 */
public class JabberInputHandler extends DefaultHandler {
    private PacketQueue packetQueue;
    private Session session;

    public JabberInputHandler(PacketQueue packetQueue) {
        this.packetQueue = packetQueue;
    }

    public void process(Session session) throws IOException, SAXException
    {
        SAXParser parser=new SAXParser();

        parser.setContentHandler(this);

        //parser.setReaderFactory(new StreamingCharFactory());
        this.session=session;

        parser.parse(new InputSource(session.getInput()));
    }
}
