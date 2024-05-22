package com.advantal.responsepayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicesRobotItems {
	private String id;
	private Long createdAt;
	private Long finishedAt;
	private Long startedAt;
	private String robotId;
	private InputParameters inputParameters;
//	private SaCapturedLists capturedLists;
	private CapturedTexts capturedTexts;
}
