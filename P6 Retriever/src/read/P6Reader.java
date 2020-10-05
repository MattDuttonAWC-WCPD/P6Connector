package read;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.primavera.ws.p6.resource.Resource;
import com.primavera.ws.p6.resource.ResourceFieldType;
import com.primavera.ws.p6.resource.ResourcePortType;
import com.primavera.ws.p6.resource.ResourceService;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentFieldType;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentPortType;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentService;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActual;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActualFieldType;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActualPortType;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActualService;
import com.primavera.ws.p6.resourcecode.ResourceCode;
import com.primavera.ws.p6.resourcecode.ResourceCodeFieldType;
import com.primavera.ws.p6.resourcecode.ResourceCodePortType;
import com.primavera.ws.p6.resourcecode.ResourceCodeService;
import com.primavera.ws.p6.resourcecodeassignment.ResourceCodeAssignment;
import com.primavera.ws.p6.resourcecodeassignment.ResourceCodeAssignmentFieldType;
import com.primavera.ws.p6.resourcecodeassignment.ResourceCodeAssignmentPortType;
import com.primavera.ws.p6.resourcecodeassignment.ResourceCodeAssignmentService;
import com.primavera.ws.p6.resourcehour.ResourceHour;
import com.primavera.ws.p6.resourcehour.ResourceHourFieldType;
import com.primavera.ws.p6.resourcehour.ResourceHourPortType;
import com.primavera.ws.p6.resourcehour.ResourceHourService;
import com.primavera.ws.p6.resourcerate.ResourceRate;
import com.primavera.ws.p6.resourcerate.ResourceRateFieldType;
import com.primavera.ws.p6.resourcerate.ResourceRatePortType;
import com.primavera.ws.p6.resourcerate.ResourceRateService;
import com.primavera.ws.p6.timesheet.Timesheet;
import com.primavera.ws.p6.timesheet.TimesheetFieldType;
import com.primavera.ws.p6.timesheet.TimesheetPortType;
import com.primavera.ws.p6.timesheet.TimesheetService;
import com.primavera.ws.p6.user.User;
import com.primavera.ws.p6.user.UserFieldType;
import com.primavera.ws.p6.user.UserPortType;
import com.primavera.ws.p6.user.UserService;

import security.SecurityHandler;
import write.LoggingHandler;

/**
 * Class to contact P6 and request copies of the ResourceAssignmentPeriodActuals and Timesheet tables.
 * @author Matt Dutton
 * 
 *
 */
public class P6Reader {

	private static final String RESOURCEHOUR_SERVICE = "/p6ws/services/ResourceHourService";
	private static final String RESOURCERATE_SERVICE = "/p6ws/services/ResourceRateService";
	private static final String RESOURCE_SERVICE = "/p6ws/services/ResourceService";
	private static final String TIMESHEET_SERVICE = "/p6ws/services/TimesheetService";
	private static final String RESOURCE_ASSIGNMENT = "/p6ws/services/ResourceAssignmentService";
	private static final String USER_SERVICE = "/p6ws/services/UserService";
	private static final String RESOURCE_CODE_SERVICE = "/p6ws/services/ResourceCodeService";
	private static final String RESOURCE_CODE_ASSIGNMENT_SERVICE = "/p6ws/services/ResourceCodeAssignmentService";
	private static final String RESOURCE_ASSIGNMENT_PERIOD_ACTUAL_SERVICE = "/p6ws/services/ResourceAssignmentPeriodActualService";

	private String host,user,pass;
	private int port;

	/**
	 * Constructor
	 * @param host - Host of P6 Web-services
	 * @param port - Port number (Should be 443) as set by Milestone/Epic
	 * @param user - Username
	 * @param pass - Password
	 */
	public P6Reader (String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
	}

