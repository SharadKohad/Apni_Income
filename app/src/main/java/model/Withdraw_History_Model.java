package model;

public class Withdraw_History_Model
{
    String date;
    String amount;
    String status;
    String charges;
    String tds;
    String paid_amount;
    String iNR_Amt;

    public String getiNR_Amt() {
        return iNR_Amt;
    }

    public void setiNR_Amt(String iNR_Amt) {
        this.iNR_Amt = iNR_Amt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }
}
