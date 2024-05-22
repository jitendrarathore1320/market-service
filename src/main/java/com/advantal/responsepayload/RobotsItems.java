package com.advantal.responsepayload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotsItems {

	 private String id;
	 private Long createdAt;
	 private Long finishedAt;
	 private Long startedAt;
     private String robotId;
     private InputParameters inputParameters;
     private CapturedLists capturedLists;
       
}
