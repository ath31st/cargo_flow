package ru.gnivc.logist.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gnivc.logist.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

  Optional<Task> findByCompanyIdAndId(int companyId, int taskId);

  Page<Task> findAllByCompanyId(int companyId, Pageable pageable);
}
