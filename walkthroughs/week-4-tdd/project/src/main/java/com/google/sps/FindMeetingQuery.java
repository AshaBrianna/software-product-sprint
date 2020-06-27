// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        
        //remove events irrelevant to group of requested attendees
        Collection<String> neededAttendees = new ArrayList<String>();
        neededAttendees = request.getAttendees();
        for (Event event : events){
            boolean matches = false;
            Collection<String> eventAttendees = new ArrayList<String>();
            eventAttendees = event.getAttendees();
            attendees_loop:
            for (String eventAttendee : eventAttendees){
                for (String neededAttendee : neededAttendees){
                    if (neededAttendee == eventAttendee){
                        matches = true;
                        break attendees_loop;
                    }
                }
            }
            if (matches == false){
                events.remove(event);
            }
        }

        //NOTE: may contain duplicates
        //create sorted array of unavailable TimeRanges
        List<TimeRange> busyTimes = new ArrayList<TimeRange>();  
        for (Event event: events){
            busyTimes.add(event.getWhen());
        }
        
        // Comparator<TimeRange> ORDER_BY_END = new Comparator<TimeRange>();
        Collections.sort(busyTimes, TimeRange.ORDER_BY_START);
        
        List<TimeRange> allOpenTimes = new ArrayList<TimeRange>();

        //start-end must be > || == duration
        long timeNeeded = request.getDuration();
        int openStart = TimeRange.START_OF_DAY;;

        //run through all busy times and check if slots around are options
        //NOTE/TODO: check bounds, account for overlapping events
        //Reminder: no possible slot should start/end earlier/later than START_OF_DAY/END_OF_DAY

        for (int i=0; i<busyTimes.size(); i++){
            int openEnd = (busyTimes.get(i)).start();
            if ((openStart-openEnd) >= timeNeeded) {
                TimeRange openTime = TimeRange.fromStartEnd(openStart, openEnd, false);
                allOpenTimes.add(openTime); 
            }
            openStart = (busyTimes.get(i)).end();
        }
        
        return allOpenTimes;
    }
}
