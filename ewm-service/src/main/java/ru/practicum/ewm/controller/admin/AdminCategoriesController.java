package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.service.admin.AdminCategoriesService;
import ru.practicum.ewm.utils.Create;
import ru.practicum.ewm.utils.Update;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Slf4j
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    @PostMapping()
    public CategoryDto create(@Validated(Create.class) @RequestBody CategoryDto categoryDto) {
        return adminCategoriesService.create(categoryDto);
    }

    @PatchMapping()
    public CategoryDto update(@Validated(Update.class) @RequestBody CategoryDto categoryDto) {
        return adminCategoriesService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteById(@PathVariable Long catId) {
        adminCategoriesService.deleteById(catId);
    }
}
