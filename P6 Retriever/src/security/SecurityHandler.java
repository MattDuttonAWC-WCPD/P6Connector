package security;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;

import oracle.security.crypto.util.Utils;
import oracle.security.xmlsec.util.Base64;
import oracle.security.xmlsec.util.XMLUtils;
import oracle.security.xmlsec.wss.WSSecurity;
import oracle.security.xmlsec.wss.WSUCreated;
import oracle.security.xmlsec.wss.WSUExpires;
import oracle.security.xmlsec.wss.WSUTimestamp;
import oracle.security.xmlsec.wss.soap.WSSOAPEnvelope;
import oracle.security.xmlsec.wss.username.UsernameToken;
import oracle.security.xmlsec.wss.util.WSSTokenUtils;
import oracle.security.xmlsec.wss.util.WSSUtils;

/**
 * SecurityHandler to enable secure connection to the P6 database.
 * NB this code has largely been taken from Oracles sample code. It has been reduced to remove functionality not used for this application.
 * 
 */
public class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

	private static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String USERNAME_TOKEN = "UsernameToken";
	private static final String TIMESTAMP_ID_PREFIX = "Timestamp-";
	private static final String SCHEMA_DATE_TIME = "http://www.w3.org/2001/XMLSchema/dateTime";


	private String user, pass;

	/**
	 * Constructor
	 * @param user P6 Username
	 * @param pass P6 Password
	 */
	public SecurityHandler (String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	/**
	 * The application utilises UsernameToken for authentication (Oracle Recommended).
	 * Creates and sets the token and the nonce for communication with P6.
	 * @param sec
	 * @param wsuId
	 * @return
	 */
	private Element addUsernameToken(WSSecurity sec, String wsuId)
	{
		// Create the basic UsernameToken information with the specified username and password
		UsernameToken unToken = WSSTokenUtils.createUsernameToken(wsuId, user, null, null, pass.toCharArray());

		// A timestamp that the server checks to see if this message has taken too long to reach the server
		unToken.setCreatedDate(new Date());

		// A token to help prevent replay attacks
		//  If a second message with the same Nonce data is sent, it would be rejected by the server
		unToken.setNonce(Base64.fromBase64(XMLUtils.randomName()));

		sec.addUsernameToken(unToken);

		return unToken.getElement();
	}

	/**
	 * Add's a timestamp to send messages.
	 * @param sec
	 * @param wsEnvelope message to add timestamp.
	 * @return String containing timestamp.
	 */
	private String addTimestamp(WSSecurity sec, WSSOAPEnvelope wsEnvelope)
	{
		WSUTimestamp timestamp = new WSUTimestamp(wsEnvelope.getOwnerDocument());
		sec.setTimestamp(timestamp);

		WSUCreated created = new WSUCreated(wsEnvelope.getOwnerDocument(), SCHEMA_DATE_TIME);
		created.setValue(new Date());

		WSUExpires expires = new WSUExpires(wsEnvelope.getOwnerDocument(), SCHEMA_DATE_TIME);
		expires.setValue(Utils.minutesFrom(new Date(), 30));
		timestamp.setCreated(created);
		timestamp.setExpires(expires);

		String rawTimestampId = TIMESTAMP_ID_PREFIX + XMLUtils.randomName();
		WSSUtils.addWsuIdToElement(rawTimestampId, timestamp.getElement());

		return rawTimestampId;
	}

	/**
	 * Creates the outgoing message in SOAP to P6. Ensures any security requirements are met.
	 * @param soapMessage content of the message to send.
	 */
	private void handleOutBoundMessage(SOAPMessage soapMessage)
	{
		try
		{
			WSSOAPEnvelope wsEnvelope = new WSSOAPEnvelope(soapMessage.getSOAPPart().getEnvelope());

			// Create the Oracle WSSecurity element so we can add security information to SOAP header
			WSSecurity sec = WSSecurity.newInstance(wsEnvelope.getOwnerDocument());
			sec.setAttributeNS("http://schemas.xmlsoap.org/soap/envelope/", "mustUnderstand", "1");
			wsEnvelope.addSecurity(sec);

			// Remember information on the authentication elements so we can encrypt and sign them later
			String authTokenId = null;
			String namespace = null;;
			String name = null;

			// Add the UsernameToken information, including Nonce token and Created time
			//  Also, store the WsuId so we can sign with it later, if encryption is enabled
			authTokenId = XMLUtils.randomName();
			addUsernameToken(sec, authTokenId);

			// Also store the namespace and name so we can load this element later
			namespace = WSSE_NAMESPACE;
			name = USERNAME_TOKEN;



			// Add Timestamp information to the header            
			String rawTimestampId = addTimestamp(sec, wsEnvelope);

		}
		catch (Exception ex)
		{
			throw new RuntimeException("Error while creating security credentials.", ex);
		}
	}

	
	/**
	 * Call back method from the Oracle Security tools. Function is called when an outbound or inbound message is sent/received.
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context)
	{
		boolean outbound = ((Boolean)context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
		SOAPMessage soapMessage = context.getMessage();

		if (outbound)
		{     
			handleOutBoundMessage(soapMessage);
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders()
	{

		return new TreeSet<QName>();
	}

}
