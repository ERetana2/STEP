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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
    
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> empty = new ArrayList<>();
    ArrayList<String> attendees = new ArrayList<String>(request.getAttendees()); // list for mandatory attendees
    ArrayList<String> allAttendees = new ArrayList<String>(attendees);
    allAttendees.addAll(request.getOptionalAttendees());
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>(
        availableTimesForAttendees(events, allAttendees, request.getDuration()));
    // return whole day if there are no attendees
    if (allAttendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    } 
    //return empty If there are no events during day(gaps), but there are optional attendees
    if(events.isEmpty() && !request.getOptionalAttendees().isEmpty()){
      return empty;
    }
    // When there are no available times with optional attendees, set available times for mandatory employees only
    if(!availableTimes.isEmpty()){
      return availableTimes;
    }
    else{
      availableTimes = new ArrayList<TimeRange>(availableTimesForAttendees(events, attendees, request.getDuration()));
    }
    // If the request has no attendees after optional attendees are excluded, return an empty collection.
    if(!attendees.isEmpty()){
      return availableTimes;
    }
    else{
      return empty;
    }
  }

  public Collection<TimeRange> availableTimesForAttendees(Collection<Event> events, ArrayList<String> attendees, long duration) {
    ArrayList<TimeRange> availableTimes = new ArrayList<>();
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();

    int meetingStartTime = TimeRange.START_OF_DAY;

    //Find unavailable times for meetings
    for(Event event: events){
      if(!Collections.disjoint(event.getAttendees(), attendees)){
        unavailableTimes.add(event.getWhen());
      }
    }
    //Sort the list of unavailable times in ascending order
    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);
    //Find available times for meetings
    for(TimeRange time: unavailableTimes){
      if(time.contains(meetingStartTime)){
        meetingStartTime = time.end();
      }else{
        if((time.start() - meetingStartTime) >= duration){
          availableTimes.add(TimeRange.fromStartEnd(meetingStartTime, time.start(), false));
        }
        if(meetingStartTime > time.end()){break;}
        meetingStartTime = time.end();
      }
    }
    //Append end of day available times
    if (TimeRange.END_OF_DAY - meetingStartTime >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(meetingStartTime, TimeRange.END_OF_DAY, true));
    }
    // Check for overlap in available times -> if there is remove time from available
    for(TimeRange validTime: availableTimes){
      for (TimeRange notValidTime: unavailableTimes) {
        if (validTime.overlaps(notValidTime)) {
          availableTimes.remove(validTime);
        }
      }
    }
    return availableTimes;
  }
}
