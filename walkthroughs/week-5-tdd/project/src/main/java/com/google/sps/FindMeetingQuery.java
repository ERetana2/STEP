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
    ArrayList<TimeRange> availableTimes = new ArrayList<>();
    ArrayList<TimeRange> unavailableTimes = new ArrayList<>();
    int meetingStartTime = TimeRange.START_OF_DAY;
    long duration = request.getDuration();

    //check edge case when there is a meeting request with no people
    if(request.getAttendees().isEmpty()){
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    //Create a list of times that are unavailable for request
    for (Event event: events){
      if(!Collections.disjoint(event.getAttendees(), request.getAttendees())){
        unavailableTimes.add(event.getWhen());
      }
    }
    //Sort the list of unavailable times and check for valid available time
    Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

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
    return availableTimes;
  }
}
