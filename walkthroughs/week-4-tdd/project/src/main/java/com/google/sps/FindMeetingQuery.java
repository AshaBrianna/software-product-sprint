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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//request has a: name, minute duration, attendees collection
//Each event in the Collection has a: name, time range, attendees collection
//time range gives start time, end time, and duration
public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        
        Collection<String> requestedAttendees = request.getAttendees();        
        Collection<TimeRange> conflicts = new Collection<>();

        //find events that are potential conflicts
        for (Event event : events.asIterable()){
            boolean matches = False;
            Set<String> eventAttendees = event.getAttendees();
            attendees_loop:
            for (String eventAttendee : eventAttendees.asIterable()){
                for (String requestedAttendee : requestedAttendees.asIterable()){
                    if (requestedAttendee == eventAttendee){
                        matches = True;
                        break attendees_loop;
                    }
                }
            }
            if (matches == True){
                conflicts.add(event.getWhen());
            }
        }

        //sort conflict events by start time
        Collections.sort(conflicts, new ORDER_BY_START());
        
        Collection<TimeRange> allOpenTimes = new Collection<>();

        long timeNeeded = request.getDuration();

        //begin looking for possible slots beginning with the earliest time
        int start = START_OF_DAY;
        int end = START_OF_DAY + timeNeeded;
        
        //add a potential timeslot
        if (contains(conflict.get(0), end) == False){
            int end = (conflict.get(1)).start();
            TimeRange openTime = fromStartEnd(start, end, False);
            allOpenTimes.add(openTime);
        }

        //how to run through all conflicts 
        // for (int i=0; i<conflicts.size(); i++){ 
        //     conflicts.get(i); 
        // }

        //Reminder: no possible slot should start/end earlier/later than START_OF_DAY/END_OF_DAY

        //function should return the collection of TimeRanges free for meetings
        return allOpenTimes;
  }
}
