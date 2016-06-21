package com.shane.chatbase.session;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by SHANE on 2016/06/21.
 */
public class Session {
    //Refactoring needed..
    private JabberID jabberID;
    private String streamID;
    private Socket socket;
    private Writer output;
    private Reader input;
    private LinkedList statusListeners=new LinkedList();
    private int status;

    public static final int DISCONNECTED=1;
    public static final int CONNECTED=2;
    public static final int STREAMING=3;
    public static final int AUTHENTICATED=4;


    public Session(Socket socket) {
        this.socket=socket;
    }
    public Session() {
        setStatus(DISCONNECTED);
    }

    public JabberID getJabberID() {
        return jabberID;
    }

    public String getStreamID() {
        return streamID;
    }

    public Socket getSocket() {
        return socket;
    }

    public Writer getOutput() throws IOException {
        if(this.output==null)
        {
            output= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        return this.output;
    }

    public Reader getInput() throws IOException {
        if(this.input==null)
        {
            input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        return this.input;
    }

    public Boolean addStatusListener(StatusListener listener)
    {
        return statusListeners.add(listener);
    }

    public Boolean removeStatusListener(StatusListener listener)
    {
        return statusListeners.remove(listener);
    }

    public int getStatus() { return status;  }

    public void setJabberID(JabberID jabberID) {
        this.jabberID = jabberID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOutput(Writer output) {
        this.output = output;
    }

    public void setInput(Reader input) {
        this.input = input;
    }

    public synchronized void setStatus(int newStatus)
    {
        status=newStatus;
        ListIterator iterator=statusListeners.listIterator();
        while(iterator.hasNext())
        {
            StatusListener listener=(StatusListener)iterator.next();
            listener.notify(status);
        }
    }
}