	/**
	 * Reads a copy of the ResourceHour table. Only	
	 * Object ID, Project Object ID, ResourceObject ID, Status
	 * TimesheetPeriodObjectID, Unapproved & Approved Hours, Date and Project name should be populated.
	 * All other field values will be null as not required.
	 * @return
	 * List of {@link ResourceHour} values. (All records in table returned).
	 * @throws Exception
	 * Will throw {@link SSLException} if certificate invalid or not loaded.
	 */
	public List<ResourceHour>readResourceHour() throws Exception{
		ResourceHourPortType rhpt = createResouceHourPort(host, port);
		List<ResourceHourFieldType> fields = new ArrayList<>();
		fields.add(ResourceHourFieldType.OBJECT_ID);
		fields.add(ResourceHourFieldType.PROJECT_OBJECT_ID);
		fields.add(ResourceHourFieldType.RESOURCE_OBJECT_ID);
		fields.add(ResourceHourFieldType.STATUS);
		fields.add(ResourceHourFieldType.TIMESHEET_PERIOD_OBJECT_ID);
		fields.add(ResourceHourFieldType.UNAPPROVED_HOURS);
		fields.add(ResourceHourFieldType.APPROVED_HOURS);
		fields.add(ResourceHourFieldType.DATE);
		fields.add(ResourceHourFieldType.PROJECT_NAME);
		fields.add(ResourceHourFieldType.LAST_UPDATE_DATE);
		fields.add(ResourceHourFieldType.LAST_UPDATE_USER);

		List<ResourceHour> resourceHours = rhpt.readResourceHours(fields, null, null);

		return resourceHours;
	}

	/**
	 * Reads a copy of the ResourceRate table.
	 * Only the following {@link ResourceRateFieldType} will be loaded:
	 * Effective Date and ResourceObject ID. All other fields set to null as not required.
	 * @return List of {@link ResourceRate} objects
	 * @throws Exception Will throw {@link SSLException} if certificate invalid or not loaded.
	 */

	public List<ResourceRate> readResourceRate() throws Exception{
		ResourceRatePortType rrpt = createResourceRatePort(host, port);
		List<ResourceRateFieldType> fields = new ArrayList<>();
		fields.add(ResourceRateFieldType.EFFECTIVE_DATE);
		fields.add(ResourceRateFieldType.RESOURCE_OBJECT_ID);

		List<ResourceRate> resourceRates = rrpt.readResourceRates(fields, null, null);
		return resourceRates;
	}


	/**
	 * Reads a copy of the Resource table.
	 * Only the following {@link ResourceFieldType} will be loaded:
	 * ObjectID, Name, Timesheet Approval Manager, Uses Timesheets. All other fields set to null as not required.
	 * @return List of {@link Resource} objects
	 * @throws Exception Will throw {@link SSLException} if certificate invalid or not loaded.
	 */
	public List<Resource> readResources() throws Exception{
		ResourcePortType rpt = createResourcePort(host,port);
		List<ResourceFieldType> fields = new ArrayList<>();
		fields.add(ResourceFieldType.OBJECT_ID);
		fields.add(ResourceFieldType.NAME);
		fields.add(ResourceFieldType.TIMESHEET_APPROVAL_MANAGER);
		fields.add(ResourceFieldType.USE_TIMESHEETS);
		fields.add(ResourceFieldType.USER_OBJECT_ID);

		List<Resource> resources = rpt.readResources(fields, null, null);
		return resources;
	}
	
	public List<ResourceAssignment>readResourceAssignments() throws Exception{
		ResourceAssignmentPortType rapt = createResourceAssignmentPort(host, port);
		List<ResourceAssignmentFieldType> fields = new ArrayList<ResourceAssignmentFieldType>();
		fields.add(ResourceAssignmentFieldType.RESOURCE_OBJECT_ID);
		fields.add(ResourceAssignmentFieldType.ACTUAL_UNITS);
		fields.add(ResourceAssignmentFieldType.PROJECT_ID);
		fields.add(ResourceAssignmentFieldType.OBJECT_ID);
		fields.add(ResourceAssignmentFieldType.LAST_UPDATE_DATE);
		fields.add(ResourceAssignmentFieldType.LAST_UPDATE_USER);
		
		List<ResourceAssignment> resAss = rapt.readResourceAssignments(fields, null, null);
		return resAss;
	}
	
	/**
	 * Reads a copy of the Resource table.
	 * Only the following {@link ResourceFieldType} will be loaded:
	 * ObjectID, Name, Timesheet Approval Manager, Uses Timesheets. All other fields set to null as not required.
	 * @return List of {@link Resource} objects
	 * @throws Exception Will throw {@link SSLException} if certificate invalid or not loaded.
	 */
	public List<ResourceCode> readResourceCodes() throws Exception{
		ResourceCodePortType rpt = createResourceCodePort(host,port);
		List<ResourceCodeFieldType> fields = new ArrayList<>();
		fields.add(ResourceCodeFieldType.CODE_TYPE_NAME);
		fields.add(ResourceCodeFieldType.CODE_VALUE);
		fields.add(ResourceCodeFieldType.OBJECT_ID);

		List<ResourceCode> resourceCodes = rpt.readResourceCodes(fields, null, null);
		return resourceCodes;
	}
	
