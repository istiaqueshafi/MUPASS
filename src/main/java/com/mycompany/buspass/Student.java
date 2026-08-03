package com.mycompany.buspass;import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String fullName;
    private String department;
    private String batch;
    private String email;
    private String phone;
    private String password;
    private String photoPath;

    private BusApplication application; 

    public Student(String studentId, String fullName, String department, String batch,
                   String email, String phone, String password) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.department = department;
        this.batch = batch;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getDepartment() { return department; }
    public String getBatch() { return batch; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDepartment(String department) { this.department = department; }
    public void setBatch(String batch) { this.batch = batch; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public BusApplication getApplication() { return application; }
    public void setApplication(BusApplication application) { this.application = application; }
}
