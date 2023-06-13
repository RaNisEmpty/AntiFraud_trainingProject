package antifraud.business.susip.dto;


import antifraud.business.susip.SuspiciousIp;

public record IpDeleteResponse (String status) {
    public static IpDeleteResponse fromEntity(SuspiciousIp suspiciousIp) {
        return new IpDeleteResponse("IP " + suspiciousIp.getIp() + " successfully removed!");
    }
}