	public List<ResourceCodeAssignment> readResourceCodeAssignments() throws Exception{
		ResourceCodeAssignmentPortType rpt = createResourceCodeAssignmentPort(host,port);
		List<ResourceCodeAssignmentFieldType> fields = new ArrayList<>();
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_CODE_OBJECT_ID);
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_CODE_VALUE);
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_CODE_TYPE_NAME);
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_OBJECT_ID);
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_ID);
		fields.add(ResourceCodeAssignmentFieldType.RESOURCE_CODE_DESCRIPTION);

		List<ResourceCodeAssignment> resourceCodeAssignments = rpt.readResourceCodeAssignments(fields, null, null);
		return resourceCodeAssignments;
	}
	
	public List<User>readUsers() throws Exception{
		UserPortType upt = createUserPort(host, port);
		List<UserFieldType> fields = new ArrayList<UserFieldType>();
		fields.add(UserFieldType.CREATE_DATE);
		fields.add(UserFieldType.NAME);
		fields.add(UserFieldType.PERSONAL_NAME);
		fields.add(UserFieldType.EMAIL_ADDRESS);
		
		List<User> users = upt.readUsers(fields, null, null);
		return users;
	}
	
	public List<com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActual> readReassActual() throws Exception{
		ResourceAssignmentPeriodActualPortType rappt = createReassPort(host, port);
		List<ResourceAssignmentPeriodActualFieldType> fields = new ArrayList<ResourceAssignmentPeriodActualFieldType>();
		fields.add(ResourceAssignmentPeriodActualFieldType.RESOURCE_ASSIGNMENT_OBJECT_ID);
		fields.add(ResourceAssignmentPeriodActualFieldType.ACTUAL_UNITS);
		fields.add(ResourceAssignmentPeriodActualFieldType.FINANCIAL_PERIOD_OBJECT_ID);
		fields.add(ResourceAssignmentPeriodActualFieldType.LAST_UPDATE_DATE);
		fields.add(ResourceAssignmentPeriodActualFieldType.LAST_UPDATE_USER);
		
		List<ResourceAssignmentPeriodActual> reasses = rappt.readResourceAssignmentPeriodActuals(fields, null, null);
		return reasses;
	}


	/**
	 * Reads a copy of the Timesheet table.
	 * Only the following {@link TimesheetFieldType} will be loaded:
	 * TimesheetPeriodObjectID, ResourceObjectID, Status All other fields set to null as not required.
	 * @return List of {@link Timesheet} objects
	 * @throws Exception Will throw {@link SSLException} if certificate invalid or not loaded.
	 */
	public List<Timesheet>readTimesheets() throws Exception{
		TimesheetPortType tpr = createTimesheetPort(host, port);
		List<TimesheetFieldType> fields = new ArrayList<>();
		fields.add(TimesheetFieldType.TIMESHEET_PERIOD_OBJECT_ID);
		fields.add(TimesheetFieldType.RESOURCE_OBJECT_ID);
		fields.add(TimesheetFieldType.STATUS);

		List<Timesheet> timesheets = tpr.readTimesheets(fields, null, null);
		return timesheets;
	}

	/**
	 * Creates a SOAP port for Reading {@link ResourceHour} from P6 Live
	 * @param hostname p6 host address
	 * @param port port number
	 * @return {@link ResourceHourPortType} to load data from.
	 * @throws Exception
	 */

	private ResourceHourPortType createResouceHourPort(String hostname, int port)throws Exception {

		String url = makeHttpURLString(hostname, port, RESOURCEHOUR_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceHourService service = new ResourceHourService (wsdlURL);
		ResourceHourPortType servicePort ;
		servicePort = service.getResourceHourPort();


		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}
	
	private UserPortType createUserPort(String host, int port)throws Exception{
		String url = makeHttpURLString(host, port, USER_SERVICE, true);
		URL wsdlURL = new URL(url);
		UserService service = new UserService(wsdlURL);
		UserPortType userPort = service.getUserPort();
		setUserTokenData((BindingProvider)userPort);
		return userPort;
	}

	/**
	 * Creates a SOAP port for Reading {@link Resource} from P6 Live
	 * @param hostname p6 host address
	 * @param port port number
	 * @return {@link ResourcePortType} to load data from.
	 * @throws Exception
	 */
	private ResourcePortType createResourcePort(String hostname, int port)throws Exception {

		String url = makeHttpURLString(hostname, port, RESOURCE_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceService service = new ResourceService (wsdlURL);
		ResourcePortType servicePort ;
		servicePort = service.getResourcePort();


		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}
	
	private ResourceAssignmentPeriodActualPortType createReassPort(String host, int port)throws Exception{

		String url = makeHttpURLString(host, port, RESOURCE_ASSIGNMENT_PERIOD_ACTUAL_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceAssignmentPeriodActualService service = new ResourceAssignmentPeriodActualService(wsdlURL);
		ResourceAssignmentPeriodActualPortType  servicePort ;
		servicePort = service.getResourceAssignmentPeriodActualPort();


		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}
	
	
	private ResourceCodePortType createResourceCodePort(String hostname, int port)throws Exception {

		String url = makeHttpURLString(hostname, port, RESOURCE_CODE_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceCodeService service = new ResourceCodeService (wsdlURL);
		ResourceCodePortType servicePort ;
		servicePort = service.getResourceCodePort();
		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}
	
	
	private ResourceCodeAssignmentPortType createResourceCodeAssignmentPort(String hostname, int port)throws Exception {

		String url = makeHttpURLString(hostname, port, RESOURCE_CODE_ASSIGNMENT_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceCodeAssignmentService service = new ResourceCodeAssignmentService (wsdlURL);
		ResourceCodeAssignmentPortType servicePort ;
		servicePort = service.getResourceCodeAssignmentPort();
		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}

	/**
	 * Creates a SOAP port for Reading {@link ResourceRate} from P6 Live
	 * @param hostname p6 host address
	 * @param port port number
	 * @return {@link ResourceRatePortType} to load data from.
	 * @throws Exception
	 */
	private ResourceRatePortType createResourceRatePort(String hostname, int port)throws Exception{
		String url = makeHttpURLString(hostname, port, RESOURCERATE_SERVICE, true);
		URL wsdlURL = new URL(url);
		ResourceRateService service = new ResourceRateService (wsdlURL);
		ResourceRatePortType servicePort ;
		servicePort = service.getResourceRatePort();
		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}


	/**
	 * Creates a SOAP port for Reading {@link Timesheet} from P6 Live
	 * @param hostname p6 host address
	 * @param port port number
	 * @return {@link TimesheetPortType} to load data from.
	 * @throws Exception
	 */
	private TimesheetPortType createTimesheetPort (String hostname, int port) throws Exception{
		String url = makeHttpURLString(hostname, port, TIMESHEET_SERVICE, true);
		URL wsdlURL = new URL(url);
		TimesheetService service = new TimesheetService (wsdlURL);
		TimesheetPortType servicePort ;
		servicePort = service.getTimesheetPort();
		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}

	/**
	 * Creates a SOAP port for Reading {@link ResourceAssignment} from P6 Live
	 * @param hostname p6 host address
	 * @param port port number
	 * @return {@link ResourceAssignmentPortType} to load data from.
	 * @throws Exception
	 */
	private ResourceAssignmentPortType createResourceAssignmentPort (String hostname, int port) throws Exception {
		String url = makeHttpURLString(hostname, port, RESOURCE_ASSIGNMENT, true);
		URL wsdlURL = new URL(url);
		ResourceAssignmentService service = new ResourceAssignmentService(wsdlURL);
		ResourceAssignmentPortType servicePort ;
		servicePort = service.getResourceAssignmentPort();
		setUserTokenData((BindingProvider)servicePort);

		return servicePort;
	}



	private void setUserTokenData(BindingProvider bp)
	{

		Binding binding = bp.getBinding();
		List<Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(new LoggingHandler());
		handlerChain.add(new SecurityHandler(user,pass));
		binding.setHandlerChain(handlerChain);

	}

	private String makeHttpURLString(String hostname, int port, String suffix, boolean bUseSSL)
	{
		StringBuilder sb = new StringBuilder("HTTPS://");
		sb.append(hostname).append(":").append(port).append(suffix);

		return sb.toString();
	}
}
