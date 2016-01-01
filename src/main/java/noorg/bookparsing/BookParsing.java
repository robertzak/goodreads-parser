package noorg.bookparsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import noorg.bookparsing.domain.Book;
import noorg.bookparsing.enrich.GraphicNovelEnricher;
import noorg.bookparsing.report.format.impl.DefaultBookFormater;
import noorg.bookparsing.report.impl.YearlyReportService;
import noorg.bookparsing.service.ParsingService;
import noorg.bookparsing.service.impl.GoodReadsParsingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Copyright 2014-2016 Robert J. Zak
 * 
 * <p>Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * <p>    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * <p>Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * <p>A simple main method, mostly for testing at this point.
 * 
 * @author Robert J. Zak
 *
 */
public class BookParsing {
	private static final Logger logger = LoggerFactory.getLogger(
			BookParsing.class);

	public static void main (final String [] args) throws Exception{
		logger.info("Starting Book Parsing");
		// TODO write a better main, allow user options/pointing at a file, etc. GUI file chooser?
		ParsingService<String, Book> parser = new GoodReadsParsingService();
		
		
		
		final URL inputUrl = BookParsing.class.getResource("/goodreads_export.csv");
		
		if(inputUrl == null){
			logger.error("Unable to locate file..");
			System.exit(-1);
		}
		
		File file = new File(inputUrl.toURI());
		
		List<Book> books = new ArrayList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	// TODO better way to skip first line?
		    	if(line.startsWith("﻿Book Id")){
		    		continue;
		    	}
		    	
		        final Book book = parser.parse(line);
		        books.add(book);
		    }
		    // line is not visible here.
		}catch(Exception e){
			logger.error("Error reading input", e);
		}
		
		// do some data enrichment
		new GraphicNovelEnricher().enrichBooks(books);
		
		// run reports
		final int [] years = {2012, 2013, 2014, 2015};
		
		for(int year: years){
			logger.info("*****************************************");
			logger.info(new YearlyReportService(year).generateReport(books, 
					new DefaultBookFormater()));
		}
	}
}
