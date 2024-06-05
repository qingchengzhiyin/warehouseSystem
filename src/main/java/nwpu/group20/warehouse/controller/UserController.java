package nwpu.group20.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.param.LoginParam;
import nwpu.group20.warehouse.param.RegistParam;
import nwpu.group20.warehouse.service.UserService;
import nwpu.group20.warehouse.service.impl.SecurityServiceImpl;
import nwpu.group20.warehouse.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController

@Slf4j//可以直接在代码写console.log
@Validated//可以限制传入参数
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    JWTUtil jwtUtil;


    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return userService.loadAllUsers();
    }

    @PostMapping("/login")
    public User login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) throws Exception {
        User user = userService.login(loginParam.getUserNickname(),loginParam.getUserPassword());
        String token = jwtUtil.creatJWT(user);
        response.setHeader("token",token);
        log.info("登陆"+token);
        return user;
    }

    @PostMapping("/regist")
    public User regist(@RequestBody @Valid RegistParam registParam, HttpServletResponse response) throws Exception{
        User user = userService.register(registParam.getUserName(),registParam.getUserPassword(),registParam.getUserType(),registParam.getUserNickname());
        return user;
    }
}
