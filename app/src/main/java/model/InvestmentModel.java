package model;

public class InvestmentModel
{
    String commit_no;
    String time;
    String amount;
    String Provide_Status;
    String BTC_Type;

    public String getCommit_no() {
        return commit_no;
    }

    public void setCommit_no(String commit_no) {
        this.commit_no = commit_no;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProvide_Status() {
        return Provide_Status;
    }

    public void setProvide_Status(String provide_Status) {
        Provide_Status = provide_Status;
    }

    public String getBTC_Type() {
        return BTC_Type;
    }

    public void setBTC_Type(String BTC_Type) {
        this.BTC_Type = BTC_Type;
    }
}
