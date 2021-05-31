package forpleuvoir.suika.network.http;


import java.util.Arrays;

public enum HttpStatusCode {
    Continue(100, "继续。客户端应继续其请求"),
    SwitchingProtocols(101, "切换协议。"),
    OK(200, "请求成功"),
    Created(201, "已创建"),
    Accepted(202, "已接受"),
    NonAuthoritativeInformation(203, "非授权信息。"),
    NoContent(204, "无内容。"),
    ResetContent(205, "重置内容。"),
    PartialContent(206, "部分内容。"),
    MultipleChoices(300, "多种选择。"),
    MovedPermanently(301, "永久移动。"),
    Found(302, "临时移动。"),
    SeeOther(303, "查看其它地址。"),
    NotModified(304, "未修改。"),
    UseProxy(305, "使用代理。"),
    Unused(306, "已经被废弃的HTTP状态码"),
    TemporaryRedirect(307, "临时重定向。"),
    BadRequest(400, "客户端请求的语法错误，服务器无法理解"),
    Unauthorized(401, "请求要求用户的身份认证"),
    PaymentRequired(402, "保留，将来使用"),
    Forbidden(403, "服务器理解请求客户端的请求，但是拒绝执行此请求"),
    NotFound(404, "服务器无法根据客户端的请求找到资源。"),
    MethodNotAllowed(405, "客户端请求中的方法被禁止"),
    NotAcceptable(406, "服务器无法根据客户端请求的内容特性完成请求"),
    ProxyAuthenticationRequired(407, "请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权"),
    RequestTimeout(408, "服务器等待客户端发送的请求时间过长，超时"),
    Conflict(409, "服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突"),
    Gone(410, "客户端请求的资源已经不存在。"),
    LengthRequired(411, "服务器无法处理客户端发送的不带Content-Length的请求信息"),
    PreconditionFailed(412, "客户端请求信息的先决条件错误"),
    RequestEntityTooLarge(413, "由于请求的实体过大，服务器无法处理，因此拒绝请求。"),
    RequestURITooLarge(414, "请求的URI过长，服务器无法处理"),
    UnsupportedMediaType(415, "服务器无法处理请求附带的媒体格式"),
    RequestedRangeNotSatisfiable(416, "客户端请求的范围无效"),
    ExpectationFailed(417, "服务器无法满足Expect的请求头信息"),
    InternalServerError(500, "服务器内部错误，无法完成请求"),
    NotImplemented(501, "服务器不支持请求的功能，无法完成请求"),
    BadGateway(502, "作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应"),
    ServiceUnavailable(503, "由于超载或系统维护，服务器暂时的无法处理客户端的请求。"),
    GatewayTimeout(504, "充当网关或代理的服务器，未及时从远端服务器获取请求"),
    HTTPVersionNotSupported(505, "服务器不支持请求的HTTP协议的版本，无法完成处理");

    private final int code;
    private final String msg;

    HttpStatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg(){
        return this.msg;
    }

    public boolean equals(int code) {
        return this.code == code;
    }

    public boolean isOk(){
       return String.valueOf(code).startsWith("2");
    }

    public static HttpStatusCode codeOf(int code) {
        return Arrays.stream(HttpStatusCode.values()).filter(v -> v.code == code).findFirst().orElse(null);
    }

}
