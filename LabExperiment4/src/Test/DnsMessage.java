package Test;

public class DnsMessage {
    public short identification;
    public short flags;
    public short numQuestions;
    public short numAnswerRRs;
    public short numAuthorityRRs;
    public short numAdditionalRRs;

    public DnsMessage() {

    }
}
