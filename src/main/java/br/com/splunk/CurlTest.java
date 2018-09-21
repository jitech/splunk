package br.com.splunk;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CurlTest {

	public static void main(String[] args) {
		
		String[] command = {"curl", "-k","http://www.google.com"};

ProcessBuilder builder = new ProcessBuilder(command);
builder.redirectErrorStream(true);

String curlResult = "";
String line = "";

try {
    Process process = builder.start();
    BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
    while (true) {
        line = r.readLine();
        if (line == null) {
            break;
        }
        curlResult = curlResult + line;
    }
} catch (Exception e) {
    e.printStackTrace();
}
	}
}
