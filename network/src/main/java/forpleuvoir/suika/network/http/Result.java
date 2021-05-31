package forpleuvoir.suika.network.http;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpResponse;
import java.util.Arrays;

/**
 * @author forpleuvoir
 */
public class Result {
    private transient static final Logger log = LoggerFactory.getLogger(Result.class);
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private int code;
    private String msg;

    /**
     * 返回数据默认为空字符串
     **/
    private Object data = "";

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }

    protected Result clone() throws CloneNotSupportedException {
        return new Result(code, msg, data);
    }

    @Override
    public String toString() {
        if (this.data instanceof Exception) {
            try {
                Result result = this.clone();
                result.data = Arrays.toString(((Exception) this.data).getStackTrace());
                return gson.toJson(result);
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage(), e);
            }
        }
        return gson.toJson(this);
    }

    public String getDataAsString() {
        if (data instanceof String) {
            return this.data.toString().replace("\"", "");
        }
        return data.toString();
    }

    public JsonObject getDataAsJsonObject() {
        return JsonParser.parseString(data.toString()).getAsJsonObject();
    }

    public JsonArray getDataAsJsonArray() {
        return JsonParser.parseString(data.toString()).getAsJsonArray();
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result msg(String msg) {
        this.msg = msg;
        return this;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public static Result getResult(String jsonStr, HttpURLConnection connection) {
        Result result = new Result();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            result.code = jsonObject.get("code").getAsInt();
            result.msg = jsonObject.get("msg").getAsString();
            result.data = jsonObject.get("data").getAsString();
        } catch (Exception e) {
            try {
                result.code = connection.getResponseCode();
                result.msg = connection.getResponseMessage();
                result.data = jsonStr;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return result;
    }

    public static <T> Result getResult(HttpResponse<T> response) {
        Result result = new Result();
        int statusCode = response.statusCode();
        var code = HttpStatusCode.codeOf(statusCode);
        if (code.isOk()) {
            try {
                if (response.body() instanceof String) {
                    JsonObject jsonObject = JsonParser.parseString((String) response.body()).getAsJsonObject();
                    result.code = jsonObject.get("code").getAsInt();
                    result.msg = jsonObject.get("msg").getAsString();
                    result.data = jsonObject.get("data");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.code = statusCode;
                result.msg = code.getMsg();
                result.data = response.body();
            }
        } else {
            result.code = statusCode;
            result.msg = code.getMsg();
            result.data = response.body();
        }
        return result;
    }

    public static Result getExceptionResult(Exception e) {
        return new Result(1010, "程序异常", e);
    }
}
