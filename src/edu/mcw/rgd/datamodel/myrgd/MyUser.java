package edu.mcw.rgd.datamodel.myrgd;

import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jdepons on 3/22/2016.
 */
public class MyUser {

    private String username;
    private boolean sendDigest;

    private boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        //make everyone ROLE_USER
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            //anonymous inner type
            public String getAuthority() {
                return "ROLE_USER";
            }
        };
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }

    public boolean isSendDigest() {
        return sendDigest;
    }

    public void setSendDigest(boolean sendDigest) {
        this.sendDigest = sendDigest;
    }

    public void setSendDigest(int sendDigest) {
        if (sendDigest ==0) {
            this.sendDigest = false;
        }else {
            this.sendDigest=true;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(int enabled) {
        if (enabled ==0) {
            this.enabled = false;
        }else {
            this.enabled=true;
        }
    }


}
