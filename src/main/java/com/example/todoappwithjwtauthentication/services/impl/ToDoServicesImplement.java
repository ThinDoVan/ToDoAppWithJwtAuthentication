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
import com.example.todoappwithjwtauthentication.repositories.*;
import com.example.todoappwithjwtauthentication.services.ToDoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    private List<ToDoResponse> pagingList(List<ToDoResponse> list, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        return list.subList(start, end);
    }

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
    public ResponseEntity<MessageResponse> addNewToDo(ToDoRequest toDoRequest) {
        if (toDoRequest.getTitle() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tiêu đề không được để trống"));
        }
        if (toDoRequest.getDeadline() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Deadline không được để trống"));
        }
        if (toDoRequest.getDeadline().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Deadline không thể trước ngày hiện tại"));
        }
        ToDo toDo = new ToDo(toDoRequest.getTitle(),
                toDoRequest.getDescription(),
                toDoRequest.getDeadline(),
                toDoRequest.getUserSet(),
                toDoRequest.getTagSet());
        toDo.setStatus(statusRepository.findByeStatus(EStatus.PENDING).orElseThrow(() -> new RuntimeException("Không tìm thấy Status")));
        if (toDoRequest.getPriorityLevel() == null) {
            toDo.setPriorityLevel(priorityLevelRepository.findByePriorityLevel(EPriorityLevel.PRIORITY_LEVEL_LOW).orElseThrow(() -> new RuntimeException("Không tìm thấy Priority Level")));
        } else {
            toDo.setPriorityLevel(priorityLevelRepository.findByePriorityLevel(toDoRequest.getPriorityLevel().getePriorityLevel()).orElseThrow(() -> new RuntimeException("Không tìm thấy Priority Level")));
        }
        toDoRepository.save(toDo);
        return ResponseEntity.ok().body(new MessageResponse("Thêm mới To do thành công"));

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
    public ResponseEntity<List<ToDoResponse>> getAllToDo(Integer page, Integer size) {
        List<ToDo> toDoList = toDoRepository.findAll()
                .stream().filter(toDo -> toDo.getStatus().geteStatus() != EStatus.DELETED).sorted()
                .sorted(Comparator.comparing((ToDo todo)-> todo.getPriorityLevel().getPriorityLevelId())
                .thenComparing(ToDo::getDeadline)).toList();
        List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(toDoList);
        return ResponseEntity.ok().body(this.pagingList(toDoResponseList, page, size));
    }

    @Override
    public ResponseEntity<?> getUserToDo(String username, Integer page, Integer size) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy User có Username " + username));
        } else {
            List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(user.get().getToDoSet().stream().toList());
            toDoResponseList.stream()
                    .filter((ToDoResponse toDo) -> toDo.getStatus() != EStatus.DELETED)
                    .sorted(Comparator.comparing(ToDoResponse::getPriorityLevel)
                            .thenComparing(ToDoResponse::getDeadline)).toList();
            return ResponseEntity.ok().body(this.pagingList(toDoResponseList, page, size));
        }
    }

    @Override
    public ResponseEntity<?> getUserToDo(List<ETag> listTags, String username, Integer page, Integer size) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy User có Username " + username));
        } else {
            List<ToDoResponse> toDoResponseList = this.convertToListToDoResponse(user.get().getToDoSet().stream().toList());
            toDoResponseList.stream()
                    .filter((ToDoResponse toDo) -> toDo.getStatus() != EStatus.DELETED)
                    .filter((ToDoResponse toDo) -> toDo.getTagSet().containsAll(listTags))
                    .sorted(Comparator.comparing(ToDoResponse::getPriorityLevel)
                            .thenComparing(ToDoResponse::getDeadline)).toList();

            return ResponseEntity.ok().body(this.pagingList(toDoResponseList, page, size));
        }
    }

    @Override
    public ResponseEntity<?> getToDo(Integer toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy To do có Id " + toDoId));
        } else {
            return ResponseEntity.ok().body(toDo.get());
        }
    }

    @Override
    public ResponseEntity<MessageResponse> removeToDo(int toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy To do có Id " + toDoId));
        } else {
            toDo.get().setStatus(statusRepository.findByeStatus(EStatus.DELETED).orElseThrow(() -> new RuntimeException("Không tìm thấy Status")));
            toDoRepository.save(toDo.get());
            return ResponseEntity.ok().body(new MessageResponse("Xóa To do thành công"));
        }
    }

    @Override
    public ResponseEntity<MessageResponse> updateToDo(int toDoId, ToDoRequest toDoRequest) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy To do có Id " + toDoId));
        } else {
            if (toDoRequest.getTitle() != null) {
                toDo.get().setTitle(toDoRequest.getTitle());
            }
            if (toDoRequest.getDescription() != null) {
                toDo.get().setDescription(toDoRequest.getDescription());
            }
            if (toDoRequest.getDeadline() != null) {
                if (toDoRequest.getDeadline().isBefore(LocalDate.now())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Deadline không thể trước ngày hiện tại"));
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
            return ResponseEntity.ok().body(new MessageResponse("Sửa thông tìn To Do thành công"));
        }
    }
}
