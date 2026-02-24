package com.victorpolicarpo.task_manager.controllers;

import com.victorpolicarpo.task_manager.dto.user.UserRequestDto;
import com.victorpolicarpo.task_manager.dto.user.UserResponseDto;
import com.victorpolicarpo.task_manager.dto.user.UserUpdateDto;
import com.victorpolicarpo.task_manager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto){
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listAll(){
        return ResponseEntity.ok(userService.listAll());
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserUpdateDto userUpdateDto, @PathVariable Long id){
        return ResponseEntity.ok(userService.update(userUpdateDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
