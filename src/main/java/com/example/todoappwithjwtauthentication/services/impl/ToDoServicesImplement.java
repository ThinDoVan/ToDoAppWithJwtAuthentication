package com.example.todoappwithjwtauthentication.services.impl;

import com.example.todoappwithjwtauthentication.dto.requests.ToDoRequest;
import com.example.todoappwithjwtauthentication.dto.responses.MessageResponse;
import com.example.todoappwithjwtauthentication.entites.Tag;
import com.example.todoappwithjwtauthentication.entites.ToDo;
import com.example.todoappwithjwtauthentication.entites.User;
import com.example.todoappwithjwtauthentication.enums.EPriorityLevel;
import com.example.todoappwithjwtauthentication.enums.EStatus;
import com.example.todoappwithjwtauthentication.repositories.PriorityLevelRepository;
import com.example.todoappwithjwtauthentication.repositories.StatusRepository;
import com.example.todoappwithjwtauthentication.repositories.ToDoRepository;
import com.example.todoappwithjwtauthentication.repositories.UserRepository;
import com.example.todoappwithjwtauthentication.services.ToDoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ToDoServicesImplement implements ToDoServices {
    @Autowired
    ToDoRepository toDoRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    PriorityLevelRepository priorityLevelRepository;
    @Autowired
    UserRepository userRepository;

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

    @Override
    public ResponseEntity<List<ToDo>> getAllToDo(Integer page, Integer size) {
        List<ToDo> toDoList = toDoRepository.findAll(PageRequest.of(page, size)).stream()
                .filter(toDo -> !toDo.getStatus().geteStatus().name().equals("DELETED"))
                .sorted(Comparator.comparing((ToDo toDo) -> toDo.getPriorityLevel().getPriorityLevelId())
                        .thenComparing(ToDo::getDeadline)).toList();
        return ResponseEntity.ok().body(toDoList);
    }

    @Override
    public ResponseEntity<?> getUserToDo(Integer userId, Integer page, Integer size) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy User có Id " + userId));
        } else {
            Pageable pageRequest = PageRequest.of(page, size);
            List<ToDo> toDoList = user.get().getToDoSet().stream()
                    .filter(toDo -> !toDo.getStatus().geteStatus().name().equals("DELETED"))
                    .sorted(Comparator.comparing((ToDo toDo) -> toDo.getPriorityLevel().getPriorityLevelId())
                            .thenComparing(ToDo::getDeadline)).toList();
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()), toDoList.size());
            List<ToDo> pageContent = toDoList.subList(start, end);
            return ResponseEntity.ok().body(pageContent);
        }
    }

    @Override
    public ResponseEntity<?> getToDo(Integer toDoId) {
        Optional<ToDo> toDo = toDoRepository.findById(toDoId);
        if (toDo.isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Không tìm thấy To do có Id "+toDoId));
        }else {
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
            if (toDoRequest.getPriorityLevel()!=null){
                toDo.get().setPriorityLevel(toDoRequest.getPriorityLevel());
            }
            if(toDoRequest.getStatus()!=null){
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
