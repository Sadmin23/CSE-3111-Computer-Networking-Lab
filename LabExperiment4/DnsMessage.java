package LabExperiment4;

import java.nio.ByteBuffer;

public class DnsMessage {
    private short identification;
    private short flags;
    private short numQuestions;
    private short numAnswerRRs;
    private short numAuthorityRRs;
    private short numAdditionalRRs;

    public DnsMessage(short identification, short flags, short numQuestions, short numAnswerRRs,
            short numAuthorityRRs, short numAdditionalRRs) {
        this.identification = identification;
        this.flags = flags;
        this.numQuestions = numQuestions;
        this.numAnswerRRs = numAnswerRRs;
        this.numAuthorityRRs = numAuthorityRRs;
        this.numAdditionalRRs = numAdditionalRRs;
    }

    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putShort(identification);
        buffer.putShort(flags);
        buffer.putShort(numQuestions);
        buffer.putShort(numAnswerRRs);
        buffer.putShort(numAuthorityRRs);
        buffer.putShort(numAdditionalRRs);

        return buffer.array();
    }

    public static DnsMessage fromByteArray(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        short identification = buffer.getShort();
        short flags = buffer.getShort();
        short numQuestions = buffer.getShort();
        short numAnswerRRs = buffer.getShort();
        short numAuthorityRRs = buffer.getShort();
        short numAdditionalRRs = buffer.getShort();

        return new DnsMessage(identification, flags, numQuestions, numAnswerRRs, numAuthorityRRs, numAdditionalRRs);
    }
}
