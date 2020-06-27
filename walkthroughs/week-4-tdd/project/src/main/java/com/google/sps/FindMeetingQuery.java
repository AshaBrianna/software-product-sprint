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
        
        List<TimeRange> allOpenTimes = new ArrayList<TimeRange>();
        Collection<String> neededAttendees = new ArrayList<String>();
        neededAttendees = request.getAttendees();
        long timeNeeded = request.getDuration();
        int dayStart = TimeRange.START_OF_DAY;
        int dayEnd = TimeRange.END_OF_DAY;
        TimeRange entireDay = TimeRange.WHOLE_DAY;

        if ( timeNeeded > entireDay.duration() ){
            return allOpenTimes;
        }

        if ( timeNeeded == 0 || events == null || events.isEmpty() || neededAttendees.isEmpty() || neededAttendees == null ){
            allOpenTimes.add(entireDay);
            return allOpenTimes; 
        }

        //remove events irrelevant to group of requested attendees
        Collection<String> eventAttendees = new ArrayList<String>();
        int overlap = 0;
        int anyConflict = 0;
        for (Event event : events) {
            eventAttendees = event.getAttendees();
            if ( ( event.getWhen().duration() <= 0 ) || ( eventAttendees.isEmpty() ) ){
                events.remove(event);
            }
            else {
               for (String neededAttendee : neededAttendees){
                    if ( eventAttendees.contains(neededAttendee) ){
                        overlap++;
                        anyConflict = 1;
                    }
                }
                if (overlap == 0) {
                    if (events.size() <= 1){
                        allOpenTimes.add(entireDay);
                        return allOpenTimes;
                    }
                    else {
                        events.remove(event);
                        System.out.println("STEP 8");
                    }
                }
            }
            overlap = 0;
        }
        
        //create sorted array of unavailable TimeRanges
        List<TimeRange> busyTimes = new ArrayList<TimeRange>();  
        for (Event event: events){
            busyTimes.add(event.getWhen());
        }
        Collections.sort(busyTimes, TimeRange.ORDER_BY_START);

        //consider slots starting at start of day
        int prospectStart = dayStart;
        int prospectEnd;
        TimeRange prospect;
        for (int i=0; i<busyTimes.size(); i++){
            prospectEnd = busyTimes.get(i).start();
            prospect = TimeRange.fromStartEnd(prospectStart, prospectEnd, false);
            if ( prospect.duration() >= timeNeeded ){
                allOpenTimes.add(prospect); 
            }
            prospectStart = busyTimes.get(i).end();
        }

        //consider slot at end of day
        Collections.sort(busyTimes, TimeRange.ORDER_BY_END);  
        TimeRange latest = busyTimes.get(busyTimes.size() - 1);      
        prospect = TimeRange.fromStartEnd(latest.end(), dayEnd, true);
        if ( prospect.duration() >= timeNeeded ){
            allOpenTimes.add(prospect); 
        }
        
        return allOpenTimes;
    }
}
