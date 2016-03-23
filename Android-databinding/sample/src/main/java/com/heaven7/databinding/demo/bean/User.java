package com.heaven7.databinding.demo.bean;

import com.heaven7.databinding.anno.DatabindingClass;

import org.heaven7.core.adapter.ISelectable;

/**
 * Created by heaven7 on 2015/11/5.
 */
@DatabindingClass
public class User extends Person implements ISelectable{

    // moved to super
    // private String username ;
    //  private String nickname ;

    private boolean male ;       //man or woman
    private boolean select;

    public User(String username, boolean male) {
        this.username = username;
        this.male = male;
    }

    public User(String username, boolean male, String nickname) {
        this.username = username;
        this.male = male;
        this.nickname = nickname;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
//moved to super
  /*  public String getNickname(){
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }*/

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", male=" + male +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    @Override
    public void setSelected(boolean selected) {
        this.select = selected;
    }
    @Override
    public boolean isSelected() {
        return select;
    }
}
