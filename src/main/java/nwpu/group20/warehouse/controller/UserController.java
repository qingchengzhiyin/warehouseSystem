package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.User;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.InfoParam;
import nwpu.group20.warehouse.param.LoginParam;
import nwpu.group20.warehouse.param.RegistParam;
import nwpu.group20.warehouse.param.finalParam.UserChangeParam;
import nwpu.group20.warehouse.service.UserService;
import nwpu.group20.warehouse.service.impl.SecurityServiceImpl;
import nwpu.group20.warehouse.service.impl.UserServiceImpl;
import nwpu.group20.warehouse.util.JWTUtil;
import nwpu.group20.warehouse.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public HttpResult<List<UserVo>> getAllUsers(){
        try{
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 1){
                return new HttpResult<>(401,"抱歉，操作员没有权限查看所有用户");
            }else if(UserServiceImpl.getCurrentUserType() == 2 || UserServiceImpl.getCurrentUserType() == 0){
                List<UserVo> users = userService.loadAllUsers();
                return new HttpResult<>(200,"查找全部用户成功",users);
            }else {
                return new HttpResult<>(401,"未登录");
            }
        }catch (RuntimeException e){
            e.printStackTrace();
            return new HttpResult<>(503,"运行时异常");
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器暂时无法处理请求");
        }


    }

    /*
    登陆
     */
    @PostMapping("/login")
    public HttpResult<UserVo> login(@RequestBody @Valid LoginParam loginParam, HttpServletResponse response) throws Exception {
        try{

            User user = userService.login(loginParam.getUserNickname(),loginParam.getUserPassword());
            if(user == null){
                return new HttpResult<>(401,"登陆失败,用户名或密码错误",null);
            }
            String token = jwtUtil.creatJWT(user);
            response.setHeader("token",token);
            System.out.println(SecurityContextHolder.getContext().getAuthentication());
            return new HttpResult<>(200,"登陆成功",new UserVo(user.getUserId(),user.getUserNickname(),user.getUserName(),user.getUserType()));
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器暂时无法处理请求");
        }

    }

    /*
    注册
     */
    @PostMapping("/regist")
    public HttpResult<Void> regist(@RequestBody @Valid RegistParam registParam, HttpServletResponse response) throws Exception{
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 2){
                User user = userService.register(registParam.getUserName(),registParam.getUserPassword(),registParam.getUserType(),registParam.getUserNickname());
                if(user == null){
                    return new HttpResult<>(401,"注册失败");
                }
                return new HttpResult<>(200,"注册成功");
            }else {
                return new HttpResult<>(401,"非管理员无权添加用户");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器暂时无法处理请求");
        }

    }

    /*
    删除用户
     */
    @DeleteMapping("/deleteUser")
    public HttpResult<Void> deleteUser(int userId){
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if (UserServiceImpl.getCurrentUserType() ==2){
                userService.removeUserByUserId(userId);
                return new HttpResult<>(200,"删除成功");
            }else {
                return new HttpResult<>(401,"非管理员无权删除用户");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    /*
    登出
     */
    @GetMapping("/logOut")
    public HttpResult<Void> logOut(HttpServletResponse response){
        try {
            userService.logOut();
            response.setHeader("token",null);
            return new HttpResult<>(200,"退出成功");
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(2021302930,"一般情况下也报不了错这个");
        }

    }

    @PostMapping("/changeUserType")
    public  HttpResult<Void> changeUserType(int userType,int userId){
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 2){
                User user = userService.loadUserByUserId(userId);
                user.setUserType(userType);
                userService.changeUserInfo(userId,user.getUserNickname(),user.getUserPassword(),user.getUserType(),user.getUserName());
                return new HttpResult<>(200,"更改成功");
            }else {
                return new HttpResult<>(401,"非管理员无权修改类型");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @PostMapping("/changeUserInfo")
    public  HttpResult<Void> changeUserInfo(@RequestBody UserChangeParam userChangeParam){
        InfoParam infoParam = userChangeParam.toInfoParam();
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 2){
                userService.changeUserInfo(infoParam.getUserId(),infoParam.getUserNickname(),infoParam.getUserPassword(),infoParam.getUserType(),infoParam.getUserName());
                return new HttpResult<>(200,"更改信息成功");
            }else{
                return new HttpResult<>(501,"只有管理员有权修改用户信息");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "根据id找用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "返回成功")
    })
    @PostMapping("/selectUserNicknameById")
    public HttpResult<String> selectUserNicknameById(int userId){
        try{
            return new HttpResult<>(200,"返回成功",userService.loadUserByUserId(userId).getUserName());
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"运行时异常");
        }
    }


}
