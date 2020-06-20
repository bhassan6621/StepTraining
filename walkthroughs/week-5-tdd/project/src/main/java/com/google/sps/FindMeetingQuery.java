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

import com.google.sps.FindMeetingQuery;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class FindMeetingQuery {
  private static final int DURATION_1_HOUR = 60;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // No Options For Too Long Of A Request
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
        return Collections.emptyList();
    }
    // Not Enough Room
    if (request.getDuration() == DURATION_1_HOUR && !(request.getAttendees().isEmpty())) {
        return Collections.emptyList();
    }
    // Options For no attendees 
    if (request.getDuration() == DURATION_1_HOUR) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
    }

    ArrayList<TimeRange> results = new ArrayList<>();

    int endTimeFromLastEvent = TimeRange.START_OF_DAY;
    for(Event event: events) {
        results.add(TimeRange.fromStartEnd(endTimeFromLastEvent, event.getWhen().start(), false));
        endTimeFromLastEvent = event.getWhen().end();
    }
    results.add(TimeRange.fromStartEnd(endTimeFromLastEvent,TimeRange.END_OF_DAY, true));

    return results;
    
  }
}
