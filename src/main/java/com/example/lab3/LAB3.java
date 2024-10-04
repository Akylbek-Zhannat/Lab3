
package com.example.lab3;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class LAB3 {


    private final int NumberX = new Random().nextInt(100) + 1;
    private final Map<Integer, User> users = new HashMap<>();


    @PostConstruct
    @ResponseBody
    public void init(){
        System.out.println("Число "+ NumberX);
    }

    @RequestMapping("/")
    @ResponseBody
    public String greet() {
        return "Akylbek Zhannat  \n  Lab3";
    }

    @RequestMapping("/guess")
    @ResponseBody
    public String guessNumber(@RequestParam(value = "number") int num) {
        if (num == NumberX) {
            return "Вы угадали харош";
        } else if (num < NumberX) {
            return "Ваше число меньше загаданного числа надо больше";
        } else {
            return "Ваше число больше загаданного числа";
        }
    }



    // таск 1 эндпоинт для книги
    @PostMapping("/book")
    @ResponseBody
    public Book receiveBook(@RequestBody Book book) {
        book.setStatus("received");
        return book;
    }


    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void initStudents() {
        students.add(new Student(1, "Zhannat", 19, "Zhan8456"));
        students.add(new Student(2, "Rakym", 20, "Rak8456"));
        students.add(new Student(3, "Nurbol", 18, "Nur8456"));
        students.add(new Student(4, "Baizhan", 19, "Bai8456"));
    }

    @GetMapping("/students")
    @ResponseBody
    public List<Student> getAllStudents() {
        return students;
    }

    // Обновление данных студента по его ID
    @PutMapping("/student/{id}")
    @ResponseBody
    public String updateStudent(@PathVariable int id, @RequestBody Student updatedStudent) {
        for (Student student : students) {
            if (student.getId() == id) {
                student.setName(updatedStudent.getName());
                student.setAge(updatedStudent.getAge());
                student.setEmail(updatedStudent.getEmail());
                return "Пользователь с id " + id + " обновлен имя " + student.getName() +
                        " возраст " + student.getAge() + " email " + student.getEmail();
            }
        }
        return "Пользователь с id " + id + " не найден";
    }

    @DeleteMapping("/student/{id}")
    @ResponseBody
    public String deleteStudent(@PathVariable int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                students.remove(student);
                return "Пользователь с id " + id + " удален";
            }
        }
        return "Пользователь с id " + id + " не найден";
    }

    // таск 2 эндпоинт добавление пользователей для тестирования
    @PostConstruct
    public void initUsers() {
        users.put(1, new User(1, "Zhannat", 19));
        users.put(2, new User(2, "Rakym", 20));
        users.put(3, new User(3, "Nurbol", 18));
        users.put(4, new User(4, "Baizhan", 19));
    }

    //  обновления пользователе по id
    @PutMapping("/user/{id}")
    @ResponseBody
    public String updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        User checkUser = users.get(id);

        if (checkUser == null) {
            return "Пользователь с id " + id + " не найден";
        }

        checkUser.setName(updatedUser.getName());
        checkUser.setAge(updatedUser.getAge());


        return "Пользователь с id " + id + " обновлен имя  " + checkUser.getName() + " возраст  " + checkUser.getAge();
    }
    @GetMapping("/users")
    @ResponseBody
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    // таск 3 эндпоинт для удаления  по ID
    @DeleteMapping("/user/{id}")
    @ResponseBody
    public String deleteUser(@PathVariable int id) {
        User removedUser = users.remove(id);

        if (removedUser == null) {
            return "Пользователь с id " + id + " не найден";
        }

        return "Пользователь с id" + id + " удален имя  " + removedUser.getName() + " возраст " + removedUser.getAge();
    }

    // таск 4 эндпоинт для создания нового пользователя с валидацией
    @PostMapping("/user")
    @ResponseBody
    public String createUser(@Valid @RequestBody User newUser, BindingResult result) {
        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getFieldErrors().forEach(error ->
                    errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n")
            );
            return errors.toString();
        }

        int newId = users.size() + 1;
        newUser.setId(newId);
        users.put(newId, newUser);

        return "Новый пользователь создан id " + newUser.getId() + " имя  " + newUser.getName() + " возраст " + newUser.getAge();
    }

    // таск 5 исключение
    @GetMapping("/throw-exception")
    public void throwException() {
        throw new RuntimeException("Это тестовое исключение");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
