package io.nguyenhongphat0.crm.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import javax.persistence.*;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    @Lob
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Resource() {
    }

    public Resource(String base64) {
        this.base64 = base64;
    }
    
    public Resource(MultipartFile file) {
        try {
			this.base64 = Base64.getEncoder().encodeToString(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
