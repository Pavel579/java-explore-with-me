package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
