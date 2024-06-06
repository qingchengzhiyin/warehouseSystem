package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.InfoParam;
import nwpu.group20.warehouse.param.LoginParam;
import nwpu.group20.warehouse.param.RegistParam;
import nwpu.group20.warehouse.service.UserService;
import nwpu.group20.warehouse.service.impl.SecurityServiceImpl;
import nwpu.group20.warehouse.util.JWTUtil;
import nwpu.group20.warehouse.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.http.HttpRequest;
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


    /*
    获取所有用户
     */
    @Operation(summary = "获取所有用户", responses = {
            @ApiResponse(description = "查找全部用户成功",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpResult.class),
                            examples = @ExampleObject(value = "{\"code\":200,\"msg\":\"查找全部用户成功\",\"data\":[{\"userId\":1,\"userNickname\":\"nickname1\",\"userName\":\"username1\",\"userType\":1},{\"userId\":2,\"userNickname\":\"nickname2\",\"userName\":\"username2\",\"userType\":2}]}")))
    })
    @GetMapping("/allUsers")
    public HttpResult getAllUsers(){
        List<UserVo> users = userService.loadAllUsers();
        return new HttpResult(200,"查找全部用户成功",users);
    }

    /*
    登陆
     */
    @PostMapping("/login")
    public HttpResult login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) throws Exception {
        User user = userService.login(loginParam.getUserNickname(),loginParam.getUserPassword());
        if(user == null){
            return new HttpResult(401,"登陆失败,用户名或密码错误",null);
        }
        String token = jwtUtil.creatJWT(user);
        response.setHeader("token",token);
        return new HttpResult(200,"登陆成功",new UserVo(user.getUserId(),user.getUserNickname(),user.getUserName(),user.getUserType()));
    }

    /*
    注册
     */
    @PostMapping("/regist")
    public HttpResult regist(@RequestBody @Valid RegistParam registParam, HttpServletResponse response) throws Exception{
        User user = userService.register(registParam.getUserName(),registParam.getUserPassword(),registParam.getUserType(),registParam.getUserNickname());
        if(user == null){
            return new HttpResult(401,"注册失败");
        }
        return new HttpResult(200,"注册成功");
    }

    /*
    删除用户
     */
    @PostMapping("/deleteUser")
    public HttpResult deleteUser(int userId){
        userService.removeUserByUserId(userId);
        return new HttpResult(200,"删除成功");
    }

    /*
    登出
     */
    @GetMapping("/logOut")
    public HttpResult logOut(HttpServletResponse response){
        userService.logOut();
        response.setHeader("token",null);
        return new HttpResult<>(200,"退出成功");
    }

    @PostMapping("/changeUserType")
    public  HttpResult changeUserType(int userType,int userId){
        User user = userService.loadUserByUserId(userId);
        user.setUserType(userType);
        userService.changeUserInfo(userId,user.getUserNickname(),user.getUserPassword(),user.getUserType(),user.getUserName());
        return new HttpResult(200,"更改成功");
    }

    @PostMapping("/changeUserInfo")
    public  HttpResult changeUserInfo(@RequestBody InfoParam infoParam){
        userService.changeUserInfo(infoParam.getUserId(),infoParam.getUserNickname(),infoParam.getUserPassword(),infoParam.getUserType(),infoParam.getUserName());
        return new HttpResult(200,"更改信息成功");
    }


}
