package com.advantal.requestpayload;

import java.util.Date;

import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiskPathStore {

	@Id
    String id;
    private String key;
    private String value;
    private Date dateTime;
}
