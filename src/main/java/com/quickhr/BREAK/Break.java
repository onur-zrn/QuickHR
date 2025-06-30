package com.quickhr.BREAK;

import com.quickhr.entity.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "tbl_break")
public class Break extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long breakId;
	
	private Long companyId;
	
	private Long userId;
	
	@Enumerated(EnumType.STRING)
	private EBreakType breakType;
	
	private LocalTime beginTime;
	
	private LocalTime endTime;
	
	private String description;
}
