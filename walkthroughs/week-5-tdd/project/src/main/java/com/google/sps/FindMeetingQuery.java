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
  /**
   * @param events a collection of events going on during the day
   * @param request user created request for an event that includes Time and attendees
   * @return a list of available times to schedule a meeting
   * Scenario 1: When there is no mandatory attendees and there is optional attendees with no gaps
   * return empty
   * Scenario 2: When there is no mandatory attendees and there is optional attendees with gaps
   * return available times around gaps for optional attendees
   * Scenario 3: When there is no mandatory attendees and there is no optional attendees 
   * return the whole day
   * Scenario 4: When there is mandatory attendees and there is no optional attendees 
   * return available times for mandatory attendees
   * Scenario 5: When there is mandatory attendees and there is optional attendees with gaps
   * return times available for both -> if there are no times available for both return
   * available times for mandatory attendees
   * Scenario 6: When there is mandatory attendees with no events and no optional attendees
   * return the whole day
   * Scenario 7: When there is mandatory attendees with no events and optional attendees with events
   * return gaps in optional attendees
   * Scenario 8: When there is mandatory attendees with no events and optional attendees with events
   * return the whole day
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<String> mandatoryAttendees = new ArrayList<String>(request.getAttendees()); 
    ArrayList<String> allAttendees = new ArrayList<String>(mandatoryAttendees);
    allAttendees.addAll(request.getOptionalAttendees());
    // return whole day if there are no attendees
    if (allAttendees.isEmpty()) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    } 
    //return empty If there are no events during day(gaps), but there are optional attendees
    if(events.isEmpty() && !request.getOptionalAttendees().isEmpty()){
      return new ArrayList<TimeRange>();
    }
    // When there are no available times with optional attendees, set available times for mandatory employees only
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>(
        availableTimesForAttendees(events, allAttendees, request.getDuration()));
    if(!availableTimes.isEmpty()){
      return availableTimes;
    }else{
      // If the request has no attendees after optional attendees are excluded, return an empty collection.
      if(!mandatoryAttendees.isEmpty()){
    	return new ArrayList<TimeRange>(availableTimesForAttendees(events, mandatoryAttendees, request.getDuration()));
      }
    }
    return new ArrayList<TimeRange>();
  }
  /**
   * @param events a collection of events going on during the day
   * @param attendees a list that contains the attendees for the available times being searched
   * @param duration length of the user requested meeting
   * @return a list that holds the available times to schedule a meeting for the current atendees
   */
  public Collection<TimeRange> availableTimesForAttendees(Collection<Event> events, ArrayList<String> attendees, long duration) {
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();

    //Find unavailable times for meetings
    for(Event event: events){
      if(!Collections.disjoint(event.getAttendees(), attendees)){
        unavailableTimes.add(event.getWhen());
      }
    }
    //Sort the list of unavailable times in ascending order
    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

    //Find available times for meetings
    ArrayList<TimeRange> availableTimes = new ArrayList<>();
    int meetingStartTime = TimeRange.START_OF_DAY;
    for(TimeRange time: unavailableTimes){
      if(time.contains(meetingStartTime)){
        meetingStartTime = Math.max(time.end(),meetingStartTime);
      }else{
        if((time.start() - meetingStartTime) >= duration){
          availableTimes.add(TimeRange.fromStartEnd(meetingStartTime, time.start(), false));
        }
        if(meetingStartTime < time.end()){
          meetingStartTime = time.end();
        }
      }
    }
    //Append end of day available times
    if (TimeRange.END_OF_DAY - meetingStartTime >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(meetingStartTime, TimeRange.END_OF_DAY, true));
    }
    return availableTimes;
  }
}
