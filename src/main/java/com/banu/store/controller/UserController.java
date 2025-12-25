package com.banu.store.controller;

import com.banu.store.entities.User;
import com.banu.store.mappers.UserMapper;
import com.banu.store.entities.CreateNewUser;
import com.banu.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @GetMapping("/users")
    public Iterable<User> getAllUsers(
            @RequestHeader( name="authToken") String authToken) //this makes compulsory to provide authToken value in Headers tab
            // to make this optional @RequestHeader(required = false,name="authToken") String authToken
    {
        System.out.println(authToken);
        return userRepository.findAll();

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user= userRepository.findById(id).orElse(null);
        if(user==null){
             return ResponseEntity.notFound().build();
             //can also use ResponseEntity<>(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(user); //can also use ResponseEntity<>(HttpStatus.OK)
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateNewUser request,
                                           UriComponentsBuilder uriBuilder){
        User user= userMapper.toEntity(request);
        userRepository.save(user);
        var uri = uriBuilder.path("users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
        /* this will return status 201 created and  a Location header
            this ia REST best practice cause it shows status and
            as well as resource location (url, which we can later use to fetch the data)
         */
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody CreateNewUser request){
        User user= userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        //user= userMapper.toEntity(request);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id){
        User user= userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
