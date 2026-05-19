/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapademo;
import java.time.LocalDate;
/**
 *
 * @author laura
 */
public class User {
    private String nickname;
    private String email;
    private String phone;
    private String password;
    private String avatarPath;
    private LocalDate birthdate;
    
    public User(String nickname, String email, String phone, String password, String avatarpath, LocalDate birthdate){
        nickname=nickname;
        email=email;
        phone=phone;
        password=password;
        avatarPath=avatarPath;
        birthdate=birthdate;
    }
    
    public String getNickname(){return nickname;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getPhone(){return phone;}
    public String getAvatarPath(){return avatarPath;}
    public LocalDate getBirthdate(){return birthdate;}
}
