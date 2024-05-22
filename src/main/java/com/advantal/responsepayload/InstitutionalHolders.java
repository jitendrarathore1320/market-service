package com.advantal.responsepayload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionalHolders {

	private String holder;// entity_name;

	private Long shares;

	private Double percent_held;

	private String dateReported;

	private Long change;

}
