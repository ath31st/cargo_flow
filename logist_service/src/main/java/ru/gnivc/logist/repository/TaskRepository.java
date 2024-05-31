package ru.gnivc.logist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}