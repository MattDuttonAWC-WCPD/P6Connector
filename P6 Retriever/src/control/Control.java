package control;
import java.io.File;
import java.util.List;

import com.primavera.ws.p6.resource.Resource;
import com.primavera.ws.p6.resourcehour.ResourceHour;
import com.primavera.ws.p6.resourcerate.ResourceRate;
import com.primavera.ws.p6.timesheet.Timesheet;

import read.P6Reader;
import write.SheetOutputter;

public class Control {
	
	/**
	 * Controller function to process the reading of data from P6 and output to a simple CSV file.
	 * @param outputDirectory - Directory to place CSV files
	 * @param user P6 Username
	 * @param pass P6 Pass
	 * @param host P6 host address
	 * @param port P6 port number
	 * @return true if read and output successful otherwise false if exception.
	 */
	public boolean control(String outputDirectory, String user, String pass, String host, String port) {
		try {
			P6Reader reader = new P6Reader(host, Integer.parseInt(port), user, pass);
			List<ResourceHour> resourceHours = reader.readResourceHour();
			List<Resource>resources = reader.readResources();
			List<ResourceRate>resourceRates = reader.readResourceRate();
			List<Timesheet>timesheets = reader.readTimesheets();
			
			SheetOutputter.outputResourceHour(resourceHours, new File(outputDirectory+"/ResourceHours.csv"));
			SheetOutputter.outputResources(resources, new File(outputDirectory+"/Resources.csv"));
			SheetOutputter.outputResourceRates(resourceRates, new File(outputDirectory+"/ResourceRates.csv"));
			SheetOutputter.outputTimesheets(timesheets, new File(outputDirectory+"/Timesheets.csv"));
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
