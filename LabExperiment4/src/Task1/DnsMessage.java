package Task1;

public class DnsMessage {
    public static short identification;
    public static short flags;
    public static short numQuestions;
    public static short numAnswerRRs;
    public static short numAuthorityRRs;
    public static short numAdditionalRRs;

    public DnsMessage(short identification, short flags, short numQuestions, short numAnswerRRs, short numAuthorityRRs, short numAdditionalRRs) {
        this.identification = identification;
        this.flags = flags;
        this.numQuestions = numQuestions;
        this.numAnswerRRs = numAnswerRRs;
        this.numAdditionalRRs = numAdditionalRRs;
    }
    public short getIdentification() {
        return identification;
    }

    public void setIdentification(short identification) {
        this.identification = identification;
    }

    public short getFlags() {
        return flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public short getNumAnswerRRs() {
        return numAnswerRRs;
    }

    public void setNumAnswerRRs(short numAnswerRRs) {
        this.numAnswerRRs = numAnswerRRs;
    }

    public short getNumAuthorityRRs() {
        return numAuthorityRRs;
    }

    public void setNumAdditionalRRs(short numQuestions) {
        this.numQuestions = numQuestions;
    }
}

