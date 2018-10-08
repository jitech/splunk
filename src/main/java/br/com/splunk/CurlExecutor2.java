package br.com.splunk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CurlExecutor2 {

	public static void main(String[] args) throws InterruptedException {

		String username = "painelcliente";
		String password = "A2bFENg1qlqxRvV7JnLY";
		String url = "https://bigdata.uoldiveo.intranet:8089/services/search/jobs";
		String search = "search=\\\"search index=api_sm sourcetype=incidentsResolved \"\n" + 
				"        		+ \"earliest=-6mon@mon latest=@mon | dedup UOLIncident.IncidentID sortby -_time | rename UOLIncident.Company as cliente | \"\n" + 
				"        		+ \"search (cliente=\\\"*\\\") (UOLIncident.ClosureCode!=\\\"Incidente Duplicado\\\" AND\"\n" + 
				"        		+ \" UOLIncident.ClosureCode!=\\\"Incidentes Duplicado\\\" AND UOLIncident.ClosureCode!=\\\"Incidente Duplicado\\\" AND\"\n" + 
				"        		+ \" UOLIncident.ClosureCode!=\\\"Encerrado por duplicidade\\\" AND UOLIncident.ClosureCode!=\\\"Rejeitado\\\" AND \"\n" + 
				"        		+ \"UOLIncident.ClosureCode!=\\\"Incidente rejeitado/cancelado\\\") (UOLIncident.AssignmentGroup!=\\\"relacionamento com cliente\\\" AND \"\n" + 
				"        		+ \"UOLIncident.AssignmentGroup!=\\\"sd uoldiveo rotinas\\\" AND UOLIncident.AssignmentGroup!=\\\"carta de risco\\\" \"\n" + 
				"        		+ \"AND UOLIncident.AssignmentGroup!=\\\"gestao de riscos\\\" AND UOLIncident.AssignmentGroup!=\\\"uoldiveo admcli-monitoracao\\\") \"\n" + 
				"        		+ \"(UOLIncident.ResolvedBy!=\\\"svcacc_ge\\\" AND UOLIncident.ResolvedBy!=\\\"svcacc_carrefour\\\" \"\n" + 
				"        		+ \"AND UOLIncident.ResolvedBy!=\\\"svcacc_ambv\\\") | timechart span=1mon count(eval('UOLIncident.SlaBreach'=\\\"false\\\")) as Dentro, \"\n" + 
				"        		+ \"count(UOLIncident.IncidentID) as Total | eval SLA=round((Dentro/Total)*100,2) | timechart \"\n" + 
				"        		+ \"span=1mon sum(eval(round(SLA,1))) as \\\"SLA de Incidentes (X)\\\", sum(newY) as \\\"Tendencia\\\"";
		
		//cURL Command: curl -u admin:admin -X POST -F cmd="lockPage" -F path="/content/geometrixx/en/toolbar/contacts" -F "_charset_"="utf-8" http://localhost:4502/bin/wcmcommand
		
		//Equivalent command conversion for Java execution
		String[] command = { "curl", "-u", username + ":" + password, "-k", url, "-d", search};
		
		//String[] command = { "curl", "-k", "http://www.google.com.br"};

		ProcessBuilder process = new ProcessBuilder(command);
		Process p;
		try {
			p = process.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			
			Thread.sleep(10000);
			
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			
			if(result.isEmpty()) {
				System.out.print("Nenhum retorno!");
			}else {
				System.out.print(result);
			}
			

		} catch (IOException e) {
			System.out.print("error");
			e.printStackTrace();
		}
	}
}