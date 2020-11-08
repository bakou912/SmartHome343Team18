package com.smart.home.backend.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputConsole {
	
	private static File outputConsoleFile;
	@Getter
	private static final List<String> lines = new ArrayList<>();
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		try {
			outputConsoleFile = ResourceUtils.getFile("classpath:console/output_console.json");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Private default constructor to hide the instance.
	 */
	private OutputConsole() {
	}
	
	/**
	 * Logging a line to the console
	 * @param line line to add
	 */
	public static void log(String line) {
		try {
			if (getLines().isEmpty() || !line.equals(OutputConsole.getLines().get(OutputConsole.getLines().size() - 1))) {
				lines.add(line);
				objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputConsoleFile, lines);
			}
		} catch(Exception ignored){}
	}
	
}
