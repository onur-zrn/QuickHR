package com.quickhr.entity;

import com.quickhr.enums.user.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	private String firstName;

	@Column(length = 50)
	private String lastName;

	private Long companyId;
	
	@Column(nullable = false, unique = true, length = 50)
	private String mail;

	private String pendingMail;

	@Column(nullable = false)
	private String password;
	
	private String avatar;
	
	private String phone;


	@Enumerated(EnumType.STRING)
	private EUserState userState;
	
	@Enumerated(EnumType.STRING)
	private EUserRole role;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "activation_code_id")
	private ActivationCode activationCode;
	
	//
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reset_password_code_id")
	private ResetPasswordCode resetPasswordCode;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "change_mail_code_id")
	private ChangeMailCode changeMailCode;
}
