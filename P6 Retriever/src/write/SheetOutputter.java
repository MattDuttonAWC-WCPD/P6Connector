package write;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;
import com.primavera.ws.p6.resource.Resource;
import com.primavera.ws.p6.resource.ResourceFieldType;
import com.primavera.ws.p6.resourceassignment.ResourceAssignment;
import com.primavera.ws.p6.resourceassignment.ResourceAssignmentFieldType;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActual;
import com.primavera.ws.p6.resourceassignmentperiodactual.ResourceAssignmentPeriodActualFieldType;
import com.primavera.ws.p6.resourcehour.ResourceHour;
import com.primavera.ws.p6.resourcehour.ResourceHourFieldType;
import com.primavera.ws.p6.resourcerate.ResourceRate;
import com.primavera.ws.p6.resourcerate.ResourceRateFieldType;
import com.primavera.ws.p6.timesheet.Timesheet;
import com.primavera.ws.p6.timesheet.TimesheetFieldType;

/**
 * Class to output the loaded information from P6 to CSV files.
 * Each table exported has it's own CSV file.
 * Files currently supported:
 * Timesheets
 * Resource
 * ResourceHour
 * ResourceRate
 * TimesheetPeriod. 
 */
public class SheetOutputter {
	
	
	/**
	 * Statics for the count of fields in each table that are used.
	 */
	private static final int TIMESHEET_FIELDS = 3;
	private static final int RESOURCEHOUR_FIELDS = 11;
	private static final int RESOURCERATE_FIELDS = 2;
	private static final int RESOURCE_FIELDS = 4;
	private static final int RESOURCE_ASS_P_A_FIELDS = 5;
	private static final int RESOURCE_ASSIGNMENT_FIELDS = 6;
	
