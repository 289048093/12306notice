package lizhao.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3612496296020601197L;

    private String username;

    private String password;

    private String email;

    private String randCode;

    private String sessionId;

    private byte status;

    private String queryRes;

    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQueryRes() {
        return queryRes;
    }

    public void setQueryRes(String queryRes) {
        this.queryRes = queryRes;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRandCode() {
        return randCode;
    }

    public void setRandCode(String randCode) {
        this.randCode = randCode;
    }

//    private String 

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserEntity) {
            UserEntity user = (UserEntity) obj;
            return user.equals(this.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (username.hashCode() << 5) - 1;
    }

}
