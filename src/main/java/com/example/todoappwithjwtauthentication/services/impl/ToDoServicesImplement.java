package com.example.todoappwithjwtauthentication.services.impl;

import com.example.todoappwithjwtauthentication.dto.requests.ToDoRequest;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.dto.responses.ToDoResponse;
import com.example.todoappwithjwtauthentication.entites.Tag;
import com.example.todoappwithjwtauthentication.entites.ToDo;
import com.example.todoappwithjwtauthentication.entites.User;
import com.example.todoappwithjwtauthentication.enums.EPriorityLevel;
import com.example.todoappwithjwtauthentication.enums.EStatus;
import com.example.todoappwithjwtauthentication.enums.ETag;
import com.example.todoappwithjwtauthentication.exceptions.DataNotFoundException;
import com.example.todoappwithjwtauthentication.exceptions.InvalidDataException;
import com.example.todoappwithjwtauthentication.repositories.*;
import com.example.todoappwithjwtauthentication.services.ToDoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ToDoServicesImplement implements ToDoServices {
    @Autowired
    ToDoRepository toDoRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    PriorityLevelRepository priorityLevelRepository;
    @Autowired
    TagsRepository tagsRepository;
    @Autowired
    UserRepository userRepository;

    private <T> Page<T> pagingList(List<T> list, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        if (end >= start) {
            List<T> pageData = list.subList(start, end);
            return new PageImpl<>(pageData, pageRequest, list.size());
        } else {
            throw new IllegalArgumentException("Vượt quá số trang tối đa");
        }    }

    private List<ToDoResponse> convertToListToDoResponse(List<ToDo> listTodo) {
        List<ToDoResponse> toDoResponseList = new ArrayList<>();
        for (ToDo todo : listTodo) {
            Set<String> nameSet = new HashSet<>();
            for (User user : todo.getUserSet()) {
                nameSet.add(user.getFullName());
            }
            ToDoResponse toDoResponse = new ToDoResponse(todo.getTitle(),
                    todo.getDescription(),
                    todo.getDeadline(),
                    todo.getStatus().geteStatus(),
                    todo.getTagSet(),
                    todo.getPriorityLevel().getePriorityLevel(),
                    nameSet);
            toDoResponseList.add(toDoResponse);
        }
        return toDoResponseList;
    }

    @Override
    public MessageResponse addNewToDo(ToDoRequest toDoRequest) {
        if (toDoRequest.getTitle() == null) {
            throw new InvalidDataException("Tiêu đề không được để trống");
        }
        if (toDoRequest.getDeadline() == null) {
            throw new InvalidDataException("Deadline không được để trống");
        }
        if (toDoRequest.getDeadline().isBefore(LocalDate.now())) {
            throw new InvalidDataException("Deadline không thể trước ngày hiện tại");
        }
        ToDo toDo = new ToDo(toDoRequest.getTitle(),
                toDoRequest.getDescription(),
                toDoRequest.getDeadline(),
                toDoRequest.getUserSet(),
                toDoRequest.getTagSet());
        toDo.setStatus(statusRepository.findByeStatus(EStatus.PENDING).orElseThrow(() -> new DataNotFoundException("Không tìm thấy Status")));
        if (toDoRequest.getPriorityLevel() == null) {
            toDo.setPriorityLevel(priorityLevelRepository.findByePriorityLevel(EPriorityLevel.PRIORITY_LEVEL_LOW).orElseThrow(() -> new DataNotFoundException("Không tìm thấy Priority Level")));
        } else {
            toDo.setPriorityLevel(priorityLevelRepository.findByePriorityLevel(toDoRequest.getPriorityLevel().getePriorityLevel()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy Priority Level")));
        }
        toDoRepository.save(toDo);
        return new MessageResponse("Thêm mới To do thành công");

    }

//    @Override
//    public ResponseEntity<List<ToDoResponse>> getAllToDo(Integer page, Integer size) {
//        List<ToDoResponse> toDoResponseList = convertToListToDoResponse(toDoRepository.findAll(PageRequest.of(page, size))
//                .stream().filter(toDo -> toDo.getStatus().geteStatus() != EStatus.DELETED).toList());
//        toDoResponseList.stream().sorted(Comparator.comparing(ToDoResponse::getPriorityLevel)
//                .thenComparing(ToDoResponse::getDeadline)).toList();
//        for (ToDoResponse todores: toDoResponseList
//             ) {
//            System.out.println(todores);
//        }
//        return ResponseEntity.ok().body(toDoResponseList);
//    }
    @Override
    public Page<ToDoResponse> getAllToDo(Integer page, Integer size) {
        List<ToDo> toDoList = toDoRepository.findAll()
                .stream().filter(toDo -> toDo.getStatus().geteStatus() != EStatus.DELETED).sorted()
                .sorted(Comparator.comparing((ToDo todo)-> todo.getPriorityLevel().getPriorityLevelId())
                .thenComparing(ToDo::getDeadline)).toList();
        List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(toDoList);
        return this.pagingList(toDoResponseList, page, size);
    }

    @Override
    public Page<ToDoResponse> getUserToDo(String username, Integer page, Integer size) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy User có Username " + username);
        } else {
            List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(user.get().getToDoSet().stream().toList()).stream()
                    .filter((ToDoResponse toDo) -> toDo.getStatus() != EStatus.DELETED)
                    .sorted(Comparator.comparing(ToDoResponse::getPriorityLevel)
                            .thenComparing(ToDoResponse::getDeadline)).toList();
            return this.pagingList(toDoResponseList, page, size);
        }
    }

    @Override
    public Page<ToDoResponse> getUserToDo(List<ETag> listTags, String username, Integer page, Integer size) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy User có Username " + username);
        } else {
            List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(user.get().getToDoSet().stream().toList()).stream()
                    .filter((ToDoResponse toDo) -> toDo.getStatus() != EStatus.DELETED)
                    .filter((ToDoResponse toDo) -> toDo.getTagSet().containsAll(listTags))
                    .sorted(Comparator.comparing(ToDoResponse::getPriorityLevel)
                            .thenComparing(ToDoResponse::getDeadline)).toList();

            return this.pagingList(toDoResponseList, page, size);
        }
    }

    @Override
    public ToDo getToDo(Integer toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy To do có Id " + toDoId);
        } else {
            return toDo.get();
        }
    }

    @Override
    public MessageResponse removeToDo(int toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy To do có Id " + toDoId);
        } else {
            toDo.get().setStatus(statusRepository.findByeStatus(EStatus.DELETED).orElseThrow(() -> new RuntimeException("Không tìm thấy Status")));
            toDoRepository.save(toDo.get());
            return new MessageResponse("Xóa To do thành công");
        }
    }

    @Override
    public MessageResponse updateToDo(int toDoId, ToDoRequest toDoRequest) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            throw new DataNotFoundException("Không tìm thấy To do có Id " + toDoId);
        } else {
            if (toDoRequest.getTitle() != null) {
                toDo.get().setTitle(toDoRequest.getTitle());
            }
            if (toDoRequest.getDescription() != null) {
                toDo.get().setDescription(toDoRequest.getDescription());
            }
            if (toDoRequest.getDeadline() != null) {
                if (toDoRequest.getDeadline().isBefore(LocalDate.now())) {
                    throw new InvalidDataException("Deadline không thể trước ngày hiện tại");
                } else {
                    toDo.get().setDeadline(toDoRequest.getDeadline());
                }
            }
            if (toDoRequest.getPriorityLevel() != null) {
                toDo.get().setPriorityLevel(toDoRequest.getPriorityLevel());
            }
            if (toDoRequest.getStatus() != null) {
                toDo.get().setStatus(toDoRequest.getStatus());
            }

            Set<User> memberSet = toDo.get().getUserSet();
            memberSet.addAll(toDoRequest.getUserSet());
            toDo.get().setUserSet(memberSet);

            Set<Tag> tagSet = toDo.get().getTagSet();
            tagSet.addAll(toDoRequest.getTagSet());
            toDo.get().setTagSet(tagSet);
            toDoRepository.save(toDo.get());
            return new MessageResponse("Sửa thông tìn To Do thành công");
        }
    }
}
