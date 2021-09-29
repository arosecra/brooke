package org.github.arosecra.brooke.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

public class CommandLine {

	public static boolean run(String[] args, PrintStream output) {
		Process process;
		try {
			process = new ProcessBuilder(args).start();
			process.waitFor(30, TimeUnit.MINUTES);
			if (process.exitValue() == 0) {
				// Success
				printProcessOutput(process.getInputStream(), output);
				return true;
			} else {
				printProcessOutput(process.getErrorStream(), output);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean run(String[] args) {
		return run(args, System.out);
	}

	private static void printProcessOutput(InputStream inputStream, PrintStream output) throws IOException {
		try (InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(isr)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				output.println(line);
			}
		}
	}
}
