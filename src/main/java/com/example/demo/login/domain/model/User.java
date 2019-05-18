package com.example.demo.login.domain.model;

import java.util.Date;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "m_user")
public class User {
	
	@Id
	@Column(name = "user_id")
	@Length(max = 50)
	private String userId;
	
	@Column(name = "password")
	@Length(max = 100)
	private String password;
	
	@Column(name = "user_name")
	@Length(max = 50)
	private String userName;
	
	@Column(name = "birthday")
	private Date birthday;
	
	@Column(name = "age")
	private int age;
	
	@Column(name = "marriage", nullable=false)
	private boolean marriage;
	
	@Column(name = "role")
	@Length(max = 50)
	private String role;        //権限

}
