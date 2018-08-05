package HR;

/**
 * Created by Ketter Collins on 2/19/2017.
 */
public class user
{


    private int empID;
    private String fName;
    private String lName;
    private String sName;
    private String idNo;
    private String email;
    private String department;
    private String doe;
    private String pNumber;
    private String dob;

    public user(int empID, String fName, String lName, String sName, String idNo, String email, String department, String doe, String pNumber, String dob) {
        this.empID = empID;
        this.fName = fName;
        this.lName = lName;
        this.sName = sName;
        this.idNo = idNo;
        this.email = email;
        this.department = department;
        this.doe = doe;
        this.pNumber = pNumber;
        this.dob = dob;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public String getPNumber() {
        return pNumber;
    }

    public void setPNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