	/**
	 * Creates Timesheets.csv for a List of {@link Timesheet} records.
	 * @param timesheets list of {@link Timesheet}s to output.
	 * @param toWriteTo {@link File} to output to
	 * @throws IOException
	 */
	public static void outputTimesheets(List<Timesheet>timesheets, File toWriteTo) throws IOException {
		String [] header = new String [TIMESHEET_FIELDS];
		for(int i = 0; i< TIMESHEET_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = TimesheetFieldType.TIMESHEET_PERIOD_OBJECT_ID.name();
				break;
			case 1:
				header[i] = TimesheetFieldType.RESOURCE_OBJECT_ID.name();
				break;
			case 2:
				header[i] = TimesheetFieldType.STATUS.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(Timesheet timesheet : timesheets) {
			String [] valueRow = new String [TIMESHEET_FIELDS];
			for(int i = 0; i< TIMESHEET_FIELDS; i++) {
				switch(i) {
				case 0:
					valueRow[i] = timesheet.getTimesheetPeriodObjectId().toString();
					break;
				case 1:
					valueRow[i] = timesheet.getResourceObjectId().toString();
					break;
				case 2:
					valueRow[i] = timesheet.getStatus();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
	
	public static void outputReassignmentPeriodActuals(List<ResourceAssignmentPeriodActual> actuals, File toWriteTo) throws IOException{
		String [] header = new String [RESOURCE_ASS_P_A_FIELDS];
		for(int i = 0; i< RESOURCE_ASS_P_A_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = ResourceAssignmentPeriodActualFieldType.RESOURCE_ASSIGNMENT_OBJECT_ID.name();
				break;
			case 1:
				header[i] = ResourceAssignmentPeriodActualFieldType.ACTUAL_UNITS.name();
				break;
			case 2:
				header[i] = ResourceAssignmentPeriodActualFieldType.FINANCIAL_PERIOD_OBJECT_ID.name();
				break;
			case 3:
				header[i] = ResourceAssignmentPeriodActualFieldType.LAST_UPDATE_DATE.name();
				break;
			case 4:
				header[i] = ResourceAssignmentPeriodActualFieldType.LAST_UPDATE_USER.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(ResourceAssignmentPeriodActual actual : actuals) {
			String [] valueRow = new String [RESOURCE_ASS_P_A_FIELDS];
			for(int i = 0; i< RESOURCE_ASS_P_A_FIELDS; i++) {
				switch(i) {
				case 0:
					valueRow[i] = actual.getResourceAssignmentObjectId().toString();
					break;
				case 1:
					valueRow[i] = actual.getActualUnits().getValue().toString();
					break;
				case 2:
					valueRow[i] = actual.getFinancialPeriodObjectId().toString();
					break;
				case 3:
					valueRow[i] = actual.getLastUpdateDate().getValue().toString();
					break;
				case 4:
					valueRow[i] = actual.getLastUpdateUser();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
	
	public static void outputReassignmens(List<ResourceAssignment> assignments, File toWriteTo) throws IOException{
		String [] header = new String [RESOURCE_ASSIGNMENT_FIELDS];
		for(int i = 0; i< RESOURCE_ASSIGNMENT_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = ResourceAssignmentFieldType.RESOURCE_OBJECT_ID.name();
				break;
			case 1:
				header[i] = ResourceAssignmentFieldType.ACTUAL_UNITS.name();
				break;
			case 2:
				header[i] = ResourceAssignmentFieldType.PROJECT_ID.name();
				break;
			case 3:
				header[i] = ResourceAssignmentFieldType.OBJECT_ID.name();
				break;
			case 5:
				header[i] = ResourceAssignmentFieldType.LAST_UPDATE_DATE.name();
				break;
			case 6:
				header[i] = ResourceAssignmentFieldType.LAST_UPDATE_USER.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(ResourceAssignment assignment : assignments) {
			String [] valueRow = new String [RESOURCE_ASSIGNMENT_FIELDS];
			for(int i = 0; i< RESOURCE_ASSIGNMENT_FIELDS; i++) {
				switch(i) {
				case 0:
					if(assignment.getResourceObjectId().getValue() != null) {
						valueRow[i] = assignment.getResourceObjectId().getValue().toString();
					}else {
						valueRow[i] = "";
					}
					
					break;
				case 1:
					valueRow[i] = assignment.getActualUnits().getValue().toString();
					break;
				case 2:
					valueRow[i] = assignment.getProjectId().toString();
					break;
				case 3:
					valueRow[i] = assignment.getObjectId().toString();
					break;
				case 4:
					valueRow[i] = assignment.getLastUpdateDate().getValue().toString();
					break;
				case 5:
					valueRow[i] = assignment.getLastUpdateUser();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
	
	/**
	 * Creates ResourceHour.csv for a List of {@link ResourceHour} records.
	 * @param ResourceHour list of {@link ResourceHour} to output.
	 * @param toWriteTo {@link File} to output to
	 * @throws IOException
	 */
	
	public static void outputResourceHour(List<ResourceHour>resourceHours, File toWriteTo) throws IOException {
		String [] header = new String [RESOURCEHOUR_FIELDS];
		for(int i = 0; i< RESOURCEHOUR_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = ResourceHourFieldType.OBJECT_ID.name();
				break;
			case 1:
				header[i] = ResourceHourFieldType.PROJECT_OBJECT_ID.name();
				break;
			case 2:
				header[i] = ResourceHourFieldType.RESOURCE_OBJECT_ID.name();
				break;
			case 3:
				header[i] = ResourceHourFieldType.STATUS.name();
				break;
			case 4:
				header[i] = ResourceHourFieldType.TIMESHEET_PERIOD_OBJECT_ID.name();
				break;
			case 5:
				header[i] = ResourceHourFieldType.UNAPPROVED_HOURS.name();
				break;
			case 6:
				header[i] = ResourceHourFieldType.APPROVED_HOURS.name();
				break;
			case 7:
				header[i] = ResourceHourFieldType.DATE.name();
				break;
			case 8:
				header[i] = ResourceHourFieldType.PROJECT_NAME.name();
				break;
			case 9:
				header[i] = ResourceHourFieldType.LAST_UPDATE_DATE.name();
				break;
			case 10:
				header[i] = ResourceHourFieldType.LAST_UPDATE_USER.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(ResourceHour resourceHour : resourceHours) {
			String [] valueRow = new String [RESOURCEHOUR_FIELDS];
			for(int i = 0; i< RESOURCEHOUR_FIELDS; i++) {
				switch(i) {
				case 0:
					valueRow [i] = resourceHour.getObjectId().toString();
					break;
				case 1:
					if(resourceHour.getProjectObjectId().getValue()!=null) {
						valueRow [i] = resourceHour.getProjectObjectId().getValue().toString();
					}else {
						valueRow [i] = "";
					}
					break;
				case 2:
					valueRow [i] = resourceHour.getResourceObjectId().toString();
					break;
				case 3:
					valueRow [i] = resourceHour.getStatus().toString();
					break;
				case 4:
					valueRow [i] = resourceHour.getTimesheetPeriodObjectId().toString();
					break;
				case 5:
					if(resourceHour.getUnapprovedHours().getValue()!=null) {
						valueRow [i] = resourceHour.getUnapprovedHours().getValue().toString();
					}else {
						valueRow [i] = "";
					}
					break;
				case 6:
					if(resourceHour.getApprovedHours().getValue()!=null) {
						valueRow [i] = resourceHour.getApprovedHours().getValue().toString();
					}else {
						valueRow[i]= "";
					}
					
					break;
				case 7:
					if(resourceHour.getDate().getValue() != null) {
						valueRow [i] = resourceHour.getDate().getValue().toString();
					}else {
						valueRow [i] = "";
					}
					
					break;
				case 8:
					valueRow [i] = resourceHour.getProjectName().toString();
					break;
				case 9:
					valueRow [i] = resourceHour.getLastUpdateDate().getValue().toString();
					break;
				case 10:
					valueRow [i] = resourceHour.getLastUpdateUser();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
	
	
	/**
	 * Creates ResourceRate.csv for a List of {@link ResourceRate} records.
	 * @param resourceRates list of {@link ResourceRate} to output.
	 * @param toWriteTo {@link File} to output to
	 * @throws IOException
	 */
	public static void outputResourceRates(List<ResourceRate>resourceRates, File toWriteTo) throws IOException {
		String [] header = new String [RESOURCERATE_FIELDS];
		for(int i = 0; i< RESOURCERATE_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = ResourceRateFieldType.EFFECTIVE_DATE.name();
				break;
			case 1:
				header[i] = ResourceRateFieldType.RESOURCE_OBJECT_ID.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(ResourceRate rates : resourceRates) {
			String [] valueRow = new String [RESOURCERATE_FIELDS];
			for(int i = 0; i< RESOURCERATE_FIELDS; i++) {
				switch(i) {
				case 0:
					valueRow[i] = rates.getEffectiveDate().toString();
					break;
				case 1:
					valueRow[i] = rates.getResourceObjectId().toString();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
	
	public static void outputResources(List<Resource>resources, File toWriteTo) throws IOException {
		String [] header = new String [RESOURCE_FIELDS];
		for(int i = 0; i< RESOURCE_FIELDS; i++) {
			switch(i) {
			case 0:
				header[i] = ResourceFieldType.OBJECT_ID.name();
				break;
			case 1:
				header[i] = ResourceFieldType.NAME.name();
				break;
			case 2:
				header[i] = ResourceFieldType.TIMESHEET_APPROVAL_MANAGER.name();
				break;
			case 3:
				header[i] = ResourceFieldType.USE_TIMESHEETS.name();
				break;
			default:
				break;
			}
		}
		CSVWriter writer = new CSVWriter(new FileWriter(toWriteTo));
		writer.writeNext(header);
		
		for(Resource resource : resources) {
			String [] valueRow = new String [RESOURCE_FIELDS];
			for(int i = 0; i< RESOURCE_FIELDS; i++) {
				switch(i) {
				case 0:
					valueRow[i] = resource.getObjectId().toString();
					break;
				case 1:
					valueRow[i] = resource.getName().toString();
					break;
				case 2:
					valueRow[i] = resource.getTimesheetApprovalManager().toString();
					break;
				case 3:
					valueRow[i] = resource.isUseTimesheets().toString();
					break;
				default:
					break;
				}
			}
			writer.writeNext(valueRow);
		}
		writer.flush();
		writer.close();
	}
}
