package write;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class LoggingHandler implements SOAPHandler<SOAPMessageContext>
{
	
	public Set<QName> getHeaders() {
        return new TreeSet<QName>();
    }

	public boolean handleMessage(SOAPMessageContext context)
    {
    	
        boolean outbound = ((Boolean)context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
        System.out.println("LoggingHandler.handleMessage called: " + (outbound ? "outbound" : "inbound"));
        dumpMsg(context, outbound);
        return true;
    }

	public void close(MessageContext context)
    {
        System.out.println("LoggingHandler.close called.");
    }

	public boolean handleFault(SOAPMessageContext context)
    {
        System.out.println("LoggingHandler.handleFault called.");
        return true;
    }

    public void dumpMsg(MessageContext context, boolean outbound) {
       try {
          SOAPMessage soapmsg = ((SOAPMessageContext)context).getMessage();
          
          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          Date date = new Date();
          System.out.println(dateFormat.format(date));
          
          System.out.println(outbound ? "HTTP Response Headers" + context.get(MessageContext.HTTP_RESPONSE_HEADERS) : "HTTP Request Headers" + context.get(MessageContext.HTTP_REQUEST_HEADERS));
          
          System.out.print(outbound ? "Outbound " : "Inbound ");
          System.out.println("SOAP Message:\n\n"+getMsgAsString(soapmsg) + "\n");

       } catch (Exception e) {
           e.printStackTrace();
       }
       return;
    }


    public String getMsgAsString(SOAPMessage message) {
        String msg  = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            msg = baos.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }
}
