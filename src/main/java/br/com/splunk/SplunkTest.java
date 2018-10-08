package br.com.splunk;

import com.splunk.HttpService;
import com.splunk.Job;
import com.splunk.SSLSecurityProtocol;
import com.splunk.SavedSearch;
import com.splunk.SavedSearchCollection;
import com.splunk.SavedSearchDispatchArgs;
import com.splunk.Service;
import com.splunk.ServiceArgs;
import com.splunk.User;
import com.splunk.UserCollection;

public class SplunkTest {

    public static void main(String[] args) throws InterruptedException {
    	 	
    	HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
    	
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setUsername("painelcliente");
        loginArgs.setPassword("A2bFENg1qlqxRvV7JnLY");
        loginArgs.setHost("bigdata.uoldiveo.intranet");
        loginArgs.setPort(8089);
        
        	
        Service service = Service.connect(loginArgs);
        
        
        /*UserCollection users = service.getUsers();
        System.out.println(users.size() + " users");
        for (User user : users.values()) {
        	System.out.println("     " + user.getName());
        }*/
        
        
        /*String myQuery = "* | head 10"; 
        String mySearchName = "Test Search";
        SavedSearch savedSearch = service.getSavedSearches().create(mySearchName, myQuery);
        System.out.println("The search '" + savedSearch.getName() + 
             "' (" + savedSearch.getSearch() + ") was saved");*/
        

        ServiceArgs namespace = new ServiceArgs();
        namespace.setApp("search");
        namespace.setOwner("painelcliente");
        SavedSearchCollection savedSearches = service.getSavedSearches(namespace);
        System.out.println(savedSearches.size() + " saved searches are available to painelcliente");
        for (SavedSearch search : savedSearches.values()) {
            System.out.println("     " + search.getName());
        }
        
        
        
     // Retrieve the new saved search
        SavedSearch search = service.getSavedSearches().get("API SM - lista de Clientes e CNPJ");

        //SavedSearchDispatchArgs dispatchArgs = new SavedSearchDispatchArgs();
        //dispatchArgs.add("CNPJ", "02.808.708/0001-07");
        //search.dispatch(dispatchArgs);
        
        // Run a saved search and poll for completion
        System.out.println("Run the '" + search.getName() + "' search ("+ search.getSearch() + ")\n");
        Job jobSavedSearch = null;

        try {
            jobSavedSearch = search.dispatch();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("Waiting for the job to finish...\n");

        while (!jobSavedSearch.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println(jobSavedSearch.getResults().toString());
    }
}