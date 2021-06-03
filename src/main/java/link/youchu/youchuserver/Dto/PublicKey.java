package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class PublicKey {
    private String kty;
    private String kid;
    private String sig;
    private String alg;
    private String n;
    private String e;
}
