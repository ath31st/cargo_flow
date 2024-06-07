package ru.gnivc.common.event;

import lombok.Getter;

@Getter
public enum EventType {
  ROUTE_CREATED("Рейс создан"),
  ROUTE_STARTED("Рейс начат"),
  ROUTE_ENDED("Рейс окончен"),
  ROUTE_CANCELLED("Рейс отменен"),
  MALFUNCTION("Поломка"),
  ACCIDENT("Авария");

  private final String description;

  EventType(String description) {
    this.description = description;
  }
}
