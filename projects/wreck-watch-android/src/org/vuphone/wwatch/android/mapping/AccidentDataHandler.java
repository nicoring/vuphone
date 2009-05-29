 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.wwatch.android.mapping;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class AccidentDataHandler extends DefaultHandler {
	
	private static final String LOG_LABEL = "VUPHONE";
	private static final String LOG_PREFIX = "AccidentDataHandler: ";
	
	private boolean inPoints = false;
	private boolean inPoint = false;
	private boolean inLat = false;
	private boolean inLon = false;
	private boolean inNotes = false;
	
	private ArrayList<EnhancedGeoPoint> points_ = new ArrayList<EnhancedGeoPoint>();
	private EnhancedGeoPoint curPoint_;
	
	@Override
	public void startElement (String uri, String localname, String qName, Attributes atts){
		if (localname.trim().equalsIgnoreCase("Points")){
			inPoints = true;
		}else if (localname.trim().equalsIgnoreCase("Latitude")){
			inLat = true;
		}else if (localname.trim().equalsIgnoreCase("Longitude")){
			inLon = true;
		}else if (localname.trim().equalsIgnoreCase("Notes")){
			inNotes = true;
		}else if (localname.trim().equalsIgnoreCase("Point")){
			inPoint = true;
			curPoint_ = new EnhancedGeoPoint();
		}
	}
	
	@Override
	public void endElement (String uri, String localname, String qName) throws SAXException{
		if (localname.trim().equalsIgnoreCase("Points")){
			inPoints = false;
			
			//We're done processing
			throw new SAXException("Done processing");
		}else if (localname.trim().equalsIgnoreCase("Latitude")){
			inLat = false;
		}else if (localname.trim().equalsIgnoreCase("Longitude")){
			inLon = false;
		}else if (localname.trim().equalsIgnoreCase("Notes")){
			inNotes = false;
		}else if (localname.trim().equalsIgnoreCase("Point")){
			curPoint_.createGeoPoint();
			points_.add(curPoint_);
			inPoint = false;
		}
	}
	
	@Override
	public void characters (char ch[], int start, int length){
		if (inLat == true){
			curPoint_.setLat(Double.parseDouble(new String(ch)));
		}else if (inLon == true){
			curPoint_.setLon(Double.parseDouble(new String(ch)));
		}else if (inNotes == true){
			curPoint_.setNotes(new String(ch));
		}
	}
	
	public ArrayList<EnhancedGeoPoint> processXML(InputSource src){
		XMLReader xr = null;
		try {
			xr = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
		} catch (SAXException e) {
			Log.e(LOG_LABEL, LOG_PREFIX + "Error getting XMLReader instance: " + e.getMessage());
			return null;
		} catch (ParserConfigurationException e) {
			Log.e(LOG_LABEL, LOG_PREFIX + "Error getting XMLReader instance: " + e.getMessage());
			return null;
		} catch (FactoryConfigurationError e) {
			Log.e(LOG_LABEL, LOG_PREFIX + "Error getting XMLReader instance: " + e.getMessage());
			return null;
		}
		
		xr.setContentHandler(this);
		try {
			xr.parse(src);
		} catch (IOException e) {
			Log.e(LOG_LABEL, LOG_PREFIX + "IOException parsing AccidentXML: " + e.getMessage());
		} catch (SAXException e) {
			if (e.getMessage().equalsIgnoreCase("Done processing")){
				Log.i(LOG_LABEL, LOG_PREFIX + "Finished processing AccidentXML");
			}else {
				Log.e(LOG_LABEL, LOG_PREFIX + "SAXException parsing AccidentXML: " + e.getMessage());
			}
		}
		
		
		return points_;
	}

}
