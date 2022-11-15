package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequestDto;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.exceptions.UserNotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.storage.CategoryRepository;
import ru.practicum.ewm.storage.CompilationRepository;
import ru.practicum.ewm.storage.EventRepository;
import ru.practicum.ewm.storage.UserRepository;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.ewm.utils.Utils.getNullPropertyNames;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final PublicService publicService;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final EventMapper eventMapper;
    private final CompilationMapper compilationMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("afsdasdfas");
        User user = userRepository.save(userMapper.mapToUser(userDto));
        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest) {
        List<User> users = userRepository.findAllByIdIn(ids, pageRequest);
        return userMapper.mapToListUserDto(users);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userMapper.mapToUserDto(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not fond")));
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.mapToCategory(categoryDto));
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.mapToCategory(categoryDto));
        return categoryMapper.mapToCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        List<Event> events = eventRepository.findAllByCategoryId(catId);
        publicService.getCategoryById(catId);
        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
        }
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (event.getEventDate().minusHours(1).isAfter(LocalDateTime.now()) && event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.PUBLISHED);
            eventRepository.save(event);
            return eventMapper.mapToEventFullDto(event);
        } else {
            throw new NotFoundException("asdfsd");
        }
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            event.setState(EventState.CANCELED);
            eventRepository.save(event);
            return eventMapper.mapToEventFullDto(event);
        } else {
            throw new NotFoundException("adsfsdfc");
        }
    }

    @Override
    public List<EventFullDto> getEventsByParams(List<Long> users, List<EventState> states, List<Long> categories,
                                                String rangeStart, String rangeEnd,
                                                PageRequest pageRequest) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart == null) {
            start = LocalDateTime.MIN;
        } else {
            start = LocalDateTime.parse(rangeStart, format);
        }
        if (rangeEnd == null) {
            end = LocalDateTime.MAX;
        } else {
            end = LocalDateTime.parse(rangeEnd, format);
        }

        Specification<Event> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (users != null && !users.isEmpty()) {
                predicates.add(builder.and(root.get("initiator").in(users)));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(builder.and(root.get("state").in(states)));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(builder.and(root.get("category").in(categories)));
            }
            predicates.add(builder.greaterThan(root.get("eventDate"), start));
            predicates.add(builder.lessThan(root.get("eventDate"), end));

            return builder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Event> events = eventRepository.findAll(specification, pageRequest);

        return eventMapper.mapToListEventFullDto(events);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, AdminUpdateEventRequestDto adminUpdateEventRequestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));
        BeanUtils.copyProperties(adminUpdateEventRequestDto, event, getNullPropertyNames(adminUpdateEventRequestDto));
        return eventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.mapToCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> shortEvents = eventMapper.mapToListEventShortDto(events);
        return compilationMapper.mapToCompilationDto(compilation, shortEvents);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found!"));
        List<Event> events = compilation.getEvents();
        events.removeIf(e -> e.getId().equals(eventId));
        //compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found!"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found!"));

        compilation.getEvents().add(event);
        compilationRepository.save(compilation);

    }

    @Override
    @Transactional
    public void removePinnedCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void setPinnedCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found!"));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
