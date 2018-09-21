package br.com.splunk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

// The entry point to the client library
import com.splunk.Application;
import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

public class SplunkTest {

    public static void main(String[] args) {
    	
    	try {
    	
    	HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
    	
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setUsername("painelcliente");
        loginArgs.setPassword("A2bFENg1qlqxRvV7JnLY");
        loginArgs.setHost("bigdata.uoldiveo.intranet");
        loginArgs.setPort(8089);
        
        Service service = Service.connect(loginArgs);
        System.out.println(">> Token: "+service.getToken());
        
        System.out.println(">> Lista de aplicações:");
        for (Application app : service.getApplications().values()) {
        	
            System.out.println(app.getName());
        }
        
        
     // Create a simple search job
        String mySearch = "search index=api_sm sourcetype=incidentsResolved "
        		+ "earliest=-6mon@mon latest=@mon | dedup UOLIncident.IncidentID sortby -_time | rename UOLIncident.Company as cliente | "
        		+ "search (cliente=\\\"*\\\") (UOLIncident.ClosureCode!=\"Incidente Duplicado\" AND"
        		+ " UOLIncident.ClosureCode!=\"Incidentes Duplicado\" AND UOLIncident.ClosureCode!=\"Incidente Duplicado\" AND"
        		+ " UOLIncident.ClosureCode!=\"Encerrado por duplicidade\" AND UOLIncident.ClosureCode!=\"Rejeitado\" AND "
        		+ "UOLIncident.ClosureCode!=\"Incidente rejeitado/cancelado\") (UOLIncident.AssignmentGroup!=\"relacionamento com cliente\" AND "
        		+ "UOLIncident.AssignmentGroup!=\"sd uoldiveo rotinas\" AND UOLIncident.AssignmentGroup!=\"carta de risco\" "
        		+ "AND UOLIncident.AssignmentGroup!=\"gestao de riscos\" AND UOLIncident.AssignmentGroup!=\"uoldiveo admcli-monitoracao\") "
        		+ "(UOLIncident.ResolvedBy!=\"svcacc_ge\" AND UOLIncident.ResolvedBy!=\"svcacc_carrefour\" "
        		+ "AND UOLIncident.ResolvedBy!=\"svcacc_ambv\") | timechart span=1mon count(eval('UOLIncident.SlaBreach'=\"false\")) as Dentro, "
        		+ "count(UOLIncident.IncidentID) as Total | eval SLA=round((Dentro/Total)*100,2) | timechart "
        		+ "span=1mon sum(eval(round(SLA,1))) as \"SLA de Incidentes (X)\", sum(newY) as \"Tendencia\"";
        Job job = service.getJobs().create(mySearch);

        // Wait for the job to finish
        while (!job.isDone()) {
            Thread.sleep(500);
        }

        // Display results
        InputStream results = job.getResults();
        String line = null;
        System.out.println(">> Results from the search job as XML:\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(results, "UTF-8"));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
        
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
}