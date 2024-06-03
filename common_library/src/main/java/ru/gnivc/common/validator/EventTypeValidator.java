package ru.gnivc.common.validator;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import ru.gnivc.common.event.EventType;
import ru.gnivc.common.exception.EventTypeException;

@UtilityClass
public class EventTypeValidator {
  public void validateIndex(Integer eventTypeIndex) {
    if (eventTypeIndex < 0 || eventTypeIndex >= EventType.values().length) {
      throw new EventTypeException(HttpStatus.BAD_REQUEST,
          eventTypeIndex + " is not a valid event type index");
    }
  }
}
