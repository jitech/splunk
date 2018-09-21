package br.com.splunk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SplunkIntegration {

	public static void main(String[] args) throws IOException {
		
		ProcessBuilder pb = new ProcessBuilder("curl -u painelcliente:A2bFENg1qlqxRvV7JnLY -k https://bigdata.uoldiveo.intranet:8089/services/search/jobs -d search=\"search index=api_sm sourcetype=incidentsResolved "
        		+ "earliest=-6mon@mon latest=@mon | dedup UOLIncident.IncidentID sortby -_time | rename UOLIncident.Company as cliente | "
        		+ "search (cliente=\"*\") (UOLIncident.ClosureCode!=\"Incidente Duplicado\" AND"
        		+ " UOLIncident.ClosureCode!=\"Incidentes Duplicado\" AND UOLIncident.ClosureCode!=\"Incidente Duplicado\" AND"
        		+ " UOLIncident.ClosureCode!=\"Encerrado por duplicidade\" AND UOLIncident.ClosureCode!=\"Rejeitado\" AND "
        		+ "UOLIncident.ClosureCode!=\"Incidente rejeitado/cancelado\") (UOLIncident.AssignmentGroup!=\"relacionamento com cliente\" AND "
        		+ "UOLIncident.AssignmentGroup!=\"sd uoldiveo rotinas\" AND UOLIncident.AssignmentGroup!=\"carta de risco\" "
        		+ "AND UOLIncident.AssignmentGroup!=\"gestao de riscos\" AND UOLIncident.AssignmentGroup!=\"uoldiveo admcli-monitoracao\") "
        		+ "(UOLIncident.ResolvedBy!=\"svcacc_ge\" AND UOLIncident.ResolvedBy!=\"svcacc_carrefour\" "
        		+ "AND UOLIncident.ResolvedBy!=\"svcacc_ambv\") | timechart span=1mon count(eval('UOLIncident.SlaBreach'=\"false\")) as Dentro, "
        		+ "count(UOLIncident.IncidentID) as Total | eval SLA=round((Dentro/Total)*100,2) | timechart "
        		+ "span=1mon sum(eval(round(SLA,1))) as \"SLA de Incidentes (X)\", sum(newY) as \"Tendencia\"");
		
		File diretorio = new File("/home/jonas/Splunk");
		if (!diretorio.exists()) {
		   diretorio.mkdirs(); 
		} else {
		   System.out.println("Diretório já existente");
		}
		
		
	    pb.directory(new File("/home/jonas/Splunk"));
	    pb.redirectErrorStream(true);
	    Process p = pb.start();
	    InputStream is = p.getInputStream();

	    FileOutputStream outputStream = new FileOutputStream("/home/jonas/Splunk/teste.json");

	    BufferedInputStream bis = new BufferedInputStream(is);
	    byte[] bytes = new byte[100];
	    int numberByteReaded;
	    
	    while ((numberByteReaded = bis.read(bytes, 0, 100)) != -1) {
	        outputStream.write(bytes, 0, numberByteReaded);
	        Arrays.fill(bytes, (byte) 0);
	    }

	    outputStream.flush();
	    outputStream.close();
	}
}
