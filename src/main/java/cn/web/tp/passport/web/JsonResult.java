package cn.web.tp.passport.web;


import cn.web.tp.passport.exception.ServiceException;
import lombok.Data;

/**
 * 封装服务器端到客户端响应结果的数据
 */
@Data
public class JsonResult {
    /**
     * 业务状态码
     */
    private Integer code;
    /**
     * 错误时的信息
     */
    private String message;
    /**响应成功后，需要响应到客户端的数据*/
    private String jwt;

    private Object data;

    public static JsonResult ok(){
        return ok(null);
    }

    public static JsonResult ok(Object data){
        JsonResult jsonResult = new JsonResult();
        jsonResult.code = ServiceCode.OK;
        jsonResult.data = data;
        return jsonResult;
    }

    public static JsonResult ok(String jwt){
        JsonResult jsonResult = new JsonResult();
        jsonResult.code = ServiceCode.OK;
        jsonResult.jwt = jwt;
        return jsonResult;
    }

    public static JsonResult fail(ServiceException e){
        return fail(e.getCode(),e.getMessage());
    }

    public static JsonResult fail(Integer code,String message){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setMessage(message);
        return jsonResult;
    }
}
