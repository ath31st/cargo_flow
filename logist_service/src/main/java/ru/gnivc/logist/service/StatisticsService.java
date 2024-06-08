package ru.gnivc.logist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gnivc.logist.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class StatisticsService {
  private final TaskRepository taskRepository;
}
