package link.youchu.youchuserver.Http;

public enum StatusEnum {

    OK(200, "200"),
    BAD_REQUEST(400, "400"),
    UnAuthorized(401,"401"),
    NOT_FOUND(404, "404"),
    INTERNAL_SERER_ERROR(500, "500");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
