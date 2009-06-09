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
package org.vuphone.wwatch.inforeq;

import java.util.ArrayList;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.routing.Route;
import org.vuphone.wwatch.routing.Waypoint;

public class InfoHandledNotification extends Notification {
	
	private ArrayList<Route> accidents_;
	private Route curRoute_;
	
	

	public InfoHandledNotification() {
		super("infohandled");
		accidents_ = new ArrayList<Route>();
		
	}
	
	public void newRoute(){
		curRoute_ = new Route();
		accidents_.add(curRoute_);
	}
	
	public void addWaypoint(double lat, double lon, long time){
		curRoute_.addWaypoint(new Waypoint(lat, lon, time));
		
	}
	
	public void addWaypoint(Waypoint w){
		curRoute_.addWaypoint(w);
	}
	
	public ArrayList<Route> getAccidents(){
		return accidents_;
	}
	
	
	

}
