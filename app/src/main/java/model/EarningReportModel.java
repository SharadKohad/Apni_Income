package model;

public class EarningReportModel
{
    String flag;
    String created_at;
    String provide_id;
    String amount;
    String earning_package;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getProvide_id() {
        return provide_id;
    }

    public void setProvide_id(String provide_id) {
        this.provide_id = provide_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEarning_package() {
        return earning_package;
    }

    public void setEarning_package(String earning_package) {
        this.earning_package = earning_package;
    }
}
